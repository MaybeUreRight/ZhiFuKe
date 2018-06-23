package com.weilay.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.utils.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.pos.app.Client;
import com.weilay.pos.entity.SendTicketInfo;
import com.weilay.pos.fragment.FriendCouponFragment;
import com.weilay.pos.fragment.NormalCouponFragment;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SendTicketListActivity2 extends TitleActivity implements
		OnClickListener {

	private final int TICKET_SUC = 1000;
	private final int TICKET_ERR = 1001;
	private Client client;
	private String couponList_url = "API/getCouponList";
	private TextView normel_tv, firend_tv;
	private ArrayList<SendTicketInfo> list_sti;

	private NormalCouponFragment ncf;
	private FriendCouponFragment fcf;
	private FragmentManager fragmentManager;
	private View normalLine, friendLine;

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
		setContentLayout(R.layout.sendticket_layout2);
		client = new Client(SendTicketListActivity2.this);
		setTitle("发券");
		Send_couponlist();
		init();
		
	}

	private void init() {
		fragmentManager = getSupportFragmentManager();
		normel_tv = (TextView) findViewById(R.id.normal_coupon);
		firend_tv = (TextView) findViewById(R.id.friend_coupon);
		normalLine = findViewById(R.id.normal_view);
		friendLine = findViewById(R.id.friend_view);
		normel_tv.setOnClickListener(this);
		firend_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.normal_coupon:
			setTab(0);
			break;
		case R.id.friend_coupon:
			setTab(1);
			break;
		default:
			break;
		}
	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	private void setTab(int index) {
		if(isDestroyed()){
			return;
		}
		initialise();// 初始化字体颜色;
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragment(transaction);// 隐藏fragment,防止多个fragment显示在界面上.
		switch (index) {
		case 0:
			normel_tv.setTextColor(Color.GREEN);
			normalLine.setVisibility(View.VISIBLE);
			if (ncf == null) {
				ncf = new NormalCouponFragment();
				Bundle bundle = new Bundle();
				// bundle.putSerializable(, list_sti);
				bundle.putParcelableArrayList("normalcoupon", list_sti);
				ncf.setArguments(bundle);
				transaction.add(R.id.coupon_fragment, ncf);
			} else {
				transaction.show(ncf);
			}
			break;

		case 1:
			firend_tv.setTextColor(Color.GREEN);
			friendLine.setVisibility(View.VISIBLE);
			if (fcf == null) {
				fcf = new FriendCouponFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("friendcoupon", list_sti);
				fcf.setArguments(bundle);
				transaction.add(R.id.coupon_fragment, fcf);
			} else {
				transaction.show(fcf);
			}
			break;
		}
		transaction.addToBackStack(null);
			transaction.commitAllowingStateLoss();
		
	}

	private void reg() {
		setTab(0);
	}

	@SuppressLint("ResourceAsColor")
	private void initialise() {
		normel_tv.setTextColor(Color.WHITE);
		firend_tv.setTextColor(Color.WHITE);
		normalLine.setVisibility(View.GONE);
		friendLine.setVisibility(View.GONE);
	}

	private void hideFragment(FragmentTransaction transaction) {
		if (ncf != null) {
			transaction.hide(ncf);
		}
		if (fcf != null) {
			transaction.hide(fcf);
		}
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
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "res_info:" + res_info);
					JSONObject jo;
					try {
						jo = new JSONObject(res_info);
						if ("0".equals(jo.getString("code"))) {
							list_sti = new Gson().fromJson(
									jo.optString("data"),
									new TypeToken<List<SendTicketInfo>>() {
									}.getType());
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
			Toast.makeText(SendTicketListActivity2.this, "网络异常!", 1).show();
		}
	}

	private void sendMessage(int resId, Object obj) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = obj;
		handler.sendMessage(message);
	}

}
