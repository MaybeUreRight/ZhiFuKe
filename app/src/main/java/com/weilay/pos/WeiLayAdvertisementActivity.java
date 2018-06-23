package com.weilay.pos;

import java.util.Timer;
import java.util.TimerTask;

import com.bumptech.glide.Glide;
import com.framework.utils.L;
import com.google.zxing.Result;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.AdvertisementQR;
import com.weilay.pos.entity.PayAdvertisementEntity;
import com.weilay.pos.listener.NetCodeEnum;
import com.weilay.pos.listener.ResponseImageListener;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class WeiLayAdvertisementActivity extends NotTitleActivity {
	private ImageView weilay_iv;
	private Timer timer;
	private final int TIME = 1111;
	private final int QRPHOTO_SUC = 2000;
	private int second = 10;
	private Bundle receivePayBundle;
	private AdvertisementQR aqr;
	private PayAdvertisementEntity pai;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.arg1 == TIME) {
				if (second <= 0) {
					// weilay_iv.setVisibility(View.GONE);
					timer.cancel();
					Intent intent = new Intent(
							WeiLayAdvertisementActivity.this,
							MainActivity.class);
					startActivity(intent);
					WeiLayAdvertisementActivity.this.finish();
				} else {
					second--;
				}
			}
			if (msg.arg1 == QRPHOTO_SUC) {
				// Bundle bundle = (Bundle) msg.obj;
				USBComPort usbComPort = new USBComPort();
				if (usbComPort.printOutPaySUC(pai)) {

				} else {
					T.showCenter("打印失败!找不到打印机.");
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.weilay_advertisement_layout);
		receivePayBundle = getIntent().getBundleExtra("receivepay");
		init();
		reg();

	}

	private void init() {
		weilay_iv = (ImageView) findViewById(R.id.advertisement_iv);
		if (pai != null) {
			String passivePicUrl = pai.getAdver_data_picUrl();
			String passiveQRcode = pai.getQrinfo_qrCode();
			String qrType = pai.getQrinfo_qrType();

			Glide.with(WeiLayAdvertisementActivity.this).load(UrlDefine.BASE_URL + passivePicUrl).into(weilay_iv);

			if (qrType.equals("G")) {
				send_qrPhoto(passiveQRcode, "passive", pai);
			} else {
				USBComPort usbComPort = new USBComPort();
				if (usbComPort.printOutPaySUC(pai)) {

				} else {
					T.showCenter("打印失败!找不到打印机.");
				}

			}
		}
		if (receivePayBundle != null) {
			aqr = new AdvertisementQR();
			aqr.setQrCode(receivePayBundle.getString("qrCode"));
			aqr.setTopMessage(receivePayBundle.getString("top"));
			aqr.setBottomMessage(receivePayBundle.getString("bottom"));
			String receivePayUrl = receivePayBundle.getString("picUrl");

			Glide.with(WeiLayAdvertisementActivity.this).load(UrlDefine.BASE_URL + receivePayUrl).into(weilay_iv);

			USBComPort usbComPort = new USBComPort();
			if (usbComPort.printOutReceivePay(aqr)) {

			} else {
				T.showCenter("打印失败!找不到打印机.");
			}

		}
	}

	private void reg() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendMessage(TIME, "");
				L.i("gg", "广告剩余时间:" + second + "秒");
			}
		}, 1000, 1000);

		weilay_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				second = 10;
			}
		});
	}

	private void send_qrPhoto(String photoUrl, final String type,
			final PayAdvertisementEntity pai) {
		HttpUtils.downloadImage(mContext, photoUrl, new ResponseImageListener() {
			
			@Override
			public void onFailed(NetCodeEnum code, String msg) {
				// TODO Auto-generated method stub
				T.showCenter("获取优惠券二维码失败（cause:"+msg+")");
			}
			
			@Override
			public void loadSuccess(Bitmap bitmap) {
				// TODO Auto-generated method stub
				if(bitmap!=null){
					Result mResult = GetUtil.ResolvePhoto(bitmap);
					if (mResult != null) {
						pai.setQrinfo_qrCode(mResult.getText());
						sendMessage(QRPHOTO_SUC, pai);
						L.i("gg", "mResult:" + mResult.getText());
					}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (timer != null) {
			timer.cancel();
		}
	}

	private void sendMessage(int resId, Object o) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = o;
		handler.sendMessage(message);
	}
}
