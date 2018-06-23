//package com.weilay.pos.util;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//
//import com.weilay.pos.client.WeiLayApplication;
//import com.weilay.pos.entity.AdvertisementQR;
//import com.weilay.pos.entity.CheckOutInfo;
//import com.weilay.pos.entity.MenDiandayin;
//import com.weilay.pos.entity.PrintPayInfo;
//import com.weilay.pos.entity.RefundPrint;
//import com.weilay.pos.entity.SendRedPackage;
//import com.weilay.pos.entity.ShiftRecord;
//import com.weilay.pos.entity.VerificationSheet;
//
//import android.content.SharedPreferences;
//import com_serialport_api.PrinterNative;
//
//public class SerialSendUtil {
//
//	private String weixin = "微信支付";
//	private String zhifubao = "支付宝";
//	private String baidu = "百度钱包";
//	private String heji = "合计";
//	private String title = "未莱科技信息有限公司";
//	private String message = "交班表";
//	private String equipment_coding = "设备编码:";
//	private String payment = "支付方式";
//	private String amount = "金额";
//	private SharedPreferences sp_port;
//	private String sn = "00000";
//	private static int paperLength = 46;// 纸张大小
//
//	public SerialSendUtil() {
//		sp_port = WeiLayApplication.getSp_port();
//		if (sp_port != null) {
//			sn = sp_port.getString("sn", "00000");
//			int item = sp_port.getInt("mPrintpaper", 0);// 值为0时纸张大小为80毫米(46字节),值为1时纸张大小为50毫米(32字节)
//			paperLength = (item == 0) ? 46 : 32;
//		}
//	}
//
//	public boolean printMenDian(MenDiandayin mddy) {
//		try {// RESET PRINTER
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//			// comMainSend.send(a);
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//
//			bs = (center("门店报表")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("门店报表时间:" + mddy.getDate()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing(weixin, mddy.getWeixin() + "元").getBytes("GB2312"));
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing(zhifubao, mddy.getZhifubao() + "元")
//					.getBytes("GB2312"));
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing("现金", mddy.getCash() + "元").getBytes("GB2312"));
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			String zj = mddy.getTotalAmount();
//			bs = (spacing(heji, zj + "元")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printShiftRecord(ShiftRecord sr) {
//		try {
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//			// comMainSend.send(a);
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bs = (center(message)).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			bs = ("交班时间:" + sr.getDate()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			bs = (equipment_coding + sn).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("操作员:" + sr.getOperator()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing(payment, amount)).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing(weixin, sr.getWeixin() + "元")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing(zhifubao, sr.getZhifubao() + "元")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing(baidu, sr.getBaidu() + "元")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			String zj = sr.getTotalamount();
//			bs = (spacing(heji, zj + "元")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printVerificationSheet(VerificationSheet vs) {
//		try {// RESET PRINTER
//			PrinterNative pn = new PrinterNative();
//
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bs = (center("核销单")).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("核销日期:" + vs.getCtime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing("设备编码:", vs.getSn())).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (spacing("核销单号:", vs.getCode())).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printCheckOut(CheckOutInfo co) {
//		try {
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bigFont(pn);
//			bs = (center(co.getTitle())).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = co.getTitle().getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("结账时间:" + co.getTxtime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("设备编码:" + co.getSn()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("交易方式:" + co.getPaytype()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("商户单号:" + co.getTx_no()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("金额:" + co.getTotalamountYuan() + "元").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//
//			}
//
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//
//	}
//
//	public boolean printRefund(RefundPrint rp) {
//		try {
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bigFont(pn);
//			bs = (center(rp.getTitle())).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("退款时间:" + rp.getTxtime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("设备编码:" + rp.getSn()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("退款方式:" + rp.getPaytype()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("商户单号:" + rp.getTx_no()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("退款单号:" + rp.getRefundno()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("退款金额:" + rp.getRefundamount() + "元").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printTest() {
//		try {// RESET PRINTER
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bs = ("打印测试开始!").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ").getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("abcdefghijklmnopqrstuvwxyz").getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("0123456789").getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("~!@#$%^&*()_+{}[]:<>?.,").getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			printQRCode("http://weixin.qq.com/r/7kOosPfEFKprrbPT9xb8", pn);
//
//			normalFont(pn);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bigFont(pn);
//			bs = ("打印测试结束!").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			normalFont(pn);
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (IOException e) {
//
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printAdvertisement( AdvertisementQR aqr) {
//		try {
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			
//
//			byte[] bs;
//
//			normalFont(pn);
//			pn.write(a, a.length);
//			bs = (aqr.getTopMessage()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			printQRCode(aqr.getQrCode(), pn);
//
//			bs = (aqr.getBottomMessage()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			normalFont(pn);
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//
//	}
//
//	public boolean printSendRedPackage( SendRedPackage srp) {
//		try {
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bigFont(pn);
//			bs = center("微信找零小票").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("设备编码:" + sn).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("找零订单号:" + srp.getTx_no()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("找零金额:" + srp.getAmount() + "元").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			printQRCode(srp.getQr_code(), pn);
//
//			normalFont(pn);
//			bs = ("交易时间:" + srp.getTrad_time()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("注意事项:").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("1.小票找零无记名,第一个扫码的人有效,请顾客自行保管。").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("2.请及时扫码收零,以免二维码模糊失效。").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public void printPic(PrintUtil comMainSend, byte[] pic) {
//
//	}
//
//	public boolean printPay(PrintPayInfo ppi) {
//		try {
//			PrinterNative pn = new PrinterNative();
//			byte[] a = line();
//
//			pn.write(a, a.length);
//			bigFont(pn);
//
//			byte[] bs;
//			bigFont(pn);
//			bs = center("支付单").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			normalFont(pn);
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("设备编码:" + sn).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//			bs = ("支付时间:" + ppi.getPayTime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("支付方式:" + ppi.getPayType()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("支付订单号:" + ppi.getTx_no()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("支付金额:" + ppi.getAmount() + "元").getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = (separator()).getBytes();
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			bs = ("" + ppi.getTopMessage()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			printQRCode(ppi.getQrCode(), pn);
//
//			normalFont(pn);
//			pn.write(a, a.length);
//			bs = ("" + ppi.getBottomMessage()).getBytes("GB2312");
//			pn.write(bs, bs.length);
//			pn.write(a, a.length);
//
//			for (int i = 0; i < 5; i++) {
//				pn.write(a, a.length);
//			}
//			bs = cutoff();
//			pn.write(bs, bs.length);
//			bs = sound();
//			pn.write(bs, bs.length);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 换行
//	 */
//	private byte[] line() {
//		byte[] a = new byte[2];
//		a[0] = 0x0d;
//		a[1] = 0x0a;
//		return a;
//	}
//
//	/**
//	 * 分隔符
//	 */
//	private String separator() {
//		int length = paperLength;
//		if (length == 32) {
//			return "--------------------------------";
//		}
//		return "----------------------------------------------";
//	}
//
//	/**
//	 * 居中
//	 * 
//	 * @param info
//	 * @return
//	 */
//	private String center(String info) {
//		byte[] b = null;
//		try {
//			b = info.getBytes("GB2312");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//		}
//		int length = b.length / 2;
//		int total_length = paperLength / 2 - length;
//		String str = "";
//		for (int i = 0; i < total_length / 2; i++) {
//			str += " ";
//		}
//
//		return str + info;
//	}
//
//	/*
//	 * 从串口打印二维码
//	 */
//	public static void printQRCode(String qrData ,PrinterNative pn) throws IOException {
//	
//		byte moduleSize = 7;
//		String encoding = "GBK";
//		// String center_Data = "";
//
//		int length = qrData.getBytes(encoding).length;
//		// L.i("gg", "qrdataLength:" + length);
//
//		// for (int i = 0; i < (paperLength / 4); i++) {
//		// center_Data += " ";
//		// }
//
//		byte b = (byte) (length + 3);
//		byte[] bs;
//		// 打印二维码矩阵
//		bs = new byte[] { 0x1D };
//		pn.write(bs, bs.length);
//
//		bs = "(k".getBytes();
//		pn.write(bs, bs.length);
//		bs = new byte[] { b };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 0 };
//		pn.write(bs, bs.length);
//
//		bs = new byte[] { 49 };
//		pn.write(bs, bs.length);
//
//		bs = new byte[] { 80 };
//		pn.write(bs, bs.length);
//
//		bs = new byte[] { 48 };
//		pn.write(bs, bs.length);
//
//		bs = qrData.getBytes();
//		pn.write(bs, bs.length);
//		bs = new byte[] { 0x1D };
//		pn.write(bs, bs.length);
//		bs = "(k".getBytes();
//		pn.write(bs, bs.length);
//		bs = new byte[] { 3 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 0 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 49 };
//		pn.write(bs, bs.length);
//
//		bs = new byte[] { 69 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 48 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 0x1D };
//		pn.write(bs, bs.length);
//		bs = "(k".getBytes();
//		pn.write(bs, bs.length);
//		bs = new byte[] { 3 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 0 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 49 };
//		pn.write(bs, bs.length);
//		bs = new byte[] { 67 };
//		pn.write(bs, bs.length);
//
//		bs = new byte[] { moduleSize };
//		pn.write(bs, bs.length);
//
//		bs = new byte[] { 0x1D };
//		pn.write(bs, bs.length);
//		bs = "(k".getBytes();
//		pn.write(bs, bs.length);
//		bs = new byte[] { 3 }; // pl
//		pn.write(bs, bs.length);
//		bs = new byte[] { 0 }; // ph
//		pn.write(bs, bs.length);
//		bs = new byte[] { 49 }; // cn
//		pn.write(bs, bs.length);
//		bs = new byte[] { 81 }; // fn
//		pn.write(bs, bs.length);
//		bs = new byte[] { 48 }; // m
//		pn.write(bs, bs.length);
//
//	}
//
//	/**
//	 * 切断
//	 * 
//	 * @return
//	 */
//
//	private byte[] cutoff() {
//		byte[] a = new byte[2];
//		a[0] = 0x1B;
//		a[1] = 0x69;
//		return a;
//	}
//
//	/**
//	 * 发出声音
//	 * 
//	 * @return
//	 */
//	private byte[] sound() {
//		byte[] d = new byte[4];
//		d[0] = 0x1b;
//		d[1] = 0x42;
//		d[2] = 0x02;
//		d[3] = 0x02;
//		return d;
//	}
//
//	/*
//	 * 放大字体
//	 */
//	private void bigFont(PrinterNative pn) {
//		byte[] b = new byte[3];
//		b[0] = 0x1B;
//		b[1] = 0x21;
//		b[2] = 0x38;
//		pn.write(b, b.length);
//
//		b[0] = 0x1C;
//		b[1] = 0x21;
//		b[2] = 0x08;
//		pn.write(b, b.length);
//	}
//
//	/*
//	 * 常规字体
//	 */
//	private void normalFont(PrinterNative pn) {
//		byte[] b = new byte[3];
//		b[0] = 0x1B;
//		b[1] = 0x21;
//		b[2] = 0x00;
//		pn.write(b, b.length);
//
//		b[0] = 0x1C;
//		b[1] = 0x21;
//		b[2] = 0x00;
//		pn.write(b, b.length);
//	}
//
//	/**
//	 * 左右对齐
//	 * 
//	 * @param start
//	 * @param end
//	 * @return
//	 */
//	private String spacing(String start, String end) {
//		byte[] b = null;
//		byte[] c = null;
//		try {
//			b = start.getBytes("GB2312");
//			c = end.getBytes("GB2312");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//		}
//		int length = b.length + c.length;
//		int total_length = paperLength - length;
//		String str = "";
//		for (int i = 0; i < total_length; i++) {
//			str += " ";
//		}
//		// start+=end;
//		return start + str + end;
//	}
//}
