package com.weilay.pos;

import com.framework.ui.DialogConfirm;
import com.framework.utils.StringUtil;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.OperatorEntity;
import com.weilay.pos.http.SystemRequest;
import com.weilay.pos.listener.LoginListener;
import com.weilay.pos.listener.OnDbClickListener;
import com.weilay.pos.push.GlobalPush;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.BarClose;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.IFlytekHelper;
import com.weilay.pos.util.KeyboardUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;
import com.weilay.pos.util.WifiUtils;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener,OnTouchListener{
	private EditText login_operator, login_pwd, login_mid;
	private String mid, operator, pwd;
	private TextView login_enter, login_network;
	private KeyboardView keyboardView;
	private TextView sn_text, version_name;
	ImageView login_logo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		//先关闭状态栏和菜单栏
		BarClose.closeBar();
		Utils.getServerTime();// 同步服务器时间
		setContentView(R.layout.login_layout);
		init();
		reg();
		initDatas();
	}


	private void init() {
		try {
			if(WeiLayApplication.ISWIFI){
				Log.i("gg", "当前连接WIFI,开始自动连接WIFI");
				WifiUtils.init(this).reconnect(true);
			}else{
				Log.i("gg", "当前连接方式为以太网,无需自动连接WIFI");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		login_operator = (EditText) findViewById(R.id.login_operator);
		login_pwd = (EditText) findViewById(R.id.login_pwd);
		login_mid = (EditText) findViewById(R.id.login_mid);
		login_enter = (TextView) findViewById(R.id.login_enter);
		login_network = (TextView) findViewById(R.id.login_network);
		version_name = (TextView) findViewById(R.id.version_name);
		sn_text = (TextView) findViewById(R.id.sn_text);
		login_logo=(ImageView)findViewById(R.id.login_logo);
		login_logo.setImageResource(Config.LOGIN_LOGO);
		if(!hasSign()){
			SystemRequest.signSnID(this);
			//如果sn码为空的，那么签名生成一个
			sn_text.postDelayed(new Runnable() {
				@Override
				public void run() {
					sn_text.setText(GetUtil.getimei(mContext));
				}
			}, 1000);
		}else{
			sn_text.setText("SN:" + GetUtil.getimei(this));
		}
		
		version_name.setText("软件版本:" + GetUtil.getversionName(this));
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
	}

	private void reg() {
		sn_text.setFocusable(true);
		sn_text.setFocusableInTouchMode(true);
		sn_text.requestFocus();
		sn_text.requestFocusFromTouch();
		login_enter.setOnClickListener(this);
		login_network.setOnClickListener(this);
		login_operator.setOnTouchListener(this);
		login_mid.setOnTouchListener(this);
		login_pwd.setOnTouchListener(this);
		login_logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IFlytekHelper.checkInstall(mContext);
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_enter:
			send_login();
			break;
		case R.id.login_network:
			Intent intent = new Intent(LoginActivity.this,
					NetWorkDeployActivity.class);
			intent.putExtra("isLogin", true);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	
	//onTouch
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		new KeyboardUtil(LoginActivity.this, getApplicationContext(),
				(EditText)arg0, keyboardView, true).showKeyboard();
		return false;
	}
	/*****
	 * @detail 初始化数据
	 */
	public void initDatas() {
		OperatorEntity opera = Utils.getCurOperator();
		if (opera != null) {
			login_operator.setText(opera.getOperator());
			login_mid.setText(opera.getMid());
		}
	}
	//判断是否已经生成sn码
	private boolean hasSign(){
		if(StringUtil.isBank(GetUtil.getimei())){
			return false;
		
		}
		return true;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.isLogin=false;
	}
	/******
	 * @Detail 登陆请求
	 * @param check
	 */
	private void send_login() {
		if(!hasSign()){
			SystemRequest.signSnID(this);//如果sn码为空的，那么签名生成一个
		}
		operator = login_operator.getText().toString();
		pwd = login_pwd.getText().toString();
		mid = login_mid.getText().toString();
		if (!StringUtil.isNotEmpty(mid, operator, pwd)) {
			DialogConfirm.ask(mContext, "登录提示", "商户代码、操作员、密码不能为空!", "确定",null);
			return;
		}
		showLoading("登陆中...");
		// 调用登陆
		Utils.login(this, mid, operator, pwd, new LoginListener() {

			@Override
			public void loginSuccess(OperatorEntity operator) {
				Utils.isLogin=true;
				// 根据代理商id订阅消息
				GlobalPush.subjectMessage(PosDefine.MQTT_SEND_CARD
						+ operator.getAgentid(),PosDefine.MQTT_PUSH_BASE+GetUtil.getimei(getApplicationContext()));
				//打开串口接收，放在登陆成功以后
				WeiLayApplication.openComPort();
				stopLoading();
				//进入"启动页面"
				StartActivity.actionStart(mContext);
			}

			@Override
			public void loginFailed(String msg,int code) {
				// TODO Auto-generated method stub
				stopLoading();
				T.showCenter("登陆出错：" + msg);
			}
		});
	}
	
	@Override
	public void pushArraival(int messageCount) {
		// TODO Auto-generated method stub
	}
}
