package com.weilay.pos.printData;

import org.rxwu.helper.entity.PrintEntity;
import org.rxwu.helper.entity.PrintEntity.ALIGN;
import org.rxwu.helper.listener.PrintListener;

import com.framework.utils.ConvertUtil;
import com.framework.utils.L;
import com.google.gson.Gson;
import com.weilay.pos.PrintSettingActivity;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.app.PayType;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.entity.MemberEntity;
import com.weilay.pos.entity.MemberTimesLevelEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.entity.QRInfoEntity;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.QRCodeUtil;
import com.weilay.pos.util.QrInfoUtil;
import com.weilay.pos.util.Utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/*****
 * @detail 打印支付相关的账单
 * @author Administrator
 * 
 */
public class PrintOrderData {
	static Bitmap preferBitmap, refundBitmap;

	/******
	 * @Detail 打印会员支付的相关订单
	 * 
	 */
	public static void printOrderPay(final PayTypeEntity payType, PrintListener listener) {
		L.i("----" + new Gson().toJson(payType));
		final PrintEntity entity = new PrintEntity(PosDefine.getPrintPaper());
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(Utils.getCurOperator().getBranch_name());
		entity.addCenter("支付单");
		entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
		entity.addLine();
		entity.addLn("设备编码：" + GetUtil.getimei());
		entity.addLn("支付时间：" + (payType.getTime() == null ? GetUtil.gettime() : payType.getTime()));
		entity.addLine();
		entity.addLn("支付方式：" + payType.getPayType().getValue());
		entity.addLn("支付单号：" + (payType.isMicro()?payType.getTx_no2():payType.getTx_no()));
		entity.addLn("订单金额：" + ConvertUtil.parseMoney(payType.getAmount()) + "元");
		entity.addLn("实收金额：" + ConvertUtil.parseMoney(payType.getAraamount() - payType.getFirstDiscount()) + "元");
		/**** 会员的折扣 *****/
		if (payType.getMemberDiscount() != 10) {
			entity.addLn("会员折扣：" + (ConvertUtil.parseMoney(payType.getMemberDiscount() * 10)) + "折");
			entity.addLn("会员折扣金额:" + ConvertUtil.parseMoney(payType.getMemberDiscountAmount()) + "元");
			entity.addLine();
		}
		if (payType.getFirstDiscount() != 0) {
			String discountType = "首单或闲时优惠：";
			if (payType.getDiscountType() != null) {
				switch (payType.getDiscountType()) {
				case "F":
					discountType = "首单优惠:";
					break;
				default:
					discountType = "闲时优惠:";
					break;
				}
			}
			entity.addLn(discountType + ConvertUtil.getMoney(payType.getFirstDiscount()));
		}
		/**** 会员优惠的金额 *****/
		if (payType.getCouponDiscountAmount() != 0) {
			entity.addLn("优惠券：" + CardTypeEnum.getTypeName(payType.getCouponType()));
			if (payType.getCouponDiscount() != 10) {
				entity.addLn("优惠券折扣：" + payType.getCouponDiscount() + "折");
			}
			entity.addLn("优惠金额:" + ConvertUtil.parseMoney(payType.getCouponDiscountAmount()) + "元");
			entity.addLine();
		}
		entity.addLn("打印时间：" + GetUtil.gettime());

		final QRInfoEntity qrinfo = payType.getQrInfo();

		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				preferBitmap = QrInfoUtil.parsePayPrefer2code(qrinfo);
				if(PrintSettingActivity.isPrintRefund() && (payType.getPayType()==PayType.ALIPAY || payType.getPayType()==PayType.WEIXIN)){
					//refundBitmap=QRCodeUtil.createQRImage(payType.isMicro()?payType.getTx_no2():payType.getTx_no(),300, 300,null,null);
					refundBitmap = QRCodeUtil.createQRImage(payType.isMicro()?payType.getTx_no2():payType.getTx_no(),PosDefine.getPrintPaper()==46?300:280,PosDefine.getPrintPaper()==46?300:280,null,null);
				}else{
					refundBitmap=null;
				}
				return preferBitmap;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				boolean printPhoto=WeiLayApplication.app.mCache.getAsInt("mPrintPhotoSelect",1)==1?true:false;
				if(printPhoto)
				{
					if (refundBitmap != null) {
						entity.swapLine();
						entity.pic2PxPoint(refundBitmap, ALIGN.CENTER);
						entity.addCenter("(可凭此退款码退款)");
					}
					entity.addLine();
					entity.setFont(PrintEntity.BIG_FONT);
					if (qrinfo != null && result != null) {
						entity.addLn(qrinfo.getTop());
						entity.pic2PxPoint(result, ALIGN.CENTER);
						entity.addLn(qrinfo.getBottom());
					}
				}
				
				entity.setFont(PrintEntity.NORMAL_FONT);
				entity.addLine();
				entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
				entity.addLn("联系电话:" + Utils.getCurOperator().getStoremobile());
				int count=WeiLayApplication.app.mCache.getAsInt("mPayPrintSelect", 0);
				while(count>=0){
					PrintHelper.printHelper(entity);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count--;
				}
				
			}
		}.execute();

	}

	/******
	 * @Detail 打印会员支付的相关订单
	 * 
	 */
	public static void printMemberRecharge(MemberEntity member, double payMoeny, double giftMoeny,MemberTimesLevelEntity level) {
		if (member == null) {
			return;
		}
		PrintEntity entity = new PrintEntity(PosDefine.getPrintPaper());
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(Utils.getCurOperator().getBranch_name());
		entity.addCenter("会员充值凭证");
		entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
		entity.addLine();
		entity.addLn("设备编码：" + GetUtil.getimei());
		entity.addLn("支付时间：" + GetUtil.gettime());
		entity.addLine();
		entity.addLn("支付方式："+(member.getMember_card_type()==1?"次数充值":"现金充值"));
		entity.addLn("会员卡号：" + member.getMembership_number());
		entity.addLn("会员昵称：" + member.getNickname());
		if(member.getMember_card_type()!=1){
			entity.addLn("充值金额：" + ConvertUtil.parseMoney(payMoeny) + "元");
			entity.addLn("赠送金额：" + ConvertUtil.parseMoney(giftMoeny) + "元");
		}else{
			if(level!=null){
				entity.addLn("充值次数：" + level.getTimes()+"次");
				entity.addLn("充值金额：" + com.weilay.pos.util.ConvertUtil.branchToYuan(ConvertUtil.getMoney(level.getLevel_amount())) + "元");
			}
			
		}
		entity.addLn("打印时间：" + GetUtil.gettime());
		entity.addLine();
		entity.setFont(PrintEntity.NORMAL_FONT);
		entity.addLine();
		entity.addLn("商家地址:" + Utils.getCurOperator().getStoreaddress());
		entity.addLn("联系电话:" + Utils.getCurOperator().getMobile());
		PrintHelper.printHelper(entity);
	}

	/******
	 * @detail 打印卡券
	 */
	public static void printCoupon(CouponEntity coupon) {
		if (coupon == null) {
			return;
		}
		PrintEntity entity = new PrintEntity(PosDefine.getPrintPaper());
		entity.setFont(PrintEntity.BIG_FONT);
		entity.addCenter(Utils.getCurOperator().getBranch_name());
		entity.addCenter("卡券小票");
		entity.setFont(PrintEntity.NORMAL_FONT);// 标准字体
		entity.addLine();
		entity.addLn("设备编码：" + GetUtil.getimei());
		entity.addLine();
		entity.addLn("卡券信息：" + coupon.getInfo());
		entity.addLn("卡券说明：" + coupon.getNotice());
		entity.addLn("打印时间：" + GetUtil.gettime());
		entity.addLine();
		entity.setFont(PrintEntity.NORMAL_FONT);
		entity.pic2PxPoint(QRCodeUtil.createQRImage(coupon.getUrl2qrcode(), 300, 300, null, null), ALIGN.CENTER);
		entity.addLine();
		entity.addLn("商家名称：" + (coupon.getMerchantname() == null ? "" : coupon.getMerchantname()));
		PrintHelper.printHelper(entity);
	}
}
