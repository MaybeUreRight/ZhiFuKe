package com.weilay.pos.entity;

/*******
 * @detail 充值次数卡
 * File Name:MemberTimesLevelEntity.java
 * Package:com.weilay.pos.entity	
 * Date: 2017年4月18日下午4:19:46
 * Author: rxwu
 * Detail:MemberTimesLevelEntity
 */
public class MemberTimesLevelEntity {
	 private int id;// "id",
	 private String  period;//有效期
	 private double level_amount;//充值金额
	 private String times;//次数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public double getLevel_amount() {
		return level_amount;
	}
	public void setLevel_amount(double level_amount) {
		this.level_amount = level_amount;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	 
}
