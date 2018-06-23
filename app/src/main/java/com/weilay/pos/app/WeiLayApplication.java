package com.weilay.pos.app;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.iflytek.cloud.SpeechUtility;
import com.lidroid.xutils.DbUtils;
import com.rxwu.helper.USBProtolHelper;
import com.weilay.pos.db.MessageDBHelper;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.MessageEntity;
import com.weilay.pos.http.SystemRequest;
import com.weilay.pos.print.ComPort;
import com.weilay.pos.util.ACache;
import com.weilay.pos.util.CmdForAndroid;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.IFlytekHelper;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;
import com.weilay.pos.util.WifiUtils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

public class WeiLayApplication extends Application {
	private static SharedPreferences sp_ip;// 保存以太网静态IP
	private static SharedPreferences sp_port;// 保存打印机设置
	private static SharedPreferences sp_login;// 保存登录信息
	private static SharedPreferences sp_wifi;// wifi密码
	private Editor portEditor;
	public static USBProtolHelper usbHelper = null;
	public static WeiLayApplication app;
	// 判断当前的网络是否是可用的
	public static boolean networkunable = false;

	private static boolean isUsb = true;
	public static boolean UPGRADE = true;
	public static boolean printLog = false;
	public static boolean ISWIFI = true;
	public static NetworkInfo networkInfo;
	public static WifiManager wifiManager;
	public static boolean Login_Suc = false;// 是否已经登录

	public static boolean isLogin_Suc() {
		return Login_Suc;
	}

	public static void setLogin_Suc(boolean login_Suc) {
		Login_Suc = login_Suc;
	}	

	public static WifiManager getWifiManager() {
		if (wifiManager == null) {
			wifiManager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
		}
		return wifiManager;
	}

	public static NetworkInfo getNetworkInfo() {
		if (networkInfo == null) {
			ConnectivityManager connManager = (ConnectivityManager) app.getSystemService(CONNECTIVITY_SERVICE);
			networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		return networkInfo;

	}

	public static USBProtolHelper getUsbHelper() {
		if (usbHelper == null || !usbHelper.isEnable()) {
			usbHelper = USBProtolHelper.init(app);
		}
		if (!usbHelper.isEnable()) {
			T.showCenter("找不到打印机");
		}
		return usbHelper;
	}

	public static USBProtolHelper getUsbHelper(boolean log) {
		if (usbHelper == null || !usbHelper.isEnable()) {
			usbHelper = USBProtolHelper.init(app);
		}
		if (!usbHelper.isEnable() && log) {
			T.showCenter("找不到打印机");
		}
		return usbHelper;
	}

	public static boolean isUsb() {
		L.i("gg", "isUsb:" + isUsb);
		return isUsb;
	}

	public static SharedPreferences getSp_wifi() {
		return sp_wifi;
	}

	public static void setUsb(boolean isUsb) {
		WeiLayApplication.isUsb = isUsb;
	}

	public static SharedPreferences getSp_port() {
		return sp_port;
	}

	public static SharedPreferences getSp_ip() {
		return sp_ip;
	}

	public static SharedPreferences getSp_login() {
		return sp_login;
	}
	public SpeechUtility sPeechUtility;
	@Override
	public void onCreate() {
		// 启动讯飞语音合成工具
		// TODO Auto-generated method stub
		super.onCreate();
		// update by rxwu
		app = this;
		installDB();
		mCache = ACache.get(this);
		sp_ip = this.getSharedPreferences("weilayIp", 0);
		sp_port = this.getSharedPreferences("weilayPort", 0);
		sp_login = this.getSharedPreferences("weilayLogin", 0);
		sp_wifi = this.getSharedPreferences("weilayWifi", 0);
		registerReceiver(networkstateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		init();
		IFlytekHelper.init(this);
	}

	private void init() {
		portEditor = sp_port.edit();
		portEditor.putString("sn", GetUtil.getimei(this));
		portEditor.commit();
		bugtags();
		ISWIFI = sp_ip.getBoolean(PosDefine.ISWIFI, true);

	}

	private void bugtags() {
		BugtagsOptions options = new BugtagsOptions.Builder().trackingLocation(true). // 是否获取位置
				trackingCrashLog(true). // 是否收集闪退
				trackingConsoleLog(true). // 是否收集控制台日志
				trackingUserSteps(true). // 是否跟踪用户操作步骤
				crashWithScreenshot(true). // 收集闪退是否附带截图
				versionName(GetUtil.getversionName(this)). // 自定义版本名称
				versionCode(GetUtil.getversionCode(this)). // 自定义版本号
				trackingNetworkURLFilter("(.*)").// 自定义网络请求跟踪的 url 规则
				build();
		Bugtags.start("a9038f73e7cff38dcf44d40aabb58817", this,

				Bugtags.BTGInvocationEventNone, options);
	}
	private final String DB_NAME = "weilai";
	public static DbUtils db;

	/*****
	 * @Detail 安装db
	 */
	private void installDB() {
		db = DbUtils.create(this, DB_NAME);
		try {
			db.createTableIfNotExist(MessageEntity.class);
			db.createTableIfNotExist(CouponEntity.class);
			// 清除超过7天的消息
			MessageDBHelper.clearMessage();

		} catch (Exception e) {
			// TODO: handle exception
			L.e(e.getLocalizedMessage());
		}
	}

	/*****
	 * @deetail 新功能工具 create by rxwu at 2016/07/08
	 */
	public ACache mCache;

	private static Handler handler = new Handler();

	public static void runOnUiThread(Runnable runable) {
		if (runable != null) {
			handler.post(runable);
		}
	}

	private BroadcastReceiver networkstateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
			case WifiManager.NETWORK_STATE_CHANGED_ACTION:
				NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				Editor editor = getSp_ip().edit();
				if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
					if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
						Log.i("gg", "wifi网络连接断开");
					} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
						WifiInfo wifiInfo = getWifiManager().getConnectionInfo();
						Log.i("gg", "连接到网络 " + wifiInfo.getSSID());
					}
				}

				if (info != null) {
					switch (info.getType()) {
					case ConnectivityManager.TYPE_ETHERNET:
						ISWIFI = false;
						editor.putBoolean(PosDefine.ISWIFI, ISWIFI);

						getWifiManager().setWifiEnabled(false);
						Log.i("gg", "networkinfo is ethernet,close wifi");
						T.showShort("本地网络已连接!");
						break;
					default:
						ISWIFI = true;
						try {
							WifiUtils.wifiInit(context).wifiReconnect();
						} catch (Exception e) {
							e.printStackTrace();
						}
						editor.putBoolean(PosDefine.ISWIFI, ISWIFI);
						CmdForAndroid.shella("su", "netcfg eth0 down");
						Log.i("gg", "networkinfo is wifi,close ethernet");
						break;
					}
				}
				editor.commit();
				networkunable = (info != null && info.isConnected()) ? true : false;
				if (networkunable) {
					L.i("网络恢复连接成功");
					if (StringUtil.isBank(GetUtil.getimei())) {
						SystemRequest.signSnID(null);// 如果sn码为空的，那么签名生成一个
					}
					Utils.getServerTime();// 同步服务器时间
				} else {
					Log.i("gg", "network is not connected.the network type is:" + info.getTypeName());
				}
				break;

			default:
				break;
			}

		}
	};
	
	

	// 串口读工具
	private static ComPort comPort;

	public static ComPort getComPort() {
		if (comPort == null) {
			return comPort = new ComPort(app);
		}
		return comPort;
	}
	public static boolean isOpen=false;
	//打开
	public static void openComPort() {
		if(!isOpen) {
			isOpen=true;
			getComPort().openMainCom();
		}
		
	}
	//关闭
	public static void closeComPort(){
		getComPort().closeMainCom();
	}

}
