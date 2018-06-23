package com.rxwu.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.rxwu.helper.entity.PrintEntity;
import org.rxwu.helper.listener.DeviceConnectListener;
import org.rxwu.helper.listener.PrintListener;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Created by rxwu on 2016/4/19.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail: USB 串口 读写类
 */
public final class USBProtolHelper /*
									 * extends AsyncTask<Integer, Integer,
									 * Integer>
									 */ {
	private static USBProtolHelper instance = null;
	private static final String TAG = "------com.weilai.helper-----";
	private UsbManager mUsbManager = null;
	private UsbDevice mDevice = null;
	private UsbEndpoint mEndpointIntr = null;
	private static UsbDeviceConnection mConnection = null;
	private PrintListener usbPrintListener = new PrintListener() {

		@Override
		public void printSuccess() {
			// TODO Auto-generated method stub
			Log.d(TAG, "打印成功");
		}

		@Override
		public void printIng() {
			// TODO Auto-generated method stub
			Log.d(TAG, "打印中");
		}

		@Override
		public void printError(String msg) {
			// TODO Auto-generated method stub
			Log.d(TAG, "打印失败");

		}
	};
	private DeviceConnectListener deviceConnectListener = new DeviceConnectListener() {

		@Override
		public void connecting() {
			// TODO Auto-generated method stub
			Log.d(TAG, "连接中");
		}

		@Override
		public void connectSuccess() {
			// TODO Auto-generated method stub
			Log.d(TAG, "连接成功");
		}

		@Override
		public void connectError(String msg) {
			// TODO Auto-generated method stub
			Log.d(TAG, "连接失败");
		}

		@Override
		public void connectCancel() {
			// TODO Auto-generated method stub
			Log.d(TAG, "连接取消");
		}
	};
	private String currentUsbPath = null;// 当前连接的usb路径

	/****
	 * @detail返回当前选择的usb路径
	 * @return
	 */
	public String getCurrentUsbPath() {
		return currentUsbPath;
	}

	public void setUsbPrintListener(PrintListener usbPrintListener) {
		if (usbPrintListener != null) {
			this.usbPrintListener = usbPrintListener;
		}
	}

	/**********
	 * @detail 监听设备连接状态的请在init方法前调用
	 * @param deviceConnectListener
	 */
	public void setDeviceConnectListener(DeviceConnectListener deviceConnectListener) {
		if (deviceConnectListener != null) {
			this.deviceConnectListener = deviceConnectListener;
		}
	}

	private final int TIMEOUT = 8 * 1000;// 8秒超时

	PendingIntent mPermissionIntent = null;

	/****
	 * @detail 默认构造方法
	 * @param context
	 * @param usbPath
	 */
	private USBProtolHelper(Context context, String usbPath) {
		try {
			deviceConnectListener.connecting();
			mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
			mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_USB_PERMISSION);
			filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
			context.registerReceiver(mUsbReceiver, filter);
			openUsb();
		} catch (Exception ex) {
			if (usbPrintListener != null) {
				usbPrintListener.printError("初始化连接出错");
			}
			if (deviceConnectListener != null) {
				deviceConnectListener.connectError(ex.getMessage() + "," + ex.getLocalizedMessage());
			}
			Log.e(TAG, "usb 转串口工具类初始化失败:" + ex.getMessage());
		}
	}

	@SuppressLint("NewApi")
	private void setDevice(UsbDevice device) {
		if (device != null) {
			UsbInterface intf = null;
			UsbEndpoint ep = null;

			int InterfaceCount = device.getInterfaceCount();
			int j;

			mDevice = device;
			for (j = 0; j < InterfaceCount; j++) {
				int i;

				intf = device.getInterface(j);
				//Log.i(TAG, "接口是:" + j + "类是:" + intf.getInterfaceClass());
				if (intf.getInterfaceClass() == 7 || intf.getInterfaceClass() == 0) {
					int UsbEndpointCount = intf.getEndpointCount();
					for (i = 0; i < UsbEndpointCount; i++) {
						ep = intf.getEndpoint(i);
						//Log.i(TAG, "端点是:" + i + "方向是:" + ep.getDirection() + "类型是:" + ep.getType());
						if (ep.getDirection() == 0 && ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
						//	Log.i(TAG, "接口是:" + j + "端点是:" + i);
							break;
						}
					}
					if (i != UsbEndpointCount) {
						break;
					}
				}
			}
			if (j == InterfaceCount) {
				Log.i(TAG, "没有打印机接口");
				return;
			}

			mEndpointIntr = ep;
			if (device != null) {
				UsbDeviceConnection connection = mUsbManager.openDevice(device);

				if (connection != null && connection.claimInterface(intf, true)) {
					Log.i(TAG, "打开成功！ ");
					mConnection = connection;
					
				} else {
					Log.i(TAG, "打开失败！ ");
					mConnection = null;
				}
			}
		} else {

		}
	}

	/*****
	 * @detail 打开usb
	 */
	public void openUsb() {
		if (mDevice != null) {
			setDevice(mDevice);
			if (mConnection == null) {
				Iterator<UsbDevice> deviceIterator = scanUsbPort();
				while (deviceIterator.hasNext()) {
					UsbDevice device = deviceIterator.next();
					mUsbManager.requestPermission(device, mPermissionIntent);
				}
			}
		} else {
			Iterator<UsbDevice> deviceIterator = scanUsbPort();
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				mUsbManager.requestPermission(device, mPermissionIntent);
			}
		}
	}

	private Iterator<UsbDevice> scanUsbPort() throws NullPointerException {
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		return deviceIterator;
	}

	public boolean isEnable() {

		if (mConnection == null) {
			Log.i(TAG, "usb connect is unable");
			return false;
		}
		//测试打印
		Log.i(TAG, "usb connect is enable");
		return true;
	}

	/**********
	 * @detail 初始化USB转串口工具类
	 * @param entity
	 * @param context
	 * @param 这里对应获取到的驱动设备
	 */
	public static USBProtolHelper init(Context context) {
		instance = new USBProtolHelper(context, null);
		return instance;
	}

	/****
	 * @detail 关闭usb打印服务
	 * 
	 */
	public void close() {
		try {
			if (mConnection != null) {
				mConnection.close();
				// deviceConnectListener.connectCancel();
			}
			mConnection = null;
		} catch (Exception ex) {
			Log.d(TAG, ex.getLocalizedMessage());
		}
	}

	/*****
	 * @Detail 请求申请权限
	 */
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						if (usbDevice != null) {
							// call method to set up device communication
							setDevice(usbDevice);
						} else {
							close();
							mDevice = usbDevice;

						}
					} else {
						Log.d(TAG, "permission denied for device " + usbDevice);
					}
				}
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				Log.i(TAG, "remove the printer devices");
				close();
				instance = null;// 清空当前实例
			}
			// 处理完成后解除绑定
			// context.unregisterReceiver(this);
		}
	};

	// 切纸
	private void cutPaper() {
		// 换行
		byte[] line = new byte[2];
		line[0] = 0x0d;
		line[1] = 0x0a;
		for (int i = 0; i < 7; i++) {
			write(line);
		}

		// 切纸
		byte[] a = new byte[2];
		a[0] = 0x1B;
		a[1] = 0x69;
		write(a);

		byte[] d = new byte[4];
		d[0] = 0x1b;
		d[1] = 0x42;
		d[2] = 0x02;
		d[3] = 0x02;
		write(d);

	}

	/*****
	 * @Detail 直接打印字符数组
	 * @param content
	 *            打印内容
	 */
	public void write(byte[] content) {
		if (mConnection != null && mEndpointIntr != null && content != null && content.length > 0) {
			mConnection.bulkTransfer(mEndpointIntr, content, content.length, TIMEOUT);
		}
	}

	/*****
	 * @detail 直接打印字节数组
	 * @param content
	 *            字节数组内容
	 * @param length
	 *            字符数组长度
	 */
	public void write(byte[] content, int length) {
		if (mConnection != null && mEndpointIntr != null && content != null && length > 0) {
			mConnection.bulkTransfer(mEndpointIntr, content, length, TIMEOUT);
		} else {
			Log.d(TAG, "usb打印失去连接");
		}
	}

	/*****
	 * @detail 打印格式内容
	 * @param printentity
	 *            打印的辅助容器实体
	 */
	public void print(PrintEntity printentity) throws Exception {
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
		List<byte[]> contents = printentity.getContents();
		// try{
		for (byte[] content : contents) {
			if ("[swap_line]".equals(new String(content, "gb2312")) || content == null) {
				write(line);
			} else {
				write(content);
			}
		}
		// }catch(UnsupportedEncodingException ex){ex.printStackTrace();}
		byte[] c = new byte[5];
		
		c[0] = 0x1b;
		c[1] = 0x40;
		c[2] = 0x1b;
		c[3] = 0x64;
		c[4] = 2;
		
		write(c);
		//write(line);*/
		cutPaper();// 打印完成，直接切纸
	}

}
