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
//import com.weilay.pos.entity.SendTicketInfo;
//import com.weilay.pos.entity.ShiftRecord;
//import com.weilay.pos.entity.VerificationSheet;
//
//import android.content.SharedPreferences;
//import com_serialport_api.PrinterNative;
//
//public class SerialComPort {
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
//	public SerialComPort() {
//		sp_port = WeiLayApplication.getSp_port();
//		if (sp_port != null) {
//			sn = sp_port.getString("sn", "00000");
//			int item = sp_port.getInt("mPrintpaper", 0);// 值为0时纸张大小为80毫米(46字节),值为1时纸张大小为50毫米(32字节)
//			paperLength = (item == 0) ? 46 : 32;
//		}
//	}
//
//	public boolean printMenDian(PrintUtil comMainSend, MenDiandayin mddy) {
//		try {// RESET PRINTER
//			byte[] a = line();
//			comMainSend.send(a);
//			bigFont(comMainSend);
//
//			byte[] bs;
//
//			bs = (center("门店报表")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = ("门店报表时间:" + mddy.getDate()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing(weixin, mddy.getWeixin() + "元").getBytes("GB2312"));
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing(zhifubao, mddy.getZhifubao() + "元")
//					.getBytes("GB2312"));
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing("现金", mddy.getCash() + "元").getBytes("GB2312"));
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			String zj = mddy.getTotalAmount();
//			bs = (spacing(heji, zj + "元")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printShiftRecord(PrintUtil comMainSend, ShiftRecord sr) {
//		try {
//			byte[] a = line();
//			comMainSend.send(a);
//
//			bigFont(comMainSend);
//			byte[] bs;
//
//			bs = (center(message)).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			bs = ("交班时间:" + sr.getDate()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			bs = (equipment_coding + sn).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("操作员:" + sr.getOperator()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (spacing(payment, amount)).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (spacing(weixin, sr.getWeixin() + "元")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing(zhifubao, sr.getZhifubao() + "元")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing(baidu, sr.getBaidu() + "元")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			String zj = sr.getTotalamount();
//			bs = (spacing(heji, zj + "元")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
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
//	public boolean printVerificationSheet(PrintUtil comMainSend,
//			VerificationSheet vs) {
//		try {// RESET PRINTER
//			byte[] a = line();
//			comMainSend.send(a);
//			bigFont(comMainSend);
//
//			byte[] bs;
//			bs = (center("核销单")).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = ("核销日期:" + vs.getCtime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing("设备编码:", vs.getSn())).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (spacing("核销单号:", vs.getCode())).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printCheckOut(PrintUtil comMainSend, CheckOutInfo co) {
//		try {
//			PrinterNative pn = new PrinterNative();
//
//			byte[] a = line();
//			comMainSend.send(a);
//			pn.write(a, a.length);
//			byte[] bs;
//			bigFont(comMainSend);
//			bs = (center(co.getTitle())).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			bs = co.getTitle().getBytes("GB2312");
//			pn.write(bs, bs.length);
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("结账时间:" + co.getTxtime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("设备编码:" + co.getSn()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("交易方式:" + co.getPaytype()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("商户单号:" + co.getTx_no()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("金额:" + co.getTotalamountYuan() + "元").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
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
//	public boolean printRefund(PrintUtil comMainSend, RefundPrint rp) {
//		try {
//			byte[] a = line();
//			comMainSend.send(a);
//			byte[] bs;
//
//			bigFont(comMainSend);
//			bs = (center(rp.getTitle())).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("退款时间:" + rp.getTxtime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("设备编码:" + rp.getSn()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("退款方式:" + rp.getPaytype()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("商户单号:" + rp.getTx_no()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("退款单号:" + rp.getRefundno()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("退款金额:" + rp.getRefundamount() + "元").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printTest(PrintUtil comMainSend) {
//		try {// RESET PRINTER
//			byte[] a = line();
//			comMainSend.send(a);
//
//			bigFont(comMainSend);
//			byte[] bs;
//			bs = ("打印测试开始!").getBytes("GB2312");
//			comMainSend.send(bs);
//
//			comMainSend.send(a);// 发送换行指令
//			comMainSend.send(a);// 发送换行指令
//			normalFont(comMainSend);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ").getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = ("abcdefghijklmnopqrstuvwxyz").getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = ("0123456789").getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = ("~!@#$%^&*()_+{}[]:<>?.,").getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			printQRCode("http://weixin.qq.com/r/7kOosPfEFKprrbPT9xb8",
//					comMainSend, 2);
//
//			normalFont(comMainSend);
//			comMainSend.send(a);// 发送换行指令
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			bigFont(comMainSend);
//			bs = ("打印测试结束!").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);// 发送换行指令
//
//			normalFont(comMainSend);
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (IOException e) {
//
//			L.i("gg", e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printAdvertisement(PrintUtil comMainSend, AdvertisementQR aqr) {
//		try {
//			byte[] a = line();
//			comMainSend.send(a);
//			byte[] bs;
//
//			normalFont(comMainSend);
//			comMainSend.send(a);
//			bs = (aqr.getTopMessage()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			printQRCode(aqr.getQrCode(), comMainSend, 2);
//
//			bs = (aqr.getBottomMessage()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			normalFont(comMainSend);
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//
//	}
//
//	public boolean printSendRedPackage(PrintUtil comMainSend, SendRedPackage srp) {
//		try {
//			byte[] a = line();
//			comMainSend.send(a);
//
//			byte[] bs;
//			bigFont(comMainSend);
//			bs = center("微信找零小票").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("设备编码:" + sn).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("找零订单号:" + srp.getTx_no()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("找零金额:" + srp.getAmount() + "元").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			printQRCode(srp.getQr_code(), comMainSend, 2);
//
//			normalFont(comMainSend);
//			bs = ("交易时间:" + srp.getTrad_time()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("注意事项:").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("1.小票找零无记名,第一个扫码的人有效,请顾客自行保管。").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("2.请及时扫码收零,以免二维码模糊失效。").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
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
//	public boolean printPay(PrintUtil comMainSend, PrintPayInfo ppi) {
//		try {
//			byte[] a = line();
//			comMainSend.send(a);
//
//			byte[] bs;
//			bigFont(comMainSend);
//			bs = center("支付单").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("设备编码:" + sn).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			bs = ("支付时间:" + ppi.getPayTime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("支付方式:" + ppi.getPayType()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("支付订单号:" + ppi.getTx_no()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("支付金额:" + ppi.getAmount() + "元").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("打印时间:" + GetUtil.gettime()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			bs = ("" + ppi.getTopMessage()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			printQRCode(ppi.getQrCode(), comMainSend, 2);
//
//			normalFont(comMainSend);
//			comMainSend.send(a);
//			bs = ("" + ppi.getBottomMessage()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public boolean printTicket(PrintUtil comMainSend, SendTicketInfo sti) {
//		byte[] a = line();
//		comMainSend.send(a);
//
//		byte[] bs;
//		bigFont(comMainSend);
//		try {
//			bs = center(sti.getMerchentname()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			bs = center(sti.getCardinfo()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			normalFont(comMainSend);
//			bs = (separator()).getBytes();
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			bs = center(sti.getDeadline()).getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			printQRCode(sti.getUrl2qrcode(), comMainSend, 2);
//			bigFont(comMainSend);
//			bs = center("请使用微信扫一扫领取卡券").getBytes("GB2312");
//			comMainSend.send(bs);
//			comMainSend.send(a);
//			for (int i = 0; i < 5; i++) {
//				comMainSend.send(a);
//			}
//			comMainSend.send(cutoff());
//			comMainSend.send(sound());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//
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
//	public static void printQRCode(String qrData, PrintUtil ComPort, int align)
//			throws IOException {
//		byte moduleSize = 7;
//		String encoding = "GBK";
//		int length = qrData.getBytes(encoding).length;
//		byte b = (byte) (length + 3);
//		byte[] alignCode = new byte[3];
//		switch (align) {
//		case 1:
//			// alignCode[0]=27;
//			// alignCode[1]=97;
//			// alignCode[2]=0;
//			break;
//
//		case 2:
//			alignCode[0] = 27;
//			alignCode[1] = 97;
//			alignCode[2] = 1;
//			ComPort.send(alignCode);
//			break;
//		case 3:
//			alignCode[0] = 27;
//			alignCode[1] = 97;
//			alignCode[2] = 2;
//			ComPort.send(alignCode);
//			break;
//		}
//
//		// 打印二维码矩阵
//		ComPort.send(new byte[] { 0x1D });// init
//		ComPort.send("(k".getBytes());// adjust height of barcode
//
//		ComPort.send(new byte[] { b }); // pl
//		ComPort.send(new byte[] { 0 }); // ph
//		ComPort.send(new byte[] { 49 }); // cn
//		ComPort.send(new byte[] { 80 }); // fn
//		ComPort.send(new byte[] { 48 }); //
//		ComPort.send(qrData.getBytes());
//
//		ComPort.send(new byte[] { 0x1D });
//		ComPort.send("(k".getBytes());
//		ComPort.send(new byte[] { 3 });
//		ComPort.send(new byte[] { 0 });
//		ComPort.send(new byte[] { 49 });
//		ComPort.send(new byte[] { 69 });
//		ComPort.send(new byte[] { 48 });
//
//		ComPort.send(new byte[] { 0x1D });
//		ComPort.send("(k".getBytes());
//		ComPort.send(new byte[] { 3 });
//		ComPort.send(new byte[] { 0 });
//		ComPort.send(new byte[] { 49 });
//		ComPort.send(new byte[] { 67 });
//		ComPort.send(new byte[] { moduleSize });
//
//		ComPort.send(new byte[] { 0x1D });
//		ComPort.send("(k".getBytes());
//		ComPort.send(new byte[] { 3 }); // pl
//		ComPort.send(new byte[] { 0 }); // ph
//		ComPort.send(new byte[] { 49 }); // cn
//		ComPort.send(new byte[] { 81 }); // fn
//		ComPort.send(new byte[] { 48 }); // m
//
//		alignCode[0] = 27;
//		alignCode[1] = 97;
//		alignCode[2] = 0;
//		ComPort.send(alignCode);
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
//	private void bigFont(PrintUtil comMainSend) {
//		byte[] b = new byte[3];
//		b[0] = 0x1B;
//		b[1] = 0x21;
//		b[2] = 0x38;
//		comMainSend.send(b);
//
//		b[0] = 0x1C;
//		b[1] = 0x21;
//		b[2] = 0x08;
//		comMainSend.send(b);
//	}
//
//	/*
//	 * 常规字体
//	 */
//	private void normalFont(PrintUtil comMainSend) {
//		byte[] b = new byte[3];
//		b[0] = 0x1B;
//		b[1] = 0x21;
//		b[2] = 0x00;
//		comMainSend.send(b);
//
//		b[0] = 0x1C;
//		b[1] = 0x21;
//		b[2] = 0x00;
//		comMainSend.send(b);
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
