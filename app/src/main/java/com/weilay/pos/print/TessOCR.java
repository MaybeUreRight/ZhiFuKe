package com.weilay.pos.print;


import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessOCR {
	private com.googlecode.tesseract.android.TessBaseAPI mTess;


	public TessOCR() {
		// TODO Auto-generated constructor stub
		mTess = new TessBaseAPI();
		// String datapath = Environment.getExternalStorageDirectory() +
		// "/tesseract/";
		// String datapath
		// =Environment.getRootDirectory().getAbsolutePath()+"/etc";
		String datapath = "system/etc";

		String language = "chi_sim";// "eng";
		// File dir = new File(datapath + "tessdata/");
		// if (!dir.exists())
		// dir.mkdirs();

		mTess.init(datapath, language);
	}

	public String getOCRResult(Bitmap bitmap) {
		mTess.setImage(bitmap);
		String result = mTess.getUTF8Text();
		mTess.clear();// 识别完成后clear一下,update by rxwu
		return result;
	}

	public void onDestroy() {
		if (mTess != null)
			mTess.end();
	}

}
