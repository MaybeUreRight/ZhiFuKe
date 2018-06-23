package com.weilay.pos.helper;

import com.framework.ui.DialogConfirm;
import com.weilay.dialog.UseCardDialog;
import com.weilay.pos.VipInfoActivity;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.listener.CardUseListener;
import com.weilay.pos.listener.GetCouponListener;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.content.Intent;

public class PayHelper {
	public static boolean useCard = false;// 是否已经使用了卡券

	/*********
	 * @detail 扫描会员卡和优惠券的处理方法
	 * @param ismember
	 *            是否为会员卡
	 * @param vipNo
	 * @param listener
	 *            如果是优惠券的话，请传入次对象
	 */
	public static void scanCard(final BaseActivity activity, final PayTypeEntity paytype, final String vipNo,
			final boolean ismember, final CardUseListener listener) {
		Utils.getCouponInfo(activity, vipNo, new GetCouponListener() {

			@Override
			public void onFailed(String msg) {
				// TODO Auto-generated method stub
				DialogConfirm.ask(activity, "获取卡券信息提示", "获取卡券信息失败", "确定", null);
			}

			@Override
			public void onData(CouponEntity coupon) {
				// TODO Auto-generated method stub
				String type = coupon.getType();
				switch (type) {
				case CardTypeEnum.MEMBER_CARD:
					if (ismember) {
						Intent intent = new Intent(activity, VipInfoActivity.class);
						intent.putExtra(PosDefine.INTENT_MEMBER_CODE, vipNo);
						intent.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
						activity.startActivity(intent);
						activity.finish();
					} else {
						T.showCenter("不是优惠券");
					}
					break;
				case CardTypeEnum.CASH:
				case CardTypeEnum.DISCOUNT:
				case CardTypeEnum.FRIEND_CASH:
					if (!ismember) {
						if (paytype.getAraamount() >= ConvertUtil.branchToYuan(coupon.getLeast_cost())) {
							UseCardDialog useCardialog = new UseCardDialog(coupon,paytype);
							useCardialog.show(activity.getSupportFragmentManager(), "使用卡券");
							useCardialog.setCardUseListener(listener);
						} else {
							T.showCenter("当前卡券必须满" + (coupon.getLeast_cost() / 100) + "元才可使用");
						}
					} else {
						T.showCenter("不是会员卡");
					}
					break;
				default:
					T.showCenter("不支持的卡券");
					break;
				}
			}
		});
	}
}
