package com.weilay.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.utils.L;
import com.weilay.listener.DialogAskListener;
import com.weilay.pos.adapter.VerificationAdapter;
import com.weilay.pos.app.Client;
import com.weilay.pos.entity.VerificationSheet;
import com.weilay.pos.print.ComPort;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.USBComPort;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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

public class VerificationSheetActivity extends TitleActivity {

	private final int HEXIAODANG = 5000;
	private ListView hexiao_listview;
	private VerificationAdapter heXiaoAdapter;
	private Client client;
	private String url = "API/queryCardConsumeLog";
	// private String url =
	// "http://192.168.1.201/admin.php/API/queryCardConsumeLog";
	private VerificationSheet heXiaoDang;
	private List<VerificationSheet> hxd_list;
	private Dialog dayin_dialog;
	private TextView hxd_dayin_date, hxd_dayin_sn, hxd_dayin_danhao, hxd_dayin_enter, hxd_dayin_cancel;
	private ComPort comPort;
	// private ProgressDialog progressDialog;
	private CountDownTimer countDownTimer;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			stopLoading();
			if (msg.arg1 == HEXIAODANG) {
				init();
				reg();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.verificationsheet_layout);
		setTitle("核销单");
		client = new Client(VerificationSheetActivity.this);
		showLoading("请稍候...");
		send_hexiaodan(GetUtil.getTimenow(-15), GetUtil.getTimenow(0));

	}

	private void init() {
		hexiao_listview = (ListView) findViewById(R.id.hexiao_listview);
		heXiaoAdapter = new VerificationAdapter(VerificationSheetActivity.this, hxd_list);
		hexiao_listview.setAdapter(heXiaoAdapter);
		TextView empty_view = (TextView) findViewById(R.id.empty_view);
		hexiao_listview.setEmptyView(empty_view);
		dayin_dialog = new Dialog(VerificationSheetActivity.this, android.R.style.Animation);
		dayin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dayin_dialog.setContentView(R.layout.verificationsheet_dayin_layout);
	}

	private void reg() {
		hexiao_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				heXiaoDang = hxd_list.get(position);
				StringBuffer str=new StringBuffer();
				str.append("核销日期:" + heXiaoDang.getCtime()+"\n");
				str.append("设备编码:"+heXiaoDang.getSn()+"\n");
				str.append("核销单号:"+heXiaoDang.getCode()+"\n");
				DialogAsk.ask(mContext, "核销单打印",str.toString(), "打印", "取消", new DialogAskListener() {
					
					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						USBComPort usbComPort = new USBComPort();
						usbComPort.printOutHX(heXiaoDang);
					}
					
					@Override
					public void cancelClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						
					}
				});
		/*		dayin_dialog.show();
				dialog_init();
				dialog_reg();*/
			}
		});
	}
/*
	private void dialog_init() {
		Window w = dayin_dialog.getWindow();
		hxd_dayin_date = (TextView) w.findViewById(R.id.hexiaodang_dayin_date);
		hxd_dayin_sn = (TextView) w.findViewById(R.id.hexiaodan_dayin_sn);
		hxd_dayin_danhao = (TextView) w.findViewById(R.id.hexiaodan_dayin_danhao);
		hxd_dayin_enter = (TextView) w.findViewById(R.id.hexiaodan_but_dayin);
		hxd_dayin_cancel = (TextView) w.findViewById(R.id.hexiaodan_but_cancel);
	}

	private void dialog_reg() {

		hxd_dayin_date.setText("核销日期:" + heXiaoDang.getCtime());
		hxd_dayin_sn.setText(heXiaoDang.getSn());
		hxd_dayin_danhao.setText(heXiaoDang.getCode());
		hxd_dayin_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// comPort.printoutHexiaodang(heXiaoDang)
				heXiaoDang.setTitle("核销单");
				USBComPort usbComPort = new USBComPort();

				if (usbComPort.printOutHX(heXiaoDang)) {
					final Dialog dialog = DialogConfirm.ask(VerificationSheetActivity.this, "打印提示", "打印成功", "确定",
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
					// final Dialog dialog = DialogUtil.dialog_(
					// VerificationSheetActivity.this, 9, 5,
					// Gravity.CENTER, "打印成功");
					//
					// Window w = dialog.getWindow();
					// w.findViewById(R.id.enter_dialog).setOnClickListener(
					// new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// // TODO Auto-generated method stub
					// dialog.dismiss();
					// dayin_dialog.dismiss();
					// }
					// });
					// w.findViewById(R.id.dialog_cancel).setOnClickListener(
					// new OnClickListener() {
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
		hxd_dayin_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dayin_dialog.dismiss();
			}
		});
	}*/

	private void send_hexiaodan(String starttiem, String endtime) {
		FormBody.Builder builder = BaseParam.getParams();
		// builder.add("sn", GetUtil.getimei(this));
		builder.add("starttime", starttiem);
		builder.add("endtime", endtime);
		Call call = client.toserver(builder, url);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					L.i("gg", "arg1:" + arg1.toString());
					stopLoading();
				}

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "res:" + res_info);
					try {
						JSONObject jo = new JSONObject(res_info);

						if (jo.getString("code").equals("0")) {
							JSONArray ja = jo.getJSONArray("data");
							hxd_list = new ArrayList<VerificationSheet>();
							for (int i = 0; i < ja.length(); i++) {
								heXiaoDang = new VerificationSheet();
								heXiaoDang.setRecno(ja.getJSONObject(i).getString("recno"));
								heXiaoDang.setMid(ja.getJSONObject(i).getString("mid"));
								heXiaoDang.setSn(ja.getJSONObject(i).getString("sn"));
								heXiaoDang.setCode(ja.getJSONObject(i).getString("code"));
								heXiaoDang.setCtime(ja.getJSONObject(i).getString("ctime"));
								hxd_list.add(heXiaoDang);
							}
							sendMessage(HEXIAODANG, "");
						} else {

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
