package org.rxwu.helper.listener;
	
/*************
 * @detail 串口数据监听回调
 * @author rxwu
 * @date 2016.4.22
 *
 */
public interface OnDataReceiverListener {
	/*****
	 * @detail 数据到达
	 */
	public void onMessage(byte[]message);
	
	
	/***
	 * @detail 数据获取超时
	 */
	public void timeOut();

}
