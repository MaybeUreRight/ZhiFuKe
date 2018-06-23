package com.weilay.pos.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.framework.utils.TimeUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.OperatorEntity;
import com.weilay.pos.zxing.RGBLuminanceSource;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class GetUtil {

	private static WifiManager wifiManager;
	private static WifiInfo currentWifiInfo;
	private static final String[] ZXING_URLS = { "http://zxing.appspot.com/scan", "zxing://scan/" };

	public static String getimei() {
		return getimei(WeiLayApplication.app);
	}

	/**
	 * 获取设备编码
	 * 
	 * @param context
	 * @return
	 */
	static public String getimei(Context context) {
		// return "weilay007";
		String SerialNumber = android.os.Build.SERIAL;
		if (SerialNumber == null || SerialNumber.length() <= 0) {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			currentWifiInfo = wifiManager.getConnectionInfo();
			SerialNumber = currentWifiInfo.getMacAddress().replace(":", "").toUpperCase();
			if (SerialNumber == null || SerialNumber.length() <= 0) {
				SerialNumber = CmdForAndroid.shellReturn();
				if (StringUtil.isEmpty(SerialNumber)) {
					DialogUtil.dialog_(context, 9, 5, Gravity.CENTER, "非法设备!").show();
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							CmdForAndroid.shella("su", "reboot -p");
						}
					}, 10000);
				} else {
					return SerialNumber;
				}
			} else {
				return SerialNumber;
			}
		}
		return SerialNumber;
	}

	static public String intString2Hex(String intStr) {
		Long l = Long.valueOf(intStr);
		return Long.toHexString(l).toUpperCase();
	}

	public static boolean isZXingURL(String dataString) {
		if (dataString == null) {
			return false;
		}
		for (String url : ZXING_URLS) {
			if (dataString.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 订单号 mid+operator+data(ms)+4bit random
	 * 
	 * @return
	 */
	static public String getOutTradeNo() {
		OperatorEntity operator = Utils.getCurOperator();
		if (operator == null) {
			L.e("你还没有登陆");
			return "";
		}
		String dateStr = TimeUtils.getNowTime(new SimpleDateFormat("yyyyMMddHHmmssSSS"));
		Random r = new Random();
		String randomInt = "";
		for (int i = 1; i < 4; i++) {
			randomInt += r.nextInt(9);
		}
		return dateStr + operator.getMid() + operator.getOperator() + randomInt;
	}

	/**
	 * 获取系统时间 yyyy年MM月dd日 HH:mm:ss
	 * 
	 * @return
	 */
	static public String gettime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date(ServerTimeUtils.getServerTime());// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	/**
	 * 
	 * @param howlong
	 *            传0为当天
	 * @return
	 */
	public static String getTimeHowLong(int howlong) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date curDate = new Date(ServerTimeUtils.getServerTime() - (howlong * 24 * 60 * 60 * 1000));// 获取当前时间
		String time = formatter.format(curDate);
		return time;
	}

	/**
	 * 退款订单号
	 * 
	 * @return
	 */
	static public String getRefundOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		String randomInt = "";
		for (int i = 1; i < 6; i++) {
			randomInt += r.nextInt(9);

		}
		key = key + randomInt;
		key = key.substring(0, 15);
		L.i("gg", "退款订单号:" + key);
		return key;
	}

	/**
	 * 发送cmd指令
	 * 
	 * @param command
	 * @return
	 */
	static public String execCommand(String command) {
		Runtime mRuntime = Runtime.getRuntime();
		StringBuffer mRespBuff = new StringBuffer();
		try {
			// Process中封装了返回的结果和执行错误的结果
			Process mProcess = mRuntime.exec(command);
			BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));

			char[] buff = new char[1024];
			int ch = 0;
			while ((ch = mReader.read(buff)) != -1) {
				mRespBuff.append(buff, 0, ch);
			}
			mReader.close();
			//
			// L.i("gg", "cmd:" + mRespBuff.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mRespBuff.toString();
	}

	/**
	 * 获取服务器版本信息
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String getString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "gbk");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取本地版本号
	 * 
	 * @param context
	 * @return
	 */
	static public int getversionCode(Context context) {
		int appVersion = 0;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

			appVersion = info.versionCode; //
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appVersion;
	}

	/**
	 * 获取本地版本名称
	 * 
	 * @return
	 */
	public static String getversionName(Context context) {
		String appVersion = null;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

			appVersion = info.versionName; //
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appVersion;
	}

	/**
	 * 获取sd卡路径
	 * 
	 * @return
	 */
	static public String getpath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = Environment.getExternalStorageDirectory();
			L.i("gg", "path:" + file.getPath());
			return file.getPath();
		}
		return null;
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	static public String getFileName(String path) {
		int separatorIndex = path.lastIndexOf("/");
		return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
	}

	/**
	 * 获取最近时间 yyyy-MM-dd 只获取年月日 0为传当天
	 */
	static public String getTimenow(int howlong) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, howlong);
		String day = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return day;
	}

	// static public String
	/**
	 * 获取最近时间 yyyy-MM-dd HH:mm:ss 获取年月日 时分秒
	 */
	static public String getTime_HH_MM_SS(int howlong) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, howlong);
		String day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		return day;
	}

	/**
	 * 获取星期时间
	 * 
	 * @param date
	 * @return
	 */
	static public String getWeek(String date) {
		String Week = "星期";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 也可将此值当参数传进来
		try {
			c.setTime(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			Week += "日";
			break;
		case 2:
			Week += "一";
			break;
		case 3:
			Week += "二";
			break;
		case 4:
			Week += "三";
			break;
		case 5:
			Week += "四";
			break;
		case 6:
			Week += "五";
			break;
		case 7:
			Week += "六";
			break;
		default:
			break;
		}
		return Week;

	}

	/** * 检测Android设备是否支持摄像机 */
	static public boolean checkCameraDevice(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	// 设置请求进度的文字大小
	static public void setDialogText(View v) {

		if (v instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) v;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = parent.getChildAt(i);
				setDialogText(child);
			}
		} else if (v instanceof TextView) {
			((TextView) v).setTextSize(30); // 是textview，设置字号
		}
	}

	// 图片转字节数组
	static public byte[] bitmapToByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] a = out.toByteArray();
		return a;
	}

	static public void closeSystemInput(EditText edit, Activity act) {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			edit.setInputType(InputType.TYPE_NULL);
		} else {
			act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(edit, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static public Result ResolvePhoto(Bitmap bitmap) {
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();

		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChecksumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
