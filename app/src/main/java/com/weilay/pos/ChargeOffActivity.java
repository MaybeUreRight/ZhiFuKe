package com.weilay.pos;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.utils.L;
import com.google.zxing.Result;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.listener.ChargeOffCouponListener;
import com.weilay.pos.listener.GetCouponListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseKeyBoard;
import com.weilay.pos.util.BaseKeyBoard.OPTIONS_KEY;
import com.weilay.pos.util.BaseKeyBoard.OnKeyListener;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/*****
 * @Detail 核销页面
 * @author rxwu
 * 
 */
@SuppressLint("NewApi")
public class ChargeOffActivity extends TitleActivity implements OnKeyListener,
		OnClickListener{

	private EditText chargeOff_code;
	String conSume_url = "API/cardConsume";
	private ImageView scanIv;
	private LinearLayout sacan_layout;
	private LinearLayout keyboardll;
	private Editable mEditable;
	private BaseKeyBoard baseKeyBoard;
	boolean scan = false;// 摄像头是否开启

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.chargeoff_layout2);
		setTitle("核销");
		chargeoff_init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void chargeoff_init() {
		// 启动摄像头
		// startScan(R.id.viewfinder_view, R.id.preview_view);
		startScan();
		getViewfinderView().setScanSize(0.6);
		sacan_layout = (LinearLayout) findViewById(R.id.scan_ll);
		scanIv = (ImageView) findViewById(R.id.scan_iv);
		keyboardll = (LinearLayout) findViewById(R.id.keyboard_ll);
		chargeOff_code = (EditText) findViewById(R.id.chargeoff_code);
		chargeOff_code.setInputType(InputType.TYPE_NULL);

		baseKeyBoard = new BaseKeyBoard(findViewById(R.id.keyboard_co));
		baseKeyBoard.setOnkeyListener(this);
		findViewById(R.id.scan_iv).setOnClickListener(this);

		mEditable = chargeOff_code.getEditableText();
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 */
	public void handleDecode(Result result) {
		super.handleDecode(result);
		String resultString = result.getText();
		L.i("gg", "扫描结果:" + resultString);
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(ChargeOffActivity.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			queryCard(resultString);
		}
	}

	/*****
	 * @detail 查询卡券的信息
	 */
	public void queryCard(final String code) {
		showLoading("正在查询卡券信息..");
		Utils.getCouponInfo(this, code, new GetCouponListener() {

			@Override
			public void onFailed(String msg) {
				// TODO Auto-generated method stub
				stopLoading();
				T.showCenter("不允许使用非本门店卡券");
			}

			@Override
			public void onData(CouponEntity coupon) {
				// TODO Auto-generated method stub
				stopLoading();
				StringBuffer stringbuffer = new StringBuffer();
				stringbuffer.append("卡号：" + code + "\n");
				stringbuffer.append("卡券信息:"
						+ (coupon.getInfo() == null ? "" : coupon.getInfo())
						+ "\n");
				stringbuffer.append("使用提示:"
						+ (coupon.getNotice() == null ? "" : coupon.getNotice())
						+ "\n");
				switch (coupon.getType()) {
				case CardTypeEnum.GIFT:
				case CardTypeEnum.FRIEND_GIFT:
				case CardTypeEnum.GROUPON:// 支持团购券
				case CardTypeEnum.SCENIC_TICKET://新增支付景区券核销
					DialogAsk.ask(ChargeOffActivity.this, "卡券使用提示",
							stringbuffer.toString(), "确定使用", "取消",
							new DialogAskListener() {
								@Override
								public void okClick(DialogInterface dialog) {
									// TODO Auto-generated method stub
									showLoading("卡券核销中..");
									send_chargeOff(code);
								}
								@Override
								public void cancelClick(DialogInterface dialog) {
									// TODO Auto-generated method stub
									T.showCenter("用户放弃使用卡券");
								}
							});
					break;
				default:
					cardConsumeNoFound("不支持此类卡券,仅支持兑换券核销");
					break;
				}
			}
		});
	}

	/*****
	 * @detail 提示卡券没找到
	 */
	public void cardConsumeNoFound(String msg) {
		if (!isFinishing() && !isDestroyed()) {
			DialogConfirm.ask(this, "卡券提示", msg, "确认",
					new DialogConfirmListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
						}
					});
		}
		chargeOff_code.setText("");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		// String inputAmount = chargeOff_code.getText().toString();
		switch (view.getId()) {
		case R.id.scan_iv:
			if (scan) {
				scan = false;
				keyboardll.setVisibility(View.VISIBLE);
				sacan_layout.setVisibility(View.GONE);
				scanIv.setImageResource(R.drawable.zxingscan);
			} else {
				restartCerame();
				scan = true;
				sacan_layout.setVisibility(View.VISIBLE);
				keyboardll.setVisibility(View.GONE);
				scanIv.setImageResource(R.drawable.icon_keyboard1);
			}
			break;
		}
	}

	private void send_chargeOff(String codeNo) {
		showLoading("正在核销卡券...");
		Utils.SendChargeOff(mContext, codeNo, new ChargeOffCouponListener() {
			
			@Override
			public void onSuc() {
				// TODO Auto-generated method stub
				DialogConfirm.ask(ChargeOffActivity.this, "核销提示", "核销成功", "确定",
						new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								ChargeOffActivity.this.finish();
							}
						});
			}
			
			@Override
			public void onErr() {
				// TODO Auto-generated method stub
				DialogConfirm.ask(ChargeOffActivity.this, "核销提示", "核销失败", "确定",
						new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								ChargeOffActivity.this.finish();
							}
						});
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onNumberClick(String num) {
		// TODO Auto-generated method stub
		if (mEditable == null) {
			return;
		}
		mEditable.append(num);
	}

	@Override
	public void onOptions(OPTIONS_KEY option) {
		// TODO Auto-generated method stub
		if (mEditable == null) {
			return;
		}
		switch (option) {
		case CLEAR:
			mEditable.clear();
			break;
		case DELETE:
			if (mEditable.length() >= 1) {
				mEditable.delete(mEditable.length() - 1, mEditable.length());
			}
			break;
		case ENTER:
			if (mEditable.length() >= 1) {
				queryCard(mEditable.toString());
			}
			break;
		case DOUBLE_ZERO:
			mEditable.append("00");
			break;
		default:
			break;
		}
	}
}
