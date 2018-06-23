package com.weilay.pos;

import com.framework.ui.DialogConfirm;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.PrintPaperAdapter;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.IFlytekHelper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class PrintSettingActivity extends TitleActivity {
	public static final String SETTING_PAYSTYLE="SETTING_PAYSTYLE";
	private Spinner printPaper_spinner, payprint_spinner;
	private Switch printphoto_switch,paystyle_switch,paysound_switch,refundprint_switch;
	private TextView printsetting_save_tv;
	private PrintPaperAdapter mPrintPaperAdapter, mPayPrintAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.printsetting_layout);
		setTitle("打印设置");
		init();
		reg();
	}

	private void init() {
		String[] printpaper = { "80毫米", "58毫米" };// 纸张宽度
		String[] payprintpaper = { "1张", "2张" };// 纸张宽度
		printPaper_spinner = (Spinner) findViewById(R.id.printpaper_spinner);
		printsetting_save_tv = (TextView) findViewById(R.id.printsetting_save_tv);
		payprint_spinner = (Spinner) findViewById(R.id.spinner_payprint);
		printphoto_switch=(Switch)findViewById(R.id.switch_printphoto);
		paystyle_switch=(Switch)findViewById(R.id.switch_paystyle);
		paysound_switch=(Switch)findViewById(R.id.switch_paysound);
		refundprint_switch=(Switch)findViewById(R.id.switch_printrefund);
		mPrintPaperAdapter = new PrintPaperAdapter(this, printpaper);
		mPayPrintAdapter = new PrintPaperAdapter(this, payprintpaper);
		printPaper_spinner.setAdapter(mPrintPaperAdapter);
		payprint_spinner.setAdapter(mPayPrintAdapter);

		printPaper_spinner.setSelection(mCache.getAsInt("mPrintpaperSelect", 0));
		payprint_spinner.setSelection(mCache.getAsInt("mPayPrintSelect", 0));
		//是否打印圖片，默認打印
		printphoto_switch.setChecked(mCache.getAsInt("mPrintPhotoSelect", 1)==1?true:false);
		paystyle_switch.setChecked(mCache.getAsInt("SETTING_PAYSTYLE",0)==1?true:false);
		paysound_switch.setChecked(mCache.getAsInt("mPaySoundSetting",0)==1?true:false);//默认不开启支付语音
		refundprint_switch.setChecked(mCache.getAsInt("mRefundPrint", 1)==1?true:false);
		
	}
	/*****
	 * @detail 是否播放支付音效
	 * @return
	 */
	public static boolean isPaySound(){
		return WeiLayApplication.app.mCache.getAsInt("mPaySoundSetting",0)==1;
	}
	/*****
	 * @detail 是否打印退款存单
	 */
	public static boolean isPrintRefund(){
		return WeiLayApplication.app.mCache.getAsInt("mRefundPrint",1)==1;
	}
	private void reg() {
		printsetting_save_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mPrintpaper = ""+printPaper_spinner.getSelectedItemPosition();
				String mpayPrintSelect =""+payprint_spinner.getSelectedItemPosition();
				String mPrintPhotoSelect=printphoto_switch.isChecked()?"1":"0";
				String mPayStyleSelect=paystyle_switch.isChecked()?"1":"0";
				String mPaySoundSelect=paysound_switch.isChecked()?"1":"0";
				String mRefundPrintSelect=refundprint_switch.isChecked()?"1":"0";
				mCache.put("mPayPrintSelect", mpayPrintSelect);
				mCache.put("mPrintPhotoSelect",mPrintPhotoSelect);
				mCache.put("mPrintpaperSelect",mPrintpaper);
				mCache.put(SETTING_PAYSTYLE,mPayStyleSelect);
				mCache.put("mPaySoundSetting",mPaySoundSelect);
				mCache.put("mRefundPrint",mRefundPrintSelect);
				if("1".equals(mpayPrintSelect)){
					IFlytekHelper.speaking(mContext,"语音提醒开启");
				}
/*				ComPort comPort = WeiLayApplication.getComPort();
				comPort.setActivity(PrintSettingActivity.this);
				comPort.openMainCom();*/
				DialogConfirm.ask(PrintSettingActivity.this, "提示", "保存成功", "确定", new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {

					}
				});
			}
		});
	}

}
