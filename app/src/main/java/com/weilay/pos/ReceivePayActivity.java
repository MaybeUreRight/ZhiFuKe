/*package com.weilay.pos;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.framework.utils.L;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import com.weilay.pos.client.Client;
import com.weilay.pos.client.WeiLayApplication;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ComPort;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.DialogUtil;
import com.weilay.pos.util.EncodingHandler;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.T;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceivePayActivity extends NotTitleActivity implements
		android.view.SurfaceHolder.Callback {

	private final int PAY_SUCCEED = 1000;
	private final int PAY_ERR = 1001;
	private final int QUERY_SUCCEED = 3001;
	private final int PAY_PASSIVE_SUC = 2002;
	private final int PAY_PASSIVE_PAYING = 2003;
	private final int PAY_PASSIVE_ERR = 2004;
	
	 * private CaptureActivityHandler handler; private ViewfinderView
	 * viewfinderView; private boolean hasSurface; private Vector<BarcodeFormat>
	 * decodeFormats; private String characterSet; private InactivityTimer
	 * inactivityTimer; private MediaPlayer mediaPlayer; private boolean
	 * playBeep; private static final float BEEP_VOLUME = 0.10f; private static
	 * final long VIBRATE_DURATION = 200L; private boolean vibrate;
	 
	private int PAYTYPE = 0;// 支付平台:0是微信、1是支付、2是百度钱包。默认使用微信支付.
	private ImageView QR_iv, weixinPay_iv, zhifubaoPay_iv;
	private TextView payType_tv, payAmount_tv;
	private String payAmount;
	private Client client;
	private String url = "admin.php/payment/Payment";
	// private ProgressDialog progressDialog;
	private ComPort comPort;
	private Timer query_timer, micor_query_timer;
	private String query_url = "payment/queryPayResult";
	private String micro_url = "Payment/microPay";
	private String payType = "W";
	private SharedPreferences sp;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			stopLoading();
			switch (msg.arg1) {
			case PAY_SUCCEED:
				closeTimer();

				Bundle pay_bundle = (Bundle) msg.obj;
				createQR(pay_bundle.getString("data"), QR_iv);
				poll(pay_bundle.getString("tx_no"),
						pay_bundle.getString("paytype"));
				break;

			case PAY_PASSIVE_ERR:
				closeTimer();
				// close_qrScan();
				DialogUtil.dialog_if(ReceivePayActivity.this, 9, 5,
						Gravity.CENTER, "付款失败!请尝试使用扫码付款!", false);
				break;
			case PAY_ERR:
				closeTimer();
				// close_qrScan();
				T.showCenter("请求付款时失败");
				break;
			case QUERY_SUCCEED:
				closeTimer();
				// close_qrScan();

				Bundle query_bundle = (Bundle) msg.obj;
				Intent intent = new Intent(ReceivePayActivity.this,
						WeiLayAdvertisementActivity.class);
				intent.putExtra("receivepay", query_bundle);
				startActivity(intent);
				ReceivePayActivity.this.finish();
				break;
			case PAY_PASSIVE_SUC:
				closeTimer();
				// close_qrScan();
				Intent passive_intent = new Intent(ReceivePayActivity.this,
						WeiLayAdvertisementActivity.class);
				Bundle passive_bundle = (Bundle) msg.obj;
				passive_intent.putExtra("passive", passive_bundle);
				startActivity(passive_intent);
				ReceivePayActivity.this.finish();
				break;
			case PAY_PASSIVE_PAYING:
				closeTimer();
				// close_qrScan();
				final Bundle paying_bundle = (Bundle) msg.obj;
				micor_query_timer = new Timer();
				micor_query_timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						send_Query(paying_bundle.getString("txNO"),
								paying_bundle.getString("payType"));
					}
				}, 10000, 4000);
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.formgetpay_layout);
		payAmount = getIntent().getStringExtra("payamount");
		
		 * hasSurface = false; inactivityTimer = new InactivityTimer(this);
		 
		sp = WeiLayApplication.getSp_login();
		client = new Client(this);

		init();
		reg();
	}

	private void init() {
		startScan();
		closeCarmera();
		
		 * CameraManager.init(getApplication()); viewfinderView =
		 * (ViewfinderView) findViewById(R.id.viewfinder_view);
		 

		weixinPay_iv = (ImageView) findViewById(R.id.weixinpay_ib);
		zhifubaoPay_iv = (ImageView) findViewById(R.id.zhifubaopay_ib);
		QR_iv = (ImageView) findViewById(R.id.payQR_iv);
		payType_tv = (TextView) findViewById(R.id.paytype_tv);
		payAmount_tv = (TextView) findViewById(R.id.payamount_tv);

		// weixinPay_iv.setImageResource(R.drawable.weixin_select);

		// progressDialog = new ProgressDialog(this, R.style.loading_dialog);
		// progressDialog.setIndeterminateDrawable(ReceivePayActivity.this
		// .getResources().getDrawable(R.anim.loading1));
		// progressDialog.setMessage("请稍等");
		// progressDialog.setIndeterminate(false);
		// progressDialog.setCancelable(false);
		// progressDialog=ProgressDialogUtil.progressDialog(ReceivePayActivity.this,
		// "请稍候...");
	}

	private void reg() {
		if (payAmount != null) {
			payAmount_tv.setText(ConvertUtil.getMoeny(payAmount) + "元");
			// progressDialog;
			showLoading("请稍候...");

			try {
				showAmount(payAmount, payType);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		weixinPay_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				payType = "W";
				initialize();
				// weixinPay_iv.setImageResource(R.drawable.weixin_select);
				payType_tv.setText("微信支付");
				if (payAmount != null) {
					// progressDialog.show();
					showLoading("请稍候...");
					if (query_timer != null) {
						query_timer.cancel();
					}
					try {
						showAmount(payAmount, payType);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
		zhifubaoPay_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				payType = "Z";
				initialize();
				// zhifubaoPay_iv.setImageResource(R.drawable.zhifubao_select);
				payType_tv.setText("支付宝支付");
				if (payAmount != null && !payAmount.equals("")) {
					// progressDialog.show();
					showLoading("请稍候...");
					if (query_timer != null) {
						query_timer.cancel();

					}
					try {
						showAmount(payAmount, payType);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
	}

	*//**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 *//*
	public void handleDecode(Result result) {
		super.handleDecode(result);
		
		 * inactivityTimer.onActivity(); playBeepSoundAndVibrate();
		 
		String resultString = result.getText();
		L.i("gg", "扫描结果:" + resultString);
		if (resultString.equals("")) {
			Toast.makeText(ReceivePayActivity.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			if (payAmount != null) {

				double mAmount = Double.valueOf(payAmount) * 100;
				String cents = String.valueOf(mAmount);
				send_passive(cents, payType, resultString);
			}
		}

	}

	
	 * private void playBeepSoundAndVibrate() { if (playBeep && mediaPlayer !=
	 * null) { mediaPlayer.start(); } if (vibrate) { Vibrator vibrator =
	 * (Vibrator) getSystemService(VIBRATOR_SERVICE);
	 * vibrator.vibrate(VIBRATE_DURATION); } }
	 

	private void poll(final String txNo, final String payType) {
		query_timer = new Timer();
		query_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				send_Query(txNo, payType);
			}
		}, 10000, 4000);
	}

	private void initialize() {
		weixinPay_iv.setImageResource(R.drawable.weixin_notselect);
		zhifubaoPay_iv.setImageResource(R.drawable.zhifubao_notselect);
	}

	
	 * 向服务器请求支付金额 amount
	 
	private void showAmount(final String amount, final String paytype) {
		final String out_trade_no = GetUtil.getOutTradeNo();
		double d = Double.valueOf(amount) * 100;

		FormBody.Builder builder = BaseParam.getParams();
		// builder.add("mid", sp.getString("loginMid", ""));
		builder.add("paytype", paytype);
		builder.add("tx_no", out_trade_no);
		builder.add("totalAmount", String.valueOf(d));
		// builder.add("sn", GetUtil.getimei(this));
		builder.add("tradetype", "jsapi");
		// builder.add("pwd", "123");
		// builder.add("operator", "00223");
		builder.add("goods", "未莱科技安全支付");
		builder.add("goodCodes", out_trade_no);
		Call call = client.toserver(builder, url);
		if (call != null) {
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					if (arg1 != null) {
						L.i("gg", arg1.toString());
						sendMessage(PAY_ERR, "");
					}
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("gg", "res_info:" + res_info);
					JSONObject jo;
					try {
						jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {
							Bundle bundle = new Bundle();
							bundle.putString("data", jo.getString("data"));
							bundle.putString("tx_no", out_trade_no);
							bundle.putString("paytype", paytype);
							sendMessage(PAY_SUCCEED, bundle);
						} else {
							sendMessage(PAY_ERR, "");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						L.i("gg", e.getMessage());
						sendMessage(PAY_ERR, "");
					}
				}
			});
		} else {
			sendMessage(PAY_ERR, "");
			Toast.makeText(ReceivePayActivity.this, "网络异常!", 0).show();
		}
	}

	private void send_Query(final String tx_no, final String paytype) {
		FormBody.Builder builder = BaseParam.getParams();
		// builder.add("mid", sp.getString("loginMid", ""));
		builder.add("tx_no", tx_no);
		// builder.add("operator", "001");
		// builder.add("pwd", "123");
		builder.add("type", "microPay");
		// builder.add("sn", GetUtil.getimei(ReceivePayActivity.this));
		builder.add("paytype", paytype);

		Call call = client.toserver(builder, query_url);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					sendMessage(123, "");
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub

					String res_info = res.body().string();
					L.i("gg", "res_info:" + res_info);
					try {
						JSONObject jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {
							Bundle bundle = new Bundle();
							JSONObject data_jo = new JSONObject(jo
									.getString("data"));
							bundle.putString("txNo", tx_no);
							bundle.putString("amount",
									data_jo.getString("totalAmount"));
							bundle.putString("payType", paytype);

							JSONObject poll_jo = new JSONObject(jo
									.getString("adver"));
							JSONArray data_ja = poll_jo.getJSONArray("data");
							bundle.putString("picUrl", data_ja.getJSONObject(0)
									.getString("picurl"));

							JSONObject jo_qrcode = new JSONObject(jo
									.getString("qrinfo"));
							bundle.putString("qrCode",
									jo_qrcode.getString("qrcode"));
							bundle.putString("top", jo_qrcode.getString("top"));
							bundle.putString("bottom",
									jo_qrcode.getString("bottom"));
							bundle.putString("qrType",
									jo_qrcode.getString("type"));
							bundle.putString("payTime", jo.getString("time"));

							sendMessage(QUERY_SUCCEED, bundle);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} else {

			sendMessage(0, "");
			Toast.makeText(this, "网络异常!", 0).show();
		}
	}

	*//**
	 * 条码支付
	 * 
	 * @param amount
	 * @param paytype
	 * @param auth_code
	 *//*
	private void send_passive(final String amount, final String paytype,
			String auth_code) {
		FormBody.Builder builder = BaseParam.getParams();
		final String out_trade_no = GetUtil.getOutTradeNo();
		String sn = GetUtil.getimei(ReceivePayActivity.this);
		// builder.add("mid", sp.getString("loginMid", ""));
		builder.add("paytype", paytype);
		builder.add("tx_no", out_trade_no);
		builder.add("totalAmount", amount);
		builder.add("auth_code", auth_code);
		// builder.add("sn", sn);
		// builder.add("pwd", "0ee24efa4853e62d96a3b679227e3966");
		// builder.add("operator", "123");
		builder.add("goods", "欢迎使用未莱科技支付系统");
		builder.add("goodCodes", "005325");
		Call call = client.toserver(builder, micro_url);
		if (call != null) {
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					if (arg1 != null) {
						sendMessage(PAY_PASSIVE_ERR, "");
					}
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					try {
						String res_info = res.body().string();
						L.i("gg", "res_info:" + res_info);
						JSONObject jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {

							JSONObject adver_jo = new JSONObject(jo
									.getString("adver"));
							JSONArray ja = adver_jo.getJSONArray("data");
							Bundle bundle = new Bundle();
							bundle.putString("txNo", out_trade_no);
							bundle.putString("amount", amount);
							bundle.putString("payType", paytype);
							bundle.putString("picUrl", ja.getJSONObject(0)
									.getString("picurl"));
							JSONObject jo_qrcode = new JSONObject(jo
									.getString("qrinfo"));
							bundle.putString("qrCode",
									jo_qrcode.getString("qrcode"));
							bundle.putString("top", jo_qrcode.getString("top"));
							bundle.putString("bottom",
									jo_qrcode.getString("bottom"));
							bundle.putString("qrType",
									jo_qrcode.getString("type"));
							bundle.putString("payTime", jo.getString("time"));

							sendMessage(PAY_PASSIVE_SUC, bundle);
						} else if (jo.getString("code").equals("1")) {
							Bundle bundle = new Bundle();
							bundle.putString("txNO", out_trade_no);
							bundle.putString("payType", paytype);
							sendMessage(PAY_PASSIVE_PAYING, bundle);
						} else {
							sendMessage(PAY_PASSIVE_ERR, "");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						sendMessage(PAY_PASSIVE_ERR, "");
						e.printStackTrace();
					}
				}
			});
		} else {
			sendMessage(0, "");
			Toast.makeText(this, "网络异常!", 0).show();
		}
	}

	private void createQR(String QRurl, ImageView view) {

		// 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
		Bitmap qrcodeBitmap;
		try {
			qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 400);
			view.setImageBitmap(qrcodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	 * public ViewfinderView getViewfinderView() { return viewfinderView; }
	 * 
	 * public void drawViewfinder() { viewfinderView.drawViewfinder(); }
	 * 
	 * public Handler getHandler() { return handler; }
	 

	
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); SurfaceView surfaceView = (SurfaceView)
	 * findViewById(R.id.preview_view); SurfaceHolder surfaceHolder =
	 * surfaceView.getHolder(); if (hasSurface) { initCamera(surfaceHolder); }
	 * else { surfaceHolder.addCallback(this);
	 * surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); }
	 * decodeFormats = null; characterSet = null; playBeep = true; AudioManager
	 * audioService = (AudioManager) getSystemService(AUDIO_SERVICE); if
	 * (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
	 * playBeep = false; } initBeepSound(); vibrate = true; }
	 
		 * private void initBeepSound() { if (playBeep && mediaPlayer == null) {
		 * // The volume on STREAM_SYSTEM is not adjustable, and users found it
		 * // too loud, // so we now play on the music stream.
		 * setVolumeControlStream(AudioManager.STREAM_MUSIC); mediaPlayer = new
		 * MediaPlayer();
		 * mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		 * mediaPlayer.setOnCompletionListener(beepListener);
		 * 
		 * AssetFileDescriptor file = getResources().openRawResourceFd(
		 * R.raw.beep); try {
		 * mediaPlayer.setDataSource(file.getFileDescriptor(),
		 * file.getStartOffset(), file.getLength()); file.close();
		 * mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
		 * mediaPlayer.prepare(); } catch (IOException e) { mediaPlayer = null;
		 * } } }
		 

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	
	 * @Override public void surfaceCreated(SurfaceHolder holder) { // TODO
	 * Auto-generated method stub if (!hasSurface) { hasSurface = true;
	 * initCamera(holder); } }
	 * 
	 * @Override public void surfaceChanged(SurfaceHolder holder, int format,
	 * int width, int height) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void surfaceDestroyed(SurfaceHolder holder) { // TODO
	 * Auto-generated method stub hasSurface = false; }
	 

	
	 * private void initCamera(SurfaceHolder surfaceHolder) { try {
	 * CameraManager.get().openDriver(surfaceHolder); } catch (IOException ioe)
	 * { return; } catch (RuntimeException e) { return; } if (handler == null) {
	 * handler = new CaptureActivityHandler(this, decodeFormats, characterSet,
	 * "receive"); } }
	 

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		closeTimer();
		// inactivityTimer.shutdown();

		// close_qrScan();
		ReceivePayActivity.this.finish();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		closeTimer();
		// close_qrScan();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		ReceivePayActivity.this.finish();
		L.i("gg", "receiveOnRestart");
		super.onRestart();
	}

	private void sendMessage(int msgId, Object o) {
		Message message = new Message();
		message.arg1 = msgId;
		message.obj = o;
		mHandler.sendMessage(message);
	}

	
	 * private void close_qrScan() { if (handler != null) {
	 * handler.quitSynchronously(); handler = null; } if (mediaPlayer != null) {
	 * mediaPlayer.release(); } CameraManager.get().closeDriver(); }
	 
	private void closeTimer() {
		if (query_timer != null) {
			query_timer.cancel();
		}
	}

}
*/