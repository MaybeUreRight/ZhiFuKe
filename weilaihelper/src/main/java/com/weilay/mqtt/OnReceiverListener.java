package com.weilay.mqtt;

import com.framework.utils.L;

public abstract class OnReceiverListener {

	public abstract void onReceiver(String topicName, int code, String json);
	
	//the default receiver message deal with
	public static OnReceiverListener getInstance(){
		OnReceiverListener listener= new OnReceiverListener() {

			@Override
			public void onReceiver(String topicName, int code, String json) {
				// TODO Auto-generated method stub
				L.d("--pushservices topicName is：" + topicName + ",code is" + code + ",the payload content was：" + json == null ? "" : json.toString());
			}
		};
		return listener;

	}

}
