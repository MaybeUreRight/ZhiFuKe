package com.weilay.pos.util;

import com.framework.utils.L;

import java.math.BigDecimal;

public class ConvertUtil {
	public static final String MONEY_FORMAT = "#0.00";

	/**
	 * 将传入对象格式化为钱币字符串格式
	 *
	 * @param str
	 * @return
	 */
	public static String parseMoney(Object str) {
		if (str == null)
			return "0.00元";
		String money = str.toString();
		if (money.indexOf("元") != -1) {
			money = money.replaceAll("元", "");
		}
		try {
			return doubleToString(Double.valueOf(money)) + "元";
		} catch (Exception e) {
			L.e("ConvertUtil->parseMoney:" + e.getMessage());
		}
		return "0.00元";
	}

	/******
	 * @detail 元转分
	 * @param yuan
	 * @return
	 */
	public static long yuanToBranch(double yuan) {
		 BigDecimal feedRate = new BigDecimal(100); 
		  return new BigDecimal(yuan).setScale(4,BigDecimal.ROUND_HALF_UP).multiply(feedRate).longValue();
	}

	/*****
	 * @Detail 分转元
	 * @return
	 */
	public static double branchToYuan(double branch) {
		BigDecimal b1 = new BigDecimal(Double.toString(branch));
		BigDecimal b2 = new BigDecimal(Double.toString(100));
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	 /*****
     * @param branch
     * @return
     * @Detail 分转千元
     */
    public static double branchToQian(double branch) {
        BigDecimal b1 = new BigDecimal(Double.toString(branch));
        BigDecimal b2 = new BigDecimal(Double.toString(100000));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	
	/*******
	 * @detail 转换为金额格式，保留两位小数
	 * @param findMoneyStr
	 *            金额字符
	 * @return
	 */
	public static double getMoeny(Object findMoneyStr) {
		// TODO Auto-generated method stub
		BigDecimal bd = null;  
		try {
			bd = new BigDecimal(String.valueOf(findMoneyStr));
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd.doubleValue();
		} catch (Exception e) {
			L.e("ConvertUtil->getMoeny" + e.getLocalizedMessage());
		}
		return -1;
	}

	/*******
	 * @Detail 格式化输出double数据，防止用科学技术法
	 * @param d
	 * @return
	 */
	public static String doubleToString(double d) {
		// TODO Auto-generated method stub
		return new BigDecimal("" + getMoeny(d)).toPlainString();
	}

}
