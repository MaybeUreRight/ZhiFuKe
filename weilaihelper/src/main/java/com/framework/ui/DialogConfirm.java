package com.framework.ui;

import com.example.weilaihelper.R;
import com.weilay.listener.DialogConfirmListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rxwu on 2016/4/1.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:确认提示弹窗
 */
public class DialogConfirm{

	private static AlertDialog.Builder builder;
	private static String mTitle = "确定要执行此操作吗?", mContent = "", mOkBtn = "确定";

	protected DialogConfirm() {

	}
	

	public static AlertDialog ask(Activity context, String title, String content, String okBtn,
			final DialogConfirmListener listener) {
		if (title != null) {
			mTitle = title;
		}
		if (okBtn != null) {
			mOkBtn = okBtn;
		}
		if (content != null) {
			mContent = content;
		}
		if(context==null || context.isFinishing() || context.isDestroyed()){
			return null;
		}
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		builder = new AlertDialog.Builder(context);
		final AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		ImageView cancel = (ImageView) view.findViewById(R.id.dialog_close);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null)
				listener.okClick(dialog);
				dialog.dismiss();
			}
		});
		TextView titleTv = (TextView) view.findViewById(R.id.dialog_confirm_title);
		Button positiveBtn = (Button) view.findViewById(R.id.dialog_confirm_sure);
		TextView contentTv = (TextView) view.findViewById(R.id.dialog_confirm_content);
		titleTv.setText(mTitle);
		positiveBtn.setText(mOkBtn);
		contentTv.setText(mContent);
		positiveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(listener!=null)
					listener.okClick(dialog);
			}
		});
		dialog.show();
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.getWindow().setContentView(view);
		// 一定得在show完dialog后来set属性
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = 450;
		dialog.getWindow().setAttributes(lp);
		return dialog;
	}
}
