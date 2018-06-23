package com.weilay.pos.titleactivity;

import com.framework.utils.L;
import com.google.zxing.Result;
import com.weilay.pos.MainActivity;
import com.weilay.pos.MessageActivity;
import com.weilay.pos.R;
import com.weilay.pos.StartActivity;
import com.weilay.pos.push.GlobalPush;
import com.weilay.pos.util.ActivityStackControlUtil;
import com.weilay.pos.zxing.ViewfinderView;

import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotTitleActivity extends BaseActivity {
	// private ImageView balk_iv, lock_iv;
	private RelativeLayout layoutContent;
	private LinearLayout ll_back, ll_homel;
	private RelativeLayout ll_message;
	private TextView messageCountTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		super.setContentView(R.layout.not_title);
		Main_init();
		Main_reg();
	}

	private void Main_init() {
		// balk_iv = (ImageView) findViewById(R.id.main_back);
		// lock_iv = (ImageView) findViewById(R.id.main_lock);
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_homel = (LinearLayout) findViewById(R.id.ll_home);
		ll_message = (RelativeLayout) findViewById(R.id.ll_message);
		messageCountTv = (TextView) findViewById(R.id.message_count);
		layoutContent = (RelativeLayout) findViewById(R.id.rl_main);
	}

	private void Main_reg() {
		ll_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				back();
			}
		});
		ll_homel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				home();
			}
		});
		ll_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				message();
			}
		});
	}

	public void setContentLayout(int resId) {

		if (layoutContent == null) {
			return;
		}
		layoutContent.removeAllViews();
		View v = LayoutInflater.from(this).inflate(resId, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layoutContent.addView(v);
	}

	protected String getRunningActivityName() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		return runningActivity;
	}

	@Override
	protected void onResume() {
		super.onResume();
		int count = GlobalPush.getNewCount();
		// 更新消息条数
		if (count == 0) {
			messageCountTv.setVisibility(View.GONE);
		} else {
			messageCountTv.setVisibility(View.VISIBLE);
			messageCountTv.setText(count + "");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 注：回调 2
		// Bugtags.onPause(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 注：回调 3
		// Bugtags.onDispatchTouchEvent(this, event);
		return super.dispatchTouchEvent(event);
	}

	// 处理扫描的结果
	public void handleDecode(Result result) {
		super.handleDecode(result);
	}

	//
	public void drawViewfinder() {

	}

	@Override
	public ViewfinderView getViewfinderView() {
		// TODO Auto-generated method stub
		return super.getViewfinderView();
	}

	@Override
	public void pushArraival(int messageCount) {
		// TODO Auto-generated method stu
		if (messageCountTv == null) {
			return;
		}
		L.d("--[NoTitleActivity]receiver the message count is :" + messageCount);
		if (messageCount != 0) {
			messageCountTv.setVisibility(View.VISIBLE);
			messageCountTv.setText("" + messageCount);
		} else {
			messageCountTv.setVisibility(View.GONE);
		}
	}

	protected void home() {
		L.e("gg", "当前Activity:" + getRunningActivityName());
		String ActivityName = getRunningActivityName();
		if (!ActivityName.equals("com.weilay.pos.MainActivity")) {
			ActivityStackControlUtil.closeAll();
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
		}
	}

	protected void back() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		if (ActivityStackControlUtil.size() > 1) {
			try {
				onBackPressed();
				finish();
			} catch (Exception ex) {
			}

		} else {
			Intent i = new Intent(getApplicationContext(), StartActivity.class);
			startActivity(i);
		}

	}

	/****
	 * @detail 跳转到消息页面
	 * @return voiｄ
	 */
	protected void message() {
		Intent intent = new Intent(NotTitleActivity.this, MessageActivity.class);
		startActivity(intent);
	}

}
