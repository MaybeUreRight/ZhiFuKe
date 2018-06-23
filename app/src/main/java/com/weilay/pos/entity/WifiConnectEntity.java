package com.weilay.pos.entity;

import java.io.Serializable;

public class WifiConnectEntity implements Serializable{
	private String name;//wifi名称
	private String pwd;//密码
	private String connectTime;//wifi接入时间
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getConnectTime() {
		return connectTime;
	}
	public void setConnectTime(String connectTime) {
		this.connectTime = connectTime;
	}
	
}
