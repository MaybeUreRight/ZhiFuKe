package com.weilay.pos.util;

import java.util.Timer;
import java.util.TimerTask;

import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.google.gson.Gson;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.WifiEntity;
import com.weilay.pos.titleactivity.BaseActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

/******
 * @Detail wifi连接管理工具类
 * @author rxwu
 *
 */
public class WifiUtils {

	private static WifiUtils instance = null;
	private BaseActivity mActivity;
	private Context mContext;

	private WifiUtils(BaseActivity activity) {
		this.mActivity = activity;
	}
	private WifiUtils(Context context) {
		this.mContext = context;
	}


	public static WifiUtils init(BaseActivity activity) {
		if (activity == null) {
			return null;
		}
		if (instance == null) {
			instance = new WifiUtils(activity);
		}
		return instance;
	}
	/**
	 * 不带进度条的初始化
	 * @param context
	 * @return
	 */
	public static WifiUtils wifiInit(Context context) {
		if (instance == null) {
			instance = new WifiUtils(context);
		}
		return instance;
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	public static boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) WeiLayApplication.app
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	private Timer wifiTimer;
	private TimerTask wifiTasker;
	private long time = 0;
	private long timeout = 30 * 60 * 1000;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				wifiReconnect();
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 不带进度条的wifi重连
	 */
	public  void wifiReconnect(){
		if (!WeiLayApplication.getSp_ip().getBoolean(PosDefine.ISWIFI, true) && WifiUtils.isWifiConnected()) {
			return;
		}
		String ssid= WeiLayApplication.app.mCache.getAsString("ssid");
		if(StringUtil.isBank(ssid))
		{
			return;
		}
		String wifijson=WeiLayApplication.app.mCache.getAsString(ssid);
		if(StringUtil.isBank(wifijson))
		{
			return;
		}
		WifiEntity mWifi=new Gson().fromJson(wifijson,WifiEntity.class);
		Log.i("gg", "不带进度条的wifi重连");
		if (!WeiLayApplication.networkunable) {
			final WifiManager wifimanger = openWifi(mContext);// 如果没打开wifi，则打开
			WifiConfiguration config = WifiUtil.createWifiInfo(ssid, mWifi.getPwd(), PosDefine.CACHE_WIFICIPHER_WPA, wifimanger);
			if (config == null) {
				return ;
			}
			int networkId = wifimanger.addNetwork(config);
			wifimanger.enableNetwork(networkId, true);
			
			if (wifiTimer != null) {
				wifiTimer.cancel();
				wifiTimer.purge();
				wifiTimer = null;
			}

			if (wifiTasker != null) {
				wifiTasker.cancel();
				wifiTasker = null;
			}
			wifiTimer = new Timer();
			wifiTasker = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					timeout += 2000;
					WifiInfo currentWifiInfo = wifimanger.getConnectionInfo();
					L.i("gg", "wifiinfo:" + currentWifiInfo.toString());
					String state = currentWifiInfo.getSupplicantState().toString();
					if (("COMPLETED".equals(state) && currentWifiInfo.getNetworkId() >= 0)
							|| "INACTIVE".equals(state)) {
						WeiLayApplication.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								T.showCenter("WIFI自动连接成功");
								cancel();
							}
						});
					} else if (time >= timeout) {
						WeiLayApplication.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								T.showCenter("WIFI自动连接超时,请手动连接!");
								handler.sendEmptyMessage(0);
								cancel();
							}
						});
					}
				}

			};
			wifiTimer.schedule(wifiTasker, 2000, 2000);
		}
	}

	public void reconnect(final boolean tip) throws Exception{
		if (!WeiLayApplication.getSp_ip().getBoolean(PosDefine.ISWIFI, true) && WifiUtils.isWifiConnected()) {
			return;
		}
		String ssid= WeiLayApplication.app.mCache.getAsString("ssid");
		if(StringUtil.isBank(ssid))
		{
			return;
		}
		String wifijson=WeiLayApplication.app.mCache.getAsString(ssid);
		if(StringUtil.isBank(wifijson))
		{
			return;
		}
		WifiEntity mWifi=new Gson().fromJson(wifijson,WifiEntity.class);
		if (!WeiLayApplication.networkunable) {
			final WifiManager wifimanger = openWifi();// 如果没打开wifi，则打开
			WifiConfiguration config = WifiUtil.createWifiInfo(ssid, mWifi.getPwd(), PosDefine.CACHE_WIFICIPHER_WPA, wifimanger);
			if (config == null) {
				return;
			}
			int networkId = wifimanger.addNetwork(config);
			wifimanger.enableNetwork(networkId, true);
			mActivity.showLoading("正在连接网络");
			if (wifiTimer != null) {
				wifiTimer.cancel();
				wifiTimer.purge();
				wifiTimer = null;
			}

			if (wifiTasker != null) {
				wifiTasker.cancel();
				wifiTasker = null;
			}
			wifiTimer = new Timer();
			wifiTasker = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					timeout += 2000;
					WifiInfo currentWifiInfo = wifimanger.getConnectionInfo();

					L.i("gg", "wifiinfo:" + currentWifiInfo.toString());
					String state = currentWifiInfo.getSupplicantState().toString();
					// ||
					//
					if (("COMPLETED".equals(state) && currentWifiInfo.getNetworkId() >= 0)
							|| "INACTIVE".equals(state)) {
						mActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(tip){
									T.showCenter("WIFI连接成功");
								}
								
								mActivity.stopLoading();// 重连成功
								cancel();
							}
						});
					} else if (time >= timeout) {
						mActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(tip){
									T.showCenter("WIFI连接超时");
								}
								mActivity.stopLoading();// 重连成功
								cancel();
							}
						});
					}
				}

			};
			wifiTimer.schedule(wifiTasker, 2000, 2000);
		}

	}

	/*****
	 * @detail 关闭轮询
	 */
	public void cancel() {
		if (wifiTimer != null) {
			wifiTimer.cancel();
			wifiTimer = null;
		}
		if (wifiTasker != null) {
			wifiTasker.cancel();
			wifiTasker = null;
		}
	}

	/*****
	 * @detail 打开wifi
	 */
	private WifiManager openWifi() {
		WifiManager wifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			// currentWifiInfo = wifiManager.getConnectionInfo();
			wifiManager.setWifiEnabled(true);
		}
		return wifiManager;
	}
	
	private WifiManager openWifi(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			// currentWifiInfo = wifiManager.getConnectionInfo();
			wifiManager.setWifiEnabled(true);
		}
		return wifiManager;
		
	}

}
