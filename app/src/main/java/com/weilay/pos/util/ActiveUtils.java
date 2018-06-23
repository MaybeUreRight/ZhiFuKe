package com.weilay.pos.util;

import java.util.Calendar;

import com.framework.utils.L;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.http.SystemRequest;

import android.text.TextUtils;

/**********
 * @Detail 设备在线时长活跃度统计工具类
 * @author rxwu
 *
 */
public class ActiveUtils {

	public static final long INTERVAL_TIME = 10 * 60 * 1000;// 每隔10分钟，计算一次
	private static final long ACTIVE_TIME = 8 * 60 * 60 * 1000;// 每隔8个小时，统计一次

	private static final String CACHE_ACTIVE_KEY = "CACHE_ACTIVE_KEY";
	private static final String CACHE_ACTIVE_LAST_TIME = "CACHE_ACTIVE_LAST_TIME";

	public static void increaseActive() {
		/// 上次的活跃时间
		ACache cache = WeiLayApplication.app.mCache;
		// 保存当天第一次开机的时间
		if (TextUtils.isEmpty(cache.getAsString(CACHE_ACTIVE_LAST_TIME))) {
			// 保存最后一次活跃的时间
			cache.put(CACHE_ACTIVE_LAST_TIME, ServerTimeUtils.server_time);
		}

		// 累计机器的在线时间
		long active_time = cache.getAsLong(CACHE_ACTIVE_KEY, 0);
		active_time += INTERVAL_TIME;
		cache.put(CACHE_ACTIVE_KEY, active_time);

		// 判断是否是当天的？
		long last_active_time = cache.getAsLong(CACHE_ACTIVE_LAST_TIME, 0);
		long server_time = ServerTimeUtils.server_time;
		if (isEqualDay(last_active_time, server_time)) {
			// 如果是同一天的？
			if (active_time > ACTIVE_TIME) {
				// 已经满足8个小时在线活跃
				// TODO 提交在线活跃，提交完成后清除活跃统计数据
				L.d("---开始提交活跃累计时长");
				SystemRequest.sendMachineActivitys(active_time);
			}
		} else {
			cache.put(CACHE_ACTIVE_LAST_TIME, ServerTimeUtils.server_time);// 重新设置开始时间
			cache.put(CACHE_ACTIVE_KEY, INTERVAL_TIME);// 清零活跃时间
		}
	}

	/******
	 * @detail 判断两个时间戳时间日期是否相同
	 * @return
	 */
	public static boolean isEqualDay(long time1, long time2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time1);
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTimeInMillis(time2);
		int day2 = calendar.get(Calendar.DAY_OF_YEAR);
		return day1 == day2;
	}
}
