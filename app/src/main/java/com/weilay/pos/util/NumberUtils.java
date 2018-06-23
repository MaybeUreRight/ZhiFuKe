package com.weilay.pos.util;

/******
 * @detail 数字判断工具
 * @author rxwu
 *
 */
public class NumberUtils {

	/******
	 * @detail 判断是否为数字（允许小数点金额）
	 * @param obj
	 * @return
	 */
	public static boolean isNum(Object obj) {
		try {
			Double.parseDouble(obj.toString());
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

	/******
	 * @detail 判断是否为int 整型
	 * @param obj
	 * @return
	 */
	public static boolean isInteger(Object obj) {
		try {
			Integer.parseInt(obj.toString());
			return true;
		} catch (Exception ex) {
		}
		return false;
	}
}
