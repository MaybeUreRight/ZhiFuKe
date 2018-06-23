package com.weilay.pos.util;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.ScanResult;
import android.util.Log;

public class EditorUtil {
	private static List<String> keys = new ArrayList<String>();
	
	/**
	 * 保存连接成功的wifi
	 * @param SSID
	 * @param Pwd
	 * @param edit
	 */
	public static void putWifi(String SSID, String Pwd, Editor edit) {
		edit.putString(SSID, Pwd);
		edit.commit();
	}

	/**
	 * 得到密码已保存的wifi
	 * @param listSR
	 * @param sp
	 * @return
	 */
	public static List<String> getWifi(List<ScanResult> listSR,
			SharedPreferences sp) {
		for (ScanResult item : listSR) {
			String SSID = item.SSID;
			Log.i("gg", "wifiName:" + SSID);
			if (SSID.equals(sp.getString(SSID, ""))) {
				keys.add(sp.getString(SSID, ""));
			}
		}
		return keys;
	}
}
