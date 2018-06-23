package com.weilay.pos.listener;

import com.weilay.pos.util.T;

import android.graphics.Bitmap;

public abstract class ResponseImageListener implements BaseResponseListener {

	/**********
	 * @Detail 图片下载成功后的保存文件
	 * @param bitmap
	 */
	public abstract void loadSuccess(Bitmap bitmap);


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
