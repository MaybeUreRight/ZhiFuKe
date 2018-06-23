package com.rxwu.helper;

import org.rxwu.helper.listener.OnDataReceiverListener;

import android.util.Log;

public final class ReadSerialPortMsg extends BaseSerialPortHelper<Integer>{
	private int wait;//设置监听间隔
	private boolean debug;
	private int getWait() {
		return wait<50?50:wait;
	}
	public void setWait(int wait) {
		if(wait<50){
			this.wait = 50;
		}else{
			this.wait=wait;
		}
	}
	
	

	public void setDebug(boolean debug) {
		this.debug = debug;
	}


protected OnDataReceiverListener mOnDataReceiverListener;//数据回调
	
    public OnDataReceiverListener getmOnDataReceiverListener() {
		return mOnDataReceiverListener;
	}
	public void setmOnDataReceiverListener(
			OnDataReceiverListener mOnDataReceiverListener) {
		this.mOnDataReceiverListener = mOnDataReceiverListener;
	}
	private boolean READING=true;
	private byte[] buffer=null;
	
	
	public ReadSerialPortMsg(String devicesName,int port){
		super(devicesName,port);
	}
	
	/*****
	 * @detail 启动
	 */
	public void start(){
		super.start();
	}
	/****
	 * @detail 暂停
	 */
	public void pause(){
		this.READING=false;
	}
	/***
	 * @detail 重启
	 */
	public void resume(){
		this.READING=true;
	}
	/******
	 * @detail 	启动消息接收服务
	 */
	@Override
	protected Integer doInBackground(Integer... arg0) {
		// TODO Auto-generated method stub
		READING=true;
		setMessage("ReadSerailPortMsg->runOnOtherThread","-------ReadSerailPortMsg->READING:"+READING);
		while(true) {
			if(READING){
				try
				{
					if (mInputStream == null){
						setMessage("ReadSerailPortMsg->runOnOtherThread","-------InputStream is:null,not init");
						READING=false;
						return ERROR;
					}
					buffer=new byte[mInputStream.available()];
					int size = mInputStream.read(buffer);
					if (size > 0){
						READING=true;
						publishProgress(SUCCESS);
						
					}else{
						setMessage("ReadSerailPortMsg->runOnOtherThread","-------wait message");
						//READING=false;
					}
					try
					{
						Thread.sleep(getWait());//寤舵椂50ms
					} catch (InterruptedException e)
					{
						e.printStackTrace();
						setMessage("ReadSerailPortMsg->InterruptedException",e.getMessage()+":"+e.getLocalizedMessage());
					}
				} catch (Throwable e)
				{
					e.printStackTrace();
				
					return ERROR;
				}
			}
		}
	}
	@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			//super.onProgressUpdate(values);
			if(mOnDataReceiverListener!=null){
				mOnDataReceiverListener.onMessage(buffer);
			}
		}
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		switch (result) {
		case SUCCESS:
			if(mOnDataReceiverListener!=null)
				//mOnDataReceiverListener.onMessage(buffer);
			break;
		case ERROR:
			setMessage("ReadSerailPortMsg->Throwable","timeOut");
			if(mOnDataReceiverListener!=null)
				mOnDataReceiverListener.timeOut();
			break;
		default:
			break;
		}
	}

	
	private void setMessage(String tag,String msg){
		if(debug){
			Log.d(tag,msg);
		}
	}

}
