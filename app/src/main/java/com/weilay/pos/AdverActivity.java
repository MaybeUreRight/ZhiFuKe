package com.weilay.pos;

import com.bumptech.glide.Glide;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.AdverEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.ActivityStackControlUtil;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.RotateImageView;
import com.weilay.pos.util.RotateTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/******
 * @detail 支付成功页面
 * @author rxwu
 * @date create at 2016/07/20
 *
 */
public class AdverActivity extends BaseActivity {

	public static void start(BaseActivity act, PayTypeEntity payType, AdverEntity adver) {
		Intent intent = new Intent();
		intent.setClass(act, AdverActivity.class);
		intent.putExtra(PosDefine.INTENTE_PAY_INFO, payType);
		intent.putExtra(PosDefine.INTENT_ADV, adver);
		act.startActivity(intent);
		act.finish();
	}

	private int time = 5;//广告时间为5秒...2017/02/22
	private Button okBtn;
	private RotateImageView adverImg;
	private RotateTextView tipTv, payTv;
	private TextView timeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adver);
		initViews();
		initDatas();
		initEvents();
	}

	private void initViews() {
		okBtn = (Button) findViewById(R.id.sure_btn);
		adverImg = (RotateImageView) findViewById(R.id.adver_img);
		adverImg.setImageResource(Config.LOGO_RES);
		tipTv = (RotateTextView) findViewById(R.id.tip);
		payTv = (RotateTextView) findViewById(R.id.pay_moeny_tv);
		timeTv = (TextView) findViewById(R.id.time);
	}

	private void initDatas() {
		PayTypeEntity paytype = (PayTypeEntity) getIntent().getSerializableExtra(PosDefine.INTENTE_PAY_INFO);
		AdverEntity adver = (AdverEntity) getIntent().getSerializableExtra(PosDefine.INTENT_ADV);
		if (adver != null) {
			Glide.with(this).load(adver.getPicurl()).into(adverImg);// 加载广告的图片
		}
		time = 3;
		timeTv.setText("" + time);
		payTv.setText("￥" + ConvertUtil.getMoeny(paytype.getAraamount()-paytype.getFirstDiscount()) + "元");
		payTv.postDelayed(runable, 100);
	}

	/*****
	 * @Detail close thread
	 */
	Runnable runable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			--time;
			timeTv.setText("" + time);
			if (time == 0) {
				finishPage();
			} else {
				payTv.postDelayed(this, 1000);
			}
		}
	};

	private void initEvents() {
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishPage();
			}
		});
	}

	/**
	 * 跳转至支付界面..2017/02/22
	 */
	private void finishPage() {
		ActivityStackControlUtil.closeAll();
		Intent intent = new Intent();
		intent.setClass(AdverActivity.this, PayActivity.class);
		startActivity(intent);
		payTv.removeCallbacks(runable);
		finish();
	}

	@Override
	public void pushArraival(int messageCount) {
		// TODO Auto-generated method stub

	}
}
