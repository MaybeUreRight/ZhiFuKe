package com.weilay.pos.titleactivity;

import com.google.zxing.Result;
import com.weilay.pos.R;
import com.weilay.pos.zxing.ViewfinderView;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class mActivity extends BaseActivity {

	private RelativeLayout layoutContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.not_down);
		main_init();
	}

	private void main_init() {
		layoutContent = (RelativeLayout) findViewById(R.id.rl_main);
	}

	public void setContentLayout(int resId) {

		if (layoutContent == null) {
			return;
		}
		layoutContent.removeAllViews();
		View v = LayoutInflater.from(this).inflate(resId, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		layoutContent.addView(v);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 注：回调 1
		// Bugtags.onResume(this);

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
		// TODO Auto-generated method stub

	}
}
