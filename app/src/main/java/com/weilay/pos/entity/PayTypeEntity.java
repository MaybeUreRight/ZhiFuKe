package com.weilay.pos.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.weilay.pos.app.PayType;
import com.weilay.pos.util.ConvertUtil;

/*****
 * @detail 支付类型对应的实体
 * @author rxwu
 *
 */
public class PayTypeEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tx_no = "";// 支付的单号
	private String tx_no2 = "";// 主扫的订单
	private PayType payType = PayType.WEIXIN;// update 20170317 cyh 支付类型使用微信 -----------支付的类型(默认储值卡支付） 
	public double amount;// 订单支付金额
	public double araamount;// 订单实付的金额
	public double disamount;// 总的折扣和优惠券的金额（amount-araamount)
	private double firstDiscount;// 首单优惠的金额

	private String memberNo;// 会员卡号
	private String memberType;// 会员类型
	private double memberDiscountableAmount;//可参与会员折扣的金额
	private double memberDiscount = 10;// 会员折扣（10为不打折）
	private double memberDiscountAmount = 0;// 会员优惠金额

	private String couponNo;// 优惠券卡号
	private double couponDiscount = 10;// 优惠券折扣（10为不打折）
	private double couponDiscountAmount = 0;// 优惠券优惠金额
	private String couponType;/// 优惠券的类型

	private QRInfoEntity qrInfo;// 支付完成后的条形码
	private String time;// 支付时间

	private String discountType ;//优惠方式:F:首单优惠,E:每单优惠,D:按天数,T:按时间
	
	private boolean isMicro = false;// 是否
	private List<AdverEntity> advers = new ArrayList<>();// 支付完成后的广告内容

	public String getCouponType() {
		return couponType;
	}
	


	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getTx_no() {
		return tx_no;
	}

	public void setTx_no(String tx_no) {
		this.tx_no = tx_no;
	}

	public String getTx_no2() {
		return tx_no2;
	}

	public void setTx_no2(String tx_no2) {
		this.tx_no2 = tx_no2;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}
	
	public double getMemberDiscountableAmount() {
		return memberDiscountableAmount;
	}
	public void setMemberDiscountableAmount(double memberDiscountableAmount) {
		this.memberDiscountableAmount = memberDiscountableAmount;
	}

	public double getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public double getMemberDiscountAmount() {
		return memberDiscountAmount;
	}

	public void setMemberDiscountAmount(double memberDiscountAmount) {
		this.memberDiscountAmount = memberDiscountAmount;
	}

	public double getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public double getCouponDiscountAmount() {
		return couponDiscountAmount;
	}

	public void setCouponDiscountAmount(double couponDiscountAmount) {
		this.couponDiscountAmount = couponDiscountAmount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAraamount() {
		return ConvertUtil.getMoeny((amount - couponDiscountAmount - memberDiscountAmount)<0?0:(amount - couponDiscountAmount - memberDiscountAmount));
	}

	public double getDisamount() {
		return amount - araamount;
	}

	public void setDisamount(double disamount) {
		this.disamount = disamount;
	}

	public QRInfoEntity getQrInfo() {
		return qrInfo;
	}

	public void setQrInfo(QRInfoEntity qrInfo) {
		this.qrInfo = qrInfo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<AdverEntity> getAdvers() {
		return advers;
	}

	public void setAdvers(List<AdverEntity> advers) {
		this.advers = advers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PayTypeEntity() {
	}

	public boolean isMicro() {
		return isMicro;
	}

	public void setMicro(boolean isMicro) {
		this.isMicro = isMicro;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public double getFirstDiscount() {
		return firstDiscount;
	}

	/*******
	 * @detail 设置首单优惠或者闲时优惠的金额，由于限时优惠和首单优惠的逻辑放在后台，因此这里需要获取实际支付的金额来计算首单优惠的金额
	 * @param firstDiscount
	 */

	public void setFirstDiscount(double firstDiscount) {
		if (firstDiscount <= 0) {
			this.firstDiscount = 0;
		} else {
			this.firstDiscount = firstDiscount;
		}
	}
	private final int flag_all=3;//使用了优惠券和会员卡优惠
	private final int flag_none=0;//不使用任何优惠
	private final int flag_coupon=1;//优惠券优惠
	private final int flag_member=2;//使用会员卡优惠
	/****
	 * @detail 
	 * @return int
	 * @param 
	 * @detail
	 */
	public int getUpdateFlag(){
		if(couponDiscountAmount>0 && memberDiscountAmount>0){
			return flag_all;
		}else if(couponDiscount>0){
			return flag_coupon;
		}else if(memberDiscountAmount>0){
			return flag_member;
		}else{
			return flag_none;
		}
	}
}
