package com.framework.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 常用时间工具类
 * <p/>
 * <p>
 * detailed comment
 *
 * @author yuantao.wu 2015-3-4
 * @see
 * @since 1.0
 */
public class TimeUtils {
	@SuppressLint("SimpleDateFormat")
	public static DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressLint("SimpleDateFormat")
	public static DateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
	@SuppressLint("SimpleDateFormat")
	public static DateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日 E"); // E表示星期几
	@SuppressLint("SimpleDateFormat")
	public static DateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 精确到小时
	@SuppressLint("SimpleDateFormat")
	public static DateFormat sdf5 = new SimpleDateFormat("HH:mm"); // 精确到小时

	public static String parseDate(DateFormat df, String date) {

		try {
			if (df.equals(sdf2) || df.equals(sdf3)) {
				return df.format(stringtoDate(date));
			} else {
				return df.format(df.parse(date));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获取当前日是星期几
	 */
	public static int getWeekday() {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		System.out.println("今天是星期" + (weekday == 1 ? 7 : weekday - 1));
		return weekday == 1 ? 7 : weekday - 1;
	}

	/**
	 * 日期转换为星期
	 *
	 * @param mdate
	 *            日期
	 * @return
	 */
	public static List<Date> dateToWeek(Date mdate) {
		System.out.println("mdate :" + mdate);
		int day = mdate.getDay();
		System.out.println("b:  " + day);
		Date fdate;
		List<Date> list = new ArrayList();
		Long fTime = mdate.getTime() - day * 24 * 3600000;
		System.out.println("fTime: " + fTime);
		for (int i = 0; i < 8; i++) {
			fdate = new Date();
			if (i == 0) {
				fdate.setTime(fTime + (i + 1 * 24 * 3600000));
				list.add(i, fdate);

			} else {
				fdate.setTime(fTime + (i * 24 * 3600000));
				list.add(i, fdate);
			}

		}

		return list;
	}

	/**
	 * long型的时间转化为字符串
	 *
	 * @param time
	 * @return
	 */
	public static String long2String(long time) {

		return sdf1.format(new Date(time));
	}

	/**
	 * 字符串转换为日期
	 *
	 * @param dateStr
	 *            字符串的格式的日期
	 * @return
	 */
	public static Date stringtoDate(String dateStr) {
		Date date = null;

		try {
			sdf1.setLenient(false);// 指定日期/时间分析是否不严格 为true时为不严格 ，否则为严格匹配该日期格式
			date = sdf1.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return date;
	}

	/**
	 * 日期转换为字符串
	 *
	 * @param date
	 *            日期
	 * @return
	 */
	public static String dateToString(java.util.Date date) {
		String result = "";

		try {
			result = sdf1.format(date);
		} catch (Exception e) {

		}
		return result;
	}

	/**
	 * 日期转换为字符串 如yyyy年MM月dd日
	 *
	 * @param date
	 *            日期
	 * @return
	 */
	public static String dateToString1(java.util.Date date) {
		String result = "";

		try {
			result = sdf2.format(date);
		} catch (Exception e) {

		}
		return result;
	}

	/**
	 * 获取当前的时间
	 *
	 * @param df
	 * @return
	 */
	public static String getNowTime(DateFormat df) {
		Date currentTime = new Date();
		String dateString = df.format(currentTime);
		return dateString;
	}

	/*****
	 * @Detail 格式化时间戳
	 * @param milltimes
	 * @param format
	 * @return
	 */
	public static String Time(long timemillis, String format) {
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat(format);
			Date date = new Date(timemillis);
			return dateformat.format(date);
		} catch (Exception ex) {
			return getNowTime(sdf1);
		}
	}

	/**
	 * @param date
	 *            时间
	 * @return
	 */
	public static String distanceNow(String date) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin = null;
		Date now = Calendar.getInstance().getTime();

		long between = 0;
		try {
			begin = dfs.parse(date);

			between = (now.getTime() - begin.getTime());// 得到两者的毫秒数
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

		int year = now.getYear() - begin.getYear();
		int month = now.getMonth() - begin.getMonth();
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		if (year != 0) {
			return day + "年前";
		} else if (month != 0) {
			return month + "月前";
		} else if (day != 0) {
			return day + "天前";
		} else if (hour != 0) {
			return hour + "小时前";
		} else if (min > 1) {
			return min + "分钟前";
		} else {
			return "刚刚";
		}

	}

	private int getMondayPlus() {
		Calendar calendar = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	/**
	 * 获取当前的
	 *
	 * @return
	 */
	public String getCurrentMonday() {
		// weeks = 0;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/***
	 * @detail 格式化跟进页面推送时间格式
	 * @param time
	 * @return
	 */
	public static String parseComeTime(long servertime, long timeStamp) {
		long curTime = servertime / (long) 1000;
		long time = curTime - (timeStamp / 1000);
		String dayStr = "";
		if /*
			 * (time < 60 && time >= 0) { dayStr= "刚刚"; } else if (time >= 60 &&
			 * time < 3600) { dayStr= time / 60 + "分钟前"; } else if (time >= 3600
			 * && time < 3600 * 24) { dayStr= time / 3600 + "小时前"; } else if
			 */ (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			final int day = (int) (time / 3600 / 24);
			switch (day) {
			case 1:
				dayStr = "昨天";
				break;
			case 2:
				dayStr = "前天";
				break;
			default:
				dayStr = day + "天前";
				break;
			}
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			dayStr = time / 3600 / 24 / 30 + "个月前";
		} else if (time >= 3600 * 24 * 30 * 12) {
			dayStr = time / 3600 / 24 / 30 / 12 + "年前";
		} else {
			dayStr = "";
		}
		Date date = new Date(timeStamp);
		/*
		 * switch (day){ case 0: dayStr=""; break; case 1: dayStr="昨天"; break;
		 * case 2: dayStr="前天"; break; default: dayStr=day+"天前"; break; }
		 */
		return dayStr + sdf5.format(date);
	}
}
