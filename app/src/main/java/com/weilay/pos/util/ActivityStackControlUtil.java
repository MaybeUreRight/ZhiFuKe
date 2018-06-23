package com.weilay.pos.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

/**
 * Created by rxwu on 2016/3/16 0016. Email锛?1158577255@qq.com
 * 
 * @detail activity 堆栈管理工具
 */
public class ActivityStackControlUtil {
	private static Map<Class, Activity> activityList = new HashMap<>();

	private static String TAG = ActivityStackControlUtil.class.getSimpleName();

	public static void remove(Class clsName) {
		if (activityList.containsKey(clsName))
			activityList.remove(clsName);
	}

	public static void add(Activity activity) {
		if (activity == null)
			return;
		activityList.put(activity.getClass(), activity);
	}

	/******
	 * @detail 除了当前的act，其他的全部关闭
	 * @param act
	 */
	public static void closeAll() {
		if (activityList == null || activityList.size() <= 0) {
			return;
		}
		for (Activity activity : activityList.values()) {
			Log.i(TAG, "exitApp()退出?");
			System.out.println(activity.getLocalClassName());
			activity.finish();
		}
		activityList.clear();
	}

	public static void exitApp() {
		closeAll();
		android.os.Process.killProcess(android.os.Process.myPid());

	}

	public static int size() {
		// TODO Auto-generated method stub
		return activityList == null ? 0 : activityList.size();
	}
}
