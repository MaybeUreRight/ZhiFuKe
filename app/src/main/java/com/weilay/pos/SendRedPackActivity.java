package com.weilay.pos;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.framework.utils.L;
import com.google.zxing.WriterException;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.entity.SendRedPackage;
import com.weilay.pos.print.ComPort;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.EncodingHandler;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SendRedPackActivity extends TitleActivity {
	private TextView zhaoling_danhao, zhaoling_jine, zhaoling_print;
	private ImageView QR_iv;
	private Bundle pack_bundle;
	private final int WEIXIN_SENDREDPACK = 2000;
	private final int QUERYPACKAGE_SUCCEED = 3000;
	private final int QUERYPACKAGE_ERR = 3001;
	private final int WEIXIN_SENDREDPACK_ERR = 2001;

	private String Pack_qrCode = "";

	private String pack_url = "API/sendRedPackage";
	private Client client;
	private ComPort comPort;
	private Timer pack_timer;
	private String query_url = "API/queryRedPackage";
	private SendRedPackage srp;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.arg1 == WEIXIN_SENDREDPACK) {
				Toast.makeText(SendRedPackActivity.this, "找零成功!", 1).show();
				SendRedPackActivity.this.finish();
			}
			if (msg.arg1 == WEIXIN_SENDREDPACK_ERR) {
				Bundle bundle = (Bundle) msg.obj;
				String str = bundle.getString("errCodeDes");
				Toast.makeText(SendRedPackActivity.this, "找零失败!原因:" + str, 1)
						.show();
			}
			if (msg.arg1 == QUERYPACKAGE_SUCCEED) {
				if (pack_timer != null) {
					pack_timer.cancel();
				}
				SendRedPackActivity.this.finish();
			}
			if (msg.arg1 == QUERYPACKAGE_ERR) {

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.send_red_pack_layout);
		client = new Client(this);
		srp = (SendRedPackage) getIntent().getSerializableExtra(
				PosDefine.INTENT_SENDREDPACK);
		setTitle("找零");
		init();
		reg();
	}

	private void init() {
		zhaoling_danhao = (TextView) findViewById(R.id.sendredpack_danhao);
		zhaoling_jine = (TextView) findViewById(R.id.sendredpack_jine);
		zhaoling_print = (TextView) findViewById(R.id.sendredpack_print);
		QR_iv = (ImageView) findViewById(R.id.pack_qr);
	}

	private void reg() {
		if (srp != null) {
			createQR(srp.getQrcode(), QR_iv);
			final String tx_no = srp.getTxno();
			zhaoling_danhao.setText(tx_no);
			zhaoling_jine.setText("找零总额:"
					+ ConvertUtil.getMoeny(srp.getAmount()) + "元");
			pack_timer = new Timer();
			pack_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					send_Query(tx_no);
				}
			}, 1000, 2000);
		}
		zhaoling_print.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (srp != null) {
					USBComPort usbComPort = new USBComPort();
					if (usbComPort.SendRedPack(srp)) {

					} else {
						T.showCenter("打印失败!找不到打印机.");
					}

				} else {
					T.showCenter("红包数据为空!");
				}
			}
		});
	}

	private void createQR(String QRurl, ImageView qr_iv) {
		// 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
		Bitmap qrcodeBitmap;
		try {
			qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 400);
			qr_iv.setImageBitmap(qrcodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void send_Query(String tx_no) {
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", tx_no);
		Call call = client.toserver(builder, query_url);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					sendMessage(QUERYPACKAGE_ERR, "");
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
							sendMessage(QUERYPACKAGE_SUCCEED, "");
						} else {
							sendMessage(QUERYPACKAGE_ERR, "");
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						sendMessage(QUERYPACKAGE_ERR, "");
					}
				}
			});
		} else {
			sendMessage(0, "");
		}

	}

	private void sendMessage(int msgId, Object o) {
		Message message = new Message();
		message.arg1 = msgId;
		message.obj = o;
		handler.sendMessage(message);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (pack_timer != null) {
			pack_timer.cancel();
		}
		super.onDestroy();
	}
}
