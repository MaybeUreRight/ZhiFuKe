package com.weilay.pos.entity;

import java.io.Serializable;

public class PayAdvertisementEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tx_no;
	private String totalAmount;
	private String payType;
	private String adver_data_picUrl;
	private String adver_data_txttitle;
	private String adver_data_txtsubtitle;
	private String adver_data_adpostion;
	private String qrinfo_qrCode;
	private String qrinfo_top;
	private String qrinfo_bottom;
	private String qrinfo_qrType;
	private String time;
	//private boolean isPassive;
	private String qrText;
	
	public String getQrText() {
		return qrText;
	}
	public void setQrText(String qrText) {
		this.qrText = qrText;
	}
	/*public boolean isPassive() {
		return isPassive;
	}
	public void setPassive(boolean isPassive) {
		this.isPassive = isPassive;
	}*/
	public String getTx_no() {
		return tx_no;
	}
	public void setTx_no(String tx_no) {
		this.tx_no = tx_no;
	}
	public String getTotalAmount() {
		double mAmount = Double.valueOf(totalAmount);

		return String.valueOf(mAmount / 100d);
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPayType() {
		String paytype = payType.equals("W") ? "微信"
				: payType.equals("Z") ? "支付宝" : "现金";
		/*if(isPassive()){
			paytype+="刷卡";
		}*/
		return paytype;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getAdver_data_picUrl() {
		return adver_data_picUrl;
	}
	public void setAdver_data_picUrl(String adver_data_picUrl) {
		this.adver_data_picUrl = adver_data_picUrl;
	}
	public String getAdver_data_txttitle() {
		return adver_data_txttitle;
	}
	public void setAdver_data_txttitle(String adver_data_txttitle) {
		this.adver_data_txttitle = adver_data_txttitle;
	}
	public String getAdver_data_txtsubtitle() {
		return adver_data_txtsubtitle;
	}
	public void setAdver_data_txtsubtitle(String adver_data_txtsubtitle) {
		this.adver_data_txtsubtitle = adver_data_txtsubtitle;
	}
	public String getAdver_data_adpostion() {
		return adver_data_adpostion;
	}
	public void setAdver_data_adpostion(String adver_data_adpostion) {
		this.adver_data_adpostion = adver_data_adpostion;
	}
	public String getQrinfo_qrCode() {
		return qrinfo_qrCode;
	}
	public void setQrinfo_qrCode(String qrinfo_qrCode) {
		this.qrinfo_qrCode = qrinfo_qrCode;
	}
	public String getQrinfo_top() {
		return qrinfo_top;
	}
	public void setQrinfo_top(String qrinfo_top) {
		this.qrinfo_top = qrinfo_top;
	}
	public String getQrinfo_bottom() {
		return qrinfo_bottom;
	}
	public void setQrinfo_bottom(String qrinfo_bottom) {
		this.qrinfo_bottom = qrinfo_bottom;
	}
	public String getQrinfo_qrType() {
		return qrinfo_qrType;
	}
	public void setQrinfo_qrType(String qrinfo_qrType) {
		this.qrinfo_qrType = qrinfo_qrType;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


}
