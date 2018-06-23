package com.weilay.pos;

import java.io.DataOutputStream;
import java.io.IOException;

import com.framework.ui.DialogConfirm;
import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.KeyboardUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ImportIPActivity extends TitleActivity implements OnTouchListener {

	private ImageView save_ip;
	private Activity act;
	private Context ctx;
	private KeyboardView keyboardView;
	/* 一定要用这个字符串，系统里面是这样定义的 */
	private String ETHERNET_USE_STATIC_IP = "ethernet_use_static_ip";// System.ETHERNET_USE_STATIC_IP
	/* 一定要用这个字符串，系统里面是这样定义的 */
	private String ETHERNET_ON = "ethernet_on";// Secure.ETHERNET_ON

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.import_ip_layout);
		setTitle("手动设置IP");
		Title_item_tv.setFocusable(true);
		Title_item_tv.requestFocus();
		if (getIntent().getBooleanExtra("isLogin", false)) {
			getLl_homel().setVisibility(View.GONE);
			getLl_message().setVisibility(View.INVISIBLE);
		} else {
			getLl_message().setVisibility(View.VISIBLE);
			getLl_homel().setVisibility(View.VISIBLE);
		}
		init();
		reg();
	}

	private void init() {
		act = this;
		ctx = this;
		save_ip = (ImageView) findViewById(R.id.save_ip);
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
		updateIpSettingsInfo();
	}

	private String[] mSettingNames = {
			/* 一定要用这些字符串，系统里面是这样定义的 */
			"ethernet_static_ip", // System.ETHERNET_STATIC_IP
			"ethernet_static_gateway", // System.ETHERNET_STATIC_GATEWAY
			"ethernet_static_netmask", // System.ETHERNET_STATIC_NETMASK
			"ethernet_static_dns1", // System.ETHERNET_STATIC_DNS1
			"ethernet_static_dns2" // System.ETHERNET_STATIC_DNS2
	};
	private int[] mPreferenceKeys = { R.id.ipaddress_et, R.id.gateway_et, R.id.netmask_et, R.id.dns1_et,
			R.id.dns2_et, };
	private String[] mNames = { "ip地址", "默认网关", "子网掩码", "域名解析服务器", "备用域名解析服务器" };
	private String[] mDefaults = { "192.168.1.1", "192.168.1.1", "255.255.255.0", "192.168.1.1", "192.168.1.1" };

	// 绑定事件
	private void reg() {
		save_ip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveIpSettingsInfo();
			}
		});

		for (int id : mPreferenceKeys) {
			findViewById(id).setOnTouchListener(this);
		}
	}

	private boolean isValid = false;// 是否验证通过

	/**
	 * 将 IP 设置信息保存到 android.provider.Settings 中. .! : 通过 通知回调的机制, 将驱动对应的
	 * EthernetStateTracker 实例 完成对 ethernet 网口的 具体配置.
	 * 
	 * @see EthernetStateTracker::SettingsObserver.
	 */
	private void saveIpSettingsInfo() {
		showLoading("正在保存....");
		ContentResolver contentResolver = getContentResolver();
		isValid = true;
		for (int i = 0; i < mSettingNames.length; i++) {
			String ipContent = ((EditText) findViewById(mPreferenceKeys[i])).getText().toString().trim();
			if (i < 3) {
				if((StringUtil.isBank(ipContent) || !isValidIpAddress(ipContent))){
					confirm(mNames[i] + "格式错误，并且不能为空");
					isValid = false;
					return;
				}
			} else {
				//验证dns
				if (!StringUtil.isBank(ipContent) && !isValidIpAddress(ipContent)) {
					isValid = false;
					confirm(mNames[i] + "格式有误");
					return;
				}
			}
			System.putString(contentResolver, mSettingNames[i], ipContent);
		}
		if(!isValid){
			//验证不通过
			stopLoading();
			return;
		}
		System.putInt(contentResolver, ETHERNET_USE_STATIC_IP, 1);
		boolean enable = Secure.getInt(getContentResolver(), ETHERNET_ON, 1) == 1;
		L.d("notify Secure.ETHERNET_ON changed. enable = " + enable);
		new AsyncTask<Void, Void, Boolean>() {
			protected Boolean doInBackground(Void[] params) {
				try {
					L.d("down eth0");
					execCommand("busybox ifconfig eth0 down");
					Thread.sleep(500);
					L.d("up eth0");
					execCommand("busybox ifconfig eth0 up");
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					L.d("down-up eth0 failed.");
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					L.d("down-up eth0 interrupted.");
					e.printStackTrace();
				}
				return false;
			};

			protected void onPostExecute(Boolean result) {
				stopLoading();
				if (result) {
					confirm("保存成功");
					
				} else {
					confirm("保存失败");
				}
			};
		}.execute();
	}

	// 初始化上次设置的网络信息
	private void updateIpSettingsInfo() {
		L.d("Static IP status updateIpSettingsInfo");
		ContentResolver contentResolver = getContentResolver();
		for (int i = 0; i < mSettingNames.length; i++) {
			EditText preference = (EditText) findViewById(mPreferenceKeys[i]);
			String settingValue = System.getString(contentResolver, mSettingNames[i]);
			preference.setText(StringUtil.isBank(settingValue) ? mDefaults[i] : settingValue);
		}
	}

	// 执行命令
	public void execCommand(String command) throws IOException {
		Process process = Runtime.getRuntime().exec("/system/xbin/su");// "/system/bin/sh"
		DataOutputStream os = new DataOutputStream(process.getOutputStream());
		os.write(command.getBytes());
		os.writeBytes("\n");
		os.flush();
		os.writeBytes("exit\n");
		os.flush();
	}

	// 提示信息
	private void confirm(String msg) {
		DialogConfirm.ask(mContext, "提示", msg, "确定", new DialogConfirmListener() {
			@Override
			public void okClick(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	// 验证
	public static boolean isValidIpAddress(String value) {

		int start = 0;
		int end = value.indexOf('.');
		int num = 0;

		while (start < value.length()) {

			if (-1 == end) {
				end = value.length();
			}

			try {
				int block = Integer.parseInt(value.substring(start, end));
				if ((block > 255) || (block < 0)) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}

			num++;

			start = end + 1;
			end = value.indexOf('.', start);
		}

		return num == 4;
	}

	// 绑定键盘事件
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (arg0 instanceof EditText) {
			new KeyboardUtil(act, ctx, ((EditText) arg0), keyboardView, true).showKeyboard();
		}
		return false;
	}

}
