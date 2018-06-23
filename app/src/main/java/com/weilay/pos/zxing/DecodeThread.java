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

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.weilay.pos.titleactivity.BaseActivity;

import android.os.Handler;
import android.os.Looper;

/**
 * This thread does all the heavy lifting of decoding the images. 解码线程
 */
final class DecodeThread extends Thread {

	public static String BARCODE_BITMAP = "barcode_bitmap";
	/*private MipcaActivityCapture mActivityCapture;

	private ReceivePayActivity mReceivePayActivity;

	private ChargeOffActivity mChargeOffActivity;
	private PaySelectActivity mPaySelectActivity;
	private PayActivity mPayActivity;*/
	private BaseActivity mActivity;
	private Hashtable<DecodeHintType, Object> hints;
	private Handler handler;
	private CountDownLatch handlerInitLatch;
	//private String actvityType;

	/*DecodeThread(MipcaActivityCapture activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ResultPointCallback resultPointCallback, String actvityType) {

		this.mActivityCapture = activity;
		handlerInitLatch = new CountDownLatch(1);
		this.actvityType = actvityType;
		hints = new Hashtable<DecodeHintType, Object>(3);

		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}

	public DecodeThread(ReceivePayActivity receivePayActivity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ViewfinderResultPointCallback resultPointCallback,
			String actvityType) {
		// TODO Auto-generated constructor stub
		this.actvityType = actvityType;
		this.mReceivePayActivity = receivePayActivity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new Hashtable<DecodeHintType, Object>(3);

		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}

	public DecodeThread(ChargeOffActivity chargeOffActivity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ViewfinderResultPointCallback resultPointCallback,
			String actvityType) {
		// TODO Auto-generated constructor stub
		this.actvityType = actvityType;
		this.mChargeOffActivity = chargeOffActivity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new Hashtable<DecodeHintType, Object>(3);

		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}

	public DecodeThread(PaySelectActivity paySelectActivity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ViewfinderResultPointCallback resultPointCallback,
			String actvityType) {
		// TODO Auto-generated constructor stub
		this.actvityType = actvityType;
		this.mPaySelectActivity = paySelectActivity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new Hashtable<DecodeHintType, Object>(3);

		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}
	public DecodeThread(PayActivity payActivity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ViewfinderResultPointCallback resultPointCallback,
			String actvityType) {
		// TODO Auto-generated constructor stub
		this.actvityType = actvityType;
		this.mPayActivity = payActivity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new Hashtable<DecodeHintType, Object>(3);

		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}
*/
	
	public DecodeThread(BaseActivity activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet,
			ViewfinderResultPointCallback resultPointCallback) {
		// TODO Auto-generated constructor stub
		//this.actvityType = actvityType;
		this.mActivity = activity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new Hashtable<DecodeHintType, Object>(3);

		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			//decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}

		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
	}
	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(mActivity, hints);
		handlerInitLatch.countDown();
		Looper.loop();
		/*switch (actvityType) {
		case "mip":
			handler = new DecodeHandler(mActivityCapture, hints);
			handlerInitLatch.countDown();
			Looper.loop();
			break;
		case "receive":
			handler = new DecodeHandler(mReceivePayActivity, hints);
			handlerInitLatch.countDown();
			Looper.loop();
			break;
		case "charge":
			handler = new DecodeHandler(mChargeOffActivity, hints);
			handlerInitLatch.countDown();
			Looper.loop();
			break;
		case "payselect":
			handler = new DecodeHandler(mPaySelectActivity, hints);
			handlerInitLatch.countDown();
			Looper.loop();
			break;
		case "pay":
			handler = new DecodeHandler(mPayActivity, hints);
			handlerInitLatch.countDown();
			Looper.loop();
			break;
		}*/

	}

}
