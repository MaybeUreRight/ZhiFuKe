package com.weilay.pos.entity;

import com.weilay.pos.util.ConvertUtil;

public class ShiftRecord {
	private String time;
	private double wechat;
	private double alipay;
	private double baidu;
	private double cash;
	private double memberPayAmt;
	private String message;
	private String title;
	private String sn;
	private String operator;
	private double rechargeAmt;
	private double refundAmt;//退款金额
	private String jobtime;//上班时间
	
	
	public String getJobtime() {
		return jobtime;
	}
	public void setJobtime(String jobtime) {
		this.jobtime = jobtime;
	}
	public double getCash() {
		return ConvertUtil.branchToYuan(cash);
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public double getTotalamount() {
		return getwechat() + getalipay() + getBaidu() + getCash()+getRechargeAmt();
	}
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	public String gettime() {
		return time;
	}

	public void settime(String time) {
		this.time = time;
	}

	public double getwechat() {
		return ConvertUtil.branchToYuan(wechat);
	}

	public void setwechat(double wechat) {
		this.wechat = wechat;
	}

	public double getalipay() {
		return ConvertUtil.branchToYuan(alipay);
	}

	public void setalipay(double alipay) {
		this.alipay = alipay;
	}

	public double getBaidu() {
		return ConvertUtil.branchToYuan(baidu);
	}

	public void setBaidu(double baidu) {
		this.baidu = baidu;
	}
	
	
	public double getMemberPayAmt() {
		return ConvertUtil.branchToYuan(memberPayAmt);
	}

	public void setMemberPayAmt(double memberPayAmt) {
		this.memberPayAmt = memberPayAmt;
	}

	public double getRechargeAmt() {
		return ConvertUtil.branchToYuan(rechargeAmt);
	}
	public void setRechargeAmt(double rechargeAmt) {
		this.rechargeAmt = rechargeAmt;
	}
	
	
	public double getRefundAmt() {
		return ConvertUtil.branchToYuan(refundAmt);
	}
	public void setRefundAmt(double refundAmt) {
		this.refundAmt = refundAmt;
	}
	
	
	
}
