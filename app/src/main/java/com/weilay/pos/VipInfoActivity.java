package com.weilay.pos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.utils.InputMoneyFilter;
import com.framework.utils.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.dialog.NumberDialog;
import com.weilay.dialog.NumberDialog.OnFinish;
import com.weilay.dialog.UseCardDialog;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.VipInfoAdapter;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.PayType;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.entity.VipInfo;
import com.weilay.pos.listener.CardUseListener;
import com.weilay.pos.listener.LoadMemberRulesListener;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.printData.PrintOrderData;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.ActivityStackControlUtil;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.IFlytekHelper;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.FormBody;

public class VipInfoActivity extends TitleActivity implements OnClickListener{
	private TextView vipinfo_type, vipinfo_discount, vipinfo_amount, vipinfo_integral;
	private TextView vipinfo_enter;
	private ListView vipinfo_listview;

	private String vipinfo_url = "API/getVipInfo";
	private VipInfo vipInfo;
	private List<CouponEntity> coupons = new ArrayList<>();
	private VipInfoAdapter mVipInfoAdapter;
	private String vipCode;
	private TextView memberPreferTv,couponPreferTv, amountTv,memberDiscountTv;//memberDiscountTv表示会员卡可折扣的金额
	private boolean cardUse = false;
	private PayTypeEntity paytype;// 支付的实体
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.vipinfo_layout);
		setTitle("会员信息");
		vipCode = getIntent().getStringExtra(PosDefine.INTENT_MEMBER_CODE);
		paytype = (PayTypeEntity) getIntent().getSerializableExtra(PosDefine.INTENTE_PAY_INFO);
		
		if (vipCode == null || paytype==null) {
			T.showCenter("获取不到会员信息或者支付信息");
			finish();
			return;
		}
		cardUse=paytype.getCouponDiscountAmount()!=0?true:false;
		init();
		reg();
		initDatas();
	}
	InputMoneyFilter filter=null;
	private void init() {
		vipinfo_type = (TextView) findViewById(R.id.vipinfo_type);
		vipinfo_discount = (TextView) findViewById(R.id.vipinfo_discount);
		vipinfo_amount = (TextView) findViewById(R.id.vipinfo_amount);
		vipinfo_integral = (TextView) findViewById(R.id.vipinfo_integral);
		vipinfo_enter = (TextView) findViewById(R.id.vipinfo_enter);
		vipinfo_listview = (ListView) findViewById(R.id.vipinfo_listview);
		memberPreferTv = (TextView) findViewById(R.id.member_prefer_tv);
		couponPreferTv=(TextView)findViewById(R.id.coupon_prefer_tv);
		amountTv = (TextView) findViewById(R.id.amount_tv);
		amountTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		amountTv.setText(paytype.getAmount() + "元");
		filter=new InputMoneyFilter(ConvertUtil.getMoeny(paytype.getAmount()-paytype.getCouponDiscountAmount()));
		mVipInfoAdapter = new VipInfoAdapter(VipInfoActivity.this, coupons);
		TextView emptyView = (TextView) findViewById(R.id.empty_view);
		vipinfo_listview.setEmptyView(emptyView);
		vipinfo_listview.setAdapter(mVipInfoAdapter);
		memberDiscountTv=(TextView)findViewById(R.id.modify_discount_money_tv);
		vipinfo_enter.setText("会员卡支付:（"+ConvertUtil.parseMoney(paytype.getAraamount())+")");
		ImageView logo=(ImageView)findViewById(R.id.vipinfo_merchantlogo);
		logo.setImageResource(Config.LOGO_RES);
		
	}

	private void reg() {
		vipinfo_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				final CouponEntity coupon = (CouponEntity) coupons.get(arg2);
				if (coupon == null) {
					T.showCenter("卡券不存在");
					return;
				}
				StringBuffer buffer = new StringBuffer();
				buffer.append("优惠卡号：" + (coupon.getCode() == null ? "" : coupon.getCode()) + "\n");
				buffer.append("卡券信息:" + (coupon.getInfo() == null ? "" : coupon.getInfo()) + "\n");
				buffer.append("使用提示:" + (coupon.getNotice() == null ? "" : coupon.getNotice()) + "\n");
				buffer.append("友情提示:" + "（一经使用，不能退还）");
				if (cardUse) {
					T.showCenter("每次支付仅限使用一张卡券");
					return;
				}
				if (CardTypeEnum.DISCOUNT.equals(coupon.getType()) && vipInfo.getDiscount() != 1) {
					T.showCenter("会员折扣不能与折扣券同时使用");
					return;
				}
				if (paytype.getAraamount() <= ConvertUtil.branchToYuan(coupon.getLeast_cost())) {
					T.showCenter("不满足使用条件:" + coupon.getInfo());
					return;
				}
				cardConsume(coupon);
			}
		});
		vipinfo_enter.setOnClickListener(this);
		memberDiscountTv.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.modify_discount_money_tv:
			if(paytype.getCouponDiscount()!=10){
				DialogConfirm.ask(mContext,"会员卡使用提示","您已经使用了折扣券，不能再使用会员折扣","放弃修改", new DialogConfirmListener() {
					
					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						return;
					}
				});
				return;
			}
			NumberDialog dialog=new NumberDialog(mContext, R.style.dialog_no_title,memberDiscountTv,"设置会员卡折扣的金额",false);
			//设置最大不可超过订单实付的金额
			dialog.setFilter(filter);
			dialog.create().show();
			dialog.setOnFinish(new OnFinish() {
				
				public void onFinish(String num) {
					// TODO Auto-generated method stub
					calcMemberDiscount(ConvertUtil.getMoeny(num));
				}
			});
			break;
		case R.id.vipinfo_enter:
			memberCardUse();
			break;
		default:
			break;
		}
		
	}
	/*****
	 * @return void
	 * @param 
	 * @detail 计算会员折扣
	 */
	private void calcMemberDiscount(double num){
		if(paytype!=null && vipInfo!=null){
			if(num==-1){
				double memberDiscountAble=paytype.getAmount()-paytype.getCouponDiscountAmount();
				//会员折扣掉的金额是
				paytype.setMemberDiscountableAmount(memberDiscountAble);
			}else{
				paytype.setMemberDiscountableAmount(num);
			}
			
			double memberDiscountMoney = paytype.getMemberDiscountableAmount() * Double.valueOf(1-vipInfo.getDiscount());
			//会员折扣后的金额是(实付金额+上次会员卡折扣的金额-优惠的金额）
			paytype.setMemberDiscount(vipInfo.getDiscount());
			paytype.setMemberDiscountAmount(ConvertUtil.getMoeny(memberDiscountMoney));
			memberPreferTv.setText("会员优惠:" + ConvertUtil.parseMoney(memberDiscountMoney));
			memberDiscountTv.setText(paytype.getMemberDiscountableAmount()+"");
			couponPreferTv.setText("优惠券:"+ConvertUtil.getMoeny(paytype.getCouponDiscountAmount())+"");
			vipinfo_enter.setText("会员卡支付:（"+ConvertUtil.parseMoney(paytype.getAraamount())+")");
		}
	}

	/******
	 * @detail 使用卡券支付
	 */
	private void memberCardUse() {
		DialogAsk.ask(this, "储值卡支付", "是否使用", "确定使用", "不使用", new DialogAskListener() {
			@Override
			public void okClick(DialogInterface dialog) {
				if(vipInfo==null){
					if(PrintSettingActivity.isPaySound()){
						try {
							IFlytekHelper.speaking(mContext,"使用失败，获取会员信息失败");
						} catch (Exception ex) {
							L.e("播放声音失败");
						}
					}
					T.showCenter("使用失败，获取会员信息失败");
					return;
				}
				double memberBanlance = ConvertUtil.branchToYuan(vipInfo.getTotalamount());// 求出会员的余额
				if (memberBanlance >= paytype.getAraamount()) {
					// 支付金额大于或等于应支付金额，可以直接支付
					paytype.setPayType(PayType.CHUZHIKA);
					mContext.showLoading("正在支付");
					Utils.updateMemberBalance(VipInfoActivity.this, true, paytype, vipInfo.getVipid(), "", "",
							new OnDataListener() {

								@Override
								public void onFailed(String msg) {
									mContext.stopLoading();
									if(PrintSettingActivity.isPaySound()){
										IFlytekHelper.speaking(mContext,"使用失败，获取会员信息失败");
									}
									// 支付失败
									T.showCenter("抱歉会员卡支付失败");
								}

								@Override
								public void onData(Object obj) {
									// 支付支付扣款成功，添加入账记录
									mContext.stopLoading();
									if(PrintSettingActivity.isPaySound()){
										IFlytekHelper.speaking(mContext,"收款成功，"+paytype.getPayType().getValue()+"收款 "+paytype.getAraamount()+"元");
									}
									PrintOrderData.printOrderPay(paytype, null);
									AdverActivity.start(VipInfoActivity.this, paytype, null);

								}
							});
				} else {
					if(PrintSettingActivity.isPaySound()){
						IFlytekHelper.speaking(mContext,"会员卡余额不足，无法支付");
					}
					T.showCenter("会员卡余额不足，无法支付");
					noPay();
				}
			}

			@Override
			public void cancelClick(DialogInterface dialog) {
				noPay();// 不适用会员卡
			}
		});
	}

	/******
	 * @detail 支付 //不使用会员卡
	 */
	private void noPay() {
		// 会员支付的信息(如果会员卡折扣的金额不为零的话，用于入账)
		//if (paytype.getMemberDiscountAmount() != 0) {
			paytype.setPayType(PayType.WEIXIN);//不使用会员卡的支付方式设置为微信支付
			PayTypeEntity memberPay = paytype;
			// 不使用会员卡支付的时候必须给后台传折扣的金额，入账
			mContext.showLoading("");
			Utils.updateMemberBalance(VipInfoActivity.this, false, memberPay, vipInfo.getVipid(), "", "",
					new OnDataListener() {

						@Override
						public void onFailed(String msg) {
							// 支付失败
							L.e("会员卡折扣金额入账失败" + msg);
							mContext.stopLoading();
							PaySelectActivity2.actionStart(mContext,paytype,true);
						}

						@Override
						public void onData(Object obj) {
							// 支付支付扣款成功，添加入账记录
							mContext.stopLoading();
							L.i("会员卡折扣金额入账成功");
							Intent intent = new Intent(VipInfoActivity.this, PaySelectActivity2.class);
							intent.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
							startActivity(intent);
							finish();

						}
					});

	}

	/*******
	 * @Detail 初始化数据
	 */
	public void initDatas() {
		Utils.getMemberUpgradeRules(this, new LoadMemberRulesListener() {

			@Override
			public void loadSuccess(List<String> rules) {
				// TODO Auto-generated method stub
				StringBuffer buffer = new StringBuffer();
				for (String rule : rules) {
					buffer.append("☞ " + rule + " ");
				}
				((TextView) findViewById(R.id.vip_hint)).setText(buffer.toString());
			}

			@Override
			public void loadFailed(String msg) {
				// TODO Auto-generated method stub

				((TextView) findViewById(R.id.vip_hint)).setVisibility(View.GONE);
			}
		});

		FormBody.Builder builder = BaseParam.getParams();
		builder.add("vipid", vipCode);
		builder.add("totalamount",""+ConvertUtil.yuanToBranch(paytype.getAraamount()));
		HttpUtils.sendPost(this, builder, vipinfo_url, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					JSONObject jo_data = new JSONObject(json.getString("data"));
					vipInfo = new Gson().fromJson(jo_data.optString("vipinfo"), VipInfo.class);
					coupons = new Gson().fromJson(jo_data.optString("coupon"), new TypeToken<List<CouponEntity>>() {
					}.getType());
					paytype.setMemberDiscount(10);
					paytype.setMemberDiscountAmount(0);
					if (vipInfo != null) {
						double discount = ConvertUtil.getMoeny(Double.valueOf(vipInfo.getDiscount()) * 10);
						vipinfo_type.setText(vipInfo.getViptype());
						vipinfo_amount.setText("余额:"
								+ ConvertUtil.getMoeny(((vipInfo.getBalance() + vipInfo.getGiveamount()) / 100)) + "元");
						vipinfo_integral.setText("积分:" + vipInfo.getBonus());
						vipinfo_discount.setText(discount + "折");
						
						// TODO 如果是已经使用过折扣券的，不能再使用会员折扣
						if (CardTypeEnum.DISCOUNT.equals(paytype.getCouponType())) {
							couponPreferTv
									.setText("优惠券:"+ConvertUtil.getMoeny(paytype.getCouponDiscountAmount())+"");
							T.showCenter("(已使用过优惠卡券，不能再享用会员卡折扣)");
							paytype.setMemberDiscount(1);
							paytype.setMemberDiscountAmount(0);
						} else {
							calcMemberDiscount(-1);
						}
						mVipInfoAdapter.notityDataSetChange(coupons);
						paytype.setMemberNo(vipInfo.getVipid());
						paytype.setMemberType(vipInfo.getViptype());
					} else {
						DialogConfirm.ask(mContext, "获取会员信息提示", "获取会员卡信息失败", "确定", null);
						clearMemberInfo();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					DialogConfirm.ask(mContext, "获取会员信息提示", "获取会员卡信息失败", "确定", null);
					clearMemberInfo();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				T.showShort(msg);
				clearMemberInfo();
			}
		});
	}

	double couponDiscountAmount = 0;
	double couponDiscount = 0;

	/******
	 * @detail 使用卡券
	 */
	private void cardConsume(final CouponEntity coupon) {
		UseCardDialog useCardialog = new UseCardDialog(coupon,paytype);
		useCardialog.show(getSupportFragmentManager(), "使用卡券");
		useCardialog.setCardUseListener(new CardUseListener() {

			@Override
			public void success(PayTypeEntity result) {
				// TODO Auto-generated method stub
				cardUse = true;
				paytype=result;
				vipinfo_enter.setText("会员卡支付:("+ConvertUtil.parseMoney(paytype.getAraamount())+")");
				couponPreferTv.setText("券优惠：" + ConvertUtil.getMoeny(paytype. getCouponDiscountAmount()) + "元");
				if(0>=paytype.getAraamount()){
					//如果卡券已满额支付，直接回调支付成功
					if (PrintSettingActivity.isPaySound()) {	
						IFlytekHelper.speaking(mContext, "收款成功，"+paytype.getPayType().getValue()+"收款 "+paytype.getAraamount()+"元");
					}
					// 支付支付扣款成功，添加入账记录
					PrintOrderData.printOrderPay(paytype, null);
					AdverActivity.start(VipInfoActivity.this, paytype, null);
				}else{
					// 刷新
					coupons.remove(coupon);
					mVipInfoAdapter.notityDataSetChange(coupons);
				}
			}
			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub	
				T.showCenter(msg);
			}
		});
		
	}

	/*******
	 * @detail 清除数据
	 */
	public void clearMemberInfo() {
		vipinfo_type.setText("");
		vipinfo_discount.setText(10 + "折");
		vipinfo_amount.setText("余额:0.0元");
		vipinfo_integral.setText("积分:0");
		mVipInfoAdapter.notityDataSetChange(null);
	}
	
	@Override
	protected void back() {
		DialogAsk.ask(this, "支付提示", "确定放弃支付吗？", "确定", "取消", new DialogAskListener() {

			@Override
			public void okClick(DialogInterface dialog) {
				L.e("gg", "当前Activity:" + getRunningActivityName());
				String ActivityName = getRunningActivityName();
				if (!ActivityName.equals("com.weilay.pos.MainActivity")) {
					ActivityStackControlUtil.closeAll();
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(i);
				}
			}

			@Override
			public void cancelClick(DialogInterface dialog) {
			}
		});
	}
	@Override
	protected void home() {
		DialogAsk.ask(this, "支付提示", "确定放弃支付吗？", "确定", "取消", new DialogAskListener() {

			@Override
			public void okClick(DialogInterface dialog) {
				L.e("gg", "当前Activity:" + getRunningActivityName());
				String ActivityName = getRunningActivityName();
				if (!ActivityName.equals("com.weilay.pos.MainActivity")) {
					ActivityStackControlUtil.closeAll();
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(i);
				}
			}

			@Override
			public void cancelClick(DialogInterface dialog) {
			}
		});
	}
}
