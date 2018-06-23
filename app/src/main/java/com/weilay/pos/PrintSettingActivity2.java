/*package com.weilay.pos;

import com.framework.utils.L;
import com.weilay.pos.adapter.PrintSettingAdapter;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.ComPort;
import com.weilay.pos.util.DialogUtil;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com_serialport_api.SerialPortFinder;

public class PrintSettingActivity2 extends TitleActivity implements
		OnCheckedChangeListener {
	private TextView printsetting_save_tv;
	private SerialPortFinder mSerialPortFinder;
	private WeiLayApplication application;
	private SharedPreferences sp_port;
	private Editor editor;
	private ComPort comPort;
	private Switch switch_serialtPrint, switch_usbPrint, switch_serialtRead,
			switch_usbRead;
	private Spinner spinner_serialPrint, spinner_usbPrint, spinner_serialRead,
			spinner_usbRead, spinner_paper;
	private PrintSettingAdapter serialPrint_adapter, usbPrint_adapter,
			serialRead_adapter, usbRead_adapter, paper_adapter;

	String[] comPortPath;// 得到所有串口
//	String[] usbPortPath;// 得到所有USB
	String[] printpaperSize = { "80毫米", "50毫米" };// 纸张宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.printsetting_layout2);
		setTitle("打印设置");
		application = (WeiLayApplication) getApplication();
		sp_port = application.getSp_port();
		editor = sp_port.edit();
		init();
		reg();
	}

	private void init() {
		mSerialPortFinder = new SerialPortFinder();

		comPortPath = mSerialPortFinder.getAllDevicesPath();// 得到所有串口

		printsetting_save_tv = (TextView) findViewById(R.id.printsetting_save_tv);

		spinner_serialPrint = (Spinner) findViewById(R.id.spinner_serialPrint);
		spinner_usbPrint = (Spinner) findViewById(R.id.spinner_usbPrint);
		spinner_serialRead = (Spinner) findViewById(R.id.spinner_serialRead);
		spinner_usbRead = (Spinner) findViewById(R.id.spinner_usbRead);
		spinner_paper = (Spinner) findViewById(R.id.spinner_paper);

		switch_serialtPrint = (Switch) findViewById(R.id.switch_serialtPrint);
		switch_usbPrint = (Switch) findViewById(R.id.switch_usbPrint);
		switch_serialtRead = (Switch) findViewById(R.id.switch_serialtRead);
		switch_usbRead = (Switch) findViewById(R.id.switch_usbRead);

		switch_serialtPrint.setOnCheckedChangeListener(this);
		switch_usbPrint.setOnCheckedChangeListener(this);
		switch_serialtRead.setOnCheckedChangeListener(this);
		switch_usbRead.setOnCheckedChangeListener(this);

		serialPrint_adapter = new PrintSettingAdapter(this, comPortPath);
		usbPrint_adapter = new PrintSettingAdapter(this, new String[] { "不可用" });
		serialRead_adapter = new PrintSettingAdapter(this, comPortPath);
		usbRead_adapter = new PrintSettingAdapter(this, comPortPath);
		paper_adapter = new PrintSettingAdapter(this, printpaperSize);

		spinner_serialPrint.setAdapter(serialPrint_adapter);
		spinner_usbPrint.setAdapter(usbPrint_adapter);
		spinner_serialRead.setAdapter(serialRead_adapter);
		spinner_usbRead.setAdapter(usbRead_adapter);
		spinner_paper.setAdapter(paper_adapter);
		// 获取上次选择的项
		if (sp_port.getBoolean("mSSP", false)) {
			switch_serialtPrint.setChecked(true);
			spinner_serialPrint.setSelection(sp_port.getInt(
					"mSerialPrintPostion", 0));
		} else {
			comPortPath = new String[] { "" };
			serialPrint_adapter.notifyDataSetChanged();
		}
		if (sp_port.getBoolean("mSUP", false)) {
			switch_usbPrint.setChecked(true);
			spinner_usbPrint
					.setSelection(sp_port.getInt("mUsbPrintPostion", 0));
		} else {
			usbPrint_adapter.notifyDataSetChanged();
		}
		if (sp_port.getBoolean("mSSR", false)) {
			switch_serialtRead.setChecked(true);
			spinner_serialRead.setSelection(sp_port.getInt(
					"mSerialReadPostion", 0));
		} else {
			comPortPath = new String[] { "" };
			serialRead_adapter.notifyDataSetChanged();
		}
		if (sp_port.getBoolean("mSUR", false)) {
			switch_usbRead.setChecked(true);
			spinner_usbRead.setSelection(sp_port.getInt("mUsbReadPostion", 0));
		} else {
			usbRead_adapter.notifyDataSetChanged();
		}
	}

	private void reg() {
		printsetting_save_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 存储选中的值
				editor.putString("mSerialPrintValue", spinner_serialPrint
						.getSelectedItem().toString());
				L.i("gg", "spinner_serialPrint.getSelectedItem():"
						+ spinner_serialPrint.getSelectedItem().toString());
				editor.putString("mUsbPrintValue", spinner_usbPrint
						.getSelectedItem().toString());
				L.i("gg", " spinner_usbPrint.getSelectedItem().toString():"
						+ spinner_usbPrint.getSelectedItem().toString());
				editor.putString("mSerialReadValue", spinner_serialRead
						.getSelectedItem().toString());
				L.i("gg", " spinner_serialRead.getSelectedItem().toString():"
						+ spinner_serialRead.getSelectedItem().toString());
				editor.putString("mUsbReadValue", spinner_usbRead
						.getSelectedItem().toString());
				L.i("gg", " spinner_usbRead.getSelectedItem().toString():"
						+ spinner_usbRead.getSelectedItem().toString());
				editor.putInt("mPrintPaperValue",
						spinner_paper.getSelectedItemPosition());

				// 存储选中的项
				editor.putInt("mSerialPrintPostion",
						spinner_serialPrint.getSelectedItemPosition());
				editor.putInt("mUsbPrintPostion",
						spinner_usbPrint.getSelectedItemPosition());
				editor.putInt("mSerialReadPostion",
						spinner_serialRead.getSelectedItemPosition());
				editor.putInt("mUsbReadPostion",
						spinner_usbRead.getSelectedItemPosition());
				editor.commit();

				ComPort comPort=WeiLayApplication.getComPort();
				comPort.setActivity(PrintSettingActivity2.this);
				comPort.openMainCom();
				DialogUtil.dialog_if(PrintSettingActivity2.this, 9, 5,
						Gravity.CENTER, "保存成功", true);
			}
		});
	}

	
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 
	@Override
	public void onCheckedChanged(CompoundButton cbView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (cbView.getId()) {
		case R.id.switch_serialtPrint:
			if (isChecked) {
				switch_usbPrint.setChecked(false);
				editor.putBoolean("mSSP", true);
				editor.putBoolean("mSUP", false);
				comPortPath = mSerialPortFinder.getAllDevicesPath();// 得到所有串口
				serialPrint_adapter.notifyDataSetChanged();
				WeiLayApplication.setUsb(false);
				spinner_serialPrint.setVisibility(View.VISIBLE);
				spinner_usbPrint.setVisibility(View.GONE);
			}
			break;

		case R.id.switch_usbPrint:
			if (isChecked) {
				switch_serialtPrint.setChecked(false);
				editor.putBoolean("mSSP", false);
				editor.putBoolean("mSUP", true);
				usbPrint_adapter.notifyDataSetChanged();
				WeiLayApplication.setUsb(true);
				spinner_usbPrint.setVisibility(View.VISIBLE);
				spinner_serialPrint.setVisibility(View.GONE);
			}
			break;
		case R.id.switch_serialtRead:
			if (isChecked) {
				switch_usbRead.setChecked(false);
				editor.putBoolean("mSSR", true);
				editor.putBoolean("mSUR", false);
				comPortPath = mSerialPortFinder.getAllDevicesPath();// 得到所有串口
				serialRead_adapter.notifyDataSetChanged();
				spinner_serialRead.setVisibility(View.VISIBLE);
				spinner_usbRead.setVisibility(View.GONE);
			}
			break;
		case R.id.switch_usbRead:
			if (isChecked) {
				switch_serialtRead.setChecked(false);
				editor.putBoolean("mSSR", false);
				editor.putBoolean("mSUR", true);

				comPortPath = mSerialPortFinder.getAllDevicesPath();// 得到所有串口
				usbRead_adapter.notifyDataSetChanged();
				spinner_usbRead.setVisibility(View.VISIBLE);
				spinner_serialRead.setVisibility(View.GONE);
			}
			break;

		}
		editor.commit();
	}
}
*/