package com.weilay.pos.app;

/**
 * Created by rxwu on 16/5/26.
 * 
 * @detail 卡券类型 todo 后续会增加
 */
public final class CardTypeEnum {
	public static final String GROUPON = "GROUPON";// 团购券类型。
	public static final String CASH = "CASH"; // 代金券类型。
	public static final String DISCOUNT = "DISCOUNT"; // 折扣券类型。
	public static final String GIFT = "GIFT";// 兑换券类型。
	public static final String MEMBER_CARD="MEMBER_CARD";// 会员卡
	public static final String FRIEND_GIFT = "FRIEND_GIFT";// 朋友兑换券
	public static final String FRIEND_CASH = "FRIEND_CASH";// 朋友券代金券
	public static final String SCENIC_TICKET="SCENIC_TICKET";//景区券

	public static String getTypeName(String type) {
		switch (type) {
		case GROUPON:
			return "团购券";
		case CASH:
			return "代金券";
		case DISCOUNT:
			return "折扣券";
		case GIFT:
			return "兑换券";
		case FRIEND_CASH:
			return "朋友代金券";
		case FRIEND_GIFT:
			return "朋友兑换券";
		case MEMBER_CARD:
			return "会员卡";
		case SCENIC_TICKET:
			return "景区券";
		default:
			return "";
		}
	}
}
