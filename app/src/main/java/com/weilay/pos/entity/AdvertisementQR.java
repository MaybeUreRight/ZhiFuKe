package com.weilay.pos.entity;

public class AdvertisementQR {
	private String qrCode;
	private String topMessage;
	private String bottomMessage;

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
