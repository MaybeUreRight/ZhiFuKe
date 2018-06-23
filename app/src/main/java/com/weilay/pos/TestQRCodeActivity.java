package com.weilay.pos;

import com.framework.ui.DialogConfirm;
import com.google.zxing.Result;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.titleactivity.TitleActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/********
 * @Detail TestQRCode 检查摄像头功能
 * File Name:TestQRCode.java
 * Package:com.weilay.pos	
 * Date: 2016年12月8日上午9:46:26
 * Author: rxwu
 */
public class TestQRCodeActivity extends  TitleActivity{
	
	public static void actionStart(Context context){
		Intent intent=new Intent(context,TestQRCodeActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.qrscantest_layout);
		
	}
	/*****
	 * @detail 初始化控件
	 * @return void
	 * @param 
	 * @detail 开启扫描
	 */
	public void init(){
		startScan();
		getViewfinderView().setScanSize(0.7);
	}
	
	@Override
	public void handleDecode(Result result) {
		// TODO Auto-generated method stub
		super.handleDecode(result);
		DialogConfirm.ask(mContext, "提示", "扫描功能正常", "确定",
				new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						mContext.finish();
					}
				});
	}
}
