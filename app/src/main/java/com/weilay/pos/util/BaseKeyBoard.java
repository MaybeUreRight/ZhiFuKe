package com.weilay.pos.util;

import java.util.HashMap;

import com.weilay.pos.R;

import android.view.View;
import android.view.View.OnClickListener;

public class BaseKeyBoard implements OnClickListener {
	private View Keyboard;

	public enum OPTIONS_KEY {
		DELETE, CLEAR, ENTER, POINT, DOUBLE_ZERO
	}

	public void setOnkeyListener(OnKeyListener onkeyListener) {
		if (onkeyListener != null) {
			this.onkeyListener = onkeyListener;
		}
	}

	private OnKeyListener onkeyListener = new OnKeyListener() {

		@Override
		public void onOptions(OPTIONS_KEY option) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNumberClick(String num) {
			// TODO Auto-generated method stub

		}
	};
	
	public interface OnKeyListener {

		public void onNumberClick(String num);

		public void onOptions(OPTIONS_KEY option);
	}

	private HashMap<Integer, String> values = new HashMap<>();
	private int[] keys = new int[] { R.id.pay_number_0, R.id.pay_number_1, R.id.pay_number_2, R.id.pay_number_3,
			R.id.pay_number_4, R.id.pay_number_5, R.id.pay_number_6, R.id.pay_number_7, R.id.pay_number_8,
			R.id.pay_number_9, R.id.pay_number_00, R.id.pay_number_point, R.id.pay_clear, R.id.pay_enter,
			R.id.pay_delete };

	public BaseKeyBoard(View keyboard) {
		this.Keyboard = keyboard;
		initDatas();
		initEvent();

	}

	/*****
	 * @detail 初始化数据
	 */
	private void initDatas() {
		for (int i = 0; i < keys.length; i++) {
			values.put(keys[i], "" + i);
		}
	}

	private void initEvent() {
		for (int i = 0; i < keys.length; i++) {
			View view = Keyboard.findViewById(keys[i]);
			if (view != null) {
				view.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.pay_number_00:
			onkeyListener.onOptions(OPTIONS_KEY.DOUBLE_ZERO);
			break;
		case R.id.pay_number_point:
			onkeyListener.onOptions(OPTIONS_KEY.POINT);
			break;
		case R.id.pay_clear:
			onkeyListener.onOptions(OPTIONS_KEY.CLEAR);
			break;
		case R.id.pay_enter:
			onkeyListener.onOptions(OPTIONS_KEY.ENTER);
			break;
		case R.id.pay_delete:
			onkeyListener.onOptions(OPTIONS_KEY.DELETE);
			break;
		default:
			onkeyListener.onNumberClick(values.get(arg0.getId()));
			break;
		}
	}
}