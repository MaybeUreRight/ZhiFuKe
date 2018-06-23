package com.weilay.pos;

import com.bumptech.glide.Glide;
import com.framework.utils.StringUtil;
import com.google.zxing.WriterException;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.SendTicketInfo;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.EncodingHandler;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SendTicketBeginAvtivity extends TitleActivity {
	private ImageView logo_iv, ticket_qrcode_iv;
	private TextView merchantName_tv, cardInfo_tv, date_tv, finish_tv,
			sendticket_stock;
	private SendTicketInfo sti;
	private Dialog ticketBegin_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.sendticketbegin_layout);
		setTitle("发券");
		sti = (SendTicketInfo) getIntent().getParcelableExtra("sti");
		if(sti==null || StringUtil.isBank(sti.getUrl2qrcode())){
			T.showCenter("获取不到卡券信息");
			finish();
			return;
		}
		init();
		initDatas();
		reg();
	}

	private void init() {
		logo_iv = (ImageView) findViewById(R.id.merchantlogo_iv);
		merchantName_tv = (TextView) findViewById(R.id.merchantname_tv);
		cardInfo_tv = (TextView) findViewById(R.id.cardinfo_tv);
		date_tv = (TextView) findViewById(R.id.sendticket_deadline);
		ticket_qrcode_iv = (ImageView) findViewById(R.id.ticket_qrcode);
		finish_tv = (TextView) findViewById(R.id.sendticket_finish);
		finish_tv.setText("发券");
		sendticket_stock = (TextView) findViewById(R.id.sendticket_stock);
		ticketBegin_dialog = new Dialog(SendTicketBeginAvtivity.this,
				android.R.style.Animation);
		ticketBegin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ticketBegin_dialog.setContentView(R.layout.sendticket_cardqr_layout);
	}
	
	
	private void initDatas(){
		createQR(sti.getUrl2qrcode(),ticket_qrcode_iv);
		finish_tv.setText("发券");
		finish_tv.setEnabled(true);
	}

	private void reg() {

		Glide.with(SendTicketBeginAvtivity.this).load(UrlDefine.BASE_URL + sti.getMerchantlogo()).into(logo_iv);
		merchantName_tv.setText(sti.getMerchentname());
		cardInfo_tv.setText(sti.getCardinfo());
		date_tv.setText(sti.getDeadline());
		sendticket_stock.setText(sti.getStock() + "张");
		createQR(sti.getUrl2qrcode(), ticket_qrcode_iv);
		finish_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				USBComPort usbComPort = new USBComPort();
				if (usbComPort.SendTicket(sti)) {

				} else {
					T.showCenter("打印失败!找不到打印机.");
				}

			}
		});
	}

	private void createQR(String QRurl, ImageView iv) {
		// 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
		Bitmap qrcodeBitmap;
		try {
			qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 700);
			iv.setImageBitmap(qrcodeBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
