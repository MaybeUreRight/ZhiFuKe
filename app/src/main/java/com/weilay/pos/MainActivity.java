package com.weilay.pos;

import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BarClose;
import com.weilay.pos.util.UpdateSerivices;
import com.weilay.pos.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends NotTitleActivity {
	private ImageView iv_hexiao, iv_zhifu, iv_jifen, task_iv, iv_gengduo,
			iv_sendticket;
	
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(Utils.getCurOperator()==null){
			finish();
			return;
		}
		super.setContentLayout(R.layout.activity_main);
		BarClose.closeBar();
		new UpdateSerivices(this).checkVersion();
		init();
		reg();
	}

	private void init() {
		iv_hexiao = (ImageView) findViewById(R.id.hexiao_iv);
		iv_zhifu = (ImageView) findViewById(R.id.zhifu_iv);
		iv_jifen = (ImageView) findViewById(R.id.jifen_iv);
		task_iv = (ImageView) findViewById(R.id.task_iv);
		iv_sendticket = (ImageView) findViewById(R.id.sendticket_iv);
		iv_gengduo = (ImageView) findViewById(R.id.gengduo_iv);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.isLogin=true;
	}

	private void reg() {
		iv_hexiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this,
						ChargeOffActivity.class);
				startActivity(i);
			}
		});

		iv_zhifu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, PayActivity.class);
				startActivity(i);
			}
		});
		iv_jifen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {                      
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, JoinVipActivity.class);
				startActivity(i);
			}
		});
		task_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TaskActivity.class);
				startActivity(intent);
			}
		});

		iv_gengduo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, MoreActivity.class);
				startActivity(i);
			}
		});
		iv_sendticket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SendTicketListActivity2.class);
				startActivity(intent);
			}
		});
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
