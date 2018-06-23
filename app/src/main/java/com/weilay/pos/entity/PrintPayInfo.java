package com.weilay.pos.entity;

import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.NumberUtils;

public class PrintPayInfo {
	private String payType;
	private String tx_no;
	private String amount;
	private String payTime;
	private String qrCode;
	private String topMessage;
	private String bottomMessage;
	private String qrType;

	public String getQrType() {
		return qrType;
	}

	public void setQrType(String qrType) {
		this.qrType = qrType;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayType() {
		String paytype = payType.equals("W") ? "微信支付"
				: payType.equals("Z") ? "支付宝" : "现金支付";
		return paytype;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTx_no() {
		return tx_no;
	}

	public void setTx_no(String tx_no) {
		this.tx_no = tx_no;
	}

	public String getAmount() {
		if (amount != null && NumberUtils.isNum(amount)) {
			return String.valueOf(ConvertUtil.getMoeny(amount) / 100);
		} else {
			return "0.00";
		}

	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getTopMessage() {
		return topMessage;
	}

	public void setTopMessage(String topMessage) {
		this.topMessage = topMessage;
	}

	public String getBottomMessage() {
		return bottomMessage;
	}

	public void setBottomMessage(String bottomMessage) {
		this.bottomMessage = bottomMessage;
	}

}
