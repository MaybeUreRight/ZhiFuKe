package com.weilay.pos.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.framework.utils.L;

import android.os.SystemClock;
import com_serialport_api.SerialPort;

public abstract class PrintUtil {
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private String readpath, sendpath;
	private ReadThread readThread;

	public byte[] bRec = null;
	// private SendThread sendThread;
	private SerialPort serialPort;

	public PrintUtil() {

	}

	// 接受线程
	public void open(String path) {
		L.i("gg", "openPath:" + path);
		try {
			serialPort = new SerialPort(new File(path), 115200, 0);
			mInputStream = serialPort.getInputStream();
			mOutputStream = serialPort.getOutputStream();
			readThread = new ReadThread();
			readThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// sendThread = new SendThread();
		// sendThread.setSuspendFlag();
		// sendThread.start();
	}

	public byte[] ComBean(byte[] buffer, int size) {

		bRec = new byte[size];
		for (int i = 0; i < size; i++) {
			bRec[i] = buffer[i];
		}
		return bRec;
	}

	byte[] readBuffer = new byte[10540];
	private final static byte[] hex = "0123456789ABCDEF".getBytes();
	private final static char[] hexCode = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String Bytes2HexString(byte[] b, int size) {
		int min_sz = size > b.length ? b.length : size;
		byte[] buffer = new byte[2 * min_sz];
		for (int i = 0; i < min_sz; i++) {
			buffer[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buffer[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buffer);
	}

	/****
	 * 转16进制
	 * 
	 * @param b
	 * @return
	 */
	public static String Bytes2HexString(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			buffer.append(hexCode[(b[i] >> 4) & 0x0f]);
			buffer.append(hexCode[b[i] & 0x0f]);
			buffer.append(" ");
		}
		return buffer.toString();
	}

	private class ReadThread extends Thread {

		@Override
		public void run() {

			super.run();
			while (!isInterrupted()) {
				try {
					if (mInputStream == null)
						return;

					// L.i("gg", "gg");
					int size = 0;

					size = mInputStream.read(readBuffer);
					// String s = Bytes2HexString(readBuffer,size);
					// Log.i("gg", "read from usb2com:" + s);
					// String s = Bytes2HexString(readBuffer,size);
					// L.i("gg", "read from usb2com:" + s);
					if (size > 0) {
						onDataReceived(ComBean(readBuffer, size));
					}
					SystemClock.sleep(25);
				} catch (Exception e) {

					e.printStackTrace();
					// Log.i("gg", e.getMessage());
					// L.i("gg", e.getMessage());
					return;
				}
			}
		}
	}

	private class SendThread extends Thread {
		public boolean suspendFlag = true;// 鎺у埗绾跨▼鐨勬墽琛?

		// private byte[] _bLoopData = new byte[0x30];

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				synchronized (this) {
					while (suspendFlag) {
						try {
							wait();
						} catch (InterruptedException e) {
							// e.printStackTrace();
							L.i("gg", e.getMessage());
						}
					}
				}
				// send(_bLoopData);
				SystemClock.sleep(5);
			}
		}

	} // ----------------------------------------------------

	public void close() {
		try {
			if (readThread != null) {
				readThread.interrupt();
			}
			if (serialPort != null) {
				serialPort.close();
			}

		} catch (Exception e) {
			// TODO: handle exception

		}

	}

	StringBuffer buffer;

	public void send(byte[] bOutArray) {
		try {
			mOutputStream.write(bOutArray);

		} catch (IOException e) {
			// e.printStackTrace();
			L.i("gg", e.getMessage());
		} catch (Exception ex) {
			L.i("gg", "串口线已经断开");
		}
	}

	protected abstract void onDataReceived(byte[] message);

}
