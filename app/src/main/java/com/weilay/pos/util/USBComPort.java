package com.weilay.pos.util;

import org.rxwu.helper.entity.PrintEntity;
import org.rxwu.helper.entity.PrintEntity.ALIGN;

import com.framework.utils.StringUtil;
import com.framework.utils.L;
import com.rxwu.helper.USBProtolHelper;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.AdvertisementQR;
import com.weilay.pos.entity.CheckOutEntity;
import com.weilay.pos.entity.MenDiandayinEntity;
import com.weilay.pos.entity.PayAdvertisementEntity;
import com.weilay.pos.entity.SendRedPackage;
import com.weilay.pos.entity.SendTicketInfo;
import com.weilay.pos.entity.ShiftRecord;
import com.weilay.pos.entity.VerificationSheet;
import com.weilay.pos.printData.PrintHelper;

import android.content.SharedPreferences;

public class USBComPort {

	private int paperLenth = 46;// 纸张大小
	private SharedPreferences sp_port;
	private byte[] alignCode = { 27, 97, 0 };
	private USBProtolHelper helper;

	public USBComPort() {
		paperLenth = PosDefine.getPrintPaper();
	}

	public boolean printOutMD(MenDiandayinEntity md) {
		try {
			PrintEntity entity = new PrintEntity(paperLenth);
			entity.setFont(PrintEntity.BIG_FONT);// 大字体
			entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
			entity.addCenter(md.getTitle());// 居中
			entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
			entity.addLine();// 打印----分割线
			entity.addLn("报表时间:" + md.getDate());// 普通字体
			entity.addLn("设备编码:" + GetUtil.getimei());// 普通字体
			entity.addLine();
			entity.addLR("微信支付", md.getWeixin() + "元");
			entity.addLR("支付宝", md.getZhifubao() + "元");
			entity.addLR("现金", md.getCash() + "元");
			entity.addLine();
			entity.addLR("合  计", md.getTotalAmount() + "元");
			entity.addLine();
			entity.addLn("打印时间:" + GetUtil.gettime());
			entity.swapLine(1);
			// new USBProtolHelper().init(entity, context, path).start();
			entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
			entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
			helper = WeiLayApplication.getUsbHelper();
			if (helper != null) {
				helper.print(entity);
			} else {
				return false;
			}

		} catch (Exception ex) {
			L.e("gg", ex == null ? "" : ex.getMessage());
			return false;
		}
		return true;

	}

	public boolean printOutJZ(CheckOutEntity jz) {
		try {

			PrintEntity entity = new PrintEntity(paperLenth);
			entity.setFont(PrintEntity.BIG_FONT);// 大字体
			entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
			entity.addCenter("结账存单");// 居中
			entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
			entity.addLine();// 打印----分割线
			entity.addLn("结账时间:" + jz.getTxtime());// 普通字体
			entity.addLn("设备编码:" + jz.getSn());
			entity.addLine();
			entity.addLn("交易方式:" + jz.getPayMethod().getValue());
			entity.addLn("商户单号:" + jz.getTx_no());
			entity.addLn("金    额:" + jz.getTotalamountYuan() + "元");
			entity.addLine();
			entity.addLn("打印时间:" + GetUtil.gettime());
			// L.i("gg", "path:" + path);
			entity.swapLine(1);
			entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
			entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
			PrintHelper.printHelper(entity);
		} catch (Exception ex) {
			// TODO: handle exception
			L.e("gg", ex == null ? "" : ex.getMessage());
			return false;
		}
		// }
		return true;
	}
	
	/*****
	 * @detail 打印退款存单
	 * @return boolean　是否打印成功
	 * @param 
	 * @detail
	 */
	public boolean printOutRP(CheckOutEntity rp) {
		try {
			PrintEntity entity = new PrintEntity(paperLenth);
			entity.setFont(PrintEntity.BIG_FONT);// 大字体
			entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
			entity.addCenter("退款存单");// 居中
			entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
			entity.addLine();// 打印----分割线
			entity.addLn("退款时间:" + rp.getTxtime());// 普通字体
			entity.addLn("设备编码:" + rp.getSn());
			entity.addLine();
			entity.addLn("退款方式:" + rp.getPayMethod().getValue());
			entity.addLn("商户单号:" + rp.getTx_no());
			entity.addLn("退款单号:" + rp.getRefundno());
			entity.addLn("退款金额:" + ConvertUtil.branchToYuan(rp.getRefundamount()) + "元");
			entity.addLine();
			entity.addLn("打印时间:" + GetUtil.gettime());
			entity.swapLine(1);
	
			entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
			entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
			PrintHelper.printHelper(entity);
		} catch (Exception ex) {
			// TODO: handle exception
			L.e("gg", ex == null ? "" : ex.getMessage());
			return false;
			// }
		}
		return true;
	}
	
	/********
	 * @detail　打印核销的订单
	 * @return boolean
	 * @param 
	 * @detail
	 */
	public boolean printOutHX(VerificationSheet hxd) {
		// String[] usb = SerialPortFinder.scanUsbPort(context);
		// for (String i : usb) {
		try {
			PrintEntity entity = new PrintEntity(paperLenth);
			entity.setFont(PrintEntity.BIG_FONT);// 大字体
			entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
			entity.addCenter(hxd.getTitle());// 居中
			entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
			entity.addLine();// 打印----分割线
			entity.addLn("核销时间:" + hxd.getCtime());// 普通字体
			entity.addLine();// 打印----分割线
			entity.addLn("设备编码:" + hxd.getSn());
			entity.addLn("核销单号:" + hxd.getCode());
			entity.addLine();
			entity.addLn("打印时间:" + GetUtil.gettime());
			entity.swapLine(1);
			entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
			entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
			PrintHelper.printHelper(entity);
		} catch (Exception ex) {
			// TODO: handle exception
			L.e("gg", ex == null ? "" : ex.getMessage());
			return false;
		}
		// }
		return true;
	}
	
	/**********
	 * @detail 打印对账存单
	 * @return boolean
	 * @param list true 是对账存单的 false是交班记录的
	 * @detail
	 */
	public boolean printOutJBRecord(boolean list,ShiftRecord jbr) {
		try {
			PrintEntity entity = new PrintEntity(paperLenth);
			entity.setFont(PrintEntity.BIG_FONT);// 大字体
			entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
			if(list){
				entity.addCenter("对账存单");// 居中
			}else{
				entity.addCenter(StringUtil.isBank(jbr.getTitle())?"交班表":jbr.getTitle());// 居中
			}
			entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
			entity.addLine();// 打印----分割线
			if(!list){
				entity.addLn("上班时间:" + (jbr.getJobtime()==null?"":jbr.getJobtime()));// 新增上班时间打印 update by rxwu at 2017/06/19
			}
			entity.addLn((list?"对账日期:":"交班时间:") + jbr.gettime());// 普通字体
			// entity.addLine();// 打印----分割线
			entity.addLn("设备编码:" + GetUtil.getimei());
			
			entity.addLn("操 作 员:" + Utils.getCurOperator().getOperator());
			entity.addLn(list?"备    注：列表中所有数据均为当天0:00至24:00的统计数据":"备     注:交班表仅统计当班时段数据，如需对账请打印对账存单");
			entity.addLine();// 打印----分割线
			entity.addLR("支付方式", "金额");
			entity.addLine();// 打印----分割线
			entity.addLR("微信支付", ConvertUtil.parseMoney(jbr.getwechat()));
			entity.addLR("支付宝", ConvertUtil.parseMoney(jbr.getalipay()));
			entity.addLR("现金", ConvertUtil.parseMoney(jbr.getCash()));
			entity.addLR("会员充值", ConvertUtil.parseMoney(jbr.getRechargeAmt()));
			entity.addLR("退款",ConvertUtil.parseMoney(jbr.getRefundAmt()));
			try{
				entity.addLR("会员支付",ConvertUtil.parseMoney(jbr.getMemberPayAmt()));
			}catch (Exception e) {
				// TODO: handle exception
			}
			entity.addLine();
			entity.addLR("收款合计:", ConvertUtil.parseMoney(jbr.getTotalamount()));
			entity.addLine();
			entity.addLn("打印时间:" + GetUtil.gettime());
			entity.swapLine(1);
			entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
			entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
			// L.i("gg", "path:" + path);
			PrintHelper.printHelper(entity);
		} catch (Exception ex) {
			// TODO: handle exception
			// L.e(ex.getMessage(), ex.getMessage());
			ex.printStackTrace();
			return false;
			// }
		}
		return true;
	}
	
	/*******
	 * @detail 打印支付账单
	 * @return boolean
	 * @param 
	 * @detail
	 */
	public boolean printOutReceivePay(AdvertisementQR aqr) {
		try {
		PrintEntity entity = new PrintEntity(paperLenth);
		entity.addCenter(Utils.getCurOperator().getBranch_name());
		entity.addLn("打印时间：" + GetUtil.gettime());
		entity.addLn(aqr.getTopMessage());
		// entity.add2DCode(, ALIGN.CENTER);
		entity.pic2PxPoint(
				QRCodeUtil.createQRImage(aqr.getQrCode(), 300, 300, null, null),
				ALIGN.CENTER);
		entity.addLn(aqr.getBottomMessage());
		entity.swapLine(1);
		entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
		entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
		PrintHelper.printHelper(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	
	/*******
	 * @detail 打印支付账单
	 * @return boolean
	 * @param 
	 * @detail
	 */
	public boolean printOutPaySUC(PayAdvertisementEntity pai) {
		try {
		PrintEntity entity = new PrintEntity(paperLenth);
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
		entity.addCenter("支付单");
		entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
		entity.addLine();
		entity.addLn("设备编码：" + GetUtil.getimei());
		entity.addLn("支付时间：" + pai.getTime());
		entity.addLine();
		entity.addLn("支付方式：" + pai.getPayType());
		entity.addLn("支付单号：" + pai.getTx_no());
		entity.addLn("支付金额：" + pai.getTotalAmount() + "元");
		entity.addLine();
		entity.addLn("打印时间：" + GetUtil.gettime());
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(pai.getQrinfo_top());
		// entity.add2DCode(), ALIGN.CENTER);
		entity.pic2PxPoint(QRCodeUtil.createQRImage(pai.getQrinfo_qrCode(),
				300, 300, null, null), ALIGN.CENTER);
		entity.addCenter(pai.getQrinfo_bottom());
		entity.swapLine(1);
		entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
		entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
		PrintHelper.printHelper(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	/*******
	 * @detail usb测试
	 * @return boolean
	 * @param 
	 * @detail
	 */
	public boolean UsbTest() {
		try {
			PrintEntity entity = new PrintEntity(paperLenth);
			entity.setFont(PrintEntity.BIG_FONT);
			entity.addLn("USB打印测试开始！");
			entity.setFont(PrintEntity.NORMAL_FONT);
			entity.addLine();
			entity.addLn("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			entity.addLn("abcdefghijklmnopqrstuvwxyz");
			entity.addLn("0123456789");
			entity.addLn("~!@#$%^&*()_+{}[]:<>?.,");
			// entity.add2DCode(,
			// ALIGN.CENTER);
			entity.pic2PxPoint(QRCodeUtil.createQRImage(Config.OFFICIAL_CODE, 300, 300,
					null, null), ALIGN.CENTER);
			entity.addLine();
			entity.setFont(PrintEntity.BIG_FONT);
			entity.addLn("USB打印测试结束！");
			entity.setFont(PrintEntity.NORMAL_FONT);
			entity.swapLine(1);
			entity.addLn("商户的地址:" + Utils.getCurOperator().getStoreaddress());
			entity.addLn("商户的联系电话:" + Utils.getCurOperator().getStoremobile());
			entity.addLn("设备的地址:" + Utils.getCurOperator().getMachineaddress());
			entity.addLn("门店电话:" + Utils.getCurOperator().getStoremobile());
			entity.addLn("总店名称:" + Utils.getCurOperator().getBusiness_name());
			entity.addLn("分店名称:" + Utils.getCurOperator().getBranch_name());
			PrintHelper.printHelper(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*******
	 * @detail 发送卡券
	 * @return boolean
	 * @param 
	 * @detail
	 */
	public boolean SendTicket(SendTicketInfo sti) {
		try {
		PrintEntity entity = new PrintEntity(paperLenth);
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(Utils.getCurOperator().getBranch_name());
		entity.addCenter(sti.getMerchentname());
		entity.addCenter(sti.getCardinfo());
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addLine();
		entity.addCenter(sti.getDeadline());
		// entity.add2DCode(, ALIGN.CENTER);
		entity.pic2PxPoint(QRCodeUtil.createQRImage(sti.getUrl2qrcode(), 300,
				300, null, null), ALIGN.CENTER);
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter("请使用微信扫一扫领取卡券!");
		// entity.addCenter("新用户请扫两次!");
		entity.setFont(PrintEntity.NORMAL_FONT);
		entity.addLine();
		entity.swapLine(1);
		entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
		entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
		PrintHelper.printHelper(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			L.e(e.getMessage());
			return false;
		}
		return true;
	}
	
	/******
	 * @detail 发送红包
	 * @return boolean
	 * @param 
	 * @detail
	 */
	public boolean SendRedPack(SendRedPackage srp) {
		try {
		PrintEntity entity = new PrintEntity(paperLenth);
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(Utils.getCurOperator().getBranch_name());// 居中
		entity.addCenter("微信找零小票");
		entity.setFont(PrintEntity.NORMAL_FONT);
		entity.addLine();
		entity.addLn("设备编码:" + GetUtil.getimei());
		entity.addLn("找零单号:" + srp.getTxno());
		entity.addLn("找零金额:" + srp.getAmount() + "元");
		entity.addLine();
		entity.pic2PxPoint(QRCodeUtil.createQRImage(srp.getQrcode(), 300, 300, null, null),
				ALIGN.CENTER);
		entity.contents.add(alignCode);
		entity.addLn("交易时间:" + srp.getTime());
		entity.addLn("打印时间:" + GetUtil.gettime());
		entity.addLine();
		entity.addLn("注意事项:");
		entity.addLn("1.小票找零无记名,第一个扫码的人有效,请顾客自行保管。");
		entity.addLn("2.请及时扫码收零,以免二维码模糊失效。");
		entity.swapLine(1);
		entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
		entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
		PrintHelper.printHelper(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
