package com.weilay.pos.entity;

import java.io.Serializable;

import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.NumberUtils;

public class VipCoupon implements Serializable {
	private String id;
	private String type;
	private String amount;
	private String info;
	private boolean selectType = false;
	private String leastmoney;

	public double getLeastmoney() {
		if (leastmoney != null && NumberUtils.isNum(leastmoney)) {
			return ConvertUtil.getMoeny(leastmoney) / 100;
		} else {
			return 0.00;
		}

	}

	public void setLeastmoney(String leastmoney) {
		this.leastmoney = leastmoney;
	}

	public boolean isSelectType() {
		return selectType;
	}

	public void setSelectType(boolean selectType) {
		this.selectType = selectType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		if (amount != null && NumberUtils.isNum(amount)) {

			return ConvertUtil.getMoeny(amount) / 100;
		} else {
			return 0.00;
		}

	}

	public String getAmountStr() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
