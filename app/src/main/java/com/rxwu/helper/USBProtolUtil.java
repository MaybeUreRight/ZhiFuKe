package com.rxwu.helper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import org.rxwu.helper.entity.PrintEntity;

/**
 * Created by rxwu on 2016/4/19.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail: USB 串口 读写类
 */
public class USBProtolUtil extends AsyncTask<Integer, Integer, Integer> {
	private UsbManager usbManager = null;
	private UsbDevice usbDevice = null;
	private UsbDeviceConnection usbConnect = null;
	private UsbInterface usbInterface = null;
	private final int TIMEOUT = 1 * 1000;// 8秒超时
	private byte[] bytes;
	private UsbEndpoint endpoint = null;
	private PrintEntity entity = null;
	private String currentPort;
	private Context mContext;

	public void printOut(PrintEntity printEntity, String port, Context context) {
		this.entity = printEntity;
		init(port);
		execute(0);// 开始执行打印
	}

	private void init(String port) {
		try {
			if (port == null || "0".equals(port)) {
				/// T.showCenter("您还没有设置打印设备,请到设置页面设置");
				return;
			}
			currentPort = port;
			PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION),
					0);
			IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
			mContext.registerReceiver(mUsbReceiver, filter);
			usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
			usbDevice = usbManager.getDeviceList().get(port);// 查找对应的usb驱动设备
			usbInterface = usbDevice.getInterface(0);
			if (usbInterface == null) {
				return;
			}
			if (!usbManager.hasPermission(usbDevice)) {
				usbManager.requestPermission(usbDevice, mPermissionIntent);// 如果没有权限的话,那么申请设备权限
			} else {
				connect();
			}
		} catch (Exception ex) {
		}
	}

	private void connect() {
		usbConnect = usbManager.openDevice(usbDevice);
		if (usbConnect == null) {
			return;
		}
		if (usbConnect.claimInterface(usbInterface, true)) {
			endpoint = usbInterface.getEndpoint(1);
		}
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// RESET PRINTER
		try {
			byte[] a = new byte[2];
			a[0] = 0x1b;
			a[1] = 0x40;
			write(a);

			// CHARACTOR SET
			byte[] b = new byte[3];
			b[0] = 0x1B;
			b[1] = 0x52;
			b[2] = 0x40;
			write(b);

			// 换行
			byte[] line = new byte[2];
			line[0] = 0x0d;
			line[1] = 0x0a;

			// 根据换行符切换
			List<byte[]> contents = entity.getContents();
			for (byte[] content : contents) {
				if (content == PrintEntity.SWAP_CMD) {
					write(line);
				} else {
					write(content);
				}
			}
			byte[] c = new byte[1];
			c[0] = 0x0c;
			write(c);

			for (int i = 0; i < 7; i++) {
				write(line);
			}
			// 切纸
			a[0] = 0x1B;
			a[1] = 0x69;
			write(a);

			byte[] d = new byte[4];
			d[0] = 0x1b;
			d[1] = 0x42;
			d[2] = 0x02;
			d[3] = 0x02;
			write(d);
			return 0;
		} catch (Exception ex) {
			return 1;
		}
	}

	public void write(byte[] content) {
		if (usbConnect != null && endpoint != null) {
			usbConnect.bulkTransfer(endpoint, content, content.length, TIMEOUT);
		}
	}

	@Override
	protected void onPostExecute(Integer integer) {
		super.onPostExecute(integer);
		switch (integer) {
		case 1:
			break;
		case 0:
			break;
		}
	}

	/*****
	 * @Detail 请求申请权限
	 */
	private final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (usbDevice != null) {
							connect();
						}
					} else {
					}
				}
			}
			context.unregisterReceiver(this);
		}

	};
}
