package com.weilay.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.framework.utils.L;
import com.weilay.pos.adapter.SendTicketAdapter;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.SendTicketInfo;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * 本类已弃用
 * 改成了SendTicketListActivity2
 * 
 * @author lenovo
 *
 */
public class SendTicketListActivity extends TitleActivity {

	private final int TICKET_SUC = 1000;
	private final int TICKET_ERR = 1001;
	private ListView ST_listview;
	private SendTicketAdapter st_adapter;
	private Client client;
	private String couponList_url = "API/getCouponList";
	private SharedPreferences sp_login;
	private SendTicketInfo sti;
	private List<SendTicketInfo> list_sti;
	private Map<String, Integer> map;

	private int[] selectTicket;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			stopLoading();
			switch (msg.arg1) {
			case TICKET_SUC:
				reg();
				break;

			case TICKET_ERR:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.sendticket_layout);
		client = new Client(SendTicketListActivity.this);
		sp_login = WeiLayApplication.getSp_login();

		setTitle("发券");
		Send_couponlist();
		init();
	}

	private void init() {
		ST_listview = (ListView) findViewById(R.id.sendticket_listview);
		TextView emptyView = (TextView) findViewById(R.id.empty_view);
		ST_listview.setEmptyView(emptyView);
	}

	private void reg() {
		st_adapter = new SendTicketAdapter(SendTicketListActivity.this, list_sti);
		ST_listview.setAdapter(st_adapter);
		ST_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				sti = list_sti.get(position);
				// view.setTag(position);
				// listview_init(view);
				if (sti != null) {
					Intent intent = new Intent(SendTicketListActivity.this, SendTicketBeginAvtivity.class);
					Bundle bundle = new Bundle();
					// bundle.putSerializable("sti", sti);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	private void Send_couponlist() {
		showLoading("请稍候...");
		FormBody.Builder builder = BaseParam.getParams();

		Call call = client.toserver(builder, couponList_url);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					sendMessage(TICKET_ERR, "");
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					String res_info = res.body().string();
					L.i("gg", "res_info:" + res_info);
					JSONObject jo;
					try {
						jo = new JSONObject(res_info);
						if ("0".equals(jo.getString("code"))) {
							JSONArray ja = jo.getJSONArray("data");
							list_sti = new ArrayList<SendTicketInfo>();
							for (int i = 0; i < ja.length(); i++) {
								sti = new SendTicketInfo();
								sti.setCardinfo(ja.getJSONObject(i).getString("cardinfo"));
								sti.setDeadline(ja.getJSONObject(i).getString("deadline"));
								sti.setMerchantlogo(ja.getJSONObject(i).getString("merchantlogo"));
								sti.setMerchentname(ja.getJSONObject(i).getString("merchantname"));
								sti.setStock(ja.getJSONObject(i).getString("stock"));
								sti.setUrl2qrcode(ja.getJSONObject(i).getString("url2qrcode"));
								list_sti.add(sti);
							}
							sendMessage(TICKET_SUC, "");
						} else {
							sendMessage(TICKET_ERR, "");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						sendMessage(TICKET_ERR, "");
					}
				}
			});
		} else {
			sendMessage(0, "");
			Toast.makeText(SendTicketListActivity.this, "网络异常!", 1).show();
		}
	}

	private void sendMessage(int resId, Object obj) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = obj;
		handler.sendMessage(message);
	}
}
