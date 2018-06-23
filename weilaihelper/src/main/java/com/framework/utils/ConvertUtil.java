package com.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by rxwu on 2015/11/4.
 */
public class ConvertUtil {

	private static Field[] getFiled(Class cls) {
		if (cls == null)
			return null;
		Field[] fields = cls.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		Class superClass = cls.getSuperclass();
		if (superClass != null) {
			Field[] superFiled = cls.getSuperclass().getDeclaredFields();
			Field[] temp = new Field[superFiled.length + fields.length];
			for (int i = 0; i < temp.length; i++) {
				if (i < fields.length) {
					temp[i] = fields[i];
				} else {
					temp[i] = superFiled[i - fields.length];
				}
			}
			fields = temp;
		}
		return fields;
	}

	public static ContentValues convertEntityToContentValues(Object obj)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (obj == null) {
			Log.e("ConvertUtil->convertEntityToContentValues", ":转换实体失败,传入实体为空");
			return null;
		}
		Log.d("ConvertUtil->convertEntityToContentValues", ":开始转换实体" + obj.getClass().getSimpleName());
		Field[] fields = getFiled(obj.getClass());
		ContentValues values = new ContentValues();
		for (Field filed : fields) {

			String name = filed.getName(); // 获取属性的名字
			name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
			String type = filed.getGenericType().toString(); // 获取属性的类型
			Object value = null;
			try {
				Method m = obj.getClass().getMethod("get" + name);
				value = m.invoke(obj);
				switch (type) {
				case "class java.lang.String":
					values.put(name, (String) value);
					break;
				case "class java.lang.Integer":
				case "int":
					values.put(name, ((Integer) value == null ? 0 : (Integer) m.invoke(obj)));
					break;
				case "class java.lang.Long":
				case "long":
					values.put(name, ((Long) value == null) ? 0 : (Long) m.invoke(obj));
					break;
				case "class java.lang.Short":
					values.put(name, ((Short) value == null) ? 0 : (Short) m.invoke(obj));
					break;
				case "class java.lang.Double":
					values.put(name, ((Double) value == null) ? 0 : (Double) m.invoke(obj));
					break;
				case "class java.lang.Boolean":
				case "boolean":
					values.put(name, ((Boolean) value == null) ? false : (Boolean) m.invoke(obj));
					break;
				case "class java.com.weilai.pos.util.Date":
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					values.put(name, format.format((Date) value).toString());
					break;
				}
				Log.d("ConvertUtils", "---字段名：" + name + ";---类型" + type + "值为：" + value);
			} catch (NoSuchMethodException ex) {
				Log.e("ConvertUtil->convertEntityToContentValues", "实体转换过程中发生错误:" + ex.getMessage());
			}

		}
		return values;
	}

	/**
	 * 转换数字
	 * 
	 * @param str
	 * @return
	 */
	public static int parseInt(Object str) {
		if (str == null) {
			return 0;
		}
		try {
			return Integer.parseInt(str.toString());
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * 判断是否中文
	 * 
	 * @param str
	 * @return
	 */
	public static double parseFloat(Object str) {
		try {
			return Float.parseFloat(str.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 判断是否中文
	 * 
	 * @param str
	 * @return
	 */
	public static String parseString(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	public static final String MONEY_FORMAT = "#0.00";

	/**
	 * 将传入对象格式化为钱币字符串格式
	 * 
	 * @param str
	 * @return
	 */
	public static String parseMoney(Object str) {
		if (str == null)
			return "0.0元";
		String money = str.toString();
		if (money.indexOf("元") != -1) {
			money = money.replaceAll("元", "");
		}
		DecimalFormat format = new DecimalFormat(MONEY_FORMAT);
		try {
			Double fl = Double.parseDouble(money);
			String temp = format.format(fl);
			return temp;
		} catch (Exception e) {
			Log.e("ConvertUtil->parseMoney:", e.getMessage());
		}
		return "0.0";
	}

	/*******
	 * @detail 去除钱币符号
	 * @param str
	 * @return
	 */
	public static double getMoney(Object str) {
		try {
			return Double.parseDouble(parseMoney(str).replaceAll("元", ""));
		} catch (Exception ex) {
		}
		return 0;
	}

	/********
	 * @param datas
	 * @return
	 * @detail 可以将传入的Map转化成List数组的形式返回
	 */
	public static <T extends List> T convertMapToList(Map datas) {
		if (datas == null) {
			return (T) new ArrayList<Object>();
		}
		List listDatas = new ArrayList();
		Iterator iterator = datas.values().iterator();
		while (iterator.hasNext()) {
			listDatas.add(iterator.next());
		}
		return (T) listDatas;
	}

	public String intString2Hex(String value) {
		Long l = Long.valueOf(value);
		return Long.toHexString(l).toUpperCase();
	}

	public static String[] convertListToArray(List items) {
		if (items == null)
			return null;
		String[] datas = new String[items.size()];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = items.get(i).toString();
		}
		return datas;
	}

	/*****
	 * @Detail 将图像转换成打印矩阵
	 * @param bitmap
	 * @return
	 */
	public static byte[] convertImage(Bitmap bitmap) {
		/** 解析图片 获取打印数据 **/
		// bitmap = createBitmap(bitmap);

		byte[] bytes = null; // 打印数据
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		System.out.println("width=" + width + ", height=" + height);
		int heightbyte = (height - 1) / 8 + 1;
		int bufsize = width * heightbyte;
		int m1, n1;
		byte[] maparray = new byte[bufsize];

		byte[] rgb = new byte[3];

		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		/** 解析图片 获取位图数据 **/
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int pixel = pixels[width * j + i]; /** 获取ＲＧＢ值 **/
				int r = Color.red(pixel);
				int g = Color.green(pixel);
				int b = Color.blue(pixel);
				// System.out.println("i=" + i + ",j=" + j + ":(" + r + ","+ g+
				// "," + b + ")");
				rgb[0] = (byte) r;
				rgb[1] = (byte) g;
				rgb[2] = (byte) b;
				if (r != 255 || g != 255 || b != 255) {// 如果不是空白的话用黑色填充
														// 这里如果童鞋要过滤颜色在这里处理
					m1 = (j / 8) * width + i;
					n1 = j - (j / 8) * 8;
					maparray[m1] |= (byte) (1 << 7 - ((byte) n1));
				}
			}
		}
		byte[] b = new byte[322];
		int line = 0;
		int j = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		/** 对位图数据进行处理 **/
		for (int i = 0; i < maparray.length; i++) {
			b[j] = maparray[i];
			j++;
			if (j == 322) { /** 322图片的宽 **/
				if (line < ((322 - 1) / 8)) {
					byte[] lineByte = new byte[329];
					byte nL = (byte) 322;
					byte nH = (byte) (322 >> 8);
					int index = 5;
					/** 添加打印图片前导字符 每行的 这里是8位 **/
					lineByte[0] = 0x1B;
					lineByte[1] = 0x2A;
					lineByte[2] = 1;
					lineByte[3] = nL;
					lineByte[4] = nH;
					/** copy 数组数据 **/
					System.arraycopy(b, 0, lineByte, index, b.length);

					lineByte[lineByte.length - 2] = 0x0D;
					lineByte[lineByte.length - 1] = 0x0A;
					baos.write(lineByte, 0, lineByte.length);
					try {
						baos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					line++;
				}
				j = 0;
			}
		}
		bytes = baos.toByteArray();
		return bytes;
	}

	/****************************/

	/**
	 * 对图片进行压缩（去除透明度）
	 *
	 * @param bitmapOrg
	 */
	public static Bitmap compressPic(Bitmap bitmap) {
		// 获取这个图片的宽和高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 指定调整后的宽度和高度
		int newWidth = 240;
		int newHeight = 240;
		Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
		Canvas targetCanvas = new Canvas(targetBmp);
		targetCanvas.drawColor(0xffffffff);
		targetCanvas.drawBitmap(bitmap, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
		return targetBmp;
	}

	/**
	 * 灰度图片黑白化，黑色是1，白色是0
	 *
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 * @param bit
	 *            位图
	 * @return
	 */
	public static byte px2Byte(int x, int y, Bitmap bit) {
		if (x < bit.getWidth() && y < bit.getHeight()) {
			byte b;
			int pixel = bit.getPixel(x, y);
			int red = (pixel & 0x00ff0000) >> 16; // 取高两位
			int green = (pixel & 0x0000ff00) >> 8; // 取中两位
			int blue = pixel & 0x000000ff; // 取低两位
			int gray = RGB2Gray(red, green, blue);
			if (gray < 128) {
				b = 1;
			} else {
				b = 0;
			}
			return b;
		}
		return 0;
	}

	/**
	 * 图片灰度的转化
	 */
	private static int RGB2Gray(int r, int g, int b) {
		int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
		return gray;
	}

	/*************************************************************************
	 * 假设一个240*240的图片，分辨率设为24, 共分10行打印 每一行,是一个 240*24 的点阵, 每一列有24个点,存储在3个byte里面。
	 * 每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
	 **************************************************************************/
	/**
	 * 把一张Bitmap图片转化为打印机可以打印的字节流
	 *
	 * @param bmp
	 * @return
	 */
	public static byte[] draw2PxPoint(Bitmap bmp) {
		// 用来存储转换后的 bitmap 数据。为什么要再加1000，这是为了应对当图片高度无法
		// 整除24时的情况。比如bitmap 分辨率为 240 * 250，占用 7500 byte，
		// 但是实际上要存储11行数据，每一行需要 24 * 240 / 8 =720byte 的空间。再加上一些指令存储的开销，
		// 所以多申请 1000byte 的空间是稳妥的，不然运行时会抛出数组访问越界的异常。
		int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
		byte[] data = new byte[size];
		
		int k = 0;
		// 设置行距为0的指令
	/*	data[k++] = 0x1B;
		data[k++] = 0x33;
		data[k++] = 0x00;*/
		// 逐行打印
		for (int j = 0; j < bmp.getHeight() / 24f; j++) {
			// 打印图片的指令
			data[k++] = 0x1B;
			data[k++] = 0x2A;
			data[k++] = 33;
			data[k++] = (byte) (bmp.getWidth() % 256); // nL
			data[k++] = (byte) (bmp.getWidth() / 256); // nH
			// 对于每一行，逐列打印
			for (int i = 0; i < bmp.getWidth(); i++) {
				// 每一列24个像素点，分为3个字节存储
				for (int m = 0; m < 3; m++) {
					// 每个字节表示8个像素点，0表示白色，1表示黑色
					for (int n = 0; n < 8; n++) {
						byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
						data[k] += data[k] + b;
					}
					k++;
				}
			}
			data[k++] = 10;// 换行
		}
		return data;
	}
	
	/*******
	 * @Detail 将字符串根据 ASCIL码转成10进制
	 * @param str
	 * @return
	 */
	public static String str2ASCIL(String str) {
		if (str == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			buffer.append("" + (int) c);
		}
		return buffer.toString();
	}
}
