package com.weilay.pos.entity;

/******
 * @detail 充值优惠实体
 * @author rxwu
 * @date 2016/07/14
 *
 */
public class RechargePreferEntity {
	/** 例如后台设置了规则，满300减100，那么preferMoney就是100，settingMoney就是300 **/
	private String recno;// 充值记录
	private String mid;// 商户编号
	private String name;// 充值赠送信息
	private double rechargeamt;// 满足赠送的金额
	private double largessamt;// 赠送的金额
	private String xdate;// 创建的日期
	private String starttime;// 有效起时间
	private String endtime;// 有效始时间

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRechargeamt() {
		return rechargeamt;
	}

	public void setRechargeamt(double rechargeamt) {
		this.rechargeamt = rechargeamt;
	}

	public double getLargessamt() {
		return largessamt;
	}

	public void setLargessamt(double largessamt) {
		this.largessamt = largessamt;
	}

	public String getXdate() {
		return xdate;
	}

	public void setXdate(String xdate) {
		this.xdate = xdate;
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

}
