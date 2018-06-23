package com.weilay.pos;

import com.rxwu.helper.USBProtolHelper;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.FormBody;

public class EquipmentState extends TitleActivity {

	private TextView dayinji, kexian, shexiangtou, wanglou;
	private Client client;
	private USBProtolHelper helper;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				wanglou.setText("已连接");
			}
			if (msg.what == 1) {
				wanglou.setText("未连接");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.equipmentstate_layout);
		setTitle("设备状态");
		client = new Client(EquipmentState.this);
		init();
		reg();
	}

	private void init() {
		dayinji = (TextView) findViewById(R.id.dayinji);
		kexian = (TextView) findViewById(R.id.kexianshebei);
		shexiangtou = (TextView) findViewById(R.id.shexiangtou);
		wanglou = (TextView) findViewById(R.id.wanglou);
	}

	private void reg() {
		PackageManager pm = this.getPackageManager();
		helper = WeiLayApplication.getUsbHelper();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		// String[] usbcomport = SerialPortFinder.scanUsbPort(this);
		// PrintEntity entity = new PrintEntity();
		ping();
		if (hasCamera) {
			shexiangtou.setText("正常");
		} else {
			shexiangtou.setText("脱机");
		}
		if (WeiLayApplication.getUsbHelper().isEnable()) {
			dayinji.setText("已连接");
		} else {
			dayinji.setText("断开连接");
		}
	}

	private void ping() {
		FormBody.Builder builder = BaseParam.getParams();
		Call call = client.toserver(builder, "");
		if (call != null) {
			handler.sendEmptyMessage(0);
		} else {
			handler.sendEmptyMessage(1);
		}
	}
}
