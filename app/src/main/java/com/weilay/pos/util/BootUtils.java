package com.weilay.pos.util;

import com.framework.ui.DialogAsk;
import com.weilay.listener.DialogAskListener;
import com.weilay.pos.titleactivity.BaseActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

public class BootUtils {
	 /******
     * 隐藏状态栏
     * @param context
     */
    public static void closeBar(Context context) {
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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void showBar(Context context){
        try {
            // 需要root 权限
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79";

            if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
                ProcID = "42"; // ICS AND NEWER
            }

            Process proc = Runtime.getRuntime().exec(new String[]{"am","startservice","-n","com.android.systemui/.SystemUIService"});

            proc.waitFor();


        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    public static void shutdown(){
    	CmdForAndroid.shella("su", "reboot -p");
    }

    public static void reboot(){
    	CmdForAndroid.shella("su", "reboot");
    }
	public static void shutdownOrReboot(BaseActivity mContext) {
		// TODO Auto-generated method stub
		DialogAsk.ask(mContext, "关机提示", "如不想进行关机操作,点击窗口之外可关闭当前窗口",
				"关机", "重启", new DialogAskListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						CmdForAndroid.shella("su", "reboot -p");
					}

					@Override
					public void cancelClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						CmdForAndroid.shella("su", "reboot");
					}
		});
	}
}
