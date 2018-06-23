package com.weilay.pos.app;

import java.io.Serializable;

import com.weilay.pos.R;

public enum PayType implements Serializable{
	WEIXIN("W", "微信", R.drawable.icon_paytype_weixin), ALIPAY("Z", "支付宝", R.drawable.icon_paytype_alipay),
	// BAIDU("B", "百度钱包",R.drawable.icon_b),
	// CARD("CARD", "储值卡",R.drawable.icon_vip),
	CASH("X", "现金", R.drawable.icon_paytype_cash), SALE("Y", "优惠券", R.drawable.icon_paytype_coupon),
	// TUANGO("TUANGO", "团购券",R.drawable.),
	CHUZHIKA("CHUZHIKA", "会员卡", R.drawable.icon_paytype_vipcard),
	OFFLINERECHAREG("OFFLINERECHAREG","会员充值",R.drawable.icon_paytype_recharge_normal);
	
	// 成员变量
	private String name;
	private String value;
	private int icon;

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	// 构造方法
	PayType(String name, String value, int icon) {
		this.name = name;
		this.value = value;
		this.icon = icon;
	}

	public static String getPayStr(String name) {
		for (PayType c : PayType.values()) {
			if (c.name.equals(name)) {
				return c.value;
			}
		}
		return "";
	}

	public static PayType getPayTypeDefine(String name) {
		for (PayType c : PayType.values()) {
			if (c.name.equals(name)) {
				return c;
			}
		}
		return null;
	}
}
