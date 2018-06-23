package com.weilay.pos.entity;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class VerificationSheet {
	private String recno;
	private String mid;
	private String sn;
	private String code;
	private String ctime;
	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRecno() {
		return recno;
	}
	public void setRecno(String recno) {
		this.recno = recno;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCtime() {
	
		long date=Long.valueOf(ctime);
		Date d = new Date(date*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return sdf.format(d);
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
}
