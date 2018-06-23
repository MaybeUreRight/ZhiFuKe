package com.weilay.pos.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;

public class BarClose {
	public static int count = 0;
	/******
	 * 隐藏状态栏
	 * 
	 * @param context
	 */
	public static void closeBar() {
		if(count>0){
			return;
		}
		count++;
		HandlerThread handler=new HandlerThread("closeBar");
		handler.start();
		new Handler(handler.getLooper()).postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// 需要root 权限
					Build.VERSION_CODES vc = new Build.VERSION_CODES();
					Build.VERSION vr = new Build.VERSION();
					String ProcID = "79";
					if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
						ProcID = "42"; // ICS AND NEWER
					}
					// 需要root 权限
					Process proc = Runtime.getRuntime().exec(
							new String[] {
									"su",
									"-c",
									"service call activity " + ProcID
											+ " s16 com.android.systemui" }); // WAS 79
					proc.waitFor();
				} catch (Exception ex) {
					T.showCenter(ex.getMessage());
				}
			}
		}, 1000);
		
	}

	public static void showBar(Context context) {
		count=0;
		try {
			// 需要root 权限
			Build.VERSION_CODES vc = new Build.VERSION_CODES();
			Build.VERSION vr = new Build.VERSION();
			String ProcID = "79";
			
				if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
					ProcID = "42"; // ICS AND NEWER
				}
			
			Process proc = Runtime.getRuntime().exec(
					new String[] { "am", "startservice", "-n",
							"com.android.systemui/.SystemUIService" });

			proc.waitFor();

		} catch (Exception ex) {
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
