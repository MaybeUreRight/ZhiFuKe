package com.weilay.pos.app;

/*****
 * @detail 支付类型
 * @author Administrator
 *
 */
public class PayDefine {

	/*******
	 * @detail 支付结果
	 */
	public static class PayResult {
		public static final String WAIT = "WAIT";// 支付中
		public static final String SUCCESS = "SUCCESS";// 支付成功
		public static final String ERROR = "ERROR";// 支付失败
		public static final String TIMEOUT = "TIMEOUT";// 获取超时
		public static final String CANCEL = "CANCEL";// 支付取消
		
        public static final int NONE=-2;
		public static final int WAIT_INT = 0;// 支付成功
		public static final int SUCCESS_INT = 1;// 支付成功
		public static final int ERROR_INT = -1;// 支付失败
		public static final int TIMEOUT_INT = 2;// 支付超时
		public static final int CANCEL_INT = 3;// 支付取消
	}

	/*****
	 * @detail 支付类型
	 */
/*	public static class PayType {
		public static final String WEIXIN = "W";// 微信支付
		public static final String ALIPAY = "Z";// 支付宝支付
		public static final String BAIDU = "B";// 百度支付
		public static final String CARD = "CARD";// 刷卡
		public static final String CASH = "X";// 现金支付
		public static final String SALE = "Y";// 优惠券
		public static final String TUANGO = "TAUNGO";// 团购
		public static final String CHUZHIKA = "CHUZHIKA";// 储值卡
	}

	public static String getPayType(String payType) {
		switch (payType) {
		case PayType.WEIXIN:
			return "微信支付";
		case PayType.ALIPAY:
			return "支付宝支付";
		case PayType.BAIDU:
			return "百度支付";
		case PayType.CARD:
			return "银行卡支付";
		case PayType.CASH:
			return "现金支付";
		case PayType.SALE:
			return "优惠券支付";
		case PayType.TUANGO:
			return "团购支付";
		case PayType.CHUZHIKA:
			return "会员卡支付";
		default:
			return "";
		}
	}*/

	/*****
	 * @detail 订单状态
	 */
	public static class OrderStatus {
		public static final String NEW = "NEW";// 下单
		public static final String PAY = "NEW";// 付款
		public static final String CANCEL = "CANCEL";// 取消
	}

}
