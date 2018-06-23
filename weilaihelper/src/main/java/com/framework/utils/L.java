package com.framework.utils;


import android.util.Log;

public class L {
	public static void e(String tag, String msg) {
		if (tag != null && msg != null)
			Log.e(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (tag != null && msg != null)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (tag != null && msg != null)
			Log.d(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (tag != null && msg != null)
			Log.v(tag, msg);
	}

	public static void e(String msg) {
		if (msg != null)
			Log.e("", msg);
	}

	public static void i(String msg) {
		if (msg != null)
			Log.i("", msg);
	}

	public static void d(String msg) {
		if (msg != null)
			Log.d("", msg);
	}

	public static void v(String msg) {
		if (msg != null)
			Log.v("", msg);
	}

}

