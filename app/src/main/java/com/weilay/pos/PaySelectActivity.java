/*package com.weilay.pos;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.weilay.dialog.UseCardDialog;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.client.CardTypeEnum;
import com.weilay.pos.client.PayDefine;
import com.weilay.pos.client.PayType;
import com.weilay.pos.client.PosDefine;
import com.weilay.pos.client.UrlDefine;
import com.weilay.pos.entity.AdverEntity;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.entity.QRInfoEntity;
import com.weilay.pos.listener.CardUseListener;
import com.weilay.pos.listener.GetCouponListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.printData.PrintOrderData;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.EncodingHandler;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.FormBody;

public class PaySelectActivity extends NotTitleActivity implements Callback, OnClickListener {
	private int currentState = PayDefine.PayResult.NONE;
	private ImageView cashIv, weixinIv, alipayIv, baiduIv, vipcardIv, couponIv;
	private ImageView pay_show_cash, pay_show_qr;
	private TextView pay_amount, pay_explain, pay_paidAmount;
	private Timer poll_timer, micor_query_timer;
	private PayType currentPayType =PayType.CASH;
	private PayTypeEntity paytype;
	private RelativeLayout zxing_layout;
	private boolean checkZxing = true;
	private android.view.ViewGroup.LayoutParams mLayoutParams;
	private boolean useCard = false;// 是否已经使用了卡券

	public static void actionStart(BaseActivity act, PayTypeEntity paytype, boolean close) {
		Intent intent = new Intent(act, PaySelectActivity2.class);
		intent.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
		act.startActivity(intent);
		act.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_capture);
		paytype = (PayTypeEntity) getIntent().getSerializableExtra(PosDefine.INTENTE_PAY_INFO);
		if (paytype == null) {
			DialogConfirm.ask(this, "支付提示", "非法支付请求", "确定", new DialogConfirmListener() {

				@Override
				public void okClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			return;
		}
		initViews();
		initDatas();
		initEvent();
	}
	//初始化视图
	private void initViews() {
		startScan();
		getViewfinderView().setScanSize(0.7);
		zxing_layout = (RelativeLayout) findViewById(R.id.rl_zxing);
		mLayoutParams = zxing_layout.getLayoutParams();
		
		cashIv = (ImageView) findViewById(R.id.paytype_cash);
		weixinIv = (ImageView) findViewById(R.id.paytype_wenxin);
		alipayIv = (ImageView) findViewById(R.id.paytype_zhifubao);
		baiduIv = (ImageView) findViewById(R.id.paytype_baidu);
		vipcardIv = (ImageView) findViewById(R.id.paytype_vipcard);
		couponIv = (ImageView) findViewById(R.id.paytype_coupon);

		pay_amount = (TextView) findViewById(R.id.pay_amount);
		pay_show_cash = (ImageView) findViewById(R.id.pay_show_cash);
		pay_show_qr = (ImageView) findViewById(R.id.pay_show_qr);
		pay_explain = (TextView) findViewById(R.id.pay_explain);
		pay_paidAmount = (TextView) findViewById(R.id.pay_paidAmount);
		pay_paidAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		pay_show_cash.setImageResource(R.drawable.ysxj);
	}

	//初始化数据
	@SuppressLint("NewApi")
	private void initDatas() {
		pay_paidAmount.setVisibility(paytype.getAmount() == paytype.getAraamount()?View.GONE:View.VISIBLE);
		pay_paidAmount.setText("应收:" + ConvertUtil.getMoeny(paytype.getAmount()) + "元");
		pay_amount.setText(ConvertUtil.getMoeny(paytype.getAraamount()) + "元");
		pay_explain.setText("请扫描二维码或将付款码对准摄像头");
		payInit(paytype);
		// 默认选择微信支付---update by rxwu at 2016/08/05
		choosePay(R.id.paytype_wenxin, weixinIv);
	}
	
	//初始化事件
	private void initEvent(){
		cashIv.setOnClickListener(this);
		weixinIv.setOnClickListener(this);
		alipayIv.setOnClickListener(this);
		baiduIv.setOnClickListener(this);
		vipcardIv.setOnClickListener(this);
		couponIv.setOnClickListener(this);
		pay_show_cash.setOnClickListener(this);
		zxing_layout.setOnClickListener(this);
	}
	
	
	*//*****
	 * @detail 初始化支付的信息
	 *//*
	private void payInit(PayTypeEntity payType) {
		if (paytype != null) {
			pay_paidAmount.setText("应收:" + ConvertUtil.getMoeny(paytype.getAmount()) + "元");
			pay_paidAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			pay_amount.setText(ConvertUtil.getMoeny(paytype.getAraamount()) + "元");
			if(!StringUtil.isNotEmpty(payType.getTx_no(),paytype.getTx_no2())){
				//默认生成两个订单号，一个主扫一个被扫
				payType.setTx_no(GetUtil.getOutTradeNo());
				payType.setTx_no2(GetUtil.getOutTradeNo());
			}
			
		}
		// 清空微信支付宝的生成信息
		setState(PayType.ALIPAY, false);
		setState(PayType.WEIXIN, false);
	}

	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pay_show_cash:
			showLoading("请稍候...");
			send_cashPay();
			break;
		case R.id.rl_zxing:
			if (checkZxing) {
				zxing_layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT));
				checkZxing = false;
			} else {
				zxing_layout.setLayoutParams(mLayoutParams);
				checkZxing = true;
			}
			break;
		default:
			choosePay(arg0.getId(), arg0);
			break;
		}
	}

	*//*******
	 * @detail 选择支付的方式
	 * @param action
	 *//*
	private void choosePay(int action, View arg0) {
		resetState();
		close_timer();
		arg0.setEnabled(false);
		switch (action) {
		case R.id.paytype_cash:
			currentPayType = PayType.CASH;
			setTitle("现金支付");
			zxing_layout.setVisibility(View.GONE);
			paytype.setPayType(PayType.CASH);
			pay_show_cash.setVisibility(View.VISIBLE);
			pay_explain.setVisibility(View.INVISIBLE);
			break;
		case R.id.paytype_wenxin:
			currentPayType = PayType.WEIXIN;
			setTitle("微信支付");
			paytype.setPayType(PayType.WEIXIN);
			zxing_layout.setVisibility(View.VISIBLE);
			pay_show_qr.setVisibility(View.VISIBLE);
			showAmount(paytype);
			break;
		case R.id.paytype_zhifubao:
			currentPayType = PayType.ALIPAY;
			setTitle("支付宝支付");
			paytype.setPayType(PayType.ALIPAY);
			zxing_layout.setVisibility(View.VISIBLE);
			pay_show_qr.setVisibility(View.VISIBLE);
			pay_explain.setText("请扫描二维码或将付款码对准摄像头");
			showAmount(paytype);
			break;
		case R.id.paytype_vipcard:
			currentPayType = com.weilay.pos.client.PayType.CHUZHIKA;
			zxing_layout.setVisibility(View.VISIBLE);
			pay_show_qr.setVisibility(View.INVISIBLE);
			pay_explain.setText("请扫描会员卡对准摄像头");
			break;
		case R.id.paytype_coupon:
			currentPayType = com.weilay.pos.client.PayType.SALE;
			setTitle("使用卡券");
			zxing_layout.setVisibility(View.VISIBLE);
			pay_show_qr.setVisibility(View.INVISIBLE);
			pay_explain.setText("请将卡券对准摄像头");
			break;
		}
	}

	*//*****
	 * @Detail 恢复按钮状态
	 *//*
	private void resetState() {
		baiduIv.setEnabled(true);
		cashIv.setEnabled(true);
		couponIv.setEnabled(true);
		vipcardIv.setEnabled(true);
		weixinIv.setEnabled(true);
		alipayIv.setEnabled(true);
		pay_explain.setVisibility(View.VISIBLE);
		pay_show_qr.setVisibility(View.GONE);
		pay_show_cash.setVisibility(View.GONE);
	}

	*//**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 *//*
	public void handleDecode(Result result) {
		super.handleDecode(result);
		if(currentState==PayDefine.PayResult.WAIT_INT){
			return;
		}
		showLoading("已扫描!正在处理...", 1, true, null);
		String resultString = result.getText();
		L.i("gg", "扫描结果:" + resultString);
		if (resultString.equals("")) {
			Toast.makeText(PaySelectActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}
		switch (currentPayType) {
		case WEIXIN:
		case ALIPAY:
			close_timer();
			// 如果是主动扫码支付的话，添加新的标识，由后台截取处理
			send_passive(paytype, resultString);
			break;

		case CHUZHIKA:
			scanCard(resultString, true);
			break;
		case SALE:
			if (useCard) {
				T.showCenter("已经使用了卡券，当前订单不可再用卡券");
			} else {
				scanCard(resultString, false);
			}
			break;
		}
	}
	
	//现金支付
	private void send_cashPay() {
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", paytype.getTx_no());
		//update by rxwu at 2016/11/7(如果有使用会员卡支付，那么会返回会员卡的信息供后台修改会员积分)
		builder.add("code", paytype.getMemberNo()==null?"":paytype.getMemberNo());
		builder.add("totalamount", ConvertUtil.doubleToString(d));

		HttpUtils.sendPost(mContext, builder, UrlDefine.URL_CASH_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject jo) {
				// TODO Auto-generated method stub
				try {
					JSONObject data_jo = new JSONObject(jo.getString("data"));
					if (jo.optJSONObject("adver").optInt("code") == 0) {
						List<AdverEntity> advers = new Gson().fromJson(jo.optJSONObject("adver").optString("data"),
								new TypeToken<List<AdverEntity>>() {
								}.getType());
						paytype.setAdvers(advers);
					}
					QRInfoEntity qrinfo = new Gson().fromJson(jo.optString("qrinfo"), QRInfoEntity.class);
					paytype.setQrInfo(qrinfo);
					paytype.setTime(jo.getString("time"));
					double araamount = ConvertUtil.branchToYuan(data_jo.optDouble("totalAmount"));
					paytype.setFirstDiscount(paytype.getAraamount() - araamount);// 记录首单优惠金额
					paySuccess();
				} catch (Exception e) {
					// TODO: handle exception'
					e.printStackTrace();
				}

			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				close_timer();
				DialogConfirm.ask(mContext, "现金支付提示", msg, "确定", new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
	}

	private void paySuccess() {
		close_timer();
		currentState = PayDefine.PayResult.SUCCESS_INT;
		PrintOrderData.printOrderPay(paytype, null);
		AdverEntity adver = (paytype.getAdvers() == null || paytype.getAdvers().size() < 1) ? null
				: paytype.getAdvers().get(0);
		AdverActivity.start(PaySelectActivity.this, paytype, adver);
	}

	// 微信支付宝方式是否显示过？
	private boolean weixin = false;
	private boolean alipay = false;
	private Bundle weixinBundle, alipayBundle;

	*//****
	 * @detail 获取请求的状态
	 *//*
	private boolean getState(PayType payType) {
		switch (payType) {
		case WEIXIN:
			return weixin;
		case ALIPAY:
			return alipay;
		default:
			return false;
		}
	}

	*//*****
	 * @detail 设置是否已经请求过
	 * @param payType
	 * @param success
	 *//*
	public void setState(PayType payType, boolean success) {
		switch (payType) {
		case WEIXIN:
			weixin = success;
			break;
		case ALIPAY:
			alipay = success;
			break;
		default:
			break;
		}
	}

	public Bundle getBundle(PayType payType) {
		switch (payType) {
		case WEIXIN:
			return weixinBundle;
		case ALIPAY:
			return alipayBundle;
		default:// 默认微信
			return weixinBundle;
		}
	}

	public void setBundle(PayType payTypem, Bundle bundle) {
		switch (payTypem) {
		case WEIXIN:
			weixinBundle = bundle;
			break;
		case ALIPAY:
			alipayBundle = bundle;
			break;
		}
	}

	private void PayShow(final Bundle bundle) {
		// handlerBundle = (Bundle) msg.obj;
		if (pay_show_qr != null) {
			pay_show_qr.setVisibility(View.VISIBLE);
			createQR(bundle.getString("data"), pay_show_qr);
		}

		// 关闭上次的轮询
		close_timer();

		poll_timer = new Timer();
		poll_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				send_QueryPay(bundle.getString("tx_no"),(PayType)bundle.getSerializable("paytype"));
			}
		}, 10000, 4000);
	}

	
	 * 向服务器请求支付金额 amount
	 
	private void showAmount(final PayTypeEntity payType) {
		final PayType payMethod = payType.getPayType();
		L.i("支付方式", payMethod+"");
		if (getState(payMethod)) {
			// sendMessage(PAY_SHOW_SUC, getBundle(payMethod));
			PayShow(getBundle(payMethod));
			return;
		}
		showLoading("请稍候");
		String d = String.valueOf(ConvertUtil.getMoeny(ConvertUtil.yuanToBranch(paytype.getAraamount())));
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("paytype", paytype.getPayType().getName());
		builder.add("tx_no", paytype.getTx_no());
		builder.add("totalAmount", d);

		builder.add("tradetype", "jsapi");
		builder.add("goods", Utils.getCurOperator().getName() + "安全支付");
		builder.add("goodCodes", paytype.getTx_no());
		builder.add("code", paytype.getMemberNo()==null?"":paytype.getMemberNo());
		HttpUtils.sendPost(mContext, builder, UrlDefine.URL_SHOW_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject jo) {
				// TODO Auto-generated method stub
				stopLoading();
				try {
					Bundle bundle = new Bundle();
					bundle.putString("tx_no", paytype.getTx_no());
					bundle.putString("data", jo.getString("data"));
					bundle.putSerializable("paytype", payMethod);
					// 请求支付结果成功后，保存支付成功的信息
					setBundle(payMethod, bundle);
					setState(payMethod, true);
					// sendMessage(PAY_SHOW_SUC, bundle);
					PayShow(bundle);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				T.showCenter(msg);
				currentState = PayDefine.PayResult.ERROR_INT;
				stopLoading();
				if (pay_show_qr != null) {
					pay_show_qr.setVisibility(View.INVISIBLE);
				}

			}
		});
	}

	*//**
	 * 条码支付
	 * 
	 * @param amount
	 * @param paytype
	 * @param auth_code
	 *//*
	private void send_passive(final PayTypeEntity paytype, String auth_code) {
		showLoading("支付进行中...");
		FormBody.Builder builder = BaseParam.getParams();
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		builder.add("paytype", paytype.getPayType().getName());
		//update by rxwu,每次启动支付的时候会默认生成两个订单编号，只有主动扫码支付的时候会使用到tx_no2,服务器需要根据这里上传的tx_no2将tx_no1记录全部update成tx_no2
		builder.add("tx_no2", paytype.getTx_no2());
		builder.add("tx_no1", paytype.getTx_no());
		//updateFlag 可以给服务器标识将要更新优惠券表还是要更新会员卡信息表，或者都不更新或者全部更新
		builder.add("updateFlag",""+paytype.getUpdateFlag());
		builder.add("totalAmount", ConvertUtil.doubleToString(d));
		builder.add("auth_code", auth_code);
		builder.add("goods", Utils.getCurOperator().getName() + "安全支付");
		builder.add("goodCodes", paytype.getTx_no());
		builder.add("code", paytype.getMemberNo()==null?"":paytype.getMemberNo());
		HttpUtils.sendPost(builder, UrlDefine.URL_MICRO_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject jo) {
				// TODO Auto-generated method stub
				stopLoading();
				try {
					JSONObject data_jo = jo.optJSONObject("data");

					if (jo.optJSONObject("adver").optInt("code") == 0) {
						List<AdverEntity> advers = new Gson().fromJson(jo.optJSONObject("adver").optString("data"),
								new TypeToken<List<AdverEntity>>() {
								}.getType());
						paytype.setAdvers(advers);
					}
					QRInfoEntity qrinfo = new Gson().fromJson(jo.optString("qrinfo"), QRInfoEntity.class);
					double araamount = ConvertUtil.branchToYuan(data_jo.optDouble("totalAmount"));
					paytype.setQrInfo(qrinfo);
					paytype.setTime(jo.getString("time"));
					paytype.setFirstDiscount(paytype.getAraamount() - araamount);
					paySuccess();
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				stopLoading();

				switch (code) {
				case 1:// USERPAYING
					currentState = PayDefine.PayResult.WAIT_INT;
					showLoading("用户输密码中...");
					close_timer();
					micor_query_timer = new Timer();
					micor_query_timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//主扫查询订单2
							send_QueryPay(paytype.getTx_no2(), paytype.getPayType());

						}
					}, 7000, 5000);
					break;
				default:
					close_timer();
					DialogConfirm.ask(mContext, "提示", "付款失败!请尝试使用扫描支付!", "确定", new DialogConfirmListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							currentState = PayDefine.PayResult.ERROR_INT;
							// TODO Auto-generated method stub
						}
					});
					break;
				}

			}
		});

	}

	private void QueryPayErr() {
		close_timer();
		currentState = PayDefine.PayResult.ERROR_INT;
		DialogConfirm.ask(mContext, "提示", "支付失败!", "确定", new DialogConfirmListener() {

			@Override
			public void okClick(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mContext.finish();
			}
		});
	}
	*//******
	 * @Detail 查询支付结果
	 * @return void
	 * @param 
	 * @detail
	 *//*
	private void send_QueryPay(final String tx_no, final PayType paymethod) {
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", tx_no);
		builder.add("type", "microPay");
		builder.add("paytype", paymethod.getName());
		HttpUtils.sendPost(builder, UrlDefine.URL_QUERY_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject jo) {
				stopLoading();
				try {
					JSONObject data_jo = new JSONObject(jo.getString("data"));

					L.i("gg->poll", data_jo.toString());
					if (jo.optJSONObject("adver").optInt("code") == 0) {
						List<AdverEntity> advers = new Gson().fromJson(jo.optJSONObject("adver").optString("data"),
								new TypeToken<List<AdverEntity>>() {
								}.getType());
						paytype.setAdvers(advers);
					}
					QRInfoEntity qrinfo = new Gson().fromJson(jo.optString("qrinfo"), QRInfoEntity.class);
					paytype.setQrInfo(qrinfo);
					// update by rxwu at 2016/08/17 设置首单的优惠金额

					double araamount = ConvertUtil.branchToYuan(data_jo.optDouble("totalAmount"));
					paytype.setDiscountType(data_jo.optString("discountType"));
					paytype.setFirstDiscount(paytype.getAraamount() - araamount);
					paySuccess();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					QueryPayErr();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				// sendMessage(PAY_POLL_ERR, "");
				switch (code) {
				case 3:
					stopLoading();
					QueryPayErr();
					break;
				default:
					break;
				}
			}
		});
	}

	*//*********
	 * @detail 扫描会员卡和优惠券的处理方法
	 * @param ismember
	 *            是否为会员卡
	 * @param vipNo
	 *//*
	private void scanCard(final String vipNo, final boolean ismember) {
		Utils.getCouponInfo(this, vipNo, new GetCouponListener() {

			@Override
			public void onFailed(String msg) {
				// TODO Auto-generated method stub
				stopLoading();
				DialogConfirm.ask(PaySelectActivity.this, "获取卡券信息提示", "获取卡券信息失败", "确定", null);
			}

			@Override
			public void onData(CouponEntity coupon) {
				// TODO Auto-generated method stub
				stopLoading();
				String type = coupon.getType();
				switch (type) {
				case CardTypeEnum.MEMBER_CARD:
					if (ismember) {
						Intent intent = new Intent(PaySelectActivity.this, VipInfoActivity.class);
						intent.putExtra(PosDefine.INTENT_MEMBER_CODE, vipNo);
						intent.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
						startActivity(intent);
						finish();
					} else {
						T.showCenter("不是优惠券");
					}
					break;
				case CardTypeEnum.CASH:
				case CardTypeEnum.DISCOUNT:
				case CardTypeEnum.FRIEND_CASH:
					if (!ismember) {
						if (paytype.getAraamount() >= (coupon.getLeast_cost() / 100)) {
							UseCardDialog useCardialog = new UseCardDialog(coupon,
									ConvertUtil.getMoeny(paytype.getAraamount()));
							useCardialog.show(getSupportFragmentManager(), "使用卡券");
							useCardialog.setCardUseListener(new CardUseListener() {

								@Override
								public void success(JSONObject result) {
									// TODO Auto-generated method stub
									useCard = true;
									paytype = new Gson().fromJson(result.toString(), PayTypeEntity.class);
									if (paytype.getAraamount() <= 0) {
										// 支付完成
										paytype.setPayType(PayType.SALE);
										PrintOrderData.printOrderPay(paytype, null);
										// 跳到广告页面
										AdverActivity.start(PaySelectActivity.this, paytype, null);
									} else {
										T.showCenter("卡券核销成功");
										payInit(paytype);
									}
								}

								@Override
								public void failed(String msg) {
									// TODO Auto-generated method stub

								}
							});
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

	private void createQR(String QRurl, ImageView iv) {
		// 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
		Bitmap qrcodeBitmap;
		try {
			qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 700);
			iv.setImageBitmap(qrcodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		close_timer();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		close_timer();
		// close_qrScan();
	}

	private void close_timer() {
		if (poll_timer != null) {
			poll_timer.cancel();
		}
		if (micor_query_timer != null) {
			micor_query_timer.cancel();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		close_timer();
		// close_qrScan();
		super.finish();
	}
	
	@Override
	protected void back() {
		if (currentState == PayDefine.PayResult.WAIT_INT) {
			DialogAsk.ask(this, "支付提示", "确定放弃支付吗？", "确定", "取消", new DialogAskListener() {

				@Override
				public void okClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					finish();
				}

				@Override
				public void cancelClick(DialogInterface dialog) {
					// TODO Auto-generated method stub

				}
			});
		}else{
			super.back();
		}
	}
}
*/