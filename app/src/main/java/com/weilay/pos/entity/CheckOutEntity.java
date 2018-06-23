package com.weilay.pos.entity;

import java.io.Serializable;

import com.weilay.pos.app.PayType;
import com.weilay.pos.util.ConvertUtil;


/******
 * 
 * File Name:CheckOutEntity.java
 * Package:com.weilay.pos.entity	
 * Date: 2016年12月12日上午10:08:53
 * Author: rxwu
 * Detail:CheckOutEntity 完结订单的详情
 */
public class CheckOutEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String recno;//对应我们系统的ID
	private String mid;//商户编码
	private String sn;//设备编码
	//MR;会员充值
	private String paytype;//支付方式
	private String tx_no;//订单编号
	private String totalamount;//订单
	
	private String txtime;//订单时间
	private String remarks;//订单备注	
	
	
	private String refno;//支付宝或者微信的订单的
	private String refundno;//微信的退款单号
	private double refundamount;//退款的金额
	private int isrefunded;//0是代表没有退款，1 表示已经退过款
	
	
	//Y表示订单成功 N 表示订单成功
	private String successed="Y";
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
	
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	
	public String getTx_no() {
		return tx_no;
	}

	public void setTx_no(String tx_no) {
		this.tx_no = tx_no;
	}

	public String getTotalamountYuan() {
		double amount = ConvertUtil.getMoeny(totalamount) / 100;
		// String myamount = String.valueOf(amount / 100d);
		return String.valueOf(amount < 0 ? 0 : amount);
	}

	public String getTotalamountFen() {
		return totalamount;
	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getTxtime() {
		return txtime;
	}

	public void setTxtime(String txtime) {
		this.txtime = txtime;
	}

	public PayType getPayMethod() {
		// TODO Auto-generated method stub
		return PayType.getPayTypeDefine(paytype);
	}
	


	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRefundno() {
		return refundno;
	}

	public void setRefundno(String refundno) {
		this.refundno = refundno;
	}

	public double getRefundamount() {
		return refundamount;
	}

	public void setRefundamount(double refundamount) {
		this.refundamount = refundamount;
	}

	public String getSuccessed() {
		return successed;
	}

	public void setSuccessed(String successed) {
		this.successed = successed;
	}

	public String getTotalamount() {
		return totalamount;
	}
	
	
	public int getIsrefunded() {
		return isrefunded;
	}
	public void setIsrefunded(int isrefunded) {
		this.isrefunded = isrefunded;
	}
	/****
	 * @detail 是否退过款
	 * @return void
	 * @param 
	 */
	public boolean isRefund(){
		return isrefunded==0?false:true;
	}
	
	
}
