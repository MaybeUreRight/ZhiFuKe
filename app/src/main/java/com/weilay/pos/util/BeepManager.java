package com.weilay.pos.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class BeepManager {
	private static final float BEEP_VOLUME = 1.0f;// 声音大小 0.1~1.0之间.
	private MediaPlayer mediaPlayer;// 播放声音
	private boolean playBeep;

	// private Activity mActivity;
	// private int rawId;

	public BeepManager(Context activity, int rawId) {
		// this.mActivity = activity;
		// this.rawId = rawId;
		initBeepSound(activity, rawId);
	}

	// public void bulidBeep() {
	// if (mActivity == null) {
	// return;
	// }
	// }

	/**
	 * 初始化音频
	 */
	private void initBeepSound(Context activity, int rawId) {
		playBeep = true;
		AudioManager audioService = (AudioManager) activity
				.getSystemService(activity.AUDIO_SERVICE);
		audioService.setStreamVolume(AudioManager.STREAM_MUSIC,audioService.getStreamMaxVolume(AudioManager.STREAM_MUSIC),  
                AudioManager.FLAG_PLAY_SOUND);  
		// audioService.adjustStreamVolume(AudioManager.STREAM_MUSIC,
		// audioService.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
		// AudioManager.FLAG_PLAY_SOUND);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		if (playBeep && mediaPlayer == null) {
			// activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			try {
				AssetFileDescriptor file = activity.getResources()
						.openRawResourceFd(rawId);
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	/**
	 * 播放声音
	 */
	public void playBeep() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public void CloseBeep() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

}
