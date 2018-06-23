package com.weilay.pos.listener;

/********
 * @Deatail 检查机器是否在线
 * @author Administrator
 *
 */
public interface OnCheckMachineStateListener {
	/******
	 * @detail 在线
	 */
	public void onLine();

	/******
	 * @detail 设备没找到
	 */
	public void nofound(int code, String msg);

	/*****
	 * @detail 网络出错
	 */
	public void connectFailed(String msg);
}
