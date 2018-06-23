package com.weilay.mqtt;

import com.ibm.micro.client.mqttv3.MqttConnectOptions;

/***
 * the push params define
 * @author rxwu
 *
 */
public class PushConstants {
	//the host address
	public final static String BROKER_HOST_NAME = "notification.zfk360.cn";
	public final static int MQTT_BROKER_PORT_NUM = 1883;
	//if the value equals true,client will keep long connect with the server
	public final static boolean MQTT_CLEAN_START =false;
	// to keep the connection active, even when the device goes to sleep.
	public final static  int KEEP_ALIVE_INTERVAL = 1000 * 60 * 28;
	//set the internal connect time out
	public final static  int  CONNECT_TIME_OUT=2*60;
	//the internal set mqtt send keep alive command
	public final static int MQTT_KEEP_ALIVE = 60 * 2;
	//qos
	public final static int[] MQTT_QUALITIES_OF_SERVICE = { 1 };
	public final static int MQTT_QUALITY_OF_SERVICE = 1;
	//the mqtt connect options
	public final static  MqttConnectOptions options=new MqttConnectOptions();
	// The broker should not retain any messages.
	public final static boolean MQTT_RETAINED_PUBLISH = false;
	// RECONNECT TIME INTERNAL
	public static final long RECONNECT_INTERVAL = 15 * 1000;
	
	public final static MqttConnectOptions getOptions(){
		MqttConnectOptions options=new MqttConnectOptions();
		options.setCleanSession(MQTT_CLEAN_START);
		options.setKeepAliveInterval(MQTT_KEEP_ALIVE);
		options.setConnectionTimeout(CONNECT_TIME_OUT);
		return options;
	}
}
