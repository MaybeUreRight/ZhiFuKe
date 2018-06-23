package com.weilay.pos;

import java.io.IOException;
import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.utils.InputMoneyFilter;
import com.framework.utils.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.entity.SendRedPackage;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BaseKeyBoard;
import com.weilay.pos.util.BaseKeyBoard.OPTIONS_KEY;
import com.weilay.pos.util.BaseKeyBoard.OnKeyListener;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.T;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SendRedPackInputActivity extends NotTitleActivity implements
		OnKeyListener {
	private TextView zhaoling_jine;
	private int select;// 值为0时是金额,值为1时是单号
	// private final int WEIXIN_ORDER = 1000;
	private final int PACK_SUCCESS = 2000;
	private final int PACK_ERR = 2001;
	private String appid = "";
	private String mch_id = "";
	private SendRedPackage srp;
	private Client client;
	private String pack_url = "API/sendRedPackage";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			stopLoading();
			// if (msg.arg1 == WEIXIN_ORDER) {
			// String jine = zhaoling_jine.getText().toString();
			// // Bundle bundle = (Bundle) msg.obj;
			//
			// Intent intent = new Intent(SendRedPackInputActivity.this,
			// SendRedPackActivity.class);
			// // Bundle mybundle = new Bundle();
			// //
			// // mybundle.putString("sendRedPackjine", jine);
			// // mybundle.putString("sendRedPackopenid",
			// // bundle.getString("orderOpenid"));
			// // mybundle.putString("sendRedPackouttradeno",
			// // bundle.getString("orderOutTradeNo"));
			// if (srp != null) {
			// intent.putExtra(PosDefine.INTENT_SENDREDPACK, srp);
			//
			// }
			// startActivity(intent);
			// // float f;
			// // f = Float.parseFloat(jine);
			// // final int amount = (int) (f * 100);
			// // new Thread() {
			// // public void run() {
			// // weixinsendredpack(openid, amount);
			// // };
			// // }.start();
			// }
			if (msg.arg1 == PACK_SUCCESS) {
				// Bundle bundle = (Bundle) msg.obj;
				Intent intent = new Intent(SendRedPackInputActivity.this,
						SendRedPackActivity.class);

				if (srp != null) {
					intent.putExtra(PosDefine.INTENT_SENDREDPACK, srp);

				}
				startActivity(intent);
				SendRedPackInputActivity.this.finish();
			}
			if (msg.arg1 == PACK_ERR) {
				T.showCenter("找零失败!");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.input_sendredpack_layout);
		client = new Client(this);
		init();
		reg();
	}

	private void reg() {
	}

	BaseKeyBoard keyBoard;
	Editable editor = null;

	private void init() {
		zhaoling_jine = (TextView) findViewById(R.id.pay_amount1);
		zhaoling_jine.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		editor = zhaoling_jine.getEditableText();
		editor.setFilters(new InputFilter[] { new InputMoneyFilter(100) });
		View view = (View) findViewById(R.id.keyboard_view);
		keyBoard = new BaseKeyBoard(view);
		keyBoard.setOnkeyListener(this);
	}

	@Override
	public void onNumberClick(String num) {
		// TODO Auto-generated method stub
		if (editor == null) {
			L.e("获取不到输入框");// 获取不到输入框
			return;
		}
		if (editor.length() < 8) {
			editor.append(num);
		}
	}

	String findMoneyStr;

	@Override
	public void onOptions(OPTIONS_KEY option) {
		// TODO Auto-generated method stub
		if (editor == null) {
			return;
		}
		switch (option) {
		case CLEAR:
			editor.clear();
			break;
		case DELETE:
			if (editor.length() >= 1) {
				editor.delete(editor.length() - 1, editor.length());
			}
			break;
		case ENTER:

			String str = editor.toString();
			// 最后一位是.,那么进行进行补零
			if (str.indexOf(".") == (str.length() - 1)) {
				editor.append("0");
				str = editor.toString();
			}
			double sendAmount = ConvertUtil.getMoeny(str);
			if (sendAmount < 1) {
				T.showCenter("找零金额不能少于1元");
				return;
			}
			if (sendAmount > 100) {
				T.showCenter("找零金额不能大于100元");
				return;
			}

			send_pack(sendAmount * 100);
			break;
		case DOUBLE_ZERO:
			if (editor.length() < 8 && editor.length() > 0) {
				if (editor.length() == 7) {
					editor.append("0");
				} else {
					editor.append("00");
				}
			}
			break;
		case POINT:
			if (editor.length() < 1 || editor.toString().contains(".")) {
				return;
			}
			editor.append(".");
			break;
		default:
			break;
		}
	}

	private void send_pack(double amount) {
		showLoading("请稍候...");
		FormBody.Builder builder = BaseParam.getParams();
		// builder.add("sn", GetUtil.getimei(this));
		builder.add("totalamount", ConvertUtil.doubleToString(amount));
		Call call = client.toserver(builder, pack_url);

		if (call != null) {
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					L.i("gg", "arg1:" + arg1);
					sendMessage(PACK_ERR, "");
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "res_info:" + res_info);
					try {
						JSONObject jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {
							// Type classOfT=new TypeToken<SendRedPackage>
							// srp=new Gson().fromJson(jo.getString("data"),
							// classOfT);
							Type classSRP = new TypeToken<SendRedPackage>() {
							}.getType();
							srp = new Gson().fromJson(jo.getString("data"),
									classSRP);
							// Bundle bundle = new Bundle();
							// JSONObject jo_data = new JSONObject(jo
							// .getString("data"));
							// bundle.putString("packQR",
							// jo_data.getString("qrcode"));
							// bundle.putString("packTxno",
							// jo_data.getString("txno"));
							// bundle.putString("packAmount",
							// jo_data.getString("amount"));
							// bundle.putString("packTime",
							// jo_data.getString("time"));
							sendMessage(PACK_SUCCESS, "");
						} else {
							sendMessage(PACK_ERR, "");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						sendMessage(PACK_ERR, "");
					}
				}
			});
		} else {
			sendMessage(0, "");
			Toast.makeText(this, "网络异常!", 0).show();
		}
	}

	private void sendMessage(int msgId, Object o) {
		Message message = new Message();
		message.arg1 = msgId;
		message.obj = o;
		handler.sendMessage(message);
	}
}
