package com.weilay.pos.entity;

import java.io.Serializable;

import com.framework.utils.ConvertUtil;

public class VipInfo implements Serializable {
	private String vipid;// 会员的卡号
	private String viptype;// 会员的类型
	private double discount;// 折扣
	private String balance;// 金额
	private String bonus;// 会员积分
	private double giveamount;// 赠送金额
	private double totalamount;//总金额
	
	private String openId;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public double getGiveamount() {
		return giveamount;
	}

	public void setGiveamount(double giveamount) {
		this.giveamount = giveamount;
	}

	public String getVipid() {
		return vipid;
	}

	public void setVipid(String vipid) {
		this.vipid = vipid;
	}

	public String getViptype() {
		return viptype;
	}

	public void setViptype(String viptype) {
		this.viptype = viptype;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getBalance() {
		return Double.valueOf(balance);
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	
	
	public double getTotalamount() {
		return ConvertUtil.getMoney(getGiveamount()+getBalance());
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}

}
