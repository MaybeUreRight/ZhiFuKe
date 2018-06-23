package com.weilay.pos.util;

import java.util.List;

import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class WifiUtil {
	/**
	 * 配置wifi
	 * 
	 * @param SSID
	 * @param Password
	 * @param Type
	 * @return
	 */
	public static WifiConfiguration createWifiInfo(String SSID, String Password, int Type, WifiManager wifiManager) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		WifiConfiguration tempConfig = isExsits(SSID, wifiManager);
		if (tempConfig != null) {
			wifiManager.removeNetwork(tempConfig.networkId);
		}
		if (Type == PosDefine.CACHE_WIFICIPHER_NOPASS) // WIFICIPHER_NOPASS
		{
			// config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			// config.wepTxKeyIndex = 0;
		}
		if (Type == PosDefine.CACHE_WIFICIPHER_WEP) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == PosDefine.CACHE_WIFICIPHER_WPA) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	/**
	 * 判断wifi是否存在
	 * 
	 * @param SSID
	 * @param wifiManager
	 * @return
	 */
	private static WifiConfiguration isExsits(String SSID, WifiManager wifiManager) {
		List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
		if (existingConfigs == null) {
			return null;
		}
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID != null) {
				if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
					return existingConfig;
				}
			}
		}
		return null;
	}

	/**
	 * 转换IP地址
	 * 
	 * @param i
	 * @return
	 */
	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
	}
	
	/** 
	 * 利用WifiConfiguration.KeyMgmt的管理机制，来判断当前wifi是否需要连接密码 
	 * @param wifiManager 
	 * @return true：需要密码连接，false：不需要密码连接 
	 */  
	public static boolean checkIsCurrentWifiHasPassword(String currentWifiSSID) {  
	   try {  
	        WifiManager wifiManager = (WifiManager) WeiLayApplication.app.getSystemService(WeiLayApplication.app.WIFI_SERVICE);  
	  
	        // 得到当前连接的wifi热点的信息  
	        WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
	  
	        // 得到当前WifiConfiguration列表，此列表包含所有已经连过的wifi的热点信息，未连过的热点不包含在此表中  
	        List<WifiConfiguration> wifiConfiguration = wifiManager.getConfiguredNetworks();  
	  
	        String currentSSID = wifiInfo.getSSID();  
	        if (currentSSID != null && currentSSID.length() > 2) {   
	            if (currentSSID.startsWith("\"") && currentSSID.endsWith("\"")) {  
	                currentSSID = currentSSID.substring(1, currentSSID.length() - 1);  
	            }  
	  
	            if (wifiConfiguration != null && wifiConfiguration.size() > 0) {  
	                for (WifiConfiguration configuration : wifiConfiguration) {  
	                    if (configuration != null && configuration.status == WifiConfiguration.Status.CURRENT) {  
	                        String ssid = null;  
	                        if (!TextUtils.isEmpty(configuration.SSID)) {  
	                            ssid = configuration.SSID;  
	                            if (configuration.SSID.startsWith("\"") && configuration.SSID.endsWith("\"")) {  
	                                ssid = configuration.SSID.substring(1, configuration.SSID.length() - 1);  
	                            }  
	                        }  
	                        if (TextUtils.isEmpty(currentSSID) || currentSSID.equalsIgnoreCase(ssid)) {  
	                            //KeyMgmt.NONE表示无需密码  
	                            return (!configuration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE));  
	                        }  
	                    }  
	                }  
	            }  
	        }
	        } catch (Exception e) {  
	            //do nothing  
	        }  
	    //默认为需要连接密码  
	    return true;  
	}  
}
