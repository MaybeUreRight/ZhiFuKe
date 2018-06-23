package com.weilay.pos.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.utils.L;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.MessageEntity;
import com.weilay.pos.util.ServerTimeUtils;

/*******
 * @Detail 消息体信息处理辅助类
 * @author rxwu
 * @date 2016/08/04
 * 
 */
public class MessageDBHelper {
	// 消息保存的时长(一个星期)
	private static long SVAE_TIME = 7 * 24 * 60 * 60 * 1000;
	public static Map<String, String> keys = new HashMap<>();
	/*********
	 * @detail 保存收到的消息
	 *//*
	public static void saveMessages(List<MessageEntity> msgs) {
	initKeys();
		if (msgs == null) {
			return;
		}
		// 排重处理
		for (MessageEntity item : msgs) {
			if (keys.containsKey(item.getMsgid())) {
				msgs.remove(item);
			}
		}
		try {
			L.e("保存了" + (msgs == null ? 0 : msgs.size()) + "条推送的信息");
			WeiLayApplication.db.saveAll(msgs);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			L.e("保存推送内容时出错：" + e.getLocalizedMessage());
		}

	}*/
	/*********
	 * @detail 保存收到的消息
	 */
	public static int saveMessage(MessageEntity item) {
		initKeys();
		// 排重处理
		if (item == null || keys.containsKey(item.getMsgid())) {
			return 0;
		}
		try {
			WeiLayApplication.db.save(item);
			keys.put(item.getMsgid(), item.getMsgid());
			return 1;
		} catch (DbException e) {
			L.e("保存推送内容时出错：" + e.getLocalizedMessage());
		}catch(Exception ex){
			L.e("保存推送内容时出错：" + ex.getLocalizedMessage());
		}
		return 0;

	}

	/*******
	 * @detail 清除所有大于7天的记录
	 */
	public static void clearMessage() {
		try {
			WeiLayApplication.db.delete(MessageEntity.class,
					WhereBuilder.b("time", "<", ServerTimeUtils.getServerTime() - SVAE_TIME));
		} catch (DbException ex) {
			L.e("删除过期信息时出错：" + ex.getLocalizedMessage());
		}
	}
	//初始化
	public static void initKeys(){
		if(keys==null || keys.size()==0){
			keys=new HashMap<String, String>();
			try{
				List<DbModel>datas=WeiLayApplication.db.findDbModelAll(Selector.from(MessageEntity.class).select("msgid"));
				List<DbModel>coupons=WeiLayApplication.db.findDbModelAll(Selector.from(CouponEntity.class).select("id"));
				//keys.clear();
				if(datas!=null && datas.size()>0){
					for(DbModel model:datas){
						keys.put(model.getString("msg_id"),model.getString("msg_id"));
					}
				}
				if(coupons!=null && coupons.size()>0){
					for(DbModel model:coupons){
						keys.put(model.getString("id"),model.getString("id"));
					}
				}
			}catch(Exception ex){
				L.e("find the receiver message keys map happend exception,cause:"+ex.getLocalizedMessage());
			}	
		}
		
	}
	/*****
	 * @detail 查找所有的消息
	 */
	public static List<MessageEntity> findAllMessages() {
		try {
			List<MessageEntity> datas = WeiLayApplication.db
					.findAll(Selector.from(MessageEntity.class).orderBy("id", true));
			L.e("总共有" + (datas == null ? 0 : datas.size() + "条推送消息"));
			keys.clear();
			if (datas != null && !datas.isEmpty() && datas.size() > 0) {
				for (MessageEntity item : datas) {
					if (item.getMsgid() != null)
						keys.put(item.getMsgid(), item.getMsgid());
				}
			}
			return datas;
		} catch (Exception ex) {
			L.e("获取推送消息列表时错误：" + ex.getLocalizedMessage());
		}
		return null;
	}

	/****
	 * @detail 删除单条信息
	 * @param messageEntity
	 */
	public static void deleteMessage(MessageEntity messageEntity) {
		// TODO Auto-generated method stub
		if(messageEntity==null){
			return;
		}
		try {
			WeiLayApplication.db.delete(messageEntity);
			if (keys != null && keys.containsKey(messageEntity.getMsgid())) {
				keys.remove(messageEntity.getMsgid());
			}
		} catch (DbException ex) {
			L.e("删除推送记录失败：");
		}
	}

	/****
	 * @detail 删除所有的信息
	 * @param messageEntity
	 */
	public static void deleteMessage() {
		// TODO Auto-generated method stub
		try {
			WeiLayApplication.db.delete(MessageEntity.class, WhereBuilder.b("id", ">", 0));
			if (keys != null) {
				keys.clear();
			}
		} catch (DbException ex) {
			L.e("删除推送记录失败：");
		}
	}

}
