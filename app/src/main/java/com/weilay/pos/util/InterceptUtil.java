package com.weilay.pos.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.framework.utils.L;
import com.weilay.pos.app.Client;
import com.weilay.pos.entity.MachineEntity;
import com.weilay.pos.print.TessOCR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InterceptUtil {
	private String upPhoto_url = "API/UploadInvoince";
	int idx_bmp = 0;

	public int getIdx_bmp() {
		return idx_bmp;
	}

	public void setIdx_bmp(int idx_bmp) {
		this.idx_bmp = idx_bmp;
	}

	public double decode(Context contexts, byte[] recBytes, TessOCR tessOCR,
			MachineEntity machineInfo) {
		String result = "";
		try {
			if (esc_decode(recBytes)) {
				result = mergeImages(contexts, tessOCR, machineInfo);
			}

			return Double.valueOf(result);
		} catch (Exception e) {
			 e.printStackTrace();
			return 0;
		}
	}

	private void deleteTempFile() {
		try {
			String path = Environment.getExternalStorageDirectory() + "/tmp/";
			File dir = new File(path);
			if (dir.exists()) {
				if (dir.isDirectory()) {
					String[] children = dir.list();
					File f;
					for (int i = 0; i < children.length; i++) {
						f = new File(path + children[i]);
						f.delete();
					}
				}
			} else {
				dir.mkdir();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			L.i("gg", e.getMessage());
		}
	}

	private Boolean esc_decode(byte[] recBytes) {
		try {
			for (int i = 0; i < recBytes.length; i++) {
				int currentByte = recBytes[i];

				if (currentByte == 0x1b) // ESC
				{
					// 解释命令
					int nextByte = recBytes[i]; // Integer.valueOf(receivedBytes[i
												// + 1], 16);
					switch (nextByte) {
					case 0x40: // ESC @
					case 0x30: // ESC 0
					case 0x32: // ESC 2
					case 0x0c: // ESC FF
					case 0x4c: // ESC L
					case 0x53: // ESC S
					{
						i += 2;
						continue;
					}
					case 0x4a: // ESC J n
					case 0x64: // ESC d n
					case 0x21: // ESC ! N
					case 0x4d: // ESC M n
					case 0x2d: // ESC - n
					case 0x45: // ESC E n
					case 0x47: // ESC G n
					case 0x33: // ESC 3 n
					case 0x20: // ESC SP n
					case 0x25: // ESC % n
					case 0x3d: // ESC = n
					case 0x3f: // ESC ? n
					case 0x52: // ESC R n
					case 0x54: // ESC T n
					case 0x56: // ESC V n
					case 0x61: // ESC a n
					case 0x74: // ESC t n
					case 0x7b: // ESC { n
					case 0x2b: // ESC + n
					case 0x2f: // ESC / c

					{
						i += 3;
						continue;
					}

					case 0x24: // ESC $ nL nH
					case 0x5c: // ESC \ nL nH
					case 0x63: // ESC c 3 n / ESC c 4 n / ESC c 5 n
					case 0x42: // ESC B n t

					{
						i += 4;
						continue;
					}

					case 0x70: // ESC p m t1 t2
					case 0x43: // ESC C m t n
					{
						i += 5;
						continue;
					}
					case 0x28: {
						switch (recBytes[i + 2]) // Integer.valueOf(receivedBytes[i
													// + 2], 16))
						{
						case 0x55: // ESC ( U n1 n2 m
						{
							i += 6;
							continue;
						}
						default: // 0x43: //ESC ( C n1 n2 m1 m2
						{
							i += 7;
							continue;
						}
						}
					}
					case 0x2a: // ESC * m nL nH d1...dk
					{
						int m = recBytes[i + 2]; // Integer.valueOf(receivedBytes[i
													// + 2], 16);
						int nL = recBytes[i + 3]; // Integer.valueOf(receivedBytes[i
													// + 3], 16);
						// int nH = recBytes[i+4];
						// //Integer.valueOf(receivedBytes[i + 4], 16);

						if (m == 1)
							m = 8 / 8;
						else
							m = 24 / 8;

						i += 5 + nL * m;

						continue;
					}

					case 0x44: // ESC D n1...nk NUL
					{
						for (int x = i + 2; x < recBytes.length; x++) {
							int m = recBytes[x]; // Integer.valueOf(receivedBytes[x],
													// 16);

							if (m == 0x00) {
								i = m;
								continue;
							}
						}
						break;
					}

					case 0x57: // ESC W xL xH yL yH dxL dxH dyL dyH
					{
						i += 10;
						continue;
					}
					}
				} else if (currentByte == 0x1c) // FS
				{
					// 解释命令
					int nextByte = recBytes[i + 1]; // Integer.valueOf(receivedBytes[i
													// + 1], 16);

					switch (nextByte) {
					case 0x26: // FS &
					case 0x2e: // FS .
					{
						i += 2;
						continue;
					}
					case 0x21: // FS ! n
					case 0x2d: // FS - n
					case 0x57: // FS W n

					{
						i += 3;
						continue;
					}
					case 0x53: // FS S n1 n2
					{
						i += 4;
						continue;
					}
					case 0x32: // FS 2 c2 c2 d1..dk
					{
						i += 4 + 72;
						continue;
					}
					case 0x70: // FS p n m
					{
						i += 4;
						continue;
					}
					// case 0x71: //FS q n [xL xH yL yH d1...dk]1...pxL xH yL yH
					// d2..dk]n
					// {

					// continue;
					// }
					}
				} else if (currentByte == 0x1d) // GS
				{
					// 解释命令
					int nextByte = recBytes[i + 1]; // Integer.valueOf(receivedBytes[i
													// + 1], 16);

					switch (nextByte) {
					case 0x3a: // GS :
					{
						i += 2;
						continue;
					}

					case 0x21: // GS ! n
					case 0x42: // GS B n
					case 0x68: // GS h n
					case 0x77: // GS w n
					case 0x48: // GS H n
					case 0x66: // GS f n
					case 0x2f: // GS / m
					case 0x61: // GS a n
					case 0x72: // GS r n
					{
						i += 3;
						continue;
					}

					case 0x56: // GS V m / GS V m n
					{
						int m = recBytes[i + 2]; // Integer.valueOf(receivedBytes[i
													// + 2], 16);
						if (m < 66)
							i += 3;
						else
							i += 4;
						continue;
					}
					case 0x24: // GS $ nL nH
					case 0x4c: // GS L nL nH
					case 0x50: // GS P x y
					case 0x57: // GS W nL nH
					case 0x5c: // GS \ nL nH
					{
						i += 4;
						continue;
					}

					case 0x5e: // GS ^ r t m
					{
						i += 5;
						continue;
					}

					case 0x28: // GS ( A pL pH n m
					{
						i += 7;
						continue;
					}

					case 0x27: // GS ' n x1sL x1sH ... xneL xneH
					{
						int n = recBytes[i + 2]; // Integer.valueOf(receivedBytes[i
													// + 2], 16);

						i += 3 + n * 2;
						continue;
					}

					case 0x6b: // GS k m d1..dk NUL
					{
						for (int x = i + 2; x < recBytes.length; x++) {
							int m = recBytes[x]; // Integer.valueOf(receivedBytes[x],
													// 16);
							if (m == 0x00) {
								i = m;
								continue;
							}
						}
						break;
					}

					case 0x76: // GS v 0 m xL xH yL yH d1...dk
					{
						int xL = changeByteToInt(recBytes[i + 4]);
						int xH = changeByteToInt(recBytes[i + 5]);
						int yL = changeByteToInt(recBytes[i + 6]);
						int yH = changeByteToInt(recBytes[i + 7]);
						int k = (xL + xH * 256) * (yL + yH * 256);

						// 绘图
						this.drawImage((xL + xH * 256), (yL + yH * 256), i + 8,
								recBytes); // i
											// +
											// 8

						i += 8 + k;

						for (int s = i; s < recBytes.length; s++) {
							if (recBytes[s] == 0x1D && recBytes[s + 1] == 0x76) {
								i = s - 1;
								break;
							}
						}

						continue;
					}

					}
				} else if (currentByte < 0x20) // 控制符且非命令：忽略不处理
				{
					switch (currentByte) {
					case 0x10: // DLE EOT n
					case 0x05: // DLE ENQ n
					{
						i += 3;
						continue;
					}

					case 0x14: // DLE DC4 n m t
					{
						i += 5;
						continue;
					}

					default: {
						continue;
					}
					}
				} else if (currentByte > 0x7f) // 中文 ： 两字节转可视符
				{
					/*
					 * byte nb = Byte.valueOf(receivedBytes[i + 1], 16); byte[]
					 * ba = new byte[] { currentByte, nb }; String str=new
					 * String(ba, "GB2312"); this.editTextLines.append(str);
					 */
					i++;
				} else {
					/*
					 * byte[] ba = new byte[] { currentByte }; String str=new
					 * String(ba); this.editTextLines.append(str);
					 */
				}
			}
			// }

			return true;
		} catch (Exception ex) {
			// ex.printStackTrace();
			Log.i("gg", ex.getMessage());
			return false;
		}
	}

	private int changeByteToInt(byte b) {
		if (b >= 0) {
			return (int) b;
		} else {
			return 256 + b;
		}
	}

	private void drawImage(int widthPix, int heightPix, int idx, byte[] vals) {
		int len, wx = widthPix * 8;
		int b;
		String str2;

		Bitmap bmp = Bitmap.createBitmap(wx, heightPix, Bitmap.Config.RGB_565); // (wx,
																				// heightPix);
		Canvas canvas = new Canvas(bmp);
		canvas.clipRect(0, 0, wx, heightPix);
		canvas.drawColor(Color.WHITE);

		try {
			for (int h = 0; h < heightPix; h++) {
				for (int w = 0; w < widthPix; w++) {
					wx = w * 8;

					b = changeByteToInt(vals[idx++]); // Integer.valueOf(vals[idx++],
														// 16);

					if (b != 0) {
						str2 = Integer.toBinaryString(b);
						len = 8 - str2.length();
						wx += len;

						for (int p = 0; p < str2.length(); p++) {
							if (str2.substring(p, p + 1).equals("1")) {
								bmp.setPixel(wx + p, h, Color.BLACK);
							}
						}
					}
				}
			}

			String file = Environment.getExternalStorageDirectory()
					+ "/tmp/pic" + String.valueOf(idx_bmp) + ".bmp";
			// Log.i("gg", "file:" + file);
			idx_bmp++;
			File f = new File(file);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			/*
			 * if (f.isFile() && f.exists()) { f.delete(); }
			 */
			CompressFormat format = Bitmap.CompressFormat.JPEG;
			int quality = 100;
			OutputStream stream = null;

			try {
				stream = new FileOutputStream(f);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Log.i("gg", e.getMessage());
			}

			bmp.compress(format, quality, stream);
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i("gg", e.getMessage());
		}
	}

	private String mergeImages(Context context, TessOCR mTessOCR,
			MachineEntity machineInfo) {
		if (idx_bmp <= 0)
			return "";

		Bitmap bmp;
		File f;
		String file, result = "";
		int preHeight = 0, totalHeight = 0, width = 0;
		int file_idx = 0, len = idx_bmp;
		// int mergeImageIndexFrom = 3, mergeImagedIndexTo = 2; // 20;
		// 从倒数第x张，到倒数第y张有效

		// 只取后边20幅图片合成
		// if (idx_bmp > mergeImageIndexFrom) {
		// file_idx = idx_bmp - mergeImageIndexFrom;
		// len = mergeImageIndexFrom - mergeImagedIndexTo + 1;
		// }

		Bitmap[] bmps = new Bitmap[len];
		// idx_bmp = 0;

		try {
			for (int i = 0; i < bmps.length; i++) {
				file = Environment.getExternalStorageDirectory() + "/tmp/pic"
						+ String.valueOf(file_idx++) + ".bmp";
				f = new File(file);
				if (f.isFile() && f.exists()) {
					bmps[i] = BitmapFactory.decodeFile(file);

					if (width < bmps[i].getWidth())
						width = bmps[i].getWidth();
					totalHeight += bmps[i].getHeight();
				}
			}

			if (width <= 0 || totalHeight <= 0)
				return "";

			bmp = Bitmap.createBitmap(width, totalHeight,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			canvas.clipRect(0, 0, width, totalHeight);
			canvas.drawColor(Color.WHITE);

			Paint paint = new Paint();
			paint.setARGB(255, 0, 0, 0);
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
			paint.setColorFilter(cf);

			for (int i = 0; i < bmps.length; i++) {
				if (bmps[i] != null) {
					canvas.drawBitmap(bmps[i], 0, preHeight, paint);
					canvas.save(Canvas.ALL_SAVE_FLAG);
					canvas.restore();

					preHeight += bmps[i].getHeight();
				}
			}

			file = Environment.getExternalStorageDirectory() + "/tmp/picx.bmp";
			f = new File(file);
			if (f.isFile() && f.exists()) {
				f.delete();
			}
			CompressFormat format = Bitmap.CompressFormat.JPEG;
			int quality = 100;
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Log.i("gg", e.getMessage());
			}

			bmp.compress(format, quality, stream);

			try {
				stream.flush();
				int x1 = 96, y1 = -480, x2 = 284, y2 = -358;
				String keyword = "合计";
				//
				if (machineInfo != null) {
					if (machineInfo.isUploadinvoiceformat()) {
						upload(context, upPhoto_url, file,
								GetUtil.getimei(context), "");
					}
					x1 = machineInfo.getX1();
					y1 = machineInfo.getY1();
					x2 = machineInfo.getX2();
					y2 = machineInfo.getY2();
					keyword = machineInfo.getKeyword();
				}
				Bitmap bitmap = Bitmap.createBitmap(bmp, x1, bmp.getHeight()
						+ y1, x2 - x1, y2 - y1);

				String _result = mTessOCR.getOCRResult(bitmap);
				Log.i("gg", "result1:" + _result);

				int idx = _result.indexOf(keyword);
				if (idx > 0 && _result.length() >= idx + keyword.length()) {
					_result = _result.substring(idx + keyword.length())
							.replace((char) (58), (char) (32))
							.replace((char) 36, (char) 32)
							.replace((char) 165, (char) 32)
							.replace((char) 44, (char) 32).replace("，", "")
							.replace(",", "").replace(" ", "").trim();
					Log.i("gg", "_result:" + _result);
					idx = _result.indexOf(13);
					if (idx > 0) {
						_result = _result.substring(0, idx).trim();
					} else {
						idx = _result.indexOf(10);
						if (idx > 0) {
							_result = _result.substring(0, idx).trim();
						}
					}
					char ca;
					for (int i = 0; i < _result.length(); i++) {
						ca = _result.charAt(i);
						if (ca >= 48 && ca <= 57 || ca == 46) {
							result += _result.charAt(i);
						} else if (i > 0) {
							break;
						}
					}
				} else {
					String ch;

					for (int i = 0; i < _result.length(); i++) {
						ch = _result.substring(i, i + 1);
						if (ch.compareTo("0") >= 0 && ch.compareTo("9") <= 0
								|| ch.equals(".") || ch.equals("．")) {
							if (ch.equals("．"))
								ch = ".";
							result += ch;
						}
					}
				}

				Log.i("gg", "result2:" + result);

				String file2 = Environment.getExternalStorageDirectory()
						+ "/tmp/picxx.bmp";
				// if (machineInfo.isUploadinvoiceformat()) {
				// upload(upPhoto_url, file2, GetUtil.getimei(getActivity()),
				// "2");
				// }
				File f2 = new File(file2);
				if (f2.isFile() && f2.exists()) {
					f2.delete();
				}
				OutputStream stream2 = null;
				try {
					stream2 = new FileOutputStream(file2);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					Log.i("gg", e.getMessage());
				}

				bitmap.compress(format, quality, stream2);

			} catch (IOException e) {
				// e.printStackTrace();
				Log.i("gg", e.getMessage());
			}
			try {
				stream.close();
			} catch (IOException e) {
				// e.printStackTrace();
				Log.i("gg", e.getMessage());
			}catch(Exception ex)
			{
				
			}
			return result;
		} catch (Exception e) {
			// e.printStackTrace();
			e.printStackTrace();
			Log.i("bmp:", e.getMessage());
			return "";
		}
	}

	private void upload(Context context, String url, String path, String sn,
			String flag) {
		Client client = new Client(context);

		Call call = client.upload(url, path, sn, flag);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					Log.i("gg", "upload:" + arg1.toString());
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					Log.i("gg", "upload:" + res_info);
				}
			});
		} else {
			T.showCenter("网络异常!");
		}
	}

	/*
	 * decode string and get amount
	 */
	public String decodeString(byte[] recBytes, int startIndex) {
		String ret = "";

		try {
			for (int i = startIndex; i < recBytes.length; i++) {
				int currentByte = recBytes[i];

				// L.i(MyFunc.Byte2Hex(recBytes[i]));

				if (currentByte == 0x1b) { // ESC
					// 解释命令
					int nextByte = recBytes[i]; // Integer.valueOf(receivedBytes[i
												// + 1], 16);
					switch (nextByte) {
					case 0x40: { // initialize printer
						// i += 1;
						continue;
					}
					case 0x21: // ESC ! N
					case 0x4d: // ESC M n
					case 0x2d: // ESC - n
					case 0x45: // ESC E n
					case 0x47: // ESC G n
					case 0x33: // ESC 3 n
					case 0x20: // ESC SP n
					case 0x25: // ESC % n
					case 0x3d: // ESC = n
					case 0x3f: // ESC ? n
					case 0x52: // ESC R n
					case 0x54: // ESC T n
					case 0x56: // ESC V n
					case 0x61: // ESC a n
					case 0x74: // ESC t n
					case 0x7b: // ESC { n
					case 0x2b: // ESC + n
					case 0x2f: // ESC / c
					{
						i += 2;
						continue;
					}

					case 0x24: // ESC $ nL nH
					case 0x5c: // ESC \ nL nH
					case 0x63: // ESC c 3 n / ESC c 4 n / ESC c 5 n
					case 0x42: // ESC B n t
					{
						i += 3;
						continue;
					}

					case 0x70: // ESC p m t1 t2
					case 0x43: // ESC C m t n
					{
						i += 4;
						continue;
					}
					case 0x28: {
						switch (recBytes[i + 2]) // Integer.valueOf(receivedBytes[i
													// + 2], 16))
						{
						case 0x55: // ESC ( U n1 n2 m
						{
							i += 5;
							continue;
						}
						default: // 0x43: //ESC ( C n1 n2 m1 m2
						{
							i += 6;
							continue;
						}
						}
					}
					case 0x2a: // ESC * m nL nH d1...dk
					{
						int m = recBytes[i + 2]; // Integer.valueOf(receivedBytes[i
													// + 2], 16);
						int nL = recBytes[i + 3]; // Integer.valueOf(receivedBytes[i
													// + 3], 16);
						// int nH = recBytes[i+4];
						// //Integer.valueOf(receivedBytes[i + 4], 16);

						if (m == 1)
							m = 8 / 8;
						else
							m = 24 / 8;

						i += 4 + nL * m;

						continue;
					}

					case 0x44: // ESC D n1...nk NUL
					{
						for (int x = i + 2; x < recBytes.length; x++) {
							int m = recBytes[x]; // Integer.valueOf(receivedBytes[x],
													// 16);

							if (m == 0x00) {
								i = m - 1;
								continue;
							}
						}
						break;
					}

					case 0x57: // ESC W xL xH yL yH dxL dxH dyL dyH
					{
						i += 9;
						continue;
					}
					}
				} else if (currentByte == 0x1c) { // FS
					// 解释命令
					int nextByte = recBytes[i + 1]; // Integer.valueOf(receivedBytes[i
													// + 1], 16);

					switch (nextByte) {
					case 0x26: // FS & //chinese mode
					case 0x2e: // FS .
					{
						// i += 1;
						continue;
					}
					case 0x21: // FS ! n
					case 0x2d: // FS - n
					case 0x57: // FS W n

					{
						i += 2;
						continue;
					}
					case 0x53: // FS S n1 n2
					{
						i += 3;
						continue;
					}
					case 0x32: // FS 2 c2 c2 d1..dk
					{
						i += 3 + 72;
						continue;
					}
					case 0x70: // FS p n m
					{
						i += 3;
						continue;
					}
					// case 0x71: //FS q n [xL xH yL yH d1...dk]1...pxL xH yL yH
					// d2..dk]n
					// {

					// continue;
					// }
					}
				} else if (currentByte == 0x1d) // GS
				{
					// 解释命令
					int nextByte = recBytes[i + 1]; // Integer.valueOf(receivedBytes[i
													// + 1], 16);

					switch (nextByte) {
					case 0x3a: // GS :
					{
						i += 1;
						continue;
					}

					case 0x21: // GS ! n
					case 0x42: // GS B n
					case 0x68: // GS h n
					case 0x77: // GS w n
					case 0x48: // GS H n
					case 0x66: // GS f n
					case 0x2f: // GS / m
					case 0x61: // GS a n
					case 0x72: // GS r n
					{
						i += 2;
						continue;
					}

					case 0x56: // GS V m / GS V m n
					{
						int m = recBytes[i + 2]; // Integer.valueOf(receivedBytes[i
													// + 2], 16);
						if (m < 66)
							i += 2;
						else
							i += 3;
						continue;
					}
					case 0x24: // GS $ nL nH
					case 0x4c: // GS L nL nH
					case 0x50: // GS P x y
					case 0x57: // GS W nL nH
					case 0x5c: // GS \ nL nH
					{
						i += 3;
						continue;
					}

					case 0x5e: // GS ^ r t m
					{
						i += 4;
						continue;
					}

					case 0x28: // GS ( A pL pH n m
					{
						i += 6;
						continue;
					}

					case 0x27: // GS ' n x1sL x1sH ... xneL xneH
					{
						int n = recBytes[i + 2]; // Integer.valueOf(receivedBytes[i
													// + 2], 16);

						i += 2 + n * 2;
						continue;
					}

					case 0x6b: // GS k m d1..dk NUL
					{
						for (int x = i + 2; x < recBytes.length; x++) {
							int m = recBytes[x]; // Integer.valueOf(receivedBytes[x],
													// 16);
							if (m == 0x00) {
								i = m - 1;
								continue;
							}
						}
						break;
					}

					case 0x76: // GS v 0 m xL xH yL yH d1...dk
					{
						int xL = changeByteToInt(recBytes[i + 4]);
						int xH = changeByteToInt(recBytes[i + 5]);
						int yL = changeByteToInt(recBytes[i + 6]);
						int yH = changeByteToInt(recBytes[i + 7]);
						int k = (xL + xH * 256) * (yL + yH * 256);

						// 绘图
						this.drawImage((xL + xH * 256), (yL + yH * 256), i + 8,
								recBytes); // i
											// +
											// 8

						i += 7 + k;

						for (int s = i; s < recBytes.length; s++) {
							if (recBytes[s] == 0x1D && recBytes[s + 1] == 0x76) {
								i = s - 1;
								break;
							}
						}

						continue;
					}
					}
				} else if (currentByte >= 0 && currentByte < 0x20) { // 控制符且非命令：忽略不处理
					switch (currentByte) {
					case 0x10: // DLE EOT n
					case 0x05: // DLE ENQ n
					{
						i += 2;
						continue;
					}

					case 0x14: // DLE DC4 n m t
					{
						i += 4;
						continue;
					}

					default: {
						continue;
					}
					}
				} else {

					if (currentByte > 0x7f || currentByte < 0) { // 中文 ： 两字节转可视符
						byte nb = recBytes[i + 1];
						byte[] ba = new byte[] { (byte) currentByte, nb };
						ret += new String(ba, "GB2312");
						L.i(ret);
						i++;
					} else if (currentByte >= 0x20 && currentByte < 0x7f) {
						byte[] ba = new byte[] { (byte) currentByte };
						ret += new String(ba, "GB2312");
					}
				}

			}

			return ret;
		} catch (Exception ex) {
			// ex.printStackTrace();
			L.i("gg", ex.getMessage());
			return "";
		}
	}

	/********
	 * @detail 获取打印数据中的金额,如果解析不到金额，则返回-1
	 * @param keyword
	 *            后台设置的关键词
	 * @param s
	 *            解析的内容
	 * @return
	 */
	public double getTotalAmount(String keyword, String s) {
		if (s == null || keyword == null || "".equals(s)
				|| s.indexOf(keyword) == -1) {
			return -1;
		}
		// 1.查找到关键词的位置
		int index = s.indexOf(keyword);
		// 2.截取关键词之后的字符串,并去除所有的空格字符
		s = s.substring(index + keyword.length(), s.length()).replaceAll(" ",
				"").replace((char) (58), (char) 32)
				.replace((char) 36, (char) 32)
				.replace((char) 165, (char) 32)
				.replace((char) 44, (char) 32).replace(":", "").replace("：", "").replace(",","" ).replace("，", "").trim();
		// 3.获取截取到字符串之后的第一组数字（位置第一个）
		/*
		 * Pattern p=Pattern.compile("([0-9.]+)"); Matcher m=p.matcher(s);
		 * if(m.find()){ return Float.parseFloat(m.group(0)); }
		 */

		String dg = "", c;

		for (int i = 0; i < s.length(); i++) {
			c = s.substring(i, i + 1);
			if (c.compareTo("0") >= 0 && c.compareTo("9") <= 0
					|| c.compareTo(".") == 0) {
				dg += c;
			} else if (c.compareTo(",") == 0) {
			} else {
				break;
			}
		}
		return Double.valueOf(dg);
	}
}
