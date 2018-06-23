package com.weilay.pos.util;

import com.weilay.pos.R;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {
	private static ProgressDialog progressDialog;

	public static ProgressDialog progressDialog(Context context, String message) {
		progressDialog = new ProgressDialog(context, R.style.loading_dialog);
		progressDialog.setIndeterminateDrawable(context.getResources()
				.getDrawable(R.anim.loading1));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(message);

		return progressDialog;
	}
}
