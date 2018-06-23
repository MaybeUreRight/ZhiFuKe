package com.weilay.pos;

import com.framework.ui.DialogConfirm;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.CmdForAndroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class WiredNetWorkActivity extends TitleActivity {
	private ImageView autoip_iv, import_iv;
	private WifiManager wifiManager;

	private SharedPreferences spf;
	private Editor editor;
	private boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.wired_network_layout);
		setTitle("有线网络");
		isLogin = getIntent().getBooleanExtra("isLogin", false);
		if (isLogin) {
			getLl_homel().setVisibility(View.INVISIBLE);
			getLl_message().setVisibility(View.INVISIBLE);
		} else {
			getLl_homel().setVisibility(View.VISIBLE);
			getLl_message().setVisibility(View.VISIBLE);
		}
		init();
		reg();
	}

	private void init() {
		spf = WeiLayApplication.getSp_ip();
		editor = spf.edit();
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		autoip_iv = (ImageView) findViewById(R.id.auto_ip);
		import_iv = (ImageView) findViewById(R.id.import_ip);

		boolean select_network = spf.getBoolean("wirednetwork", false);

		if (select_network) {
			autoip_iv.setImageResource(R.drawable.auto_ip_notselect);
			import_iv.setImageResource(R.drawable.static_ip_select);
		} else {
			autoip_iv.setImageResource(R.drawable.auto_ip_select);
			import_iv.setImageResource(R.drawable.static_ip_notselect);
		}
	}

	private void reg() {
		closeWifi();// 关闭Wifi
		CmdForAndroid.shella("su","netcfg eth0 up");// 打开以太网

		import_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putBoolean("wirednetwork", true);
				editor.commit();
				autoip_iv.setImageResource(R.drawable.auto_ip_notselect);
				import_iv.setImageResource(R.drawable.static_ip_select);
				Intent intent = new Intent(WiredNetWorkActivity.this,
						ImportIPActivity.class);
				intent.putExtra("isLogin", isLogin);
				startActivity(intent);
			}
		});
		autoip_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putBoolean("wirednetwork", false);
				editor.commit();
				autoip_iv.setImageResource(R.drawable.auto_ip_select);
				import_iv.setImageResource(R.drawable.static_ip_notselect);
				if (CmdForAndroid.shella("su","netcfg eth0 dhcp")) {// 自动设置IP
					DialogConfirm.ask(mContext, "网络提示", "自动获取IP设置成功!", "确定", new DialogConfirmListener() {
						
						@Override
						public void okClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							WiredNetWorkActivity.this.finish();
						}
					});
					
				}

			}
		});
	}

	private void closeWifi() {
		if (wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}
}
