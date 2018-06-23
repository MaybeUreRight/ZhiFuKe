package com.weilay.pos;

import java.io.File;

import com.weilay.pos.util.CmdForAndroid;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	private MediaPlayer meidaPlayer;
	private VideoView mVideoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		mVideoView = (VideoView) findViewById(R.id.test_videoview);
		play();
	}

	private void play() {
		final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";
		final File video = new File(path);
		if (video.exists()) {
			// T.showCenter("视频路径："+path);
			mVideoView.setVisibility(View.VISIBLE);
			MediaController controller = new MediaController(this);
			controller.setVisibility(View.GONE);
			controller.hide();
			mVideoView.setMediaController(controller);
			mVideoView.setVideoPath(path);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mVideoView.setLayoutParams(layoutParams);
			mVideoView.start();
			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					mediaPlayer.setVolume(50f, 50f);
					mediaPlayer.start();
					mediaPlayer.setLooping(true);
				}
			});
			mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
					if (video.exists()) {
						video.delete();
					}
					return true;
				}
			});
			mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					// TODO 暂停播放完视频后检查更新
					mVideoView.setVideoPath(path);
					mVideoView.start();
				}
			});
			mVideoView.requestFocus();
		} else {
			boolean isComplete = CmdForAndroid.shella("su", "cp /system/etc/test.mp4 " + path);
			if (isComplete) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						play();
					}

				}, 1000);
			}

		}
	}

}
