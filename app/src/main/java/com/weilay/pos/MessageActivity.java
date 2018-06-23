package com.weilay.pos;

import java.util.ArrayList;
import java.util.List;

import com.framework.utils.L;
import com.google.gson.Gson;
import com.weilay.dialog.GetCardDialog;
import com.weilay.pos.adapter.MessageAdapter;
import com.weilay.pos.app.MessageType;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.db.CouponDBHelper;
import com.weilay.pos.db.MessageDBHelper;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.MessageEntity;
import com.weilay.pos.listener.GetCouponListener;
import com.weilay.pos.listener.TaskReceiverListener;
import com.weilay.pos.push.GlobalPush;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MessageActivity extends TitleActivity {

	private ListView messageLv;
	private MessageAdapter messageAdapter;
	private List<MessageEntity> datas = new ArrayList<>();
	private ImageView soundIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_message);
		GlobalPush.clearNewCount();
		setTitle("消息中心");
		messageLv = (ListView) findViewById(R.id.message_lv);
		soundIv=(ImageView)findViewById(R.id.sound_setting);
		soundIv.setVisibility(View.VISIBLE);
		View emptyView = findViewById(R.id.empty_view);
		messageLv.setEmptyView(emptyView);
		messageAdapter = new MessageAdapter(this, datas);
		messageLv.setAdapter(messageAdapter);
		initDatas();
		initEvents();
	}
	String sound="true";
	/******
	 * @detail 初始化数据
	 */
	public void initDatas() {
		sound=WeiLayApplication.app.mCache.getAsString(PosDefine.CACHE_SOUND_CONFIG);
		soundIv.setImageResource("true".equals(sound)?R.drawable.icon_sound:R.drawable.icon_mute);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				datas = MessageDBHelper.findAllMessages();
				L.d("---" + new Gson().toJson(datas));
				return null;
			}

			@Override
			protected void onPostExecute(Void arg0) {
				// TODO Auto-generated method stub
				messageAdapter.notifyDataSetChange(datas);
			}
		}.execute();
	}

	boolean onitemclick = false;

	public void initEvents() {
		soundIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sound="true".equals(sound)?"false":"true";
				T.showCenter("true".equals(sound)?"已经打开消息提醒声音":"已经关闭消息提醒声音");
				WeiLayApplication.app.mCache.put(PosDefine.CACHE_SOUND_CONFIG,sound);
				soundIv.setImageResource("true".equals(sound)?R.drawable.icon_sound:R.drawable.icon_mute);
			}
		});
		messageLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (onitemclick) {
					return;
				}
				onitemclick = true;
				showLoading("正在查询卡券信息");
				try{
					final MessageEntity message = datas.get(arg2);
					switch (message.getType()) {
					case MessageType.CARD:
						// 如果是卡券的话,查询卡券的信息
						Utils.getAdverCardInfo(mContext, message.getMsgid(), new GetCouponListener() {

							@Override
							public void onData(final CouponEntity coupon) {
								// TODO Auto-generated method stub
								stopLoading();
								GetCardDialog dialog = new GetCardDialog(coupon);
								dialog.setTaskReceiverListener(new TaskReceiverListener() {

									@Override
									public void receiver() {
										// TODO Auto-generated method stub
										MessageDBHelper.deleteMessage(message);
										datas.remove(message);
										messageAdapter.notifyDataSetChange(datas);
										coupon.setId(message.getMsgid());
										// 接受任务的时候保存卡券
										saveCoupon(coupon);

									}

									@Override
									public void cancel() {
										// TODO Auto-generated method stub
										// 拒绝接收任务也要删除卡券的信息（谢总说的）
										MessageDBHelper.deleteMessage(message);
										datas.remove(message);
										messageAdapter.notifyDataSetChange(datas);

									}
								});
								dialog.show(getSupportFragmentManager(), "卡券任务详情");
								onitemclick = false;
							}

							@Override
							public void onFailed(String msg) {
								// TODO Auto-generated method stub
								onitemclick = false;
								stopLoading();
								T.showCenter("找不到对应的卡券");
							}

						});
						break;
					default:
						onitemclick = false;
						break;
					}
				}catch(Exception ex){
					
				}
			}
		});
	}

	/******
	 * @detail 保存收到的卡券信息
	 * @param coupon
	 */
	private void saveCoupon(final CouponEntity coupon) {
		if (coupon == null) {
			return;
		}
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				CouponDBHelper.saveCoupons(coupon);// 保存卡券
				return null;
			}

			protected void onPostExecute(Void result) {

			}
		}.execute();
	}

	@Override
	public void pushArraival(int messageCount) {
		// TODO Auto-generated method stub
		super.pushArraival(messageCount);
		initDatas();
	}

}
