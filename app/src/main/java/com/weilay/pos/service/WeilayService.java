package com.weilay.pos.service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.framework.utils.L;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.util.BaseParam;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class WeilayService extends Service {
	private Client client;

	// private String iaonline_url = "API/iAmOnline";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		client = new Client(this);
		joinadalliance();
	}

	private void joinadalliance() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				send_joinadalliance();

			}
		}, 5000, 3600000);// 登录后每隔1小时发一次请求
	}

	private void send_joinadalliance() {
		FormBody.Builder builder = BaseParam.getParams();
		Call call = client.toserver(builder, UrlDefine.URL_IAMONLINE);
		if (call != null) {
			call.enqueue(new Callback() {


				@Override
				public void onFailure(Call arg0, IOException ioe) {
					// TODO Auto-generated method stub
					L.i("gg", "ioe-->iaonline:" + ioe.toString());
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "res_info-->iaonline:" + res_info);
				}
			});

		} else {
		}
	}

}
