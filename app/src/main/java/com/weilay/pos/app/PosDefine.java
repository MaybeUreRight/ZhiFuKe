package com.weilay.pos.app;

import android.content.SharedPreferences;

/*******
 * @Detail 常量配置的类
 * @author Administrator
 * 
 */
public class PosDefine {
	//设备的sn_key
	public static final String SN_KEY="sn_key";

	// 推送定义
	public static final String MQTT_PUSH_BASE = "banklay_";
	public static final String MQTT_SEND_CARD = MQTT_PUSH_BASE + "card_";// +代理商id
	public static final String MQTT_GLOBAL = MQTT_PUSH_BASE + "global";// +全局
	public static final String MQTT_PAYMENT = MQTT_PUSH_BASE + "payment_";

	// 订阅的action
	public static final String ACTION_RECEIVER_CARD = "RECEIVER_CARD";// 发券

	// topics
	// public static final String TOPIC_

	public static final String CACHE_MACHINE_INFO = "CACHE_MACHINE_INFO";// 缓存机器的信息
	public static String CACHE_OPERATOR = "CACHE_OPERATOR";// 操作员信息缓存
	public static String CACHE_BAND = "CACHE_BAND";// 检查设备是否已经绑定
	public static String CACHE_WIFICONNECT = "wificonnect";// 保存最后一次连接的wifi信息
	public static final String CACHE_UPGRADE_TYPE = "UPGRADE_TYPE";
	public static final String CACHE_MESSAGE_COUNT ="CACHE_MESSAGE_COUNT";// 保存新消息的条数
	
	public static final String CACHE_SOUND_CONFIG="CACHE_SOUND_CONFIG";//声音开关
	
	public static final String ISWIFI="isWifi";//判断有线网络还是无线网络

	// intent的参数
	public static String INTENT_PAY_ACTION = "INTENT_PAY_ACTION";// 为xx支付
	public static final String INTENT_COUPON = "INTENT_COUPON";
	public static final String INTENT_PAY_AMOUNT = "INTENT_PAY_AMOUNT";// 支付金额
	public static final String INTENTE_PAY_INFO = "INTENT_PAY_INFO";// 支付信息
	public static final String ISINTERCEPT = "isIntercept";// 图文识别
	public static final String INTERCEPT_PAYAMOUNT = "payAmount";// 识别出来的金额
	public static final String INTENT_ADV = "INTENT_ADV";// 广告
	public static final String INTENT_TX_NO = "INTENT_TX_NO";// 订单号
	public static final String INTENT_MEMBER_DISCOUNT = "INTENT_MEMBER_DISCOUNT";// 会员折扣
	public static final String INTENT_MEMBER_CODE = "INTENT_MEMBER_CODE";// 会员编号
	public static final String INTENT_MESSAGE = "INTENT_MESSAGE";//推送的消息
	public static final String INTENT_SENDREDPACK="sendredpack";
	
	public static final String WIFI_SCANLIST="ScanList";
	public static final String WIFI_CONNECTED="Connected";
	// 整型参数
	public static int CACHE_WIFICIPHER_NOPASS = 1;
	public static int CACHE_WIFICIPHER_WEP = 2;
	public static int CACHE_WIFICIPHER_WPA = 3;

	
	// /常量(升级的常量）
	public static final String CONSTANT_APGRADE_AUTO = "A";// 自动升级
	public static final String CONSTANT_APGRADE_MANUAL = "M";// 手动升级
	public static final String CONSTANT_APGRADE_FORCE = "F";// 强制升级
	
	public static String CURRENT_APGRADE_MODE=CONSTANT_APGRADE_AUTO;

	static SharedPreferences sp_port;
	public static String IPADDRESS="ipaddress";
	public static String NETMASK="netmask";
	public static String GATEWAY="gateway";
	public static String DNS1="dns1";
	public static String DNS2="dns2";

	public static int getPrintPaper() {
		int item =  WeiLayApplication.app.mCache.getAsInt("mPrintpaperSelect", 80);
		return (item == 80) ? 46 : 32;
	}
}
