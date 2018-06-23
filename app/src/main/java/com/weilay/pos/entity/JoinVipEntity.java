package com.weilay.pos.entity;

import java.io.Serializable;

public class JoinVipEntity implements Serializable {
	private String name;
	private String cardinfo;
	private String starttime;
	private String endtime;
	private String logo;
	private String bgurl;
	private boolean isFriendCard;
	private String show_qrcode_url;
	private String url2qrcode;
	
	public String getBgurl() {
		return bgurl;
	}
	public void setBgurl(String bgurl) {
		this.bgurl = bgurl;
	}
	public String getUrl2qrcode() {
		return url2qrcode;
	}
	public void setUrl2qrcode(String url2qrcode) {
		this.url2qrcode = url2qrcode;
	}
	public boolean isFriendCard() {
		return isFriendCard;
	}
	public void setFriendCard(boolean isFriendCard) {
		this.isFriendCard = isFriendCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardinfo() {
		return cardinfo;
	}
	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getShow_qrcode_url() {
		return show_qrcode_url;
	}
	public void setShow_qrcode_url(String show_qrcode_url) {
		this.show_qrcode_url = show_qrcode_url;
	}
	
}
