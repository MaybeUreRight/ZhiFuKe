package com.weilay.pos.listener;

import com.weilay.pos.util.T;

public abstract class StringResponseListener implements BaseResponseListener {

	/******
	 * @Detail 请求成功
	 * @param json
	 *            返回的成功值
	 */
	public  abstract void onSuccess(String str);

	public abstract void onFailed(int code, String msg);

	@Override
	public void onFailed(NetCodeEnum code, String msg) {
		// TODO Auto-generated method stub
		switch (code) {
		case TIMEOUT:
		case UNKNOWHOST:
		case NETWORK_UNABLE:
			T.showCenter(msg);
			break;
		default:
			break;
		}
		onFailed(-10, msg);
	}

	/******
	 * @detail 网络请求错误
	 */
	public void networkError() {

	}

	/*****
	 * @detail 连接超时
	 */
	public void timeOut() {
		T.showCenter("连接超时");
	}

	/*****
	 * @detail 服务器连接超时
	 */
	public void serverTimeOut() {
		T.showCenter("服务器连接超时");
	}

}