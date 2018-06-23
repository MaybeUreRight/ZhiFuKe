package com.weilay.pos.util;

import java.io.InputStream;

import com.framework.utils.L;
import com.weilay.pos.entity.QRInfoEntity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

/*****
 * 
 * @Detail 将优惠卡券中的图片转换成内容
 * @author rxwu
 *
 */
public class QrInfoUtil {
	public static Bitmap parsePayPrefer2code(final QRInfoEntity qrInfoEntity) {
		if (qrInfoEntity == null) {
			return null;
		}
		String type = qrInfoEntity.getType() == null ? "" : qrInfoEntity.getType().toLowerCase();
		Bitmap bitmap = null;
		try {
			switch (type) {
			case "g":
				final String downloadurl = qrInfoEntity.getQrcode();
				if (downloadurl != null) {
					InputStream is = new java.net.URL(downloadurl).openStream();
					BitmapFactory.Options options = new BitmapFactory.Options();
					bitmap = BitmapFactory.decodeStream(is, new Rect(0, 0, 0, 0), options);
					Matrix matrix = new Matrix();
					matrix.postScale(0.8f, 0.8f);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
				}
				break;
			default:
				bitmap = QRCodeUtil.createQRImage(qrInfoEntity.getQrcode(), 300, 300, null, null);
				break;
			}
		} catch (Exception ex) {
			L.e(ex.getLocalizedMessage());
		}
		return bitmap;

	}
}
