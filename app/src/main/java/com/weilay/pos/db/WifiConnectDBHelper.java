package com.weilay.pos.db;

import java.util.HashMap;
import java.util.List;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.WifiConnectEntity;

public class WifiConnectDBHelper {
	private static HashMap<String, String> keys = new HashMap<String, String>();

	public static void saveMessage(List<WifiConnectEntity> listWce) {
		if (listWce == null) {
			return;
		}
		for (WifiConnectEntity item : listWce) {
			if (keys.containsKey(item.getName())) {
				listWce.remove(item);
			}
		}
		try {
			WeiLayApplication.db.save(listWce);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<WifiConnectEntity> QueryMessage() {
		try {
			List<WifiConnectEntity> listWce = WeiLayApplication.db
					.findAll(Selector.from(WifiConnectEntity.class).orderBy(
							"name", true));
			keys.clear();
			if (listWce != null && !listWce.isEmpty() && listWce.size() > 0) {
				for (WifiConnectEntity item : listWce) {
					keys.put(item.getName(), "");
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void Updata(WifiConnectEntity wifiConnectEntity){
		
	}

}
