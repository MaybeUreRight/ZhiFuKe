package com.weilay.pos.listener;

import org.json.JSONObject;

public abstract class OnDataListener<T> extends ResponseListener {
	public abstract void onData(T obj);

	public  void onFailed(String msg){}

	/*****
	 * @detail 失败调用
	 * @param code
	 * @param msg
	 */
	public void onFailed(int code, String msg) {
		
	}

	@Override
	public void onSuccess(JSONObject json) {
		// TODO Auto-generated method stub
		
	}
}
