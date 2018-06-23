package com.weilay.dialog;

import com.framework.ui.DialogConfirm;
import com.framework.utils.StringUtil;
import com.google.gson.Gson;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.R;
import com.weilay.pos.adapter.PrintPaperAdapter;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.WifiEntity;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.KeyboardUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.WifiAdmin;
import com.weilay.pos.util.WifiUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.inputmethodservice.KeyboardView;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class WifiDialog extends Dialog implements android.view.View.OnClickListener, OnTouchListener, TextWatcher {
	private BaseActivity mContext;
	private Window win;

	
	private String[] securitys = new String[] { "没密码", "SECURITY_WEP"/*, "SECURITY_PSK", "SECURITY_EAP" */};
	private TextView wifi_connect_cancel, wifi_connect_enter;
	private EditText pwd_et, ssid_et;
	private Spinner securtity_sp;
	private PrintPaperAdapter securtityAdapter;

	private KeyboardView keyboardView;

	private WifiEntity mWifi;
	private WifiManager wifiManager;

	public WifiDialog(BaseActivity context, WifiEntity wifi) {
		super(context);
		this.mContext = context;
		this.mWifi = wifi;
	/*	if(mWifi==null){
			showTip("当前wifi不可用");
			return;
		}*/
		setContentView(R.layout.wifi_connect_layout);
		init();
		initEvent();
	}

	private void init() {
		if(mWifi!=null){
			try{
				String wifiJson=WeiLayApplication.app.mCache.getAsString(mWifi.getSsid());
				if(!StringUtil.isBank(wifiJson)){
					WifiEntity wifi=new Gson().fromJson(wifiJson,WifiEntity.class);
					mWifi=wifi;
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wifiManager=(WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point screenResolution = new Point(display.getWidth(), display.getHeight());
		win = getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = screenResolution.x * 10 / 10; // 宽度
		lp.height = (int) (screenResolution.y * 8.4 / 10); // 高度
		win.setGravity(Gravity.BOTTOM);
		win.setAttributes(lp);

		pwd_et = (EditText) findViewById(R.id.wifi_pwd);
		ssid_et = (EditText) findViewById(R.id.wifi_ssid);
		securtity_sp = (Spinner) findViewById(R.id.wifi_secutry);
		securtityAdapter = new PrintPaperAdapter(mContext, securitys);
		securtity_sp.setAdapter(securtityAdapter);
		
		wifi_connect_cancel = (TextView) findViewById(R.id.wifi_connect_cancel);
		wifi_connect_enter = (TextView) findViewById(R.id.wifi_connect_enter);
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

	}

	private void initEvent() {
		wifi_connect_cancel.setOnClickListener(this);
		wifi_connect_enter.setOnClickListener(this);
		
		pwd_et.setOnTouchListener(this);
		pwd_et.addTextChangedListener(this);
		if (mWifi== null) {
			ssid_et.setEnabled(true);
			ssid_et.setOnTouchListener(this);
			securtity_sp.setSelection(0);
		} else {
			securtity_sp.setSelection(mWifi.getSecurity());
			pwd_et.setText(mWifi.getPwd());
			ssid_et.setText(mWifi.getSsid());
			ssid_et.setEnabled(false);
		}
		securtity_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2 == 0){
					//选择第一个，不用密码
					pwd_et.setVisibility(View.GONE);
					wifi_connect_enter.setEnabled(true);
					wifi_connect_enter.setBackgroundResource(R.drawable.but_round_select_green);
				}else{
					pwd_et.setVisibility(View.VISIBLE);
					if(pwd_et.getText().toString().length()<8){
						wifi_connect_enter.setEnabled(false);
						wifi_connect_enter.setBackgroundResource(R.drawable.round_gray);
					}else{
						wifi_connect_enter.setEnabled(true);
						wifi_connect_enter.setBackgroundResource(R.drawable.but_round_select_green);
					}
				}
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
			case R.id.wifi_connect_cancel:
				dismiss();
				break;
			case R.id.wifi_connect_enter:
				String ssid = ssid_et.getText().toString();
				String pwd = pwd_et.getText().toString();
				connect(ssid,pwd);
				break;
			}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case R.id.wifi_pwd:
			new KeyboardUtil(mContext, mContext, pwd_et, keyboardView, false).showKeyboard();
			break;
		case R.id.wifi_ssid:
			new KeyboardUtil(mContext, mContext, ssid_et, keyboardView, false).showKeyboard();
			break;
		default:
			break;
		}
		return false;
	}

	private boolean wifiTextChange(final EditText editText, final TextView enter) {
		if (editText != null) {
			final String pwd = editText.getText().toString().trim();
			if (pwd.length() >= 8) {
				Log.i("gg", "Enabled");
				enter.setEnabled(true);
				enter.setBackgroundResource(R.drawable.but_round_select_green);
				return true;
			} else {
				Log.i("gg", "Enabled false");
				enter.setEnabled(false);
				enter.setBackgroundResource(R.drawable.round_gray);
			}
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		wifiTextChange(pwd_et, wifi_connect_enter);
	}

	/*****
	 * @detail 连接wifi
	 * @return void
	 * @param
	 * @detail
	 */
	private void connect(String ssid,String pwd) {
		if(mWifi==null){
			mWifi=new WifiEntity();
		}
		mWifi.setSsid(ssid);
		mWifi.setPwd(pwd);
		int position = securtity_sp.getSelectedItemPosition() < 0 ? 0 : securtity_sp.getSelectedItemPosition();
		if (StringUtil.isBank(ssid)) {
			T.showCenter("输入要连接网络的ssid");
			return;
		}
		 WifiConfiguration configuration=null;
		if (position == 0) {
			mWifi.setSecurity(WifiEntity.SECURITY_NONE);
			// 不需要密码连接
			configuration=WifiAdmin.getInstance(mContext).createWifiInfo(mWifi.getSsid(),"",1,"wt");
		}else{
			mWifi.setSecurity(WifiEntity.SECURITY_WEP);
			mWifi.setPwd(pwd);
		    configuration=WifiAdmin.getInstance(mContext).createWifiInfo(mWifi.getSsid(), mWifi.getPwd(), 3, "wt");//创建wifi链
		}
		if (configuration != null) {
			  mContext.showLoading("正在连接...");
			  WifiAdmin.getInstance(mContext).addNetwork(configuration);
			  queryConectState.removeMessages(1);
              queryConectState.sendEmptyMessageDelayed(1,2*1000);//延后两秒查询
		  }else{
			  showTip("无线连接失败");
		  }
	}
	
	
	
	
	/*******
	 * @Detail 显示提示
	 * @return void
	 * @param 
	 * @detail
	 */
	private void showTip(String msg){
		DialogConfirm.ask(mContext,"wifi连接提示",msg,"确定", new DialogConfirmListener() {
			
			@Override
			public void okClick(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				WifiDialog.this.dismiss();
			}
		});
	}
	
	
	int MAX_TIME=3;//重试次数
    /*******
     * @detail 判断查询的状态
     */
    Handler queryConectState = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WifiInfo currentWifiInfo =  WifiAdmin.getInstance(mContext).getWifiInfo();
            Log.i("gg", "wifiinfo:" + currentWifiInfo.toString());
            String state=currentWifiInfo.getSupplicantState().toString();
            switch (state){
                case "COMPLETED":
                   // NetUtilHelper.setWireLessMode(wifi);//保存成功的wifi链接
                	showTip("wifi连接成功");
                    dismiss();
                    mContext.stopLoading();//连接成功
                    WeiLayApplication.app.mCache.put("ssid",mWifi.getSsid());
                    WeiLayApplication.app.mCache.put(mWifi.getSsid(),new Gson().toJson(mWifi,WifiEntity.class));
                    break;
                case "DISCONNECTED":
                    mContext.stopLoading();
                    showTip("wif连接失败，请重试");
                 // 尝试重连wifi
            		try {
            			WifiUtils.init(mContext).reconnect(false);
            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                    dismiss();
                    break;
                default:
                    if(MAX_TIME>0){
                        removeMessages(1);
                       // T.showShort("再次查询");
                        sendEmptyMessageDelayed(1, 5000);//再次查询
                        MAX_TIME--;
                    }else{
                        MAX_TIME=3;
                        mContext.stopLoading();
                        removeMessages(1);
                        T.showShort("连接失败");
                        try {
                			WifiUtils.init(mContext).reconnect(false);
                		} catch (Exception e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                        dismiss();
                        showTip("wif连接超时，请重试");//(ERROR,"连接失败");
                        //重新打开
                      /*  WifiAdmin.getInstance(mContext).closeWifi();
                        WifiAdmin.getInstance(mContext).OpenWifi();*/
                    }

                    break;
            }
        }
    };

}
