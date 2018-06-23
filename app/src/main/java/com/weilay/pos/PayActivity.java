package com.weilay.pos;

import com.framework.utils.InputMoneyFilter;
import com.weilay.pos.app.PayAction;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BaseKeyBoard;
import com.weilay.pos.util.BaseKeyBoard.OPTIONS_KEY;
import com.weilay.pos.util.BaseKeyBoard.OnKeyListener;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.T;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*********
 * @Detail 支付输入页面
 * @author Administrator
 * 
 */
public class PayActivity extends NotTitleActivity implements OnKeyListener {

	private TextView jine_tv, vipcouponinfo_tv, payinfo_santype_tv;
	private String payType = "";
	private int payAction;// 为xx支付
	private String vipNo;
	private Editable edit;
	private BaseKeyBoard baseKeyboard;
	private LinearLayout keyboardll;
	private PayTypeEntity paytype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.input_pay_layout);
		init();
		reg();
	}

	private void init() {
		Intent payIntent = getIntent();
		payType = payIntent.getStringExtra("payType");
		// 获取支付的意图 update by rxwu at 2016/07/11
		payAction = payIntent.getIntExtra(PosDefine.INTENT_PAY_ACTION,
				PayAction.DEFAULT_PAY);

		// startScan();
		// getViewfinderView().setVisibility(View.VISIBLE);
		View keyboardView = findViewById(R.id.keyboard_view);
		baseKeyboard = new BaseKeyBoard(keyboardView);
		baseKeyboard.setOnkeyListener(this);
		jine_tv = (TextView) findViewById(R.id.pay_amount);
		vipcouponinfo_tv = (TextView) findViewById(R.id.vipcouponinfo);
		// payinfo_santype_tv = (TextView) findViewById(R.id.payinfo_santype);
		// paidAmount_tv = (TextView) findViewById(R.id.paidAmount_tv);
		// scanIv = (ImageView) findViewById(R.id.scan_iv);
		edit = jine_tv.getEditableText();
		edit.setFilters(new InputFilter[] { new InputMoneyFilter(10000000) });
		// scanll = (LinearLayout) findViewById(R.id.scan_ll);
		keyboardll = (LinearLayout) findViewById(R.id.keyboard_ll);
	}

	boolean isScan = false;

	private void reg() {
		if (payType != null && payType.equals("vipRecharge")) {
			payinfo_santype_tv.setText("会员编号");
			vipcouponinfo_tv.setText("请出示会员二维码");
		}
	}

	boolean scanable = true;

	/**
	 * 监听外设小键盘的Enter键 KeyCode值为160
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gg", "onKeyDown" + keyCode);
		if (keyCode == 160) {
			String str = jine_tv.getText().toString();
			pay(str);
		}
		return super.onKeyDown(keyCode, event);
	}

	/******
	 * @detail 支付处理
	 * @param InputAmount
	 */
	public void pay(String InputAmount) {
		switch (payAction) {
		case PayAction.MEMBER_RECHARGE_PAY:// 会员充值先扫描会员卡
			if (TextUtils.isEmpty(vipNo)) {
				T.showCenter("请先让顾客扫描会员卡");
				break;
			}
		default:
			double f;
			if (InputAmount.equals("")) {
				InputAmount = "0";
			}
			try {
				// update 将单位改成分
				f = ConvertUtil.getMoeny(InputAmount);
				if (f >= 0.01f) {
					if (f > 1000000f) {
						Toast.makeText(PayActivity.this, "支付金额超过限额", 0).show();
					} else {
						// 将支付信息封装好传递到支付页面
						paytype = new PayTypeEntity();
						paytype.setTx_no(GetUtil.getOutTradeNo());
						paytype.setAmount(f);
						Intent i = new Intent(PayActivity.this,
								PaySelectActivity2.class);
						i.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
						startActivity(i);
						finish();
					}
				} else {
					T.showLong("输入金额必须大于0.01!而且不能为空!");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		}

	}

	@Override
	public void onNumberClick(String num) {
		// TODO Auto-generated method stub
		if (edit == null) {
			return;
		}
		if (edit.length() < 8) {
			edit.append(num);
		} else {
			T.showCenter("超过输入的长度");
		}
	}

	@Override
	public void onOptions(OPTIONS_KEY option) {
		// TODO Auto-generated method stub
		if (edit == null) {
			return;
		}
		switch (option) {
		case CLEAR:
			edit.clear();
			break;
		case DELETE:
			if (edit.length() >= 1) {
				edit.delete(edit.length() - 1, edit.length());
			}
			break;
		case ENTER:
			String str = jine_tv.getText().toString();
			pay(str);
			break;
		case DOUBLE_ZERO:
			if (edit.length() < 8) {
				if (edit.length() == 7) {
					edit.append("0");
				} else {
					edit.append("00");
				}
			}
			break;
		case POINT:
			if (edit.length() < 1 || edit.toString().contains(".")) {
				return;
			}
			edit.append(".");
			break;
		default:
			break;
		}
	}

	public static void actionStart(BaseActivity activity) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(activity,PayActivity.class);
		activity.startActivity(intent);
		
	}
}
