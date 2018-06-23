package com.weilay.pos.push;

import org.json.JSONObject;

import com.framework.utils.L;
import com.weilay.mqtt.OnReceiverListener;
import com.weilay.mqtt.PushServices;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.listener.PayListener;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.GetUtil;

/******
 * @Detail 支付结果的推送
 * @author rxwu
 * @Date 2016/8/11
 */
public class PaymentPush {
	private static PushServices pushServices;

	/*******
	 * @detail 查询支付的结果
	 * @param order_no
	 *            支付的订单号
	 * @param listener
	 *            支付结果回调监听
	 */
	public static PushServices push(BaseActivity mContext, final PayTypeEntity paytype, final PayListener listener) {
		final String topicName = PosDefine.MQTT_PAYMENT + paytype.getPayType() + "_" + paytype.getTx_no();
		final String micro_topicName = PosDefine.MQTT_PAYMENT + paytype.getPayType() + "_" + paytype.getTx_no2();
		pushServices = new PushServices(mContext,GetUtil.getimei());
		pushServices.subscribe(new OnReceiverListener() {
			@Override
			public void onReceiver(String topic, int code, String json) {
				// TODO Auto-generated method stub
				if ((topicName).equals(topic)) {
					try {
						JSONObject dataJson = new JSONObject(json);
						if (dataJson.optInt("code") == 0) {
							if (code == 0) {
								// 支付成功
								listener.onSuccess(paytype);
								pushServices.stop();// 支付成功，关闭推送服务
							} else {
								// 支付失败
								listener.onFailed("	失败");
								pushServices.stop();
							}
						}
					} catch (Exception ex) {
						L.e("获取支付信息的时候发生异常（cause:" + ex.getLocalizedMessage() + ")");
						listener.onFailed("支付失败（cause:" + ex.getLocalizedMessage() + ")");
					}
				}
			}
		}, topicName, micro_topicName);
		return pushServices;
	}

	/*******
	 * @detail 停止推送的请求
	 */
	public static void stop() {
		if (pushServices != null) {
			pushServices.stop();
		}
	}

}
