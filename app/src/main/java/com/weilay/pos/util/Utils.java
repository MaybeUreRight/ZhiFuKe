package com.weilay.pos.util;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.framework.utils.L;
import com.weilay.listener.DialogAskListener;
import com.weilay.pos.StartActivity;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.CheckOutEntity;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.JoinVipEntity;
import com.weilay.pos.entity.MachineEntity;
import com.weilay.pos.entity.MemberEntity;
import com.weilay.pos.entity.MemberTimesLevelEntity;
import com.weilay.pos.entity.OperatorEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.entity.RechageLockEntity;
import com.weilay.pos.listener.CardUseListener;
import com.weilay.pos.listener.ChargeOffCouponListener;
import com.weilay.pos.listener.CheckOutListener;
import com.weilay.pos.listener.GetCouponListener;
import com.weilay.pos.listener.JoinVipListener;
import com.weilay.pos.listener.LoadMemberRulesListener;
import com.weilay.pos.listener.LoginListener;
import com.weilay.pos.listener.NetCodeEnum;
import com.weilay.pos.listener.OnCheckMachineStateListener;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.listener.RefundListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.titleactivity.BaseActivity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.FormBody;

/*******
 * @detail 系统工具
 * @author rxwu
 * @date 2016/07/08
 */
public class Utils {
	public static boolean isLogin=false;
	public static void login(final BaseActivity act, String mid, String operator, String pwd,
			final LoginListener loginListener) {
		final String pwdStr = PasswordEncode.parsePassword(pwd);
		L.e(pwdStr);
		FormBody.Builder builder = BaseParam.getBaseParams();
		builder.add("mid", mid);
		builder.add("operator", operator);
		builder.add("pwd", pwdStr);
		HttpUtils.sendPost(act, builder, UrlDefine.URL_LOGIN, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				// update by rxwu
				try {
					OperatorEntity operatorEntity = new Gson()
							.fromJson(json.optJSONArray("data").optJSONObject(0).toString(), OperatorEntity.class);
					operatorEntity.setPassword(pwdStr);
					Utils.saveOperator(operatorEntity);// 保存用户信息
					loginListener.loginSuccess(operatorEntity);
				} catch (Exception ex) {
					loginListener.loginFailed("登录返回格式有误", NetCodeEnum.NOJSON.getCode());
				}

			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				loginListener.loginFailed(msg, code);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				loginListener.loginFailed("网络异常", NetCodeEnum.NETWORK_UNABLE.getCode());
			}
		});
	}

	/*******
	 * @detail 查询订单识别的信息
	 */
	public static void queryOrderRead(final OnDataListener listener) {
		String cache_machine = WeiLayApplication.app.mCache.getAsString(PosDefine.CACHE_MACHINE_INFO);
		if (cache_machine != null && !TextUtils.isEmpty(cache_machine)) {
			listener.onData(new Gson().fromJson(cache_machine, MachineEntity.class));
			return;
		}
		FormBody.Builder builder = BaseParam.getParams();
		HttpUtils.sendPost(builder, UrlDefine.MACHINE_INFO_URL, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					List<MachineEntity> machines = new Gson().fromJson(json.optString("data"),
							new TypeToken<List<MachineEntity>>() {
							}.getType());
					if (machines != null && machines.size() > 0) {
						L.d("获取机器码信息成功!");
						MachineEntity machineInfo = machines.get(0);
						listener.onData(machineInfo);
						WeiLayApplication.app.mCache.put(PosDefine.CACHE_MACHINE_INFO, new Gson().toJson(machineInfo), 10 * 60);
					} else {
						listener.onData(null);
					}

				} catch (Exception ex) {
					listener.onFailed("获取机器码信息失败" + ex.getLocalizedMessage());
					Log.e("", "获取机器码信息失败");
				}

			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed("获取机器码信息失败" + msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.onFailed("网络请求异常");
			}
		});
	}

	/*******
	 * @detail 机器是否在线
	 */
	public static void MachineOnLine(final BaseActivity activty, final OnCheckMachineStateListener listener) {
		FormBody.Builder param = BaseParam.getParams();
		// 检查设备是否在线
		HttpUtils.sendPost(activty, param, UrlDefine.URL_CHECK_MACHINE_STATE, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				// Log.e("gg", "MachineOnLine->onSuccess:" +
				// json.toString());
				listener.onLine();
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.nofound(code, msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.connectFailed("网络请求失败");
			}

		});
	}

	/***********
	 * @detail 获取当前用户的信息
	 */
	public static OperatorEntity getCurOperator() {
		String operatorStr = WeiLayApplication.app.mCache.getAsString(PosDefine.CACHE_OPERATOR);
		if (!TextUtils.isEmpty(operatorStr)) {
			OperatorEntity operator = new Gson().fromJson(operatorStr, OperatorEntity.class);
			return operator;
		}
		return null;
	}

	/***********
	 * @detail 保存用户的信息
	 */
	public static boolean saveOperator(OperatorEntity operator) {
		if (operator != null) {
			WeiLayApplication.app.mCache.put(PosDefine.CACHE_OPERATOR, new Gson().toJson(operator));
			return true;
		}
		return false;

	}

	/*****
	 * @detail 根据会员卡号获取卡券的详情
	 * @param vipNo
	 */
	public static void getCouponInfo(BaseActivity act, final String vipNo, final GetCouponListener listener) {
		// TODO Auto-generated method stub
		FormBody.Builder params = BaseParam.getParams();
		params.add("code", vipNo);
		HttpUtils.sendPost(act, params, UrlDefine.URL_GET_COUPON_INFO, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					CouponEntity coupon = new Gson().fromJson(json.optString("data"), CouponEntity.class);
					if (coupon != null) {
						coupon.setCode(vipNo);
						listener.onData(coupon);
					} else {
						listener.onFailed("查无卡券");
					}
				} catch (Exception ex) {
					Log.e("", ex.getLocalizedMessage());
					listener.onFailed("获取卡券信息出错");
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed(msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.onFailed("网络异常");
			}
		});

	}

	/*****
	 * @detail 根据会员卡号获取卡券的详情
	 * @param cid
	 *            卡券的id
	 */
	public static void getAdverCardInfo(BaseActivity act, final String cid, final GetCouponListener listener) {
		// TODO Auto-generated method stub
		FormBody.Builder params = BaseParam.getParams();
		params.add("cid", cid);
		HttpUtils.sendPost(act, params, UrlDefine.URL_GET_ADVER_CARD_INFO, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					List<CouponEntity> coupons = new Gson().fromJson(json.optString("data"),
							new TypeToken<List<CouponEntity>>() {
							}.getType());
					CouponEntity coupon = null;
					if (coupons != null && coupons.size() > 0) {
						coupon = coupons.get(0);
						coupon.setId(cid);
						listener.onData(coupon);
					} else {
						listener.onFailed("查无卡券");
					}
				} catch (Exception ex) {
					Log.e("", ex.getLocalizedMessage());
					listener.onFailed("获取卡券信息出错");
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed(msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.onFailed("网络异常");
			}
		});

	}

	/********
	 * @detail 卡券核销
	 * @param discountamount
	 *            卡券优惠的金额
	 * @param paidamount
	 *            订单实付的金额
	 */
	public static void cardConsume(BaseActivity act, final CouponEntity coupon, final PayTypeEntity mPay,
			final double couponDiscountAmount, final double paidamount, final double couponDiscount, String open_id,
			final CardUseListener listener) {
		if (coupon == null) {
			return;
		}
		FormBody.Builder params = BaseParam.getParams();
		params.add("code", coupon.getCode());
		params.add("tx_no", mPay.getTx_no());
		params.add("receivable", "" + ConvertUtil.yuanToBranch(couponDiscountAmount));
		params.add("paidamount", "" + ConvertUtil.yuanToBranch(paidamount));
		params.add("openId", open_id != null ? open_id : "");
		HttpUtils.sendPost(act, params, UrlDefine.URL_CARD_CONSUME, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					if (json.optInt("code") == 0) {
						mPay.setCouponDiscountAmount(couponDiscountAmount);
						mPay.setCouponType(coupon.getType());
						mPay.setCouponDiscount(couponDiscount);
						listener.success(mPay);
					} else {
						listener.failed(json.optString("message"));
					}
				} catch (Exception ex) {
					Log.e("", ex.getLocalizedMessage());
					listener.failed("核销失败");
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.failed(msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.failed("网络异常");
			}
		});

	}

	/********
	 * @detail 更新会员的余额
	 */
	public static void updateMemberBalance(BaseActivity act, boolean memberPay, PayTypeEntity type, String membershipno,
			String open_id, String phone, final OnDataListener listener) {
		double d = ConvertUtil.yuanToBranch(type.getAraamount());
		FormBody.Builder params = BaseParam.getParams();
		params.add("phone", phone);
		params.add("open_id", open_id);
		params.add("code", membershipno);
		params.add("balance", "" + (memberPay ? ConvertUtil.doubleToString(d) : 0));
		params.add("recordBalance", "");
		params.add("tx_no", type.getTx_no());
		params.add("member_card_pay_amt", "" + ConvertUtil.doubleToString(d));
		params.add("discount_amt", "" + ConvertUtil.yuanToBranch(type.getMemberDiscountAmount()));
		HttpUtils.sendPost(act, params, UrlDefine.URL_UPDATE_MEMBER_RECHARGE, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				if (json.optInt("code") == 0) {
					listener.onData(json);
				} else {
					listener.onFailed(json.optString("message"));
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed(msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.onFailed("网络异常");
			}
		});
	}

	/*********
	 * @detail 会员充值
	 * @param rechargeMoeny
	 *            普通充值的充值金额
	 * @param rechargeid
	 *            次数充值的ID
	 */
	public static void memberRecharge(BaseActivity act, MemberEntity member, double rechargeMoeny,
			MemberTimesLevelEntity level, double giftMoeny, final OnDataListener listener) {
		String tx_no = GetUtil.getOutTradeNo();
		long recharge = 0;
		long gift = ConvertUtil.yuanToBranch(giftMoeny);
		FormBody.Builder builder = BaseParam.getParams();
		String rechargeUrl = "";
		if (member.getMember_card_type() == 1) {
			rechargeUrl = UrlDefine.URL_MEMBER_TIME_RECHARGE;
			recharge = (int) level.getLevel_amount();
			builder.add("rechargeId", "" + level.getId());
		} else {
			rechargeUrl = UrlDefine.URL_MEMBER_RECHARGE;
			recharge = ConvertUtil.yuanToBranch(rechargeMoeny);
		}
		builder.add("phone", "");
		builder.add("membership_number", member.getMembership_number());
		builder.add("rechargeAmt", "" + recharge);
		builder.add("giftAmt", "" + gift);
		builder.add("tx_no", tx_no);
		HttpUtils.sendPost(act, builder, rechargeUrl, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				listener.onData(json);
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed(msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.onFailed("网络出错");
			}
		});
	}

	/**********
	 * @detail 查询会员的升级规则
	 */
	public static void getMemberUpgradeRules(BaseActivity act, final LoadMemberRulesListener listener) {
		FormBody.Builder params = BaseParam.getParams();
		HttpUtils.sendPost(act, params, UrlDefine.URL_GET_MEMBER_UPGRADE_RULES, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<List<String>>() {
				}.getType();
				try {
					List<String> rules = new Gson().fromJson(json.optString("data"), type);
					if (rules == null) {
						listener.loadFailed("商家未创建升级规则");
					} else {
						listener.loadSuccess(rules);
					}
				} catch (Exception ex) {
					listener.loadFailed("商家未创建升级规则");
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.loadFailed(msg);
			}
		});
	}

	/********
	 * @detail 获取互投卡券
	 * @param act
	 * @param onDataListener
	 */
	public static void loadTaskCoupon(BaseActivity act, String cids, final OnDataListener onDataListener) {
		FormBody.Builder params = BaseParam.getParams();
		params.add("cids", cids);
		HttpUtils.sendPost(act, params, UrlDefine.URL_GET_ADVER_CARD_INFO, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					Type couponType = new TypeToken<List<CouponEntity>>() {
					}.getType();
					List<CouponEntity> datas = new Gson().fromJson(json.optString("data"), couponType);
					onDataListener.onData(datas);
				} catch (Exception ex) {
					onDataListener.onFailed(ex.getLocalizedMessage());
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				if (code == 404) {
					onDataListener.onFailed(code, msg);
				} else {
					onDataListener.onFailed(msg);
				}
			}
		});

	}

	/********
	 * @detail 获取互投卡券的二维码
	 * @param act
	 * @param onDataListener
	 */
	public static void getCouponQRCode(BaseActivity act, String cardId, final OnDataListener onDataListener) {
		FormBody.Builder params = BaseParam.getParams();
		params.add("id", cardId);
		HttpUtils.sendPost(act, params, UrlDefine.URL_GET_ADVER_QRCODE, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					String qrcode = json.optString("data");
					onDataListener.onData(qrcode);
				} catch (Exception ex) {
					onDataListener.onFailed(ex.getLocalizedMessage());
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				onDataListener.onFailed(msg);
			}
		});

	}

	private static final String HAS_SYNCHRONIZED = "HAS_SYNCHRONIZED";

	/********
	 * @detail 获取服务器的时间
	 */
	public static void getServerTime() {
		/*
		 * if (!TextUtils.isEmpty(WeiLayApplication.app.mCache
		 * .getAsString(HAS_SYNCHRONIZED))) { return; }
		 */
		FormBody.Builder params = BaseParam.getParams();
		HttpUtils.sendPost(params, UrlDefine.URL_GET_SERVER_TIME, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				/*
				 * WeiLayApplication.app.mCache.put(HAS_SYNCHRONIZED, "1", 3 *
				 * 60);
				 */
				long time = json.optLong("data");// 服务器返回的时间必须乘以1000才可以
				ServerTimeUtils.setServerTime(time * 1000);
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailed(NetCodeEnum code, String msg) {
				// TODO Auto-generated method stub
				// super.onFailed(code, msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				// super.networkError();
			}
		});
	}

	public static void SendChargeOff(BaseActivity activity, String codeNo,
			final ChargeOffCouponListener chargeOffCouponListener) {
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("code", codeNo);
		// builder.add("sn", GetUtil.getimei(ChargeOffActivity.this));
		// Call call = get.toserver(builder, conSume_url);
		HttpUtils.sendPost(activity, builder, UrlDefine.URL_CHARGEOFF_COUPON, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				chargeOffCouponListener.onSuc();

			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				chargeOffCouponListener.onErr();
			}
		});
	}

	public static void SendCheckOut(BaseActivity acitivty, FormBody.Builder builder,
			final CheckOutListener checkOutListener) {
		HttpUtils.sendPost(acitivty, builder, UrlDefine.URL_CHECKOUT, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<List<CheckOutEntity>>() {
				}.getType();

				try {
					List<CheckOutEntity> list = new Gson().fromJson(json.getString("data"), type);
					checkOutListener.onSuc(list);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				checkOutListener.onErr();
			}
		});
	}

	/************
	 * @detail 发起退款申请
	 * @return void
	 * @param
	 * @detail
	 */
	public static void SendRefund(BaseActivity activity, FormBody.Builder builder,
			final RefundListener refundListener) {

	}

	/*****
	 * @detail 领取会员卡
	 * @return void
	 * @param
	 * @detail
	 */
	public static void JoinVip(BaseActivity activity, FormBody.Builder builder, final JoinVipListener joinVipListener) {
		HttpUtils.sendPost(activity, builder, UrlDefine.URL_JOINVIP, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<JoinVipEntity>() {
				}.getType();
				JoinVipEntity jvi;
				try {
					jvi = new Gson().fromJson(json.getString("data"), type);
					joinVipListener.onSuc(jvi);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				switch (code) {
				case 404:
					joinVipListener.on404(msg);
					break;
				default:
					joinVipListener.onErr();
					break;
				}
			}
		});
	}

	/*****
	 * 
	 * @detail 绑定激活设备
	 */
	public static void activeDevice(final BaseActivity activty) {
		DialogAsk.ask(activty, "激活设备提示", "您的设备尚未激活，请问你确认现在激活吗？", "确认", "取消", new DialogAskListener() {

			@Override
			public void okClick(DialogInterface dialog) {
				// TODO no active
				// 激活设备
				FormBody.Builder params = BaseParam.getParams();
				HttpUtils.sendPost(params, UrlDefine.URL_ACTIVE_MACHINE, new ResponseListener() {
					@Override
					public void onSuccess(JSONObject json) {
						// TODO Auto-generated method stub
						T.showCenter("设备已激活");
						StartActivity.actionStart(activty);
					}

					@Override
					public void onFailed(int code, String msg) {
						// TODO Auto-generated method stub
						T.showShort("激活失败");

					}
				});

			}

			@Override
			public void cancelClick(DialogInterface dialog) {
				// TODO Auto-generated method stub
				T.showCenter("如需使用设备请先激活设备");
			}
		});

	}

	// 获取解锁的二维码
	public static void getRechargeLock(final OnDataListener listener) {
		FormBody.Builder params = BaseParam.getParams();
		params.add("optype", "1");
		HttpUtils.sendPost(params, UrlDefine.URL_RECHAGE_LOCK, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				listener.onData(new Gson().fromJson(json.optString("data"), RechageLockEntity.class));
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed(code, msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				super.networkError();
				listener.onFailed(NetCodeEnum.NETWORK_UNABLE.getCode(), "网络出错");
			}
		});

	}

	// 查询解锁是否成功
	public static void queryRechageLock(final RechageLockEntity mLock, final OnDataListener<Integer> listener) {
		FormBody.Builder params = BaseParam.getParams();
		params.add("optype", mLock.getOptype());
		params.add("sceneid", mLock.getSceneid());
		HttpUtils.sendPost(params, UrlDefine.URL_CHECK_RECHAGE_LOCK, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				listener.onData(0);
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				listener.onFailed(code, msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				super.networkError();
				listener.onFailed(NetCodeEnum.NETWORK_UNABLE.getCode(), "网络出错");
			}
		});

	}

}
