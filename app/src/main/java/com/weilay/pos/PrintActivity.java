package com.weilay.pos;

import com.weilay.pos.titleactivity.TitleActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PrintActivity extends TitleActivity {
	private ImageView iv_hexiaodan, iv_mendianbaobiao, iv_jiaobanjilu,
			iv_jiezhang, iv_tuikuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.print_layout);
		setTitle("打印");
		init();
		reg();
	}

	private void init() {
		iv_mendianbaobiao = (ImageView) findViewById(R.id.mendian_iv);
		iv_jiaobanjilu = (ImageView) findViewById(R.id.jiaobanjilu_iv);
		iv_hexiaodan = (ImageView) findViewById(R.id.hexiaodang_iv);
		iv_jiezhang = (ImageView) findViewById(R.id.jiezhang_iv);
		iv_tuikuan = (ImageView) findViewById(R.id.tuikuan_iv);
	}

	private void reg() {
		iv_hexiaodan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PrintActivity.this,
						VerificationSheetActivity.class);
				startActivity(i);
			}
		});
		iv_mendianbaobiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(PrintActivity.this,
						MenDianActivity.class);
				startActivity(intent);
			}
		});
		iv_jiaobanjilu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(PrintActivity.this,
						ShiftRecordActivity.class);
				startActivity(intent);
			}
		});
		iv_jiezhang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrintActivity.this,
						CheckOutActivity.class);
				startActivity(intent);
			}
		});
		iv_tuikuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrintActivity.this,
						RefundPrintActivity.class);
				startActivity(intent);
			}
		});
	}
}
