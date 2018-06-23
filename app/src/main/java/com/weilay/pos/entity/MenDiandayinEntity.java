package com.weilay.pos.entity;

import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.NumberUtils;

public class MenDiandayinEntity {
	private String date;
	private String weixin;
	private String zhifubao;
	private String baidu;
	private String message;
	private String title;
	private String totalAmount;
	private String cash;

	public String getCash() {
		if (cash != null && NumberUtils.isNum(cash)) {
			 double amount = Double.valueOf(cash);
			return String.valueOf(amount / 100);
		} else {
			return "0.00";
		}
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getTotalAmount() {
		double w = Double.valueOf(getWeixin());
		double z = Double.valueOf(getZhifubao());
		double c = Double.valueOf(getCash());
		double mount = w + z + c;
		return ConvertUtil.getMoeny("" + mount) + "";
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWeixin() {
		if (weixin != null && NumberUtils.isNum(weixin)) {
			double amount = Double.valueOf(weixin);
			return "" + ConvertUtil.getMoeny(amount / 100);
		} else {
			return "0.00";
		}
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getZhifubao() {
		if (zhifubao != null&&NumberUtils.isNum(zhifubao)) {
			double amount = Double.valueOf(zhifubao);
			return "" + ConvertUtil.getMoeny(amount / 100);

		} else {
			return "0.00";
		}
	}

	public void setZhifubao(String zhifubao) {
		this.zhifubao = zhifubao;
	}

}
