/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weilay.pos.zxing;

import java.util.Vector;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.weilay.pos.R;
import com.weilay.pos.titleactivity.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 */
public final class CaptureActivityHandler extends Handler {

	private static String TAG = CaptureActivityHandler.class.getSimpleName();

	/*
	 * private MipcaActivityCapture mActivityCapture;
	 * 
	 * private ReceivePayActivity mReceivePayActivity;
	 * 
	 * private ChargeOffActivity mChargeOffActivity; private PaySelectActivity
	 * mPaySelectActivity; private PayActivity mPayActivity;
	 */
	private BaseActivity mActivity;
	private DecodeThread decodeThread;
	private State state;
//	private String activityType;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}
	/*
	 * public CaptureActivityHandler(MipcaActivityCapture activity,
	 * Vector<BarcodeFormat> decodeFormats, String characterSet, String
	 * activityType) { this.mActivity = activity; this.activityType =
	 * activityType; decodeThread = new DecodeThread( activity, decodeFormats,
	 * characterSet, new
	 * ViewfinderResultPointCallback(activity.getViewfinderView()),
	 * activityType); decodeThread.start(); state = State.SUCCESS; // Start
	 * ourselves capturing previews and decoding.
	 * CameraManager.get().startPreview(); restartPreviewAndDecode(); }
	 * 
	 * public CaptureActivityHandler(ReceivePayActivity receivePayActivity,
	 * Vector<BarcodeFormat> decodeFormats, String characterSet, String
	 * activityType) { // TODO Auto-generated constructor stub this.activityType
	 * = activityType; this.mReceivePayActivity = receivePayActivity;
	 * decodeThread = new DecodeThread(receivePayActivity, decodeFormats,
	 * characterSet, new ViewfinderResultPointCallback(
	 * receivePayActivity.getViewfinderView()), activityType);
	 * decodeThread.start(); state = State.SUCCESS; // Start ourselves capturing
	 * previews and decoding. CameraManager.get().startPreview();
	 * restartPreviewAndDecode(); }
	 * 
	 * public CaptureActivityHandler(ChargeOffActivity chargeOffActivity,
	 * Vector<BarcodeFormat> decodeFormats, String characterSet, String
	 * activityType) { // TODO Auto-generated constructor stub this.activityType
	 * = activityType; this.mChargeOffActivity = chargeOffActivity; decodeThread
	 * = new DecodeThread(chargeOffActivity, decodeFormats, characterSet, new
	 * ViewfinderResultPointCallback( chargeOffActivity.getViewfinderView()),
	 * activityType); decodeThread.start(); state = State.SUCCESS; // Start
	 * ourselves capturing previews and decoding.
	 * CameraManager.get().startPreview(); restartPreviewAndDecode(); }
	 * 
	 * public CaptureActivityHandler(PaySelectActivity paySelectActivity,
	 * Vector<BarcodeFormat> decodeFormats, String characterSet, String
	 * activityType) { // TODO Auto-generated constructor stub this.activityType
	 * = activityType; this.mPaySelectActivity = paySelectActivity; decodeThread
	 * = new DecodeThread(paySelectActivity, decodeFormats, characterSet, new
	 * ViewfinderResultPointCallback( paySelectActivity.getViewfinderView()),
	 * activityType); decodeThread.start(); state = State.SUCCESS; // Start
	 * ourselves capturing previews and decoding.
	 * CameraManager.get().startPreview(); restartPreviewAndDecode(); }
	 */

	public CaptureActivityHandler(BaseActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet/*,
			String activityType*/) {
		// TODO Auto-generated constructor stub
		///this.activityType = activityType;
		this.mActivity = activity;
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ViewfinderResultPointCallback(activity.getViewfinderView()));
		decodeThread.start();
		state = State.SUCCESS;
		// Start ourselves capturing previews and decoding.
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.auto_focus:
			// Log.d(TAG, "Got auto-focus message");
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure what
			// else to do.
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			break;
		case R.id.restart_preview:
			// Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			break;
		case R.id.decode_succeeded:
			// Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
//			Bundle bundle = message.getData();

			/***********************************************************************/
			// Bitmap barcode = bundle == null ? null :
			// (Bitmap)
			// bundle.getParcelable(DecodeThread.BARCODE_BITMAP);//锟斤拷锟矫憋拷锟斤拷锟竭筹拷
			/*
			 * if (mActivityCapture != null) {
			 * mActivityCapture.handleDecode((Result) message.obj); } if
			 * (mReceivePayActivity != null) {
			 * mReceivePayActivity.handleDecode((Result) message.obj); } if
			 * (mChargeOffActivity != null) {
			 * mChargeOffActivity.handleDecode((Result) message.obj); } if
			 * (mPaySelectActivity != null) {
			 * mPaySelectActivity.handleDecode((Result) message.obj); } if
			 * (mPayActivity != null) { mPayActivity.handleDecode((Result)
			 * message.obj); }
			 */
			
			mActivity.handleDecode((Result) message.obj);

			// 锟斤拷锟截斤拷锟?
			// /***********************************************************************/
			break;
		case R.id.decode_failed:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
			break;
		case R.id.return_scan_result:
			// Log.d(TAG, "Got return scan result message");
			mActivity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			/*
			 * if (mActivityCapture != null) {
			 * mActivityCapture.setResult(Activity.RESULT_OK, (Intent)
			 * message.obj); mActivityCapture.finish(); }
			 * 
			 * if (mReceivePayActivity != null) {
			 * mReceivePayActivity.setResult(Activity.RESULT_OK, (Intent)
			 * message.obj); mReceivePayActivity.finish(); } if
			 * (mChargeOffActivity != null) {
			 * mChargeOffActivity.setResult(Activity.RESULT_OK, (Intent)
			 * message.obj); } if (mPaySelectActivity != null) {
			 * mPaySelectActivity.setResult(Activity.RESULT_OK, (Intent)
			 * message.obj); } if (mPayActivity != null) { mPayActivity
			 * .setResult(Activity.RESULT_OK, (Intent) message.obj); }
			 */
			mActivity.finish();
			break;
		case R.id.launch_product_query:
			// Log.d(TAG, "Got product query message");
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			mActivity.startActivity(intent);
			/*
			 * if (mActivityCapture != null) {
			 * mActivityCapture.startActivity(intent); }
			 * 
			 * if (mReceivePayActivity != null) {
			 * mReceivePayActivity.startActivity(intent); } if
			 * (mChargeOffActivity != null) {
			 * mChargeOffActivity.startActivity(intent); } if
			 * (mPaySelectActivity != null) {
			 * mPaySelectActivity.startActivity(intent); } if (mPayActivity !=
			 * null) { mPayActivity.startActivity(intent); }
			 */

			break;
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}

	public void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			mActivity.drawViewfinder();
		/*	switch (activityType) {
			case "mip":
				mActivityCapture.drawViewfinder();
				break;
			case "receive":
				mReceivePayActivity.drawViewfinder();
				break;
			case "charge":
				mChargeOffActivity.drawViewfinder();
				break;
			case "payselect":
				mPaySelectActivity.drawViewfinder();
				break;
			case "pay":
				mPayActivity.drawViewfinder();
				break;
			}*/
		}
	}

}
