package com.weilay.pos.http;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.weilay.pos.app.PayType;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.CheckOutEntity;
import com.weilay.pos.listener.NetCodeEnum;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;

import okhttp3.FormBody;

/*****
 *
 * File Name:RefundRequest.java
 * Package:com.weilay.pos.http	
 * Date: 2016年12月12日上午10:05:08
 * Author: rxwu
 * Detail:RefundRequest 退款请求
 */
public class RefundRequest {
	/*********
	 * @return void
	 * @param 
	 * @detail 查询退款单的详情
	 */
	public static void queryRefundDetail(BaseActivity activity,String tx_no,PayType paytype,final OnDataListener listener){
		FormBody.Builder params=BaseParam.getParams();
		params.add("tx_no", tx_no);
		params.add("txno", tx_no);
		params.add("paytype", paytype.getName());
		HttpUtils.sendPost(params, UrlDefine.URL_REFUND_DETAIL, new ResponseListener() {
			@Override
			public void onSuccess(JSONObject json) {
				try{
					//查找成功
					CheckOutEntity checkout=new Gson().fromJson(json.optString("data"),CheckOutEntity.class);
					listener.onData(checkout);
				}catch(Exception ex){
					T.showCenter("获取退款订单信息出错");
					listener.onFailed("获取退款订单信息出错");
				}
			}
			
			@Override
			public void onFailed(int code, String msg) {
				if(code==1){
					listener.onFailed(1,"订单已退过款");
				}else{
					listener.onFailed(code,msg);
				}
			}
		});
	}
	
	
	/********
	 * @Detail 申请退款
	 */
	public static void refund(BaseActivity activity,CheckOutEntity checkout,final OnDataListener listener){
		FormBody.Builder builder = BaseParam.getParams();
		final String refund_no=GetUtil.getRefundOutTradeNo();
		builder.add("tx_no", checkout.getTx_no());
		builder.add("refundno", refund_no);
		builder.add("totalAmount", "" + checkout.getTotalamountFen());
		builder.add("refundAmount", "" + checkout.getRefundamount());
		builder.add("paytype", checkout.getPaytype());
		builder.add("remarks", "");
		HttpUtils.sendPost(builder, UrlDefine.URL_REFUND, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO 退款成功返回处理逻辑，这里暂时不知道返回的数据格式
				listener.onData(refund_no);
				
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				switch (code) {
				case 403:
					listener.onFailed(403,"该商户没有退款权限!请到微信官方申请退款权限!");
					break;
				default:
					listener.onFailed(code,msg);
					break;
				}
			}
			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				listener.onFailed(NetCodeEnum.NETWORK_UNABLE,"网络不可用");
			}
		});
	}
} 
