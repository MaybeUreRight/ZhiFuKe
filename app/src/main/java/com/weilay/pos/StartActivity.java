package com.weilay.pos;

import java.util.Timer;
import java.util.TimerTask;

import com.framework.utils.L;
import com.weilay.pos.app.Config;
import com.weilay.pos.service.WeilayService;
import com.weilay.pos.titleactivity.mActivity;
import com.weilay.pos.util.GetUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StartActivity extends mActivity implements OnClickListener {
	private int SIGN = 1000;
	private ImageView start_iv, unlock_iv;
	private int[] imageStart = {Config.RES_START1, R.drawable.start2, R.drawable.start3 };
	private int imageNum = 0;
	private Timer timer;
	//启动
	public static void actionStart(Activity act){
		//进入启动页面
		Intent intent = new Intent(act,
				StartActivity.class);
		act.startActivity(intent);
		act.finish();
		act.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);	
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == SIGN) {
				if (imageNum > imageStart.length - 1) {
					imageNum = 0;
				}
				// L.i("gg", imageNum+""+imageStart.length);
				start_iv.setImageResource(imageStart[imageNum++]);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.start_layout);
		// 隐藏标题栏
		// BootUtils.closeBar(this);
		init();
		reg();
		L.i("gg", "当前版本:" + GetUtil.getversionName(this));
	}

	private void init() {
		Intent weilayService = new Intent(StartActivity.this, WeilayService.class);
		startService(weilayService);// 设备在线
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendMessage(SIGN);
			}
		}, 1000, 2000);
		start_iv = (ImageView) findViewById(R.id.start_iv);
		start_iv.setImageResource(imageStart[0]);
		unlock_iv = (ImageView) findViewById(R.id.unlock_iv);
	}

	private void reg() {
		unlock_iv.setOnClickListener(this);
		findViewById(R.id.Rl_2).setOnClickListener(this);
		start_iv.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent i = new Intent(StartActivity.this, MainActivity.class);
		startActivity(i);
	}

	private void sendMessage(int id) {
		Message msg = new Message();
		msg.what = id;
		handler.sendMessage(msg);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==155){
			//如果*号件，进入支付页面
			PayActivity.actionStart(this);
		}
		return super.onKeyDown(keyCode, event);
	}
}
