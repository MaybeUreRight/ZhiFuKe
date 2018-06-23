package com.weilay.pos.printData;

import org.rxwu.helper.entity.PrintEntity;

import com.rxwu.helper.USBProtolHelper;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.util.T;

import android.os.AsyncTask;


/*****
 * @detail 打印辅助类
 * File Name:PrintHelper.java
 * Package:com.weilay.pos.printData	
 * Date: 2016年11月1日下午2:27:07
 * Author: rxwu
 * Detail:PrintHelper
 */
public class PrintHelper {
	public synchronized static void printHelper(final PrintEntity entity){
		if(entity==null){
			T.showCenter("获取不到打印内容");
			return;
		}
		final USBProtolHelper result=WeiLayApplication.getUsbHelper();
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				if(result!=null){
					try {
						result.print(entity);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}
}
