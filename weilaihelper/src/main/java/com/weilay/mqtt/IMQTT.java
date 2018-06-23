package com.weilay.mqtt;

public interface IMQTT {
	//register the callback
	public void registerCallback(PushHandler handler);
	
	//reconnect
	public void reconnect();
	
	//
}
