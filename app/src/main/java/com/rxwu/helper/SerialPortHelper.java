package com.rxwu.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.rxwu.helper.entity.PrintEntity;
import org.rxwu.helper.listener.PrintListener;

import android.util.Log;

public class SerialPortHelper extends BaseSerialPortHelper<Integer> {
	private static final String TAG=SerialPortHelper.class.getSimpleName();
	private PrintEntity entity = null;
	public String currentPath;
	private PrintListener printListener = new PrintListener() {

		@Override
		public void printSuccess() {
			// TODO Auto-generated method stub

		}

		@Override
		public void printIng() {
			// TODO Auto-generated method stub

		}

		@Override
		public void printError(String msg) {
			// TODO Auto-generated method stub

		}
	};// 打印监听//打印监听

	public PrintListener getPrintListener() {
		return printListener;
	}

	public void setPrintListener(PrintListener printListener) {
		if (printListener != null) {
			this.printListener = printListener;
		}
	}

	private String errorMsg = "错误信息";

	public SerialPortHelper(String deviceName, int booter) {
		super(deviceName, booter);
		// TODO Auto-generated constructor stub
		this.currentPath=deviceName;
		this.booter=booter;
	}

	public SerialPortHelper init(PrintEntity entity) {
		this.entity = entity;
		return this;
	}

	@Override
	protected Integer doInBackground(Integer... arg0) {
		// TODO Auto-generated method stub
		try {
			if (entity == null) {
				errorMsg = "获取不到显示内容";
				return ERROR;
			}
			if (mOutputStream == null) {
				errorMsg = "连接断开";
				return ERROR;
			}

			byte[] a = new byte[2];
			a[0] = 0x1b;
			a[1] = 0x40;
			mOutputStream.write(a);

			// CHARACTOR SET
			byte[] b = new byte[3];
			b[0] = 0x1B;
			b[1] = 0x52;
			b[2] = 0x40;
			mOutputStream.write(b);

			// 换行
			byte[] line = new byte[2];
			line[0] = 0x0d;
			line[1] = 0x0a;

			// 根据换行符切换
			List<byte[]> contents = entity.getContents();
			try {
				for (byte[] content : contents) {
					if ("[swap_line]".equals(new String(content, "gb2312"))
							|| content == null) {
						mOutputStream.write(line);
					} else {
						mOutputStream.write(content);
					}
				}
			} catch (UnsupportedEncodingException ex) {
				errorMsg = "打印内容编码格式错误";
				return ERROR;
			}
			byte[] c = new byte[1];
			c[0] = 0x0c;
			mOutputStream.write(c);

			for (int i = 0; i < 7; i++) {
				mOutputStream.write(line);
			}

		} catch (Exception ex) {
			errorMsg = "连接断开(cause:" + ex.getLocalizedMessage() + ")";
			return ERROR;
		}
		return SUCCESS;
	}

	/*****
	 * @detail 自己定义输入内容，适合直接转接打印的方式，系统不会做其他任何打印相关的控制
	 * @param message
	 */
	public void write(byte[] message) {
		if (mOutputStream != null) {
			try {
				mOutputStream.write(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*****
	 * @Detail 切纸
	 */
	public void cutpaper() {
		try {
			if (mOutputStream != null) {
				// 切纸
				byte[] a = new byte[2];
				a[0] = 0x1B;
				a[1] = 0x69;
				mOutputStream.write(a);
				byte[] d = new byte[4];
				d[0] = 0x1b;
				d[1] = 0x42;
				d[2] = 0x02;
				d[3] = 0x02;
				mOutputStream.write(d);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG,e.getLocalizedMessage());
		}

	}

	/*****
	 * @Detail 打印执行结果
	 */
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		switch (result) {
		case ERROR:
			deviceConnectListener.connectError(errorMsg);
			printListener.printError(errorMsg);
			break;
		case SUCCESS:
			deviceConnectListener.connectSuccess();
			printListener.printSuccess();
			break;
		}
	}

}
