package com.weilay.pos.http;

import java.util.List;

import org.json.JSONObject;

import com.framework.utils.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.pos.AdverActivity;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.AdverEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.entity.QRInfoEntity;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.listener.PayListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.printData.PrintOrderData;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.widget.Toast;
import okhttp3.FormBody;

public class PaymentRequest {

	/******
	 * @detail 显示扫描的二维码
	 */
	public static void showAmount(final BaseActivity mContext, final PayTypeEntity paytype, final OnDataListener listener) {
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("paytype", paytype.getPayType().getName());
		builder.add("tx_no", paytype.getTx_no());
		builder.add("totalAmount", ConvertUtil.doubleToString(d));
		builder.add("tradetype", "jsapi");
		builder.add("goods", Utils.getCurOperator().getName() + "安全支付");
		builder.add("goodCodes", paytype.getTx_no());
		HttpUtils.sendPost(builder, UrlDefine.URL_SHOW_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				// 请求支付结果成功后，保存支付成功的信息
				listener.onData(json.optString("data"));
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				L.e("请求支付二维码失败" + msg);
				listener.onFailed("请求支付失败," + msg);
			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				super.networkError();
				listener.onFailed("网络异常");
			}
		});
	}

	/******
	 * @detail 现金支付
	 */
	public static void cashPay(final BaseActivity mContext, final PayTypeEntity paytype) {
		mContext.showLoading("请稍候...");
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", paytype.getTx_no());
		builder.add("totalamount",ConvertUtil.doubleToString(d));
		HttpUtils.sendPost(builder, UrlDefine.URL_CASH_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject jo) {
				// TODO Auto-generated method stub
				mContext.stopLoading();
				if (jo.optJSONObject("adver").optInt("code") == 0) {
					List<AdverEntity> advers = new Gson().fromJson(jo.optJSONObject("adver").optString("data"),
							new TypeToken<List<AdverEntity>>() {
							}.getType());
					paytype.setAdvers(advers);
				}
				QRInfoEntity qrinfo = new Gson().fromJson(jo.optString("qrinfo"), QRInfoEntity.class);
				paytype.setQrInfo(qrinfo);
				paytype.setTime(jo.optString("time"));
				// PaymentPush.stop();//停止推送服务【现金不需要】
				PrintOrderData.printOrderPay(paytype, null);
				AdverEntity adver = (paytype.getAdvers() == null || paytype.getAdvers().size() < 1) ? null
						: paytype.getAdvers().get(0);
				AdverActivity.start(mContext, paytype, adver);
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				mContext.stopLoading();
				T.showCenter("现金支付失败");

			}

			@Override
			public void networkError() {
				// TODO Auto-generated method stub
				mContext.stopLoading();
				super.networkError();
				Toast.makeText(mContext, "网络异常!", 1).show();
			}
		});
	}

	/*********
	 * @detail 主动扫码支付
	 */
	public static void microPay(final BaseActivity activity, final PayTypeEntity paytype, String auth_code,
			final PayListener listener) {
		FormBody.Builder builder = BaseParam.getParams();
		double d = ConvertUtil.yuanToBranch(paytype.getAraamount());
		builder.add("paytype", paytype.getPayType().getName());
		builder.add("tx_no", paytype.getTx_no2());
		builder.add("totalAmount",ConvertUtil.parseMoney(d));
		builder.add("auth_code", auth_code);
		builder.add("goods", "欢迎使用未莱科技支付系统");
		builder.add("goodCodes", "005325");
		HttpUtils.sendPost(builder, UrlDefine.URL_MICRO_PAY, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub

				if (json.optJSONObject("adver").optInt("code") == 0) {
					List<AdverEntity> advers = new Gson().fromJson(json.optJSONObject("adver").optString("data"),
							new TypeToken<List<AdverEntity>>() {
							}.getType());
					paytype.setAdvers(advers);
				}
				QRInfoEntity qrinfo = new Gson().fromJson(json.optString("qrinfo"), QRInfoEntity.class);
				paytype.setQrInfo(qrinfo);
				paytype.setTime(json.optString("time"));
				paytype.setMicro(true);// 标明是主扫的支付方式
				listener.onSuccess(paytype);
			}

			@Override
			public void onFailed(int code, String msg) {
				if (code == 1) {
					// 支付中
					listener.paying(paytype);
				} else {
					listener.onFailed(msg);
				}
			}
		});
	}
}
