package com.weilay.pos;

import com.framework.utils.L;
import com.google.zxing.Result;
import com.weilay.pos.titleactivity.NotTitleActivity;

import android.os.Bundle;
import android.view.View;

public class ZxingScanActivity extends NotTitleActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.zxingsacn_layout);
		init();
		reg();
	}

	private void init() {
		startScan();
		getViewfinderView().setVisibility(View.VISIBLE);
	}

	private void reg() {

	}

	// boolean scanable = true;

	/**
	 * 处理扫描结果
	 */
	public void handleDecode(Result result) {
		super.handleDecode(result);
		String resultString=result.getText();
		L.i("gg","扫描结果:"+resultString);
		if(!resultString.equals("")){
			showLoading("正在处理二维码...");
		}
	}
}
