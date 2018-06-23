package com.weilay.pos.app;

import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.weilay.pos.service.WeilayService;
import com.weilay.pos.util.BootUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		L.e("com.weilay.pos.app.BroadcastStart->onReceive");
		int boot=0;
		if((boot=getBoot())>0){
			saveBoot(boot-1);
			//继续重启
			BootUtils.reboot();//(mContext);
		}else{
			Intent launchIntent = context.getPackageManager()
					.getLaunchIntentForPackage("com.weilay.pos");
			context.startActivity(launchIntent);

			Intent setviceIntent = new Intent(context, WeilayService.class);
			context.startService(setviceIntent);
		}
	}
	private static final String CACHE_BOOT_KEY="CACHE_BOOT_KEY";
	//保存开机次数
	public static void saveBoot(int boot){
		WeiLayApplication.app.mCache.put(CACHE_BOOT_KEY,boot+"");
	}
	//获取开机次数
	public static int getBoot(){
		String bootStr=WeiLayApplication.app.mCache.getAsString(CACHE_BOOT_KEY);
		if(StringUtil.isBank(bootStr)){
			return 0;
		}
		return Integer.parseInt(bootStr);
	}
}
