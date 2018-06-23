package com.weilay.pos;

import org.rxwu.helper.entity.CustomDisplayEntity;
import org.rxwu.helper.listener.CustomDisplayListener;

import com.framework.utils.L;
import com.rxwu.helper.CustomDisplayHelper;
import com.weilay.pos.adapter.ComPortAdapter;
import com.weilay.pos.titleactivity.TitleActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import com_serialport_api.SerialPortFinder;

public class KeXianTiaoShiActivity extends TitleActivity {
	private Spinner duankou_spinner;
	private TextView kexiantiaoshi_tv;
	private ComPortAdapter adapter;
	private SerialPortFinder mSerialPortFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.kexiantiaoshi_layout);
		setTitle("客显调试");
		init();
		reg();
	}

	private void init() {
		mSerialPortFinder = new SerialPortFinder();
		String[] entryValues = mSerialPortFinder.getAllDevicesPath();
//		String[] usb = mSerialPortFinder.scanUsbPort(this);

		duankou_spinner = (Spinner) findViewById(R.id.duankou_spinner);
		kexiantiaoshi_tv = (TextView) findViewById(R.id.kexian_begin_tv);
		adapter = new ComPortAdapter(KeXianTiaoShiActivity.this,
				entryValues);
		duankou_spinner.setAdapter(adapter);

	}

	private void reg() {
		kexiantiaoshi_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomDisplayEntity entity = new CustomDisplayEntity(
						CustomDisplayEntity.TYPE_INCOME, "12.34");
			
				CustomDisplayHelper util = new CustomDisplayHelper(
						duankou_spinner.getSelectedItem().toString(), 2400);
				util.setCustomDisplayListener(new CustomDisplayListener() {
					
					@Override
					public void showSuccess() {
						// TODO Auto-generated method stub
						L.i("gg", "showSuccess");
					}
					
					@Override
					public void showError(String arg0) {
						// TODO Auto-generated method stub
						L.i("gg", "showError");
					}
				});
				util.show(entity);

			}
		});
	}

}
