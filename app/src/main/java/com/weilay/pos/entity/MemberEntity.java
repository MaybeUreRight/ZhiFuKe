package com.weilay.pos.entity;

import java.io.Serializable;

/**
 * Created by rxwu on 2016/4/20.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:会员信息实体
 */
public class MemberEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nickname;// 昵称
	private String level;// 会员等级
	private String membership_number;// 会员编号
	private int member_card_type;//会员卡的类型 0表示次数卡 1表示普通卡
	//private String member_times_level;//会员卡的次数等级
	private int bonus;// 积分
	private double giveamount;// 赠送余额
	private double balance;// 余额
	private String phone;// 手机号码
	private String openId;//
	
	
	public int getMember_card_type() {
		return member_card_type;
	}
/*	public String getMember_times_level() {
		return member_times_level;
	}*/
	public void setMember_card_type(int member_card_type) {
		this.member_card_type = member_card_type;
	}
/*	public void setMember_times_level(String member_times_level) {
		this.member_times_level = member_times_level;
	}*/
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getGiveamount() {
		return giveamount;
	}

	public void setGiveamount(double giveamount) {
		this.giveamount = giveamount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMembership_number() {
		return membership_number;
	}

	public void setMembership_number(String membership_number) {
		this.membership_number = membership_number;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
