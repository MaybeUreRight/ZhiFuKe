package com.weilay.pos;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.utils.L;
import com.weilay.pos.app.Client;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.DialogUtil;
import com.weilay.pos.util.KeyboardUtil;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class IntegralSanActivity extends NotTitleActivity {

	private String vipCode;
	private TextView vip_tv, jifen_scan_enter;
	private EditText jifen_et;
	private Client client;
	private String url = "API/updateMemberBonus";
	private final int JIFEN = 9000;
	private final int JIFEN_ERR = 9001;
	private KeyboardView keyboardView;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == JIFEN) {
				DialogUtil.dialog_if(IntegralSanActivity.this, 9, 5,
						Gravity.CENTER, "积分添加成功!", true);
			}
			if (msg.arg1 == JIFEN_ERR) {
				DialogUtil.dialog_if(IntegralSanActivity.this, 9, 5,
						Gravity.CENTER, "积分添加失败!", false);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.integral_scan_layout);
		vipCode = getIntent().getStringExtra("vipCode");
		client = new Client(IntegralSanActivity.this);
		init();
		reg();
	}

	private void init() {
		vip_tv = (TextView) findViewById(R.id.jifen_vipcode);
		jifen_et = (EditText) findViewById(R.id.jifen_et);
		jifen_scan_enter = (TextView) findViewById(R.id.jifen_scan_enter);
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
		// int inputtype = jifen_et.getInputType();
		// jifen_et.setInputType(inputtype);
		vip_tv.setFocusable(true);
		vip_tv.setFocusableInTouchMode(true);
		vip_tv.requestFocus();
		vip_tv.requestFocusFromTouch();
	}

	private void reg() {
		if (vipCode != null) {
			vip_tv.setText(vipCode);
		}
		L.i("gg", "vipCode:" + vipCode);
		final String jf = jifen_et.getText().toString();
		jifen_scan_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (vipCode != null) {
					send_jfien(vipCode, jf);
				}
			}
		});
		jifen_et.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				new KeyboardUtil(IntegralSanActivity.this,
						getApplicationContext(), jifen_et, keyboardView, true)
						.showKeyboard();
				return false;

			}
		});
	}

	private void send_jfien(String kahao, String jifen) {
		FormBody.Builder builder = BaseParam.getParams();
		// builder.add("sn", GetUtil.getimei(this));
		builder.add("code", kahao);
		builder.add("bonus", jifen);
		builder.add("recordBonus", "哈哈哈,捡到积分了!");

		Call call = client.toserver(builder, url);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					L.i("gg", "err:" + arg1.toString());
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "jifen_res:" + res_info);

					try {
						JSONObject jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {
							sendMessage(JIFEN, "");
						} else {
							sendMessage(JIFEN_ERR, "");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} else {
			sendMessage(0, "");
			Toast.makeText(this, "网络异常!", 0).show();
		}
	}

	private void sendMessage(int resId, Object o) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = o;
		handler.sendMessage(message);
	}
}
