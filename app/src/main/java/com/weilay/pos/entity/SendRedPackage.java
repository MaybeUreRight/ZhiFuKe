package com.weilay.pos.entity;

import java.io.Serializable;

import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.NumberUtils;

public class SendRedPackage implements Serializable {
	private String txno;
	private String amount;
	private String time;
	private String qrcode;
	private String errcode;

	public String getTxno() {
		return txno;
	}

	public void setTxno(String txno) {
		this.txno = txno;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
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
}
