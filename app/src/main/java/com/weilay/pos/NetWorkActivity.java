package com.weilay.pos;

import com.weilay.pos.titleactivity.TitleActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NetWorkActivity extends TitleActivity {
	private ImageView wangluozhenduan_iv, wangluopeizhi_iv,wanglouxinxi_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.network_layout);
		setTitle("网络管理");
		init();
		reg();

	}

	private void init() {
		wangluozhenduan_iv = (ImageView) findViewById(R.id.wangluozhenduan_iv);
		wangluopeizhi_iv = (ImageView) findViewById(R.id.wangluopeizhi_iv);
		wanglouxinxi_iv=(ImageView) findViewById(R.id.wangluoxinxi_iv);
	}

	private void reg() {
		wangluozhenduan_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NetWorkActivity.this,
						NetworkTestActivity.class);
				startActivity(i);
			}
		});
		wangluopeizhi_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));// 进入无线网络配置界面
				Intent i = new Intent(NetWorkActivity.this,
						NetWorkDeployActivity.class);
				startActivity(i);
			}
		});
		wanglouxinxi_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(NetWorkActivity.this,
						NetworkInfoActivity.class);
				startActivity(i);
			}
		});
	}
	
	
}
