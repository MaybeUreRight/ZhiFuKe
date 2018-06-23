package com.weilay.pos;

import com.framework.ui.DialogConfirm;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/******
 * @Detail 设备检测
 * File Name:DetectionActivity.java
 * Package:com.weilay.pos	
 * Date: 2016年11月1日下午2:20:30
 * Author: rxwu
 * Detail:DetectionActivity
 */
public class DetectionActivity extends TitleActivity {
	private ImageView dayinjiance_iv, qrjiance_iv, kexiantiaoshi_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.detection_layout);
		setTitle("设备检测");
		init();
		reg();
	}

	private void init() {
		dayinjiance_iv = (ImageView) findViewById(R.id.dayinjiance_iv);
		qrjiance_iv = (ImageView) findViewById(R.id.qrjiance_iv);
		kexiantiaoshi_iv = (ImageView) findViewById(R.id.kexiantiaoshi_iv);
	}

	private void reg() {
		dayinjiance_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if (WeiLayApplication.isUsb()) {
				USBComPort usbComPort = new USBComPort();
				if (usbComPort.UsbTest()) {
					DialogConfirm.ask(mContext, "测试提示", "已发出打印指令!请查看打印机", "确定", new DialogConfirmListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							
						}
					});
				} else {
					T.showCenter("打印失败!找不到打印机.");
				}
				// } else {
				// WeiLayApplication.getComPort().printoutTest();
				// }

			}
		});
		qrjiance_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TestQRCodeActivity.actionStart(DetectionActivity.this);
			}
		});
		kexiantiaoshi_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DetectionActivity.this, KeXianTiaoShiActivity.class);
				startActivity(intent);
			}
		});
	}
}
