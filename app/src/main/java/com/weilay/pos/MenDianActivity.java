package com.weilay.pos;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.ui.DialogConfirm;
import com.framework.utils.L;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.MenDianAdapter;
import com.weilay.pos.app.Client;
import com.weilay.pos.entity.MenDiandayinEntity;
import com.weilay.pos.print.ComPort;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MenDianActivity extends TitleActivity {

	private final int MENDIAN = 7000;
	private final int MENDIAN_ERR = 7001;
	private String get_url = "Payment/Bill";
	private ListView listView_mendian;
	private MenDianAdapter adapter;
	private Dialog dayin_dialog;
	private TextView dayin_date, dayin_weixin_jine, dayin_zhifubao_jine, dayin_heji_jine, dayin_cash;
	private TextView but_dayin, but_back;
	private ComPort comPort;
	private Client client;
	private String[] date_array;
	private CountDownTimer countDownTimer;
	private MenDiandayinEntity md;
	// private ProgressDialog progressDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			stopLoading();
			if (msg.arg1 == MENDIAN) {
				dayin_dialog.show();
				dialog_init();
				dialog_reg();
			}
			if (msg.arg1 == MENDIAN_ERR) {
				// DialogUtil.dialog_if(MenDianActivity.this, 9, 5,
				// Gravity.CENTER, md.getMessage(), false);
				// DialogUtil.singleDialog(MenDianActivity.this, 9, 4,
				// Gravity.CENTER, md.getMessage(), false).show();
				DialogConfirm.ask(mContext, "提示", md.getMessage(), "确定", new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
					}
				});
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.mendian_layout);
		setTitle("门店报表");
		// comPort.setActivity(MenDianActivity.this);
		client = new Client(MenDianActivity.this);
		init();
		reg();
	}

	private void init() {
		// progressDialog = new ProgressDialog(this, R.style.loading_dialog);
		// progressDialog.setIndeterminateDrawable(MenDianActivity.this
		// .getResources().getDrawable(R.anim.loading1));
		// progressDialog.setIndeterminate(false);
		// progressDialog.setCancelable(false);
		// progressDialog.setMessage("请稍候...");
		date_array = new String[15];
		for (int i = 0; i < 15; i++) {
			String day = GetUtil.getTimeHowLong(i);
			date_array[i] = day + "  " + GetUtil.getWeek(day);
		}
		listView_mendian = (ListView) findViewById(R.id.mendian_listview);
		TextView empty_view = (TextView) findViewById(R.id.empty_view);
		listView_mendian.setEmptyView(empty_view);
		adapter = new MenDianAdapter(MenDianActivity.this, date_array);
		listView_mendian.setAdapter(adapter);
		dayin_dialog = new Dialog(MenDianActivity.this, android.R.style.Animation);
		dayin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dayin_dialog.setContentView(R.layout.mendian_dayin_layout);
	}

	private void reg() {
		listView_mendian.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				md = new MenDiandayinEntity();
				String date = date_array[position];
				md.setDate(date);
				showLoading("请稍候...");
				send_jiaoban(date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10));
			}
		});

	}

	private void dialog_init() {
		Window w = dayin_dialog.getWindow();
		dayin_date = (TextView) w.findViewById(R.id.mendian_dayin_date);
		dayin_weixin_jine = (TextView) w.findViewById(R.id.mendian_dayin_weixin_jine);
		dayin_zhifubao_jine = (TextView) w.findViewById(R.id.mendian_dayin_zhifubao_jine);
		dayin_cash = (TextView) w.findViewById(R.id.mendian_dayin_cash);
		dayin_heji_jine = (TextView) w.findViewById(R.id.mendian_dayin_heji_jine);
		but_dayin = (TextView) w.findViewById(R.id.mendian_but_dayin);
		but_back = (TextView) w.findViewById(R.id.mendian_but_cancel);
	}

	private void dialog_reg() {
		String date = md.getDate();
		dayin_date.setText("报表时间:" + date);
		dayin_weixin_jine.setText(md.getWeixin() + "元");
		dayin_zhifubao_jine.setText(md.getZhifubao() + "元");
		dayin_cash.setText(md.getCash() + "元");
		dayin_heji_jine.setText(md.getTotalAmount() + "元");

		but_dayin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				USBComPort usbComPort = new USBComPort();
				if (usbComPort.printOutMD(md)) {
					final Dialog dialog = DialogConfirm.ask(MenDianActivity.this, "打印提示", "打印成功", "确定",
							new DialogConfirmListener() {

								@Override
								public void okClick(DialogInterface dialog) {
									// TODO Auto-generated method stub
									dayin_dialog.dismiss();
									if (countDownTimer != null) {
										countDownTimer.cancel();
									}
								}
							});
					// final Dialog dialog =
					// DialogUtil.dialog_(MenDianActivity.this, 9, 5,
					// Gravity.CENTER, "打印成功");
					// Window w = dialog.getWindow();
					// w.findViewById(R.id.enter_dialog).setOnClickListener(new
					// OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// // TODO Auto-generated method stub
					// dialog.dismiss();
					// dayin_dialog.dismiss();
					// }
					// });
					// w.findViewById(R.id.dialog_cancel).setOnClickListener(new
					// OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// // TODO Auto-generated method stub
					// dialog.dismiss();
					// }
					// });
					// dialog.show();
					countDownTimer = new CountDownTimer(3000, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							dayin_dialog.dismiss();
						}
					};
					countDownTimer.start();
				} else {
					T.showCenter("打印失败!找不到打印机.");
				}

			}
		});
		but_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dayin_dialog.dismiss();
			}
		});
	}

	private void send_jiaoban(final String date) {
		FormBody.Builder builder = BaseParam.getParams();
		// mid：商户编号
		// password 操作员密码
		// operator: 操作员号

		// builder.add("sn", GetUtil.getimei(MenDianActivity.this));
		builder.add("date", date);
		Call call = client.toserver(builder, get_url);
		if (call != null) {
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					if (arg1 != null) {
						sendMessage(MENDIAN_ERR, "");
					}
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "Response:" + res_info);
					try {
						JSONObject jo = new JSONObject(res_info);

						if (jo.getString("code").equals("0")) {
							JSONObject jo_data = new JSONObject(jo.getString("data"));
							md.setTitle("门店报表");
							md.setWeixin(jo_data.getString("wechat"));

							md.setZhifubao(jo_data.getString("alipay"));
							// jbr.setBaidu(jo_data.getString("baidu"));

							sendMessage(MENDIAN, "");
						} else {
							md.setMessage(jo.getString("message"));
							sendMessage(MENDIAN_ERR, "");
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// if(comPort!=null){
		// comPort.CloseComPort();
		// }
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// if(comPort!=null){
		// comPort.CloseComPort();
		// }
	}
}
