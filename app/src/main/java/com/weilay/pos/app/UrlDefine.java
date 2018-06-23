package com.weilay.pos.app;

public class UrlDefine {
	public static final String BASE_URL = Config.ROOT_URL;
	/*******
	 * @detail 获取系统信息
	 */
	public static final String URL_GET_SERVER_TIME = "API/getServerTime";// 获取系统时间
	public static final String URL_IAMONLINE = "API/iAmOnline";// 设备是否在线
	public static final String MACHINE_INFO_URL = "API/getMachineInfo";// 获取机器信息
	public static final String URL_LOGIN = "API/checkMerchantOperator";// 登陆
	public static final String URL_CHECK_MACHINE_STATE = "API/checkMachineState";// 检查设备是否在线
	public static final String URL_ACTIVE_MACHINE="API/activeMachine";//激活设备

	public static final String URL_UPDATE_MACHINE_ACTIVE = "API/updateMachineActiveTimes";// 更新机器的活跃度
	public static final String URL_POS_SHIFT = "API/getTodayOrdersToPOS";
	public static final String URL_GET_SN="http://getsn.banklay.cn/getsn/s/fszlh";
	/*******
	 * @detail 获取会员的详情
	 */
	public static final String URL_MEMBER_RECHARGE = "API/MemberRecharge";// 会员充值
	public static final String URL_MEMBER_TIME_RECHARGE="API/MemberTimesRecharge";//会员按此充值
	public static final String URL_UPDATE_MEMBER_RECHARGE = "API/updateMemberBalance";// 更新会员余额
	public static final String URL_RECHARGE_PREFERS = "API/memberRechargeCoupon";// 查询充值赠送规则
	public static final String URL_GET_MEMBER_IFNO = "API/getMemberInfo";// 获取会员的基础信息
	public static final String URL_GET_MEMBER_UPGRADE_RULES = "API/getMemberUpgradeRules";
	public static final String URL_JOINVIP="API/putInQRCode";

	/*******
	 * @detail 获取卡券的详情
	 */
	public static final String URL_GET_COUPON_INFO = "API/getCouponInfo";// 查询卡券的信息详情
	public static final String URL_CARD_CONSUME = "API/cardConsume";// 卡券核销
	public static final String URL_GET_TASK_COUPONS = "API/getAdverCardList";// 获取卡券互投列表
	public static final String URL_GET_ADVER_QRCODE = "API/getAdverCardQRCode";// 获取卡券的qrcode
	public static final String URL_GET_ADVER_CARD_INFO = "API/getAdverCardInfo";
	public static final String URL_CHARGEOFF_COUPON = "API/cardConsume";//核销卡券
	// / 支付
	public static final String URL_CASH_PAY = "Payment/cashPay";//现金支付
	public static final String URL_SHOW_PAY = "admin.php/payment/Payment";//显示金额
	public static final String URL_QUERY_PAY = "Payment/queryPayResult";//查询支付结果
	public static final String URL_MICRO_PAY = "Payment/microPay";//条码付款
	public static final String URL_CHECKOUT= "API/queryPaymentLog";//获取结账单
	public static final String URL_REFUND="Payment/refund";//退款
	public static final String URL_REFUND_DETAIL="API/queryOrder";//查询退款单的详情
	public static final String URL_REFUNDLOG="API/queryRefundLog";
	public static final String URL_RECHAGE_LOCK="API/qrcodeLock";
	public static final String URL_CHECK_RECHAGE_LOCK="API/checkQrcodeLock";
	
	public static String getRealUrl(String url) {
		return BASE_URL + url;
	}
}
