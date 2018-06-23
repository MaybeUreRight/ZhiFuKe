package com.weilay.pos.entity;


/*********
 * @Detail 充值锁的实体
 * File Name:RechageLockEntity.java
 * Package:com.weilay.pos.entity	
 * Date: 2017年2月24日下午2:09:07
 * Author: rxwu
 * Detail:RechageLockEntity
 */
public class RechageLockEntity {
	private String optype="1";
	private String sceneid;
	private String url;
	public String getSceneid() {
		return sceneid;
	}
	public void setSceneid(String sceneid) {
		this.sceneid = sceneid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	
	
}
