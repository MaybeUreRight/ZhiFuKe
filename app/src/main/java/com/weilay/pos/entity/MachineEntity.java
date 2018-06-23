package com.weilay.pos.entity;

/*******
 * @detail 获取订单的识别格式
 * @author Administrator
 *
 */
public class MachineEntity {

	private String code;
	private String message;
	private String machinename;
	private String recognizetype;
	private String keyword;
	private String charset;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private String ip;
	private String merchanrtname;
	private String joinadalliance;
	private boolean uploadinvoiceformat;
	private String upgradetype;
	
	public String getUpgradetype() {
		return upgradetype;
	}
	
	public void setUpgradetype(String upgradetype) {
		this.upgradetype = upgradetype;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMachinename() {
		return machinename;
	}

	public void setMachinename(String machinename) {
		this.machinename = machinename;
	}

	public String getRecognizetype() {
		return recognizetype;
	}

	public void setRecognizetype(String recognizetype) {
		this.recognizetype = recognizetype;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMerchanrtname() {
		return merchanrtname;
	}

	public void setMerchanrtname(String merchanrtname) {
		this.merchanrtname = merchanrtname;
	}

	public String getJoinadalliance() {
		return joinadalliance;
	}

	public void setJoinadalliance(String joinadalliance) {
		this.joinadalliance = joinadalliance;
	}

	public boolean isUploadinvoiceformat() {
		return uploadinvoiceformat;
	}

	public void setUploadinvoiceformat(boolean uploadinvoiceformat) {
		this.uploadinvoiceformat = uploadinvoiceformat;
	}

}
