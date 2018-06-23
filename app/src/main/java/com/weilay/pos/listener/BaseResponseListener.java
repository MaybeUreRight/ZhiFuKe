package com.weilay.pos.listener;

/********
 * @detail 网络请求的基础响应接口
 * @author rxwu
 *
 */
public interface BaseResponseListener {

	/*****
	 * @detail 请求失败
	 * @param code
	 *            错误代码
	 * @param msg
	 *            错误的信息
	 */
	public abstract void onFailed(NetCodeEnum code, String msg);

	/****
	 * @detail 网络错误
	 */
	public void networkError();

	/*****
	 * @detail 连接超时
	 */
	public void timeOut();

	/*****
	 * @detail 服务器连接超时
	 */
	public void serverTimeOut();
}
