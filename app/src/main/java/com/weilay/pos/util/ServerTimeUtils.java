package com.weilay.pos.util;

import java.util.Timer;
import java.util.TimerTask;

/**********
 * @detail 计算服务器时间的工具类
 * @author Administrator
 *
 */
public class ServerTimeUtils {
	public static long addTime = 0;
	public static long server_time = 0;// 默认为系统当前的时间

	
	private static Timer timer = null;
	private static TimerTask task = null;
	
	public static synchronized void setServerTime(long server_time1) {
		server_time = server_time1;
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		timer = new Timer();
		if (task != null) {
			task.cancel();
			task = null;
		}

		task = new TimerTask() {

			@Override
			public void run() {
				// L.i("服务器时间增加" + server_time);
				// TODO Auto-generated method stub
				server_time = server_time + 1000;// 每次累加1秒
				addTime += 1000;
				if (addTime >= ActiveUtils.INTERVAL_TIME) {
					ActiveUtils.increaseActive();
					addTime = 0;
				}
			}
		};
		timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
	}

	/**
	 * @return
	 */
	public static long getServerTime() {
		if (server_time == 0) {
			return System.currentTimeMillis();
		}
		return server_time;
	}


}
