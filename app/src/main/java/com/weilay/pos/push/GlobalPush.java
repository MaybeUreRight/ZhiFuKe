package com.weilay.pos.push;

import com.framework.utils.L;
import com.google.gson.Gson;
import com.weilay.mqtt.OnReceiverListener;
import com.weilay.mqtt.PushServices;
import com.weilay.pos.R;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.db.MessageDBHelper;
import com.weilay.pos.entity.MessageEntity;
import com.weilay.pos.util.BeepManager;
import com.weilay.pos.util.GetUtil;

import android.content.Intent;
import android.os.AsyncTask;

/********
 * @Detail 全局推送类
 * @author rxwu
 * @date 2016.08.10
 * 
 */
public class GlobalPush {
	private static PushServices pushServices;
	public static long NEW_MESSAGE_INTERVAL = 1 * 60 * 60;// 新消息提示保存1h(暂时不使用）
	private static BeepManager beepManager;

	public static int getNewCount() {
		return WeiLayApplication.app.mCache.getAsInt(PosDefine.CACHE_MESSAGE_COUNT, 0);
	}

	public static void addNewCount(int newCount) {
		int count = getNewCount() + newCount;
		if("true".equals(WeiLayApplication.app.mCache.getAsString(PosDefine.CACHE_SOUND_CONFIG))){
			L.e("新消息共有--" + count + "条");
			if (beepManager == null) {
				beepManager = new BeepManager(WeiLayApplication.app, R.raw.message);
			}
			beepManager.playBeep();
		}
		WeiLayApplication.app.mCache.put(PosDefine.CACHE_MESSAGE_COUNT, "" + count);
	}

	public static void clearNewCount() {
		WeiLayApplication.app.mCache.remove(PosDefine.CACHE_MESSAGE_COUNT);
	}

	public static void subjectMessage(final String... agent_id) {
		/*if (TextUtils.isEmpty(agent_id)) {
			return;
		}*/
		new Thread() {
			@Override
			public void run() {
				super.run();
				queryMessage(agent_id);
			}
		}.start();
	}

	/*********
	 * @detail 在这里订阅消息
	 */
	private static void queryMessage(String... topic_name) {
		pushServices = new PushServices(WeiLayApplication.app, GetUtil.getimei());
		pushServices.start();
		pushServices.subscribe(new OnReceiverListener() {

			@Override
			public void onReceiver(String topicName, int code, final String json) {
				// if (topicName == TOPIC_NAME) {
				// 解析收到的数据
				L.d("--收到推送内容：" + json);

				new AsyncTask<Void, Void, MessageEntity>() {

					@Override
					protected MessageEntity doInBackground(Void... arg0) {
						// 更新推送的消息数量
						MessageEntity msg = null;
						// 保存新的消息
						try {
							msg = new Gson().fromJson(json, MessageEntity.class);
							int newCount = MessageDBHelper.saveMessage(msg);
							addNewCount(newCount);// 如果能新增成功，那么标记为新的消息
						} catch (Exception ex) {
							L.e("解析推送内容时出错:" + ex.getLocalizedMessage());
						}
						return msg;
					}

					protected void onPostExecute(MessageEntity msg) {
						// TODO 发送全局的广告通知
						Intent intent = new Intent();
						intent.setAction(PosDefine.ACTION_RECEIVER_CARD);
						intent.putExtra(PosDefine.INTENT_MESSAGE, msg);
						WeiLayApplication.app.sendBroadcast(intent);
					};
				}.execute();
			}
		}, topic_name);
	}

	public static void stopPush() {
		try {
			if (pushServices != null) 
			{
				pushServices.stop();
			}
		} catch (Exception ex) {
		}

	}
}
