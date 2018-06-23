package com.weilay.pos.util;

import com.weilay.pos.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DialogUtil {
	public interface OnDialogListener {
		public void onFinish();

		public void onCancel();
	}

	public static Dialog dialog_if(final Activity act, int width, int height,
			int gravity, String message, final boolean off,
			final OnDialogListener listener) {
		final Dialog dialog = new Dialog(act);
		// wifi_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout);
		// dialog.setTitle(title);
		WindowManager manager = (WindowManager) act
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point screenResolution = new Point(display.getWidth(),
				display.getHeight());

		Window window = dialog.getWindow();
		TextView message_tv = (TextView) window.findViewById(R.id.message);
		TextView enter_tv = (TextView) window.findViewById(R.id.enter_dialog);
		TextView cancel_tv = (TextView) window.findViewById(R.id.dialog_cancel);
		message_tv.setText(message);
		enter_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onFinish();
				}
				if (off) {
					dialog.dismiss();
					act.finish();
				} else {
					dialog.dismiss();
				}
			}
		});
		cancel_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onCancel();
				}
				if (off) {
					dialog.dismiss();
					act.finish();
				} else {
					dialog.dismiss();
				}
			}
		});
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.width = screenResolution.x * width / 10; // 宽度
		lp.height = screenResolution.y * height / 10; // 高度
		// lp.alpha = 0.8f; // 透明度
		window.setGravity(gravity);
		window.setAttributes(lp);
		return dialog;
	}
	public static Dialog singleDialog(final Activity act,int width,int height,int gravity,String message,final boolean off){
		final Dialog dialog = new Dialog(act, R.style.FullHeightDialog);
		// wifi_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout);
		// dialog.setTitle(title);
		WindowManager manager = (WindowManager) act
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point screenResolution = new Point(display.getWidth(),
				display.getHeight());

		Window window = dialog.getWindow();
		TextView message_tv = (TextView) window.findViewById(R.id.message);
		TextView enter_tv = (TextView) window.findViewById(R.id.enter_dialog);
		TextView cancel_tv = (TextView) window.findViewById(R.id.dialog_cancel);
		message_tv.setText(message); 
		cancel_tv.setVisibility(View.GONE);
		enter_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(off){
					dialog.dismiss();
					act.finish();
				}else{
					dialog.dismiss();
				}
			}
		});
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.width = screenResolution.x * width / 10; // 宽度
		lp.height = screenResolution.y * height / 10; // 高度
//		 lp.alpha = 0.8f; // 透明度
		window.setGravity(gravity);
		window.setAttributes(lp);
		return dialog;
	}

	/**
	 * 点击确定后是否关闭Activity
	 * 
	 * @param act
	 * @param width
	 * @param height
	 * @param gravity
	 * @param message
	 * @param off
	 *            该值为真时关闭当前窗口
	 * @return
	 */
	public static void dialog_if(final Activity act, int width, int height,
			int gravity, String message, final boolean off) {
		if(act!=null && !act.isFinishing() && !act.isDestroyed())
		{
			 dialog_if(act, width, height, gravity, message, off, null).show();
		}

	}
	
	public static void dialog_if(Activity act,String msg,boolean off){
		if(act!=null && !act.isFinishing() && !act.isDestroyed())
		{
			 dialog_if(act, 9,5, Gravity.CENTER, msg, off, null).show();
		}
	}
	/**
	 * 
	 * 系统更新专用
	 * 
	 * @param act
	 * @param width
	 * @param height
	 * @param gravity
	 * @param message
	 * @param path
	 * @return
	 */
	public static Dialog dialog(final Activity act, int width, int height,
			int gravity, String message, final String path) {

		final Dialog dialog = new Dialog(act, R.style.FullHeightDialog);
		// wifi_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout);
		// dialog.setTitle(title);
		WindowManager manager = (WindowManager) act
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point screenResolution = new Point(display.getWidth(),
				display.getHeight());

		Window window = dialog.getWindow();
		TextView message_tv = (TextView) window.findViewById(R.id.message);
		TextView enter_tv = (TextView) window.findViewById(R.id.enter_dialog);
		TextView cancel_tv = (TextView) window.findViewById(R.id.dialog_cancel);
		message_tv.setText(message);
		enter_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.example.weilayitem");
				intent.putExtra("path", path);
				act.startActivity(Intent.createChooser(intent, "weilayitem"));
				dialog.dismiss();
			}
		});
		cancel_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.width = screenResolution.x * width / 10; // 宽度
		lp.height = screenResolution.y * height / 10; // 高度
		// lp.alpha = 0.8f; // 透明度
		window.setGravity(gravity);
		window.setAttributes(lp);
		return dialog;
	}

	/**
	 * 仅返回dialog
	 * 
	 * @param act
	 * @param width
	 * @param height
	 * @param gravity
	 * @param message
	 * @return
	 */
	public static Dialog dialog_(final Context act, int width, int height,
			int gravity, String message,final OnDialogListener listener) {
		if(listener!=null){
			
		}
		final Dialog dialog = new Dialog(act, R.style.FullHeightDialog);
		// wifi_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout);
		// dialog.setTitle(title);
		WindowManager manager = (WindowManager) act
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point screenResolution = new Point(display.getWidth(),
				display.getHeight());

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		TextView message_tv = (TextView) window.findViewById(R.id.message);
		message_tv.setText(message);
		TextView enter_dialog=(TextView)window.findViewById(R.id.enter_dialog);
		TextView cancel_dialog=(TextView)window.findViewById(R.id.dialog_cancel);
		enter_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(listener!=null){

					listener.onFinish();
				}
				if(dialog!=null){

					dialog.dismiss();
				}
			}
		});
		cancel_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(listener!=null){

					listener.onCancel();
				}
				if(dialog!=null){
					dialog.dismiss();
				}
			}
		});
		lp.width = screenResolution.x * width / 10; // 宽度
		lp.height = screenResolution.y * height / 10; // 高度
		// lp.alpha = 0.8f; // 透明度
		window.setGravity(gravity);
		window.setAttributes(lp);
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if(listener!=null){

					listener.onCancel();
				}
			}
		});
		
		return dialog;
	}
	
	
	public static Dialog dialog_(final Context act, int width, int height,
			int gravity, String message){
		return dialog_(act, width, height, gravity, message,null);
	}
	public static Dialog singleDialog_(Activity act,int width,int height,int gravity){
		return null;
		
	}
}
