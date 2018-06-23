/*package com.weilay.pos.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;

public class PlayUtil {
	private static PlayUtil instance;
	private SoundPool soundPool;
	private Context mContext;

	*//*****
	 * @detail 初始化
	 * @return
	 *//*
	public static PlayUtil init(Context context) {
		if (instance == null) {
			instance = new PlayUtil(context);
		}
		return instance;
	}

	public PlayUtil(Context context) {
		mContext = context;
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);// maxStream
																	// ——
																	// 同时播放的流的最大数量streamType
																	// ——
																	// 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)srcQuality
																	// ——
																	// 采样率转化质量，当前无效果，使用0作为默认值

	}

	*//****
	 * @detail 播放资源
	 * @param res
	 *//*
	public void play(int res) {
		try {
			if (soundPool != null) {
				int soundID1 = soundPool.load(mContext, res, 1);
				if (soundID1 == 0) {
					// 记载失败
					L.d("play success");
					SystemClock.sleep(100);// 休眠加载100ms后播放
					soundPool.play(soundID1, 1, 1, 1, 1, 1);
				} else {
					soundPool.play(soundID1, 1, 1, 1, 1, 1);
					// 加载成功
					L.d("play error");
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			L.e("play happend error,cause:" + e.getLocalizedMessage());
		}finally{
			release();//最后释放
		}
	}

	*//*****
	 * @detail 释放所有资源
	 *//*
	public void release() {
		if (soundPool != null) {
			soundPool.release();
		}
	}

}
*/