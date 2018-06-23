package com.weilay.pos.titleactivity;

import java.io.IOException;

import com.bugtags.library.Bugtags;
import com.framework.utils.L;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.weilay.pos.PayActivity;
import com.weilay.pos.R;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.listener.LoadingListener;
import com.weilay.pos.push.GlobalPush;
import com.weilay.pos.util.ACache;
import com.weilay.pos.util.ActivityStackControlUtil;
import com.weilay.pos.util.BeepManager;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.ProgressDialogUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.zxing.ViewfinderView;
import com.weilay.pos.zxing.ZxingActivityHandler;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/*******
 * @Detail 基础的activity
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity implements
		android.view.SurfaceHolder.Callback {
	public ACache mCache;
	public int RESULT_FAILED = -2;
	protected BaseActivity mContext;
	private LoadingListener loadingListener = new LoadingListener() {

		@Override
		public void timeOut() {
			// TODO Auto-generated method stub
			T.showCenter("请求超时");
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			// T.showCenter("取消");
		}
	};

	ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {// 保持屏幕常亮
		super.onCreate(savedInstanceState);
		ActivityStackControlUtil.add(this);
		mContext = this;
		hasSurface = false;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mCache = ACache.get(this);
		WeiLayApplication.getUsbHelper(false).write("".getBytes());
		IntentFilter filter = new IntentFilter();
		filter.addAction(PosDefine.ACTION_RECEIVER_CARD);
		registerReceiver(mqttReceiver, filter);
	}

	/*****
	 * 定义广播接收器
	 */
	BroadcastReceiver mqttReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action = arg1.getAction();
			/*
			 * int count = arg1.getIntExtra(PosDefine.ACTION_RECEIVER_CARD, 0);
			 * if (count > 0) { L.d("receiver " + count + " message");
			 */
			pushArraival(GlobalPush.getNewCount());
			/*
			 * } else { // 收到消息，但是数量为0
			 * L.d("receiver message,but the count is zero"); }
			 */
		}
	};

	/**
	 * 监听外设小键盘 *(keyCode=155)是进入支付界面,Enter(keyCode=160|66)是确认支付
	 * BackSpace(keyCode=67)返回
	 */
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		Log.i("gg", "keyCode:" + keyCode);
		switch (keyCode) {
		case 155:
			if (WeiLayApplication.isLogin_Suc()) {
				Intent intent = new Intent(this, PayActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	};

	/******
	 * @Detail 显示进度条
	 * @param text
	 */
	public void showLoading(String text) {
		showLoading(text, 25, true, null);
	}

	/******
	 * @Detail 显示进度条
	 * @param text
	 * @param loadingListener
	 */
	public void showLoading(String text, LoadingListener loadingListener) {
		showLoading(text, 25, true, loadingListener);
	}

	/******
	 * @Detail 显示进度条
	 * @param text
	 * @param closeable
	 */
	public void showLoading(String text, boolean closeable) {
		showLoading(text, 25, closeable, null);
	}

	/*****
	 * @Detail 显示一个加载进度条
	 * @param text
	 *            text 显示加载的提示内容
	 * @param sec
	 *            多少秒后超时隐藏进度条
	 * @param loadingListener
	 *            loading过程的监听
	 * @param closeable
	 *            是否可以关闭
	 */

	public void showLoading(String text, int sec, boolean closeable,
			final LoadingListener loadingListener) {
		if (!isFinishing()) { // this.loadingListener = loadingListener;
			if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			if (sec <= 25) {
				sec = 25;// 默认15s后无响应提示超时
			}
			loadingDialog = ProgressDialogUtil.progressDialog(this, text);
			loadingDialog.setCancelable(closeable);
			loadingDialog.setCanceledOnTouchOutside(closeable);

			loadingDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					if (loadingListener != null) {
						loadingListener.onCancel();
					}
					arg0.dismiss();
				}
			});

			if (!loadingDialog.isShowing() && !isFinishing()) {
				loadingDialog.show();
			}

			View dialog_view = loadingDialog.getWindow().getDecorView();
			GetUtil.setDialogText(dialog_view);
			dialog_view.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					stopLoading();// 到时间就将进度条隐藏
					if (loadingListener != null) {
						loadingListener.timeOut();
					}
				}
			}, sec * 1000);
		}
	}

	/*****
	 * @Detail 停止进度条加载
	 */
	public void stopLoading() {
		if (loadingDialog != null && loadingDialog.isShowing()
				&& (!isFinishing() || !isDestroyed())) {
			loadingDialog.dismiss();
		}
		loadingDialog = null;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		closeCarmera();
		// BeepClose();
		if (beepManager != null) {
			beepManager.CloseBeep();
		}
		// 注：回调 2
		Bugtags.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stubz
		ActivityStackControlUtil.remove(getClass());
		if (mqttReceiver != null) {
			unregisterReceiver(mqttReceiver);
		}
		stopLoading();
		closeCarmera();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resumeCamera();

		// 注：回调 1
		Bugtags.onResume(this);
	}

	// 处理扫描的结果
	public void handleDecode(Result result) {
		if (beepManager != null) {
			beepManager.playBeep();
		}
		// inactivityTimer.onActivity();
		// closeCarmera();//关闭暂停摄像头
		// 2s后重启摄像头
		handler2.removeCallbacks(restartCamera);
		handler2.postDelayed(restartCamera, 5 * 1000);
	}

	Runnable restartCamera = new Runnable() {
		public void run() {
			L.e("正在重启摄像头");
			restartCerame();
		}
	};
	private Handler handler2 = new Handler();
	private ZxingActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private SurfaceView surfaceView;
	CameraManager mCameraManager;

	private BeepManager beepManager;// 播放声音

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	public SurfaceView getSurfaceView() {
		return surfaceView;
	}

	public CameraManager getmCameraManager() {
		return mCameraManager;
	}

	/******
	 * @detail 初始化摄像头
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		// update to 2016-08-16
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (mCameraManager.isOpen()) {
			return;
		}
		try {
			mCameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new ZxingActivityHandler(this, mCameraManager);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	/*****
	 * @detail 重新恢复摄像头
	 */
	public void resumeCamera() {

		if (surfaceView != null) {
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			if (hasSurface) {
				initCamera(surfaceHolder);
			} else {
				surfaceHolder.addCallback(this);
			}
			restartCerame();
		}
	}

	public void restartCerame() {
		useCamera = true;
		if (handler != null) {
			handler.restartPreviewAndDecode();
		}
	}

	private boolean useCamera = false;

	public void startScan() {
		mCameraManager = new CameraManager(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		viewfinderView.setmCameraManager(mCameraManager);

		viewfinderView.setVisibility(View.VISIBLE);
		surfaceView.setVisibility(View.VISIBLE);
		useCamera = true;
		resumeCamera();
		beepManager = new BeepManager(this, R.raw.beep);

	}

	/*****
	 * @detail 关闭摄像头
	 */
	public void closeCarmera() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		if (useCamera) {
			if (mCameraManager != null) {
				mCameraManager.closeDriver();

			} else {
				Log.i("gg", "Can't close driver ! CameraManager is null !");
			}
			useCamera = false;
		}

	}

	public Handler getHandler() {
		// TODO Auto-generated method stub
		return handler;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		if (!hasSurface) {
			hasSurface = true;
			initCamera(arg0);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		hasSurface = false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 注：回调 3
		// Bugtags.onDispatchTouchEvent(this, event);
		return super.dispatchTouchEvent(event);
	}

	public abstract void pushArraival(int messageCount);
}
