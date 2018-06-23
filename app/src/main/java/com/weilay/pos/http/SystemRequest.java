package com.weilay.pos.http;
import com.framework.utils.StringUtil;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.listener.StringResponseListener;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;

import okhttp3.FormBody;

public class SystemRequest {
	/*****
	 * @detail 发送机器的活跃度
	 */
	public static void sendMachineActivitys(long activeTimes) {
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("totalseconds", "" + (activeTimes / 1000));
		HttpUtils.sendPost(builder, UrlDefine.URL_UPDATE_MACHINE_ACTIVE, null);
	}
	
	

	/*****
	 * @date 2016/11/18
	 * 
	 * @author rxwu
	 * 
	 * @detail 为机器注册绑定一个sn码，
	 * -此方法在应用版本大于29的时候才启用，少于29的应用版本还是应用原先的sn码地址（作兼容）
	 * -先检查本地的是否文件有保存这个sn码，如果没有的话，那么向服务请求生成一个新的sn码地址，然后保存到文件中，再次启动直接读取文件中的sn码
	 * -如果请求失败应用应该多次尝试检查应用是否激活
	 * -保存到sdcard中的sn码，刷机也不会消失，除非格式化机器，后期再讨论新的解决方案，现在解决sn码唯一的问题
	 * 
	 */
	public static void signSnID(final BaseActivity context){
		//如果本地没有保存sn_key
		if(StringUtil.isBank(WeiLayApplication.app.mCache.getAsString(PosDefine.SN_KEY))){
			//如果版本大于29的版本，采取从服务获取sn码的方案
			if(GetUtil.getversionCode(WeiLayApplication.app)>=40){
				if(context!=null)context.showLoading("正在注册失败，请稍候");
				FormBody.Builder builder = BaseParam.getParams();
				HttpUtils.sendStrPost(builder, UrlDefine.URL_GET_SN, new StringResponseListener() {
					
					@Override
					public void onSuccess(String str) {
						// TODO 保存sn码
						if(context!=null)context.stopLoading();
						WeiLayApplication.app.mCache.put(PosDefine.SN_KEY,str);
					}
					
					@Override
					public void onFailed(int code, String msg) {
						// TODO Auto-generated method stub
						if(context!=null)context.stopLoading();
						T.showCenter("激活设备失败("+msg+")");
					}
				});
			}else{
				//其他版本则直接获取当前的sn码保存
				WeiLayApplication.app.mCache.put(PosDefine.SN_KEY,GetUtil.getimei());
			}
			
		}
	}

}
