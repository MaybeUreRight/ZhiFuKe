package org.rxwu.helper.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.framework.utils.ConvertUtil;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.Log;

/**
 * Created by rxwu on 2016/4/8.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:打印内容实体封装类
 */
public final class PrintEntity {
	private PrintEntity print;
	public static byte[] PRINT_CMD = null;// 打印
	public static byte[] SWAP_CMD = null;// 换行
	public static final int BIG_FONT = 1;
	public static final int NORMAL_FONT = 2;
	public static final int SMALL_FONT = 3;
	private static int CURRENT_FONT = NORMAL_FONT;// 当前显示的字体
	private int MAX_WORLDS = 42;// 设置一行显示多少个字符

	public List<byte[]> contents = new ArrayList<byte[]>();

	/******
	 * @Detail 无参构造函数，默认每行显示42个字符
	 */
	public PrintEntity() {
		print = this;
		try {
			PRINT_CMD = "[print]".getBytes();
			SWAP_CMD = "[swap_line]".getBytes();
		} catch (Exception e) {
			Log.d("PrintEntity->init",
					e.getMessage() + ":" + e.getLocalizedMessage());
		}
	}

	/*******
	 * @detail 构造方法
	 * @param MAX_WORLD
	 *            设置打印机每行可显示的条数
	 */
	public PrintEntity(int MAX_WORLD) {
		print = this;
		if (MAX_WORLD > 0 && MAX_WORLD < 1000) {
			this.MAX_WORLDS = MAX_WORLD;
		}
		try {
			PRINT_CMD = "[print]".getBytes();
			SWAP_CMD = "[swap_line]".getBytes();
		} catch (Exception e) {
			Log.d("PrintEntity->init",
					e.getMessage() + ":" + e.getLocalizedMessage());
		}
	}

	/************
	 * @param font
	 * @detail 设置字体风格
	 */
	public PrintEntity setFont(int font) {
		CURRENT_FONT = font;
		switch (font) {
		case BIG_FONT:
			bigFont();
			break;
		case SMALL_FONT:
			break;
		case NORMAL_FONT:
			normalFont();
			break;
		}
		return print;
	}

	/*****
	 * @detail 新增内容并且换行
	 * @param content添加的新的内容
	 * @return
	 */
	public PrintEntity addLn(String content) {
		try {
			contents.add(content.getBytes("GB2312"));
			contents.add(SWAP_CMD);
		} catch (Exception ex) {
			Log.d("PrintEntity->addLn",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/*****
	 * @param content
	 *            打印的内容
	 * @return 返回打印内容的容器
	 * @detail 将添加的内容居中显示
	 */
	public PrintEntity addCenter(String content) {
		try {
			int length = content.getBytes("GB2312").length;
			if (length < MAX_WORLDS) {
				// 在左边补充空格
				for (int i = 0; i < ((MAX_WORLDS - length) / 2); i++) {
					content = " " + content;
				}
			}
			contents.add(content.getBytes("GB2312"));
			contents.add(SWAP_CMD);
		} catch (Exception ex) {
			Log.d("PrintEntity->addCenter",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/********
	 * @return 打印内容的容器
	 * @detail 添加打印的内容到右边
	 */
	public PrintEntity addR(String right) {
		try {
			int rightLength = right.getBytes("GB2312").length;
			int length = MAX_WORLDS - rightLength;
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					right = " " + right;
				}
				contents.add(right.getBytes("GB2312"));
			}
			contents.add(SWAP_CMD);
		} catch (Exception ex) {
			Log.d("PrintEntity->addR",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/********
	 * @return 返回打印内容的容器
	 * @detail 将内容按左，中，右添加
	 */
	public PrintEntity addLCR(String left, String center, String right) {
		try {
			int leftLength = left.getBytes("GB2312").length;
			int rightLength = right.getBytes("GB2312").length;
			int centerLength = center.getBytes("GB2312").length;
			int length = MAX_WORLDS / 3;
			if (length > 0) {
				for (int i = 0; i < (length - leftLength); i++) {
					left = left + " ";
				}
				contents.add(left.getBytes("GB2312"));

				for (int i = 0; i < ((length - centerLength) / 2); i++) {
					center = " " + center + " ";
				}
				contents.add(center.getBytes("GB2312"));
				for (int i = 0; i < (length - rightLength); i++) {
					right = " " + right;
				}
				contents.add(right.getBytes("GB2312"));
			}
			contents.add(SWAP_CMD);
		} catch (Exception ex) {
			Log.d("PrintEntity->addLCR",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/********
	 * @return
	 * @detail 将内容按左，右居中对齐添加
	 */
	public PrintEntity addLR(String left, String right) {
		try {
			int leftLength = left.getBytes("GB2312").length;
			int rightLength = right.getBytes("GB2312").length;
			int length = MAX_WORLDS - (leftLength + rightLength);
			if (length > 0) {
				contents.add(left.getBytes("GB2312"));
				for (int i = 0; i < length; i++) {
					right = " " + right;
				}
				contents.add(right.getBytes("GB2312"));
			}
			contents.add(SWAP_CMD);
		} catch (Exception ex) {
			Log.d("PrintEntity->addLR",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/******
	 * @detail 画分割线。默认为------分割
	 * @return
	 */
	public PrintEntity addLine() {
		StringBuffer lines = new StringBuffer();
		for (int i = 0; i < MAX_WORLDS; i++) {
			lines.append("-");
		}
		try {
			contents.add(lines.toString().getBytes("GB2312"));
			contents.add(SWAP_CMD);
		} catch (UnsupportedEncodingException ex) {
			Log.d("PrintEntity->addLine",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/*****
	 * @detail 画分割线
	 * @param style
	 *            分割字符，默认为“－”
	 * @return
	 */
	public PrintEntity addLine(char style) {
		StringBuffer lines = new StringBuffer();
		for (int i = 0; i < MAX_WORLDS; i++) {
			lines.append((style != 0) ? ("" + style) : "-");
		}
		try {
			contents.add(lines.toString().getBytes("GB2312"));
			contents.add(SWAP_CMD);
		} catch (UnsupportedEncodingException ex) {
			Log.d("PrintEntity->addLine",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/*****
	 * @detail 开始的指令
	 */
	public static byte[] starts = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x1B, 0x40, 0x1B, 0x33, 0x00 };

	/*****
	 * @Detail 结束的指令
	 * 
	 */
	public static byte[] end = { 0x1d, 0x4c, 0x1f, 0x00 };
	
	public static byte[] center = { 0x1b, 0x61, 0x31 };//居中
	public static byte[] left = { 0x1b, 0x61, 0x30 };//左对齐
	public static byte[] right = { 0x1b, 0x61, 0x32 };//右对齐

	/*******
	 * @Detail 打印图片
	 * @param bit
	 * @return
	 */
	public PrintEntity pic2PxPoint(Bitmap bit, ALIGN align) {
		if (bit == null) {
			return print;
		}
		setAlign(align);
		try {
			contents.add(starts);
			contents.add(center);
			contents.add(ConvertUtil.draw2PxPoint(bit));
			contents.add(end);
			resetPrinter();
		} catch (Exception ex) {
			Log.d("PrintEntity->addLine",
					ex.getMessage() + ":" + ex.getLocalizedMessage());
		}
		return print;
	}

	/*******
	 * 换行方法
	 * 
	 * @return
	 */
	public PrintEntity swapLine() {
		contents.add(SWAP_CMD);
		return print;
	}

	/*******
	 * 换行方法
	 * 
	 * @param num
	 *            换几行
	 * @return
	 */
	public PrintEntity swapLine(int num) {
		for (int i = 0; i < num; i++) {
			contents.add(SWAP_CMD);
		}
		return print;
	}

	/*
	 * @detail 放大字体
	 */
	private byte[] bigFont() {

		byte[] b = new byte[3];
		b[0] = 0x1B;
		b[1] = 0x21;
		b[2] = 0x38;
		contents.add(b);
		b[0] = 0x1C;
		b[1] = 0x21;
		b[2] = 0x08;
		contents.add(b);
		return b;
	
	}

	/*
	 * @detail 常规字体
	 */
	private void normalFont() {
		byte[] b = {0x1d, 0x21, 0x00};
//		b[0] = 0x1D;
//		b[1] = 0x21;
//		b[2] = 0x00;
//		contents.add(b);
//		b[0] = 0x1C;
//		b[1] = 0x21;
//		b[2] = 0x00;
		contents.add(b);
	}

	/*
	 * @detail 复位打印机
	 */
	private byte[] resetPrinter() {
		byte[] b = new byte[2];
		b[0] = 0x1B;
		b[1] = 0x40;
		contents.add(b);
		return b;
	}

	public enum ALIGN {
		LEFT, CENTER, RIGHT
	}

	/******
	 * @detail 设置打印居中的方式
	 * @param align
	 */
	private void setAlign(ALIGN align) {
		byte[] result = new byte[3];
		switch (align) {
		case LEFT:
			result[0] = 27;
			result[1] = 97;
			result[2] = 0;
			contents.add(result);
			break;
		case CENTER:
			result[0] = 27;
			result[1] = 97;
			result[2] = 1;
			contents.add(result);
			break;
		case RIGHT:
			result[0] = 27;
			result[1] = 97;
			result[2] = 2;
			contents.add(result);
			break;
		}
	}

	/****
	 * @Detail 打印图片,二维码
	 * @param orientation
	 *            1:left,2:center,3:right
	 * @size
	 * @return
	 */
	public PrintEntity add2DCode(String qrcode, ALIGN orientation) {
		try {
			swapLine();
			setAlign(orientation);
			contents.add("      ".getBytes("gb2312"));
			// 控制居中
			byte moduleSize = 8;
			String encoding = "GBK";
			int length = qrcode.getBytes(encoding).length;
			byte b = (byte) (length + 3);
			// 打印二维码矩阵
			contents.add(new byte[] { 0x1D });// init
			contents.add("(k".getBytes());// adjust height of barcode
			contents.add(new byte[] { b }); // pl
			contents.add(new byte[] { 0 }); // ph
			contents.add(new byte[] { 49 }); // cn
			contents.add(new byte[] { 80 }); // fn
			contents.add(new byte[] { 48 }); //
			contents.add(qrcode.getBytes());

			contents.add(new byte[] { 0x1D });
			contents.add("(k".getBytes());
			contents.add(new byte[] { 3 });
			contents.add(new byte[] { 0 });
			contents.add(new byte[] { 49 });
			contents.add(new byte[] { 69 });
			contents.add(new byte[] { 48 });

			contents.add(new byte[] { 0x1D });
			contents.add("(k".getBytes());
			contents.add(new byte[] { 3 });
			contents.add(new byte[] { 0 });
			contents.add(new byte[] { 49 });
			contents.add(new byte[] { 67 });
			contents.add(new byte[] { moduleSize });

			contents.add(new byte[] { 0x1D });
			contents.add("(k".getBytes());
			contents.add(new byte[] { 3 }); // pl
			contents.add(new byte[] { 0 }); // ph
			contents.add(new byte[] { 49 }); // cn
			contents.add(new byte[] { 81 }); // fn
			contents.add(new byte[] { 48 }); // m
		} catch (Exception ex) {
		}
		setFont(CURRENT_FONT);// 恢复当前字体
		contents.add(SWAP_CMD);
		setAlign(orientation);
		swapLine();
		return print;
	}

	public List<byte[]> getContents() {
		return contents;
	}
}
