package com.weilay.pos.entity;

import java.io.Serializable;

/**
 * Created by rxwu on 2016/6/16.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:优惠券信息
 */
@SuppressWarnings("serial")
public class CouponEntity implements Serializable {
	private String tx_no;// 订单号
	private boolean auto;// 是否设置成自动发券
	private String id;
	private String code;// 卡号
	private int stock;// 库存
	private String deadline;//有效期
	private String cardinfo;// 卡券的信息
	private String merchantlogo;// 商户logo
	private String url2qrcode;// 卡券二维码地址
	private String merchantname;// 商户名称
	private double amount;// 金额
	private String info;// 说明
	private String type;// 卡券类型
	private String date = "";// 卡券有效期
	private String logo;// 卡券对logo
	private String notice;// 使用提示
	private String title;// 卡券标题
	// private int qty;// 卡券的数量
	private double merchantcommission;// 任务奖励（分）
	private double leastmoney;// 原先的可使用 限制金额
	private double least_cost;// 满？少可使用（使用条件）
	private double reduce_cost;
	private long time;// 对应数据库里面保存的最后一次时间

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private void gettime() {
		// TODO Auto-generated method stub

	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getMerchantcommission() {
		return merchantcommission;
	}

	public void setMerchantcommission(double merchantcommission) {
		this.merchantcommission = merchantcommission;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public double getLeastmoney() {
		return leastmoney;
	}

	public void setLeastmoney(double leastmoney) {
		this.leastmoney = leastmoney;
	}

	public boolean isAuto() {
		return auto;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getCardinfo() {
		return cardinfo;
	}

	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}

	public String getMerchantlogo() {
		return merchantlogo;
	}

	public void setMerchantlogo(String merchantlogo) {
		this.merchantlogo = merchantlogo;
	}

	public String getUrl2qrcode() {
		return url2qrcode;
	}

	public void setUrl2qrcode(String url2qrcode) {
		this.url2qrcode = url2qrcode;
	}

	public String getMerchantname() {
		return merchantname;
	}

	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	/*
	 * public int getQty() { return qty; }
	 * 
	 * public void setQty(int qty) { this.qty = qty; }
	 */

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		if (date != null) {
			this.date = date;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private boolean use;

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getLeast_cost() {
		return least_cost;
	}

	public void setLeast_cost(double least_cost) {
		this.least_cost = least_cost;
	}

	public double getReduce_cost() {
		return reduce_cost;
	}

	public void setReduce_cost(double reduce_cost) {
		this.reduce_cost = reduce_cost;
	}

	public String getTx_no() {
		return tx_no;
	}

	public void setTx_no(String tx_no) {
		this.tx_no = tx_no;
	}
}
