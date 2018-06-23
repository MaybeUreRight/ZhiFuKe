/*package com.rxwu.helper;

import org.rxwu.helper.listener.DeviceConnectListener;
import org.rxwu.helper.listener.OnDataReceiverListener;
import org.rxwu.helper.listener.PrintListener;

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
import android.os.Handler;
import android.util.Log;

public class ReadUsbProtoMsg {
	private static final String TAG = "------com.weilai.helper-----";
	private UsbManager usbManager = null;
	private UsbDevice usbDevice = null;
	private UsbDeviceConnection usbConnect = null;
	private UsbInterface usbInterface = null;
	private PrintListener usbPrintListener;
	private DeviceConnectListener deviceConnectListener;;
	private OnDataReceiverListener dataReceiverListener;
	private UsbEndpoint readPoint = null;
	private int wait = 1000;

	private boolean READING = true;

	*//****
	 * @detail 暂停
	 *//*
	public void pause() {
		this.READING = false;
	}

	*//***
	 * @detail 重启
	 *//*
	public void resume() {
		this.READING = true;
	}

	*//*****
	 * @detail 设置等待的时间
	 * @param wait
	 *//*
	public void setWait(int wait) {
		if (wait < 200) {
			wait = 1000;
		} else {
			this.wait = wait;
		}
	}

	public void setDataReceiverListener(
			OnDataReceiverListener dataReceiverListener) {
		if (dataReceiverListener != null) {
			this.dataReceiverListener = dataReceiverListener;
		}
	}

	private Thread querymsgThread = new Thread() {
		public void run() {
			while (true) {
				if(READING){
					if(readPoint!=null){
						byte[] byte2 = new byte[512];
						if (readPoint != null) {
							int ret = usbConnect.bulkTransfer(readPoint, byte2,
									byte2.length, 3000);
							if (ret != -1 && dataReceiverListener != null) {
								queryMessage.sendEmptyMessage(1);
							}
						}
					}
				}
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
	};
	private Handler queryMessage = new Handler() {
		public void handleMessage(android.os.Message msg) {
			byte[]message=(byte[])msg.obj;
			dataReceiverListener.onMessage(message);
			//queryMessage.sendEmptyMessageDelayed(1, wait);// 每隔一秒读取
		};
	};

	public ReadUsbProtoMsg init(Context context, String usbName) {
		try {
			if (deviceConnectListener != null) {
				deviceConnectListener.connecting();
			}
			PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
					context, 0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
			context.registerReceiver(mUsbReceiver, filter);
			usbManager = (UsbManager) context
					.getSystemService(Context.USB_SERVICE);
			usbDevice = usbManager.getDeviceList().get(usbName);// 查找对应的usb驱动设备
			usbManager.requestPermission(usbDevice, mPermissionIntent);
			usbConnect = usbManager.openDevice(usbDevice);
			usbInterface = usbDevice.getInterface(0);
			readPoint = usbInterface.getEndpoint(0);
			querymsgThread.start();//开启查询信息的线程
			usbConnect.claimInterface(usbInterface, true);
			if (deviceConnectListener != null) {
				deviceConnectListener.connectSuccess();
			}
		} catch (Exception ex) {
			if (usbPrintListener != null) {
				usbPrintListener.printError("初始化连接出错");
			}
			if (deviceConnectListener != null) {
				deviceConnectListener.connectError(ex.getMessage() + ","
						+ ex.getLocalizedMessage());
			}
			Log.e(TAG, "usb 转串口工具类初始化失败:" + ex.getMessage());
		}
		// UsbDeviceConnection
		return this;
	}

	*//*****
	 * @Detail 请求申请权限
	 *//*
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private static final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);

					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (device != null) {
							// call method to set up device communication
						}
					} else {
						Log.d("--", "permission denied for device " + device);
					}
				}
			}
		}
	};
}
*/