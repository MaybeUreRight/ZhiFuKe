package com.rxwu.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.rxwu.helper.listener.DeviceConnectListener;

import android.os.AsyncTask;
import android.util.Log;

import com_serialport_api.SerialPort;

/********
 * @detail 串口辅助基础类
 * @author Administrator
 *
 */
abstract class BaseSerialPortHelper<T> extends AsyncTask<Integer, Integer, Integer>{
	private static final String TAG=BaseSerialPortHelper.class.getSimpleName();
	protected static   final int SUCCESS=0;
	protected  static final int ERROR=1;
	protected  static final int WAIT=2;
	
	protected String deviceNameString;
	public int booter;
	
	protected DeviceConnectListener deviceConnectListener=new DeviceConnectListener() {
		
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
	};//设备连接监听

	
	protected FileOutputStream mOutputStream;
	protected FileInputStream mInputStream;
	protected SerialPort sp;
	public DeviceConnectListener getDeviceConnectListener() {
		return deviceConnectListener;
	}
	public void setDeviceConnectListener(DeviceConnectListener deviceConnectListener) {
		if(deviceConnectListener!=null){
			this.deviceConnectListener = deviceConnectListener;
		}
	}
	protected BaseSerialPortHelper(String deviceName,int booter){
		this.deviceNameString=deviceName;
		this.booter=booter;
	}
	/****
	 * @detail 初始化串口读写类
	 */
	private void init(){
		try{
            sp = new SerialPort(new File(deviceNameString), booter, booter);
            if (sp != null) {
                mOutputStream = (FileOutputStream) sp.getOutputStream();
                mInputStream = (FileInputStream) sp.getInputStream();
                if(deviceConnectListener!=null){
                	deviceConnectListener.connectSuccess();
                }
            }else{
            	deviceConnectListener.connectError("打开失败，可以重启应用试试");
            }
        } catch (SecurityException e) {
            	deviceConnectListener.connectError("连接出错：没有所选端口的读写权限");
        } catch (IOException e) {
        	// TODO Auto-generated catch block
            	deviceConnectListener.connectError("连接出错："+e.getLocalizedMessage());
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        		deviceConnectListener.connectError("连接出错："+e.getLocalizedMessage());
		}
		
	}
	
	/*******
	 * @detail 开启
	 */
	@SuppressWarnings("unchecked")
	protected void start() {
		init();
		execute(0);
	}
	
	/********
	 * @detail 关闭串口
	 */
	public void close() throws IOException{
		if(deviceConnectListener!=null){
			deviceConnectListener.connectCancel();//用户断开连接
		}
		if(mOutputStream!=null)
			mOutputStream.close();
		if(mInputStream!=null)
			mInputStream.close();
		if(sp!=null)
			sp.close();
	}
}
