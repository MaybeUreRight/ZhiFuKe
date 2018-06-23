package com.weilay.pos;

import java.lang.reflect.Type;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.listener.DialogAskListener;
import com.weilay.pos.adapter.ShiftRecordAdapter;
import com.weilay.pos.app.Client;
import com.weilay.pos.entity.ShiftRecord;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.print.ComPort;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.FormBody;

public class ShiftRecordActivity extends TitleActivity {

	private final int JIAOBAN = 7000;
	private final int JIAOBAN_ERR = 7001;
	private final int JIAOBAN_COMMIT = 7002;
	private ListView listView_jiaoban;
	private Dialog dayin_dialog;
	private TextView dayin_date, dayin_weixin_jine, dayin_zhifubao_jine, dayin_heji_jine, dayin_cash;
	private TextView but_dayin, but_back;
	private ShiftRecordAdapter adapter;
	private ComPort comPort;
	private String get_url = "Payment/Bill";
	private Client client;
	private String[] date_array = new String[15];
	private CountDownTimer countDownTimer;
	// private List<JiaoBanRecord> list_dayin;
	private ShiftRecord sr;

	/*
	 * private Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { // TODO Auto-generated
	 * method stub super.handleMessage(msg); stopLoading(); switch (msg.arg1) {
	 * case JIAOBAN: dayin_dialog.show(); diaglog_init(); diaglog_reg(); break;
	 * 
	 * case JIAOBAN_ERR:
	 * 
	 * break; } } };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.shift_record_layout);
		setTitle("对账存单");
		client = new Client(ShiftRecordActivity.this);
		init();
		reg();
	}

	private void init() {
		for (int i = 0; i < 15; i++) {
			String day = GetUtil.getTimeHowLong(i);
//			date_array[i] = day + "  " + GetUtil.getWeek(day);
//			Log.i("gg", "day:"+day+",i:"+i);
			date_array[i] = day ;
		}

		listView_jiaoban = (ListView) findViewById(R.id.jiaobanrecord_listview);
		TextView emtpy = (TextView) findViewById(R.id.empty_view);
		listView_jiaoban.setEmptyView(emtpy);
		adapter = new ShiftRecordAdapter(ShiftRecordActivity.this, date_array);
		listView_jiaoban.setAdapter(adapter);
		dayin_dialog = new Dialog(ShiftRecordActivity.this, android.R.style.Animation);
		dayin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dayin_dialog.setContentView(R.layout.shift_record_dayin_layout);

		// dismissHandler.sendEmptyMessageDelayed(1, 15000);
	}

	private void reg() {
		listView_jiaoban.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				sr = new ShiftRecord();
				sr.settime(date_array[position]);
				String date = sr.gettime();
				showLoading("正在查询对账存单...");
				send_jiaoban(date);
			}
		});
	}

	// 获取对账存单请求 update by rxwu 修改获取对账存单的请求方式和打开弹窗的样式
	private void send_jiaoban(final String date) {
		String jiaobanDate=date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("date", jiaobanDate);
		HttpUtils.sendPost(builder, get_url, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				stopLoading();
				// TODO Auto-generated method stub
				try {
					Type type = new TypeToken<ShiftRecord>() {
					}.getType();
					sr = new Gson().fromJson(json.optString("data"), type);
					sr.settime(date);
					StringBuffer printBuffer = new StringBuffer();
					printBuffer.append("交班时间:" + date+ "\n");
					printBuffer.append("微信支付:" + ConvertUtil.parseMoney(sr.getwechat()) + "\n");
					printBuffer.append("支付宝支付:" + ConvertUtil.parseMoney(sr.getalipay()) + "\n");
					printBuffer.append("现金支付:" + ConvertUtil.parseMoney(sr.getCash()) + "\n");
					printBuffer.append("会员充值:"+ConvertUtil.parseMoney(sr.getMemberPayAmt())+"\n");
					printBuffer.append("退款:"+ConvertUtil.parseMoney(sr.getRefundAmt())+"\n");
					printBuffer.append("会员支付:"+ConvertUtil.parseMoney(sr.getMemberPayAmt())+"\n\n\n");
					printBuffer.append("总计:" + ConvertUtil.parseMoney(sr.getTotalamount()) + "\n");
					DialogAsk.ask(mContext, "打印对账存单", printBuffer.toString(), "打印", "取消", new DialogAskListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							USBComPort usbComPort = new USBComPort();
							usbComPort.printOutJBRecord(true,sr);
						}

						@Override
						public void cancelClick(DialogInterface dialog) {
							// TODO Auto-generated method stub

						}
					});
				} catch (Exception ex) {
					T.showCenter("返回格式有误");
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				stopLoading();
				T.showCenter("获取失败：" + msg);
			}
		});
		/*
		 * Call call = client.toserver(builder, get_url); if (call != null) {
		 * call.enqueue(new Callback() {
		 * 
		 * @Override public void onFailure(Call arg0, IOException arg1) { //
		 * TODO Auto-generated method stub
		 * 
		 * if (arg1 != null) { L.i("gg", "arg1:" + arg1.toString());
		 * sendMessage(JIAOBAN_ERR, ""); } }
		 * 
		 * @Override public void onResponse(Call arg0, Response res) throws
		 * IOException { // TODO Auto-generated method stub String res_info =
		 * res.body().string(); L.i("gg", "Response:" + res_info);
		 * 
		 * try { JSONObject jo = new JSONObject(res_info);
		 * 
		 * if (jo.getString("code").equals("0")) { // JSONObject jo_data = new
		 * JSONObject();
		 * 
		 * // sr.setwechat(jo_data.getString("wechat")); // L.i("gg", "wechat:"
		 * + // jo_data.getString("wechat")); //
		 * sr.setZhifubao(jo_data.getString("alipay")); //
		 * jbr.setBaidu(jo_data.getString("baidu"));
		 * 
		 * sendMessage(JIAOBAN, ""); } else {
		 * sr.setMessage(jo.getString("message")); sendMessage(JIAOBAN_ERR, "");
		 * } } catch (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } }); } else { sendMessage(0, "");
		 * Toast.makeText(this, "网络异常!", 0).show(); }
		 */

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// if (comPort != null) {
		// comPort.CloseComPort();
		// }
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// if (comPort != null) {
		// comPort.CloseComPort();
		// }
	}
	/*
	 * private void sendMessage(int msgId, Object o) { Message message = new
	 * Message(); message.arg1 = msgId; message.obj = o;
	 * handler.sendMessage(message); }
	 */
}
