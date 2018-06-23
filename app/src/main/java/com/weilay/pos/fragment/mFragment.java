package com.weilay.pos.fragment;

import java.io.IOException;

import com.framework.utils.L;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.weilay.pos.R;
import com.weilay.pos.app.BaseFragment;
import com.weilay.pos.util.BeepManager;
import com.weilay.pos.zxing.ViewfinderView;
import com.weilay.pos.zxing.ZxingActivityHandler;

import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public abstract class mFragment extends BaseFragment implements
android.view.SurfaceHolder.Callback{
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		closeCarmera();
		if(beepManager!=null){
			beepManager.CloseBeep();
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeCarmera();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resumeCamera();
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
				handler = new ZxingActivityHandler(mContext, mCameraManager);
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
		mCameraManager = new CameraManager(mContext);
		viewfinderView = (ViewfinderView) mRootView.findViewById(R.id.viewfinder_view);
		surfaceView = (SurfaceView) mRootView.findViewById(R.id.preview_view);
		viewfinderView.setmCameraManager(mCameraManager);

		viewfinderView.setVisibility(View.VISIBLE);
		surfaceView.setVisibility(View.VISIBLE);
		useCamera = true;
		resumeCamera();

		beepManager = new BeepManager(mContext, R.raw.beep);

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

}
