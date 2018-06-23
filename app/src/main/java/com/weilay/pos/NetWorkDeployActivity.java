package com.weilay.pos;

import com.framework.ui.DialogAsk;
import com.weilay.listener.DialogAskListener;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.listener.OnDbClickListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BarClose;
import com.weilay.pos.util.CmdForAndroid;
import com.weilay.pos.util.IFlytekHelper;
import com.weilay.pos.util.InstallUtil;
import com.weilay.pos.util.T;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NetWorkDeployActivity extends TitleActivity {
	private ImageView wired_network, wifi_network;
	private SharedPreferences spf;
	// private WeiLayApplication wla;
	private Editor editor;
	private boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.network_deploy_layout);
		setTitle("网络配置");
		// ll_homel.setVisibility(View.INVISIBLE);
		// ll_message.setVisibility(View.INVISIBLE);
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
		wired_network = (ImageView) findViewById(R.id.wired_network);
		wifi_network = (ImageView) findViewById(R.id.wifi_network);

		boolean iswifi = spf
				.getBoolean(PosDefine.ISWIFI, true);

		if (iswifi) {// ture时为wifi
			wired_network.setImageResource(R.drawable.wirednetwork_notselect);
			wifi_network.setImageResource(R.drawable.wifi_select);
		} else {
			wired_network.setImageResource(R.drawable.wirednetwork_select);
			wifi_network.setImageResource(R.drawable.wifi_notselect);

		}

	}

	private void reg() {
	Title_item_tv.setOnTouchListener(new OnDbClickListener() {
			
			@Override
			public void onDBClick(View v, MotionEvent event) {
				CmdForAndroid.shella("su", "am start -n com.android.settings/.Settings");
				//InstallUtil.installApk(mContext);
				/*T.showCenter("进入测试模式");
				CmdForAndroid.shella("su", "am start -n com.android.settings/.Settings");
				BarClose.showBar(mContext);*/
				//startActivity(new Intent(mContext,SystemTestActivity.class));
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
//				DialogAsk.ask(NetWorkDeployActivity.this, "测试模式提示", "确定进入设置？","确定" , "取消", new DialogAskListener() {
//					
//					@Override
//					public void okClick(DialogInterface dialog) {
//						// TODO Auto-generated method stub
////						CmdForAndroid.shella("su", "am start -n com.farproc.wifi.analyzer/.MainScreen");
////						BarClose.showBar(mContext);
//					}
//					
//					@Override
//					public void cancelClick(DialogInterface dialog) {
//						// TODO Auto-generated method stub
////						CmdForAndroid.shella("su", "am start -n com.android.calculator2/.Calculator");
////						BarClose.showBar(mContext);
//					}
//				});
			}
		});
		wired_network.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putBoolean(PosDefine.ISWIFI, false);
				editor.commit();

				wired_network.setImageResource(R.drawable.wirednetwork_select);
				wifi_network.setImageResource(R.drawable.wifi_notselect);

				Intent intent = new Intent(NetWorkDeployActivity.this,
						WiredNetWorkActivity.class);
				intent.putExtra("isLogin", isLogin);
				startActivity(intent);

			}
		});
		wifi_network.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putBoolean(PosDefine.ISWIFI, true);
				editor.commit();
				wired_network
						.setImageResource(R.drawable.wirednetwork_notselect);
				wifi_network.setImageResource(R.drawable.wifi_select);

				Intent intent = new Intent(NetWorkDeployActivity.this,
						WifiActivity.class);
				intent.putExtra("isLogin", isLogin);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		BarClose.closeBar();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BarClose.closeBar();
	}
}
