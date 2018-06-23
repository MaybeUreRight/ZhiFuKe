package com.weilay.pos;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.google.gson.Gson;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.ShiftRecord;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.push.GlobalPush;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;
import com.weilay.pos.util.Utils;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import okhttp3.FormBody;

/*********
 * @detail 交班页面
 * @author chenyihao update by rxwu at 2016/08/19
 *
 */
public class ShiftActivity extends TitleActivity implements OnClickListener {
	private TextView weixin_jine, zhifubao_jine,recharge_jine,heji_jine,refund_jine, jiaoban_enter, jiaoban_cancel, jiaoban_cash,memberpay_jine;
	private TextView operator_exchange;
	private ShiftRecord sr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.exchange_layout);
		setTitle("交班");
		init();
		reg();
	}

	private void init() {
		weixin_jine = (TextView) findViewById(R.id.weixin_jine);
		zhifubao_jine = (TextView) findViewById(R.id.zhifubao_jine);
		recharge_jine=(TextView)findViewById(R.id.recharge_jine);
		refund_jine=(TextView)findViewById(R.id.refund_jine);
		heji_jine = (TextView) findViewById(R.id.heji_jine);
		jiaoban_cash = (TextView) findViewById(R.id.shift_cash);
		memberpay_jine=(TextView)findViewById(R.id.memberpay_jine);
		jiaoban_enter = (TextView) findViewById(R.id.jiaoban_enter);
		jiaoban_cancel = (TextView) findViewById(R.id.jiaoban_cancel);
		operator_exchange = (TextView) findViewById(R.id.operator_exchange);
		try{
			operator_exchange.setText("操作员:" + Utils.getCurOperator().getOperator());
		}catch(NullPointerException ex){
			
		}
	}

	private void reg() {
		jiaoban_enter.setOnClickListener(this);
		jiaoban_cancel.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getjiaoban(false);
	}
	/**********
	 * @Detail 交班请求 update by rxwu
	 * @param isShift
	 *            如果为true，那么是确认交班，否则为获取交班数据
	 */
	private void getjiaoban(final boolean isShift) {
		showLoading(isShift ? "正在交班..." : "正在获取交班数据");
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("click", "" + isShift);
		HttpUtils.sendPost(builder, UrlDefine.URL_POS_SHIFT, new ResponseListener() {
			@Override
			public void onSuccess(JSONObject json) {
				stopLoading();
				try {
					sr = new Gson().fromJson(json.optString("data"), ShiftRecord.class);
					weixin_jine.setText(ConvertUtil.parseMoney(sr.getwechat()));
					zhifubao_jine.setText(ConvertUtil.parseMoney(sr.getalipay()));
					jiaoban_cash.setText(ConvertUtil.parseMoney(sr.getCash()));
					refund_jine.setText(ConvertUtil.parseMoney(sr.getRefundAmt()));
					heji_jine.setText(ConvertUtil.parseMoney(sr.getTotalamount()));
					recharge_jine.setText(ConvertUtil.parseMoney(sr.getRechargeAmt()));
					memberpay_jine.setText(ConvertUtil.parseMoney(sr.getMemberPayAmt()));
					if (isShift) {
						DialogConfirm.ask(mContext, "交班提示", "交班成功", "确定", new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								if (sr != null) {
									sr.setTitle("交班表");
									USBComPort usbComPort = new USBComPort();
									usbComPort.printOutJBRecord(false,sr);
								}
								shift();
							}
						});
					}
				} catch (Exception ex) {
					if (isShift) {
						DialogConfirm.ask(mContext,"交班提示", "交班失败", "确定", new DialogConfirmListener(){

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								shift();
							}
							
						});
					} else {
						T.showCenter("获取交班数据格式有误");
					}
				}
			
			}

			@Override
			public void onFailed(int code, final String msg) {
				stopLoading();
				DialogConfirm.ask(mContext, "交班提示", "交班失败("+msg+")", "确定", new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (isShift) {
							shift();
						} else {
							T.showCenter(msg);
						}
					}
				});
				
			}
			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				stopLoading();
				DialogAsk.ask(mContext, "交班提示", "交班失败(网络出错，请重连网络)", "仍然登出账号", "去重连网络",new DialogAskListener() {
					
					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
							shift();
						
					}
					
					@Override
					public void cancelClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ShiftActivity.this,WifiActivity.class);
						intent.putExtra("isLogin", true);
						startActivity(intent);
					}
				});
			}
		});
	}

	// 交班切换到登陆页面
	private void shift() {
		//WeiLayApplication.closeComPort();
		// TODO Auto-generated method stub
		Utils.isLogin=false;
		Intent intent = new Intent(ShiftActivity.this, LoginActivity.class);
		ShiftActivity.this.startActivity(intent);
		GlobalPush.stopPush();// 交班时候停止接收
		finish();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.jiaoban_enter:
			DialogAsk.ask(mContext, "交班提示", "是否确定交班", "确定", "取消", new DialogAskListener() {

				@Override
				public void okClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					getjiaoban(true);
				}

				@Override
				public void cancelClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
				}
			});
			break;
		case R.id.jiaoban_cancel:
			onBackPressed();
			finish();
			break;
		default:
			break;
		}
	}
}
