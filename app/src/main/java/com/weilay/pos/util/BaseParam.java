package com.weilay.pos.util;

import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.OperatorEntity;

import okhttp3.FormBody;

/*******
 * @detail http请求基本参数封装
 * @author rxwu
 * @date 2016/07/08
 *
 */
public class BaseParam {
	public static FormBody.Builder getParams(){
		FormBody.Builder builder=new FormBody.Builder();
		OperatorEntity operatorEntity=Utils.getCurOperator();
		builder.add("sn",GetUtil.getimei(WeiLayApplication.app));//// ;
		builder.add("versionCode", ""+GetUtil.getversionCode(WeiLayApplication.app));
		builder.add("versionName", ""+GetUtil.getversionName(WeiLayApplication.app));
		builder.add("deviceType", "box");
		if(operatorEntity!=null){
			builder.add("mid", operatorEntity.getMid());
			builder.add("operator", operatorEntity.getOperator());
			builder.add("pwd", operatorEntity.getPassword());
		}
		return builder;
	}
	
	
	/*********
	 * @
	 * @return
	 */
	public static FormBody.Builder getBaseParams(){
		FormBody.Builder builder=new okhttp3.FormBody.Builder();
		builder.add("sn",GetUtil.getimei(WeiLayApplication.app));//;
		builder.add("versionCode", ""+GetUtil.getversionCode(WeiLayApplication.app));
		builder.add("versionName", ""+GetUtil.getversionName(WeiLayApplication.app));
		builder.add("deviceType", "box");
		return builder;
	}

}
