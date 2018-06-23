package com.weilay.pos;

import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.GetUtil;

import android.os.Bundle;
import android.widget.TextView;

public class EquipmentInfoActivity extends TitleActivity {
	private TextView shebei_imei, shebei_type, shebei_banben;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.equipmentinfo_layout);
		setTitle("设备信息");
		init();
		reg();
	}

	private void init() {
		shebei_imei = (TextView) findViewById(R.id.info_imei);
		shebei_type = (TextView) findViewById(R.id.info_type);
		shebei_banben = (TextView) findViewById(R.id.info_banben);
	}

	private void reg() {
		shebei_imei.setText("设备编码: "
				+ GetUtil.getimei(EquipmentInfoActivity.this));
		shebei_type.setText("设备型号: ZFK2016");
		shebei_banben.setText("客户端版本: " + GetUtil.getversionName(this));
	}
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          