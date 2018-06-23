package com.weilay.pos.entity;

import android.net.wifi.ScanResult;

/*******
 * @detail wifi 配置实体 File Name:WifiEntity.java Package:com.weilay.pos.entity
 *         Date: 2017年1月13日下午4:19:36 Author: rxwu Detail:WifiEntity
 */
public class WifiEntity {
	// wifi安全模式
	public static final int SECURITY_NONE = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_PSK = 2;
	public static final int SECURITY_EAP = 3;
	private String ssid="";
	private String pwd="";
	private int security=SECURITY_NONE;//安全验证方式
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getSecurity() {
		return security;
	}
	public void setSecurity(int security) {
		this.security = security;
	}
	public static WifiEntity parseScanResult(ScanResult sr) {
		// TODO Auto-generated method stub
		if(sr==null){
			return null;
		}
		WifiEntity entity=new WifiEntity();
		entity.setSsid(sr.SSID);
		String capabilities = sr.capabilities;
		if (!capabilities.contains("WPA")
				&& !capabilities.contains("WPS")
				&& !capabilities.contains("WEP")
				&& capabilities.contains("ESS")) {
			//无密码
			entity.setSecurity(SECURITY_NONE);
		}
		return entity;
	}
	
	
}
