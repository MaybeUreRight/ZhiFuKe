package com.weilay.pos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.ui.HorizontalListView;
import com.framework.utils.L;
import com.framework.utils.NetworkUtils;
import com.framework.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.weilay.dialog.UseCardDialog;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.PayAdapter;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.app.PayDefine;
import com.weilay.pos.app.PayType;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.AdverEntity;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.OperatorEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.entity.QRInfoEntity;
import com.weilay.pos.listener.CardUseListener;
import com.weilay.pos.listener.GetCouponListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.printData.PrintOrderData;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.ActivityStackControlUtil;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.BeepManager;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.EncodingHandler;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.IFlytekHelper;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.FormBody;

public class PaySelectActivity2 extends NotTitleActivity implements Callback,
		OnClickListener, OnItemClickListener {
	private int currentState = PayDefine.PayResult.NONE;
	private boolean scaning = false;// 没有扫描
	private LinearLayout otherLl, cashLl, premissionLl;
	private ImageView pay_show_qr;
	private TextView pay_amount, pay_explain, pay_paidAmount;
	private Timer poll_timer, micor_query_timer;
	private PayType currentPayType = PayType.CASH;
	private PayTypeEntity paytype;
	private RelativeLayout zxing_layout;
	private boolean checkZxing = true;
	private boolean checkCode = true;
	private android.view.ViewGroup.LayoutParams mLayoutParams;
	private android.view.ViewGroup.LayoutParams qrCodeLayoutParams;
	private boolean useCard = false;// 是否已经使用了卡券

	private HorizontalListView payHlv;
	private PayType[] paytypes = new PayType[] { PayType.WEIXIN,
			PayType.ALIPAY, PayType.CASH, PayType.CHUZHIKA, PayType.SALE };
	private PayAdapter mAdapter;
	
	public static void actionStart(BaseActivity act, PayTypeEntity paytype,
			boolean close) {
		Intent intent = new Intent(act, PaySelectActivity2.class);
		intent.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
		act.startActivity(intent);
		act.finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		paytype = (PayTypeEntity) intent
				.getSerializableExtra(PosDefine.INTENTE_PAY_INFO);
		if (paytype != null) {
			payInit(paytype);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_capture2);
		paytype = (PayTypeEntity) getIntent().getSerializableExtra(
				PosDefine.INTENTE_PAY_INFO);
		boolean networkable = NetworkUtils.isNetworkable(this);
		if (paytype == null) {
			DialogConfirm.ask(this, "支付提示", "非法支付请求", "确定",
					new DialogConfirmListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							finish();
						}
					});
			return;
		}
		if (!networkable) {
			DialogConfirm.ask(this, "支付提示", "当前的网络不可用", "确定", null);
		}
		initViews();
		initDatas();
		initEvent();
	}

	// 初始化视图
	private void initViews() {
		startScan();
		getViewfinderView().setScanSize(0.7);

		otherLl = (LinearLayout) findViewById(R.id.other_payment);
		cashLl = (LinearLayout) findViewById(R.id.cash_payment);
		premissionLl = (LinearLayout) findViewById(R.id.premission_ll);
		zxing_layout = (RelativeLayout) findViewById(R.id.rl_zxing);
		mLayoutParams = zxing_layout.getLayoutParams();

		// update by rxwu
		payHlv = (HorizontalListView) findViewById(R.id.choose_pay_hlv);
		// 显示支付金额
		pay_amount = (TextView) findViewById(R.id.pay_amount);
		// 支付的二维码
		pay_show_qr = (ImageView) findViewById(R.id.pay_show_qr);
		qrCodeLayoutParams = pay_show_qr.getLayoutParams();
		// 其他支付的提示文本
		pay_explain = (TextView) findViewById(R.id.pay_explain);
		// 原价
		pay_paidAmount = (TextView) findViewById(R.id.pay_paidAmount);
		pay_paidAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}

	List<PayType> results = new ArrayList<>();

	// 初始化数据
	@SuppressLint("NewApi")
	private void initDatas() {
		pay_explain.setText("请扫描二维码或将付款码对准摄像头");
		Collections.addAll(results, paytypes);
		mAdapter = new PayAdapter(mContext, results);
		payHlv.setAdapter(mAdapter);
		payInit(paytype);
		// 默认选择微信支付---update by rxwu at 2016/08/05
	}

	// 初始化事件
	private void initEvent() {
		payHlv.setOnItemClickListener(this);
		findViewById(R.id.pay_show_cash).setOnClickListener(this);
		zxing_layout.setOnClickListener(this);
		pay_show_qr.setOnClickListener(this);
	}

	boolean first = true;

	/*****
	 * @detail 初始化支付的信息
	 */
	private void payInit(PayTypeEntity payType) {
		// update by rxwu,检查权限
		if (paytype != null) {
			pay_paidAmount.setVisibility(paytype.getAmount() == paytype
					.getAraamount() ? View.GONE : View.VISIBLE);
			pay_paidAmount.setText("应收:"
					+ ConvertUtil.getMoeny(paytype.getAmount()) + "元");
			pay_paidAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			pay_amount.setText(ConvertUtil.getMoeny(paytype.getAraamount())+ "元");
			if (!StringUtil.isNotEmpty(payType.getTx_no(), paytype.getTx_no2())) {
				// 默认生成两个订单号，一个主扫一个被扫
				payType.setTx_no(GetUtil.getOutTradeNo());
				payType.setTx_no2(GetUtil.getOutTradeNo());
			}
			mAdapter.notifyDataSetChange(results);
		}
		// 默认选择微信支付
		choosePay(payType.getPayType());// update 20170317 cyh 支付方式使用实体类的
	}
	/******
	 * @detail 修改界面上的二维码和摄像头风格
	 * @return void
	 * @param 
	 * @detail
	 */
	private void qrClick(){
		if (checkCode) {
			pay_explain.setVisibility(View.GONE);
			zxing_layout.setVisibility(View.GONE);
		} else {
			pay_explain.setVisibility(View.VISIBLE);
			zxing_layout.setVisibility(View.VISIBLE);
		}
		pay_show_qr
				.setLayoutParams(checkCode ? new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT)
						: qrCodeLayoutParams);
		checkCode = !checkCode;
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pay_show_cash:
			showLoading("请稍候...");
			send_cashPay();
			break;
		case R.id.pay_show_qr:
			qrClick();
			break;
		case R.id.rl_zxing:
			if (checkZxing) {
				pay_show_qr.setVisibility(View.GONE);
				pay_explain.setVisibility(View.GONE);
			} else {
				pay_show_qr.setVisibility(View.VISIBLE);
				pay_explain.setVisibility(View.VISIBLE);
			}
			zxing_layout
					.setLayoutParams(checkZxing ? new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT)
							: mLayoutParams);
			checkZxing = !checkZxing;
			break;
		}
	}

	public void resetState() {
		if (zxing_layout != null && pay_show_qr != null) {
			pay_explain.setVisibility(View.VISIBLE);
			zxing_layout.setLayoutParams(mLayoutParams);
			pay_show_qr.setLayoutParams(qrCodeLayoutParams);
		}
	}

	/*******
	 * @detail 选择支付的方式
	 * @param action
	 */
	private void choosePay(PayType paymethod) {
		resetState();
		currentPayType = paymethod;
		paytype.setPayType(currentPayType);
		close_timer();
		setTitle(paymethod.getValue() + "支付");
		mAdapter.setSelect(currentPayType);
		int permission = checkPremission(paymethod);
		switch (paymethod) {
		case WEIXIN:
		case ALIPAY:
			pay_explain
					.setText(permission == OperatorEntity.PAY_PERMISSION_ALL ? "请扫描二维码或将付款码对准摄像头"
							: (permission == OperatorEntity.PAY_PERMISSION_MICRO ? "请将付款码对准摄像头"
									: "请扫描二维码支付"));
			if (permission == OperatorEntity.PAY_PERMISSION_ALL
					|| permission == OperatorEntity.PAY_PREMISSION_UNIFIED) {
				showAmount(paytype);
			}
			break;
		case CHUZHIKA:
		case SALE:
			pay_explain.setText(paymethod == PayType.CHUZHIKA ? "请将会员卡对准摄像头"
					: "请将卡券对准摄像头");
			break;
		default:
			break;
		}
	}

	// 设置显示 现金支付/第三方支付/没有权限的面板
	private static final int cash = 0, other = 1, premission = 2;

	public void setViewVisible(int num) {
		cashLl.setVisibility(num == cash ? View.VISIBLE : View.GONE);
		otherLl.setVisibility(num == other ? View.VISIBLE : View.GONE);
		premissionLl
				.setVisibility(num == premission ? View.VISIBLE : View.GONE);
		if (num != premission) {
			zxing_layout.setVisibility(View.GONE);
			pay_show_qr.setVisibility(View.GONE);
		}

	}

	/******
	 * @detial 检查权限，来控制面板的显示
	 * @return void
	 */
	private int checkPremission(PayType type) {
		OperatorEntity operator = Utils.getCurOperator();
		int permission = operator.getPermission(type);
		setViewVisible(type == PayType.CASH ? cash : other);
		switch (permission) {
		case OperatorEntity.PAY_PERMISSION_NONE:
			setViewVisible(premission);
			break;
		case OperatorEntity.PAY_PERMISSION_MICRO:
			zxing_layout.setVisibility(View.VISIBLE);
			break;
		case OperatorEntity.PAY_PREMISSION_UNIFIED:
			pay_show_qr.setVisibility(View.VISIBLE);
			break;
		case OperatorEntity.PAY_PERMISSION_ALL:
			zxing_layout.setVisibility(View.VISIBLE);
			pay_show_qr.setVisibility(View.VISIBLE);
			if(mCache.getAsInt("SETTING_PAYSTYLE",0)==1){
				//如果已经设置了支付的风格为大二维码的风格
				checkCode=true;
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						qrClick();
					}
				}, 300);
				
			}
			break; 
		}
		return permission;

	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public synchronized void handleDecode(Result result) {
		super.handleDecode(result);
		if (scaning) {
			return;
		}
		scaning = true;
		showLoading("已扫描!正在处理...", 1, true, null);
		String resultString = result.getText();
		L.i("gg", "扫描结果:" + resultString);
		if (StringUtil.isBank(resultString)) {
			scaning = false;
			Toast.makeText(PaySelectActivity2.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
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
		default:
			break;
		}
	}
	private boolean cashpaying=false;

	// 现金支付
	private void send_cashPay() {
		if(cashpaying){
			return;
		}
		cashpaying=true;
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", paytype.getTx_no());
		// update by rxwu at 2016/11/7(如果有使用会员卡支付，那么会返回会员卡的信息供后台修改会员积分)
		builder.add("code",
				paytype.getMemberNo() == null ? "" : paytype.getMemberNo());
		builder.add("totalamount", ConvertUtil.doubleToString(d));
		HttpUtils.sendPost(mContext, builder, UrlDefine.URL_CASH_PAY,
				new ResponseListener() {

					@Override
					public void onSuccess(JSONObject jo) {
						// TODO Auto-generated method stub
						cashpaying=false;
						try {
							JSONObject data_jo = new JSONObject(jo
									.getString("data"));
							if (jo.optJSONObject("adver").optInt("code") == 0) {
								List<AdverEntity> advers = new Gson().fromJson(
										jo.optJSONObject("adver").optString(
												"data"),
										new TypeToken<List<AdverEntity>>() {
										}.getType());
								paytype.setAdvers(advers);
							}
							QRInfoEntity qrinfo = new Gson().fromJson(
									jo.optString("qrinfo"), QRInfoEntity.class);
							paytype.setQrInfo(qrinfo);
							paytype.setTime(jo.getString("time"));
							paytype.setMicro(false);// 标识不是主扫
							double araamount = ConvertUtil.branchToYuan(data_jo
									.optDouble("totalAmount"));
							paytype.setFirstDiscount(paytype.getAraamount()
									- araamount);// 记录首单优惠金额
							paySuccess();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailed(int code, String msg) {
						cashpaying=false;
						close_timer();
						DialogConfirm.ask(mContext, "现金支付提示", msg, "确定",
								new DialogConfirmListener() {

									@Override
									public void okClick(DialogInterface dialog) {
									}
								});
					}
				});
	}

	BeepManager beepManager = null;
	private void paySuccess() {
		if(PrintSettingActivity.isPaySound()){
			try {
				IFlytekHelper.speaking(mContext,"收款成功，"+paytype.getPayType().getValue()+"收款 "+paytype.getAraamount()+"元");
			} catch (Exception ex) {
				L.e("播放声音失败");
			}
		}
		close_timer();
		currentState = PayDefine.PayResult.SUCCESS_INT;
		PrintOrderData.printOrderPay(paytype, null);
		AdverEntity adver = (paytype.getAdvers() == null || paytype.getAdvers()
				.size() < 1) ? null : paytype.getAdvers().get(0);
		AdverActivity.start(PaySelectActivity2.this, paytype, adver);
	}

	private void PayShow(String qrcode, final String tx_no) {
		if (pay_show_qr != null) {
			createQR(qrcode, pay_show_qr);
		}
		// 关闭上次的轮询
		close_timer();

		poll_timer = new Timer();
		poll_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				send_QueryPay(tx_no);
			}
		}, 10000, 4000);
	}

	// 保存订单的支付二维码
	private static final String CACHE_ORDER_KEY = "SAVE_ORDER_";

	/*
	 * 向服务器请求支付二维码
	 */
	private void showAmount(final PayTypeEntity payType) {
		final PayType payMethod = payType.getPayType();
		final String key = CACHE_ORDER_KEY + payMethod.getName()
				+ paytype.getTx_no() + paytype.getAraamount();
		if (!StringUtil.isBank(mCache.getAsString(key))) {
			// 已经请求过
			PayShow(mCache.getAsString(key), paytype.getTx_no());
		} else {
			// 第一次请求支付
			L.i("支付方式", payMethod.getName());
			showLoading("请稍候");
			
			String d = ConvertUtil
					.yuanToBranch(paytype.getAraamount())+"";
			FormBody.Builder builder = BaseParam.getParams();
			builder.add("paytype", paytype.getPayType().getName());
			builder.add("tx_no", paytype.getTx_no());
			builder.add("totalAmount", d);
			builder.add("tradetype", "jsapi");
			builder.add("goods", Utils.getCurOperator().getName() + "安全支付");
			builder.add("goodCodes", paytype.getTx_no());
			builder.add("code",
					paytype.getMemberNo() == null ? "" : paytype.getMemberNo());
			HttpUtils.sendPost(mContext, builder, UrlDefine.URL_SHOW_PAY,
					new ResponseListener() {
						@Override
						public void onSuccess(JSONObject jo) {
							// TODO Auto-generated method stub
							currentState = PayDefine.PayResult.WAIT_INT;
							stopLoading();
							try {
								String payCode = jo.getString("data");
								mCache.put(key, payCode, 1 * 24 * 60 * 60);// 保存一天
								// 已经请求过
								PayShow(mCache.getAsString(key),paytype.getTx_no());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailed(int code, String msg) {
							// TODO Auto-generated method stub
							T.showCenter(msg);
							currentState = PayDefine.PayResult.ERROR_INT;
							if (pay_show_qr != null) {
								pay_show_qr.setVisibility(View.INVISIBLE);// 如果不可支付
							}
							stopLoading();
						}
					});
		}
	}

	/**
	 * 条码支付
	 * 
	 * @param amount
	 * @param paytype
	 * @param auth_code
	 */
	private void send_passive(final PayTypeEntity paytype, String auth_code) {
		showLoading("支付进行中...");
		FormBody.Builder builder = BaseParam.getParams();
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		builder.add("paytype", paytype.getPayType().getName());
		// rxwu,每次启动支付的时候会默认生成两个订单编号，只有主动扫码支付的时候会使用到tx_no2,服务器需要根据这里上传的tx_no2将tx_no1记录全部update成tx_no2
		builder.add("tx_no2", paytype.getTx_no2());
		builder.add("tx_no1", paytype.getTx_no());
		// updateFlag 可以给服务器标识将要更新优惠券表还是要更新会员卡信息表，或者都不更新或者全部更新
		builder.add("updateFlag", "" + paytype.getUpdateFlag());
		builder.add("totalAmount", ConvertUtil.doubleToString(d));
		builder.add("auth_code", auth_code);
		builder.add("goods", Utils.getCurOperator().getName() + "安全支付");
		builder.add("goodCodes", paytype.getTx_no());
		builder.add("code",
				paytype.getMemberNo() == null ? "" : paytype.getMemberNo());
		HttpUtils.sendPost(builder, UrlDefine.URL_MICRO_PAY,
				new ResponseListener() {
					@Override
					public void onSuccess(JSONObject jo) {
						stopLoading();
						try {
							JSONObject data_jo = jo.optJSONObject("data");

							if (jo.optJSONObject("adver").optInt("code") == 0) {
								List<AdverEntity> advers = new Gson().fromJson(
										jo.optJSONObject("adver").optString(
												"data"),
										new TypeToken<List<AdverEntity>>() {
										}.getType());
								paytype.setAdvers(advers);
							}
							QRInfoEntity qrinfo = new Gson().fromJson(
									jo.optString("qrinfo"), QRInfoEntity.class);
							double araamount = ConvertUtil.branchToYuan(data_jo
									.optDouble("totalAmount"));
							paytype.setMicro(true);// 标识是主扫支付的
							paytype.setQrInfo(qrinfo);
							paytype.setTime(jo.getString("time"));
							paytype.setFirstDiscount(paytype.getAraamount()
									- araamount);
							paySuccess();
							scaning = false;
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							scaning = false;
						}
					}

					@Override
					public void onFailed(int code, String msg) {
						// TODO Auto-generated method stub
						stopLoading();

						switch (code) {
						case 1:// USERPAYING
							paytype.setMicro(true);// 标识是主扫支付的
							currentState = PayDefine.PayResult.WAIT_INT;
							showLoading("用户输密码中...");
							close_timer();
							micor_query_timer = new Timer();
							micor_query_timer.schedule(new TimerTask() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									// 主扫查询订单2
									send_QueryPay(paytype.getTx_no2());

								}
							}, 7000, 5000);
							break;
						default:
							scaning = false;
							close_timer();
							DialogConfirm.ask(mContext, "提示",
									"付款失败!请尝试使用扫描支付!（" + msg + ")", "确定",
									new DialogConfirmListener() {

										@Override
										public void okClick(
												DialogInterface dialog) {
											currentState = PayDefine.PayResult.ERROR_INT;
										}
									});
							break;
						}

					}
				});

	}

	private void QueryPayErr(String msg) {
		close_timer();
		currentState = PayDefine.PayResult.ERROR_INT;
		if(PrintSettingActivity.isPaySound()){
			try {
				IFlytekHelper.speaking(mContext,"收款失败，"+msg+"元");
			} catch (Exception ex) {
				L.e("播放声音失败");
			}
		}
		DialogConfirm.ask(mContext, "提示", "支付失败!（" + msg + ")", "确定",
				new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						mContext.finish();
					}
				});
	}

	/******
	 * @Detail 查询支付结果
	 * @return void
	 * @param
	 * @detail
	 */
	private void send_QueryPay(final String tx_no) {
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", tx_no);
		builder.add("type", "microPay");
		builder.add("paytype", paytype.getPayType().getName());
		HttpUtils.sendPost(builder, UrlDefine.URL_QUERY_PAY,
				new ResponseListener() {
					@Override
					public void onSuccess(JSONObject jo) {
						stopLoading();
						try {
							JSONObject data_jo = new JSONObject(jo
									.getString("data"));

							L.i("gg->poll", data_jo.toString());
							if (jo.optJSONObject("adver").optInt("code") == 0) {
								List<AdverEntity> advers = new Gson().fromJson(
										jo.optJSONObject("adver").optString(
												"data"),
										new TypeToken<List<AdverEntity>>() {
										}.getType());
								paytype.setAdvers(advers);
							}
							QRInfoEntity qrinfo = new Gson().fromJson(
									jo.optString("qrinfo"), QRInfoEntity.class);
							paytype.setQrInfo(qrinfo);
							// update by rxwu at 2016/08/17 设置首单的优惠金额
							double araamount = ConvertUtil.branchToYuan(data_jo
									.optDouble("totalAmount"));
							paytype.setDiscountType(data_jo
									.optString("discountType"));
							paytype.setFirstDiscount(paytype.getAraamount()
									- araamount);
							paytype.setTime(jo.optString("time"));
							paySuccess();
						} catch (Exception e) {
							e.printStackTrace();
							QueryPayErr("");
						}
					}

					@Override
					public void onFailed(int code, String msg) {
						// TODO Auto-generated method stub
						switch (code) {
						case 3:
							stopLoading();
							QueryPayErr(msg);
							break;
						default:
							if(!StringUtil.isBank(msg)){
								//T.showCenter(msg);
							}
							break;
						}
					}
				});
	}

	/*********
	 * @detail 扫描会员卡和优惠券的处理方法
	 * @param ismember
	 *            是否为会员卡
	 * @param vipNo
	 */
	private void scanCard(final String vipNo, final boolean ismember) {
		Utils.getCouponInfo(this, vipNo, new GetCouponListener() {

			@Override
			public void onFailed(String msg) {
				scaning = false;
				stopLoading();
				DialogConfirm.ask(PaySelectActivity2.this, "获取卡券信息提示",
						"获取卡券信息失败", "确定", null);
			}

			@Override
			public void onData(CouponEntity coupon) {
				// TODO Auto-generated method stub
				scaning = false;
				stopLoading();
				String type = coupon.getType();
				switch (type) {
				case CardTypeEnum.MEMBER_CARD:
					if (ismember) {
						Intent intent = new Intent(PaySelectActivity2.this,
								VipInfoActivity.class);
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
						if (paytype.getCouponDiscountAmount() != 0) {
							DialogConfirm.ask(mContext, "优惠券使用提示",
									"每个订单只能使用一次优惠券", "放弃使用",
									new DialogConfirmListener() {

										@Override
										public void okClick(
												DialogInterface dialog) {
											// TODO Auto-generated method stub
											return;
										}
									});
							return;
						}
						if (CardTypeEnum.DISCOUNT.equals(type)
								&& paytype.getMemberDiscountAmount() != 0) {
							// 如果是折扣券，并且已经使用过会员卡折扣
							DialogConfirm.ask(mContext, "优惠券使用提示",
									"您已经使用过会员卡折扣，不能再使用折扣券", "放弃使用",
									new DialogConfirmListener() {

										@Override
										public void okClick(
												DialogInterface dialog) {
											// TODO Auto-generated method stub
											return;
										}
									});
							return;
						}
						if (paytype.getAraamount() >= (coupon.getLeast_cost() / 100)) {
							UseCardDialog useCardialog = new UseCardDialog(
									coupon, paytype);
							useCardialog.show(getSupportFragmentManager(),
									"使用卡券");
							useCardialog
									.setCardUseListener(new CardUseListener() {

										@Override
										public void success(PayTypeEntity result) {
											// TODO Auto-generated method stub
											useCard = true;
											paytype = result;
											if (paytype.getAraamount() <= 0) {
												// 支付完成
												paytype.setPayType(PayType.SALE);
												PrintOrderData.printOrderPay(
														paytype, null);
												// 跳到广告页面
												AdverActivity
														.start(PaySelectActivity2.this,
																paytype, null);
											} else {
												T.showCenter("卡券核销成功");
												payInit(paytype);
											}
										}

										@Override
										public void failed(String msg) {
											// TODO Auto-generated method stub
											T.showCenter(msg);
										}
									});
						} else {
							T.showCenter("当前卡券必须满"
									+ (coupon.getLeast_cost() / 100) + "元才可使用");
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
		super.onDestroy();
		scaning = false;
		close_timer();

	}

	@Override
	protected void onPause() {
		super.onPause();
		close_timer();
	}

	private void close_timer() {
		scaning = false;
		if (poll_timer != null) {
			poll_timer.cancel();
		}
		if (micor_query_timer != null) {
			micor_query_timer.cancel();
		}
	}

	@Override
	public void finish() {
		close_timer();
		// close_qrScan();
		super.finish();
	}

	@Override
	protected void back() {
		if (currentState == PayDefine.PayResult.WAIT_INT) {
			DialogAsk.ask(this, "支付提示", "确定放弃支付吗？", "确定", "取消",
					new DialogAskListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							currentState = PayDefine.PayResult.CANCEL_INT;
							L.e("gg", "当前Activity:" + getRunningActivityName());
							String ActivityName = getRunningActivityName();
							if (!ActivityName
									.equals("com.weilay.pos.MainActivity")) {
								ActivityStackControlUtil.closeAll();
								Intent i = new Intent(getApplicationContext(),
										MainActivity.class);
								startActivity(i);
							}
						}

						@Override
						public void cancelClick(DialogInterface dialog) {
							currentState = PayDefine.PayResult.WAIT_INT;
						}
					});
		} else {
			currentState = PayDefine.PayResult.CANCEL_INT;
			super.back();
		}
	}

	protected void home() {
		if (currentState == PayDefine.PayResult.WAIT_INT) {
			DialogAsk.ask(this, "支付提示", "确定放弃支付吗？", "确定", "取消",
					new DialogAskListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							currentState = PayDefine.PayResult.CANCEL_INT;
							L.e("gg", "当前Activity:" + getRunningActivityName());
							String ActivityName = getRunningActivityName();
							if (!ActivityName
									.equals("com.weilay.pos.MainActivity")) {
								ActivityStackControlUtil.closeAll();
								Intent i = new Intent(getApplicationContext(),
										MainActivity.class);
								startActivity(i);
							}
						}

						@Override
						public void cancelClick(DialogInterface dialog) {
							currentState = PayDefine.PayResult.WAIT_INT;
						}
					});
		} else {
			currentState = PayDefine.PayResult.CANCEL_INT;
			super.home();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		choosePay(paytypes[arg2]);
	}
}
