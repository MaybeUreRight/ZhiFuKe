/*package com.weilay.pos.entity;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.NumberUtils;

public class RefundPrint implements Serializable{
	private String recno;
	private String mid;
	private String sn;
	private String paytype;
	private String tx_no;
	private String totalamount;
	private String sucessed;
	private String refno;
	private String txtime;
	private String title;
	private String refundno;
	private double refundamount;
	private String remarks;

	public String getRefundno() {
		return refundno;
	}

	public void setRefundno(String refundno) {
		this.refundno = refundno;
	}

	public double getRefundamount() {
		if (NumberUtils.isNum(refundamount)) {
			return ConvertUtil.getMoeny(refundamount) / 100;
		} else {
			return 0.00;
		}

	}

	public void setRefundamount(double refundamount) {
		this.refundamount = refundamount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

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

	public String getPaytype() {
		String type = "W".equals(paytype) ? "微信支付"
				: "Z".equals(paytype) ? "支付宝支付" : "现金支付";
		return type;
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

	public String getTotalamount() {
		if (totalamount != null && NumberUtils.isNum(totalamount)) {

			return String.valueOf(ConvertUtil.getMoeny(totalamount) / 100);
		} else {
			return "0.00";
		}

	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public String getSucessed() {
		return sucessed;
	}

	public void setSucessed(String sucessed) {
		this.sucessed = sucessed;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getTxtime() {
		long date = Long.valueOf(txtime);
		Date d = new Date(date * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	public void setTxtime(String txtime) {
		this.txtime = txtime;
	}

}
*/