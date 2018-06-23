package com.weilay.pos.util;

import com.weilay.pos.R;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.WeiLayApplication;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rxwu on 2016/3/16.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail: Toast管理类
 */
public class T {
	public static Toast t;
	private static TextView text;
	private static View layout;

	private T() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static void show(CharSequence message, boolean center, int duration) {
		if (t == null) {
			t = Toast.makeText(WeiLayApplication.app, message, duration);
			layout = LayoutInflater.from(
					WeiLayApplication.app.getApplicationContext()).inflate(
					R.layout.toast_layout, null);
			((ImageView)layout.findViewById(R.id.t_logo)).setImageResource(Config.LOGIN_LOGO);
			text = (TextView) layout.findViewById(R.id.toast_tv);
		}
		text.setText(message);
		t.setView(layout);
		if (center)
			t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		t.show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param message
	 */
	public static void showShort(CharSequence message) {
		show(message, false, Toast.LENGTH_SHORT);
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param message
	 */
	public static void showLong(CharSequence message) {
		show(message, false, Toast.LENGTH_LONG);
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param message
	 * @param duration
	 */
	public static void show(CharSequence message, int duration) {
		show(message, false, duration);
	}

	/**************
	 * @detail 中间显示Toast
	 */
	public static void showCenter(CharSequence message) {
		show(message, true, Toast.LENGTH_SHORT);
	}

}