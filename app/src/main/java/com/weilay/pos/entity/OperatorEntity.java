package com.weilay.pos.entity;

/*******
 * @detail 操作员的信息
 * @author Administrator
 *
 */
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.framework.utils.StringUtil;
import com.weilay.pos.app.PayType;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/3/22.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:操作员登陆后的信息实体
 */
public class OperatorEntity implements Serializable {
	//支付的权限0表示没有支付权限 1表示有主扫被扫两种权限 2.刷卡 3.扫码支付
	public static final int PAY_PERMISSION_NONE=0;
	public static final int PAY_PERMISSION_ALL=1;
	public static final int PAY_PERMISSION_MICRO=2;
	public static final int PAY_PREMISSION_UNIFIED=3;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mid;// 商户编号
	private String name;// 名称
	private String password;// 密码（保存加密后）
	private String validto;// 最后一次登录时间
	private String shopname;// 商店名称

	private String upgradetype="";
	
	public String getUpgradetype() {
		return upgradetype;
	}
	
	public void setUpgradetype(String upgradetype) {
		this.upgradetype = upgradetype;
	}
	//是否有支付的权限
	private int wechatpay;//微信支付
	private int alipay;//支付宝支付权限
	private int memberpay=PAY_PERMISSION_MICRO;//会员卡默认主扫权限
	private int salepay=PAY_PERMISSION_MICRO;//优惠券默认主扫权限
	
	
	private String operator;// 操作员号
	private String operatorname;// 操作员姓名
	private String img;// 操作员头像
	
	private String status;// 操作员状态
	private String statusname;
	private String lastdailyclosing;// 上次交班时间
	private String joinadalliance;// 是否加入广告联盟

	
	private String address;// 商户的地址
	private String mobile;//商户的联系电话

	private String machineaddress;// 设备的地址
	private String storemobile;// 门店电话
	private String storeaddress;// 门店地址
	private String business_name;// 总店名称
	private String branch_name;// 分店名称
	private int qrcode_lock_1;//会员充值锁	1表示需要验证
	
	

	public int getQrcode_lock_1() {
		return qrcode_lock_1;
	}

	public void setQrcode_lock_1(int qrcode_lock_1) {
		this.qrcode_lock_1 = qrcode_lock_1;
	}

	public String getMachineaddress() {
		return TextUtils.isEmpty(machineaddress)?address:machineaddress;
	}

	public void setMachineaddress(String machineaddress) {
		this.machineaddress = machineaddress;
	}
	public String getStoremobile() {
		return StringUtil.isNotEmpty(storemobile)?storemobile:mobile;
	}

	public void setStoremobile(String storemobile) {
		this.storemobile = storemobile;
	}

	public String getStoreaddress() {
		//update by rxwu，要求先显示 
		return TextUtils.isEmpty(storeaddress)?address:storeaddress;
	}

	public void setStoreaddress(String storeaddress) {
		this.storeaddress = storeaddress;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getBranch_name() {
		return business_name+"-"+branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	// 代理商信息
	private String agentid;// 代理商id
	private String agentname;// 代理商名称

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getJoinadalliance() {
		return joinadalliance;
	}

	public void setJoinadalliance(String joinadalliance) {
		this.joinadalliance = joinadalliance;
	}

	public String getLastdailyclosing() {
		if (lastdailyclosing == null || "".equals(lastdailyclosing)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(new Date());// .curDate(TimeUtil.sdf4);
		}
		return lastdailyclosing;
	}

	public void setLastdailyclosing(String lastdailyclosing) {
		this.lastdailyclosing = lastdailyclosing;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getName() {

		return StringUtil.isNotEmpty(name) ? name : "广州未莱";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValidto() {
		return validto;
	}

	public void setValidto(String validto) {
		this.validto = validto;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	

	public int getWechatpay() {
		return wechatpay;
	}

	public void setWechatpay(int wechatpay) {
		this.wechatpay = wechatpay;
	}

	public int getAlipay() {
		return alipay;
	}

	public void setAlipay(int alipay) {
		this.alipay = alipay;
	}

	public int getMemberpay() {
		return memberpay;
	}

	public void setMemberpay(int memberpay) {
		this.memberpay = memberpay;
	}

	public boolean isJoinadalliance() {
		if (getJoinadalliance() != null && "y".equals(getJoinadalliance().toLowerCase())) {
			return true;
		}
		return false;
	}
	
	/*****
	 * @detail 根据支付方式获取对应的权限
	 * @return int
	 * @param 
	 * @detail
	 */
	public  int getPermission(PayType paytype)
	{
		switch (paytype) {
		case WEIXIN:
			return wechatpay;
		case CHUZHIKA:
			return memberpay;
		case ALIPAY:
			return alipay;
		case SALE:
			return salepay;
		default:
			return PAY_PERMISSION_ALL;
		}
	}
}
