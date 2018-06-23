package com.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******
 * @Create By rxwu at 2016/03/16
 * @Email 11587255@qq.com
 * @detail String工具类
 */
public class StringUtil {

    /**
     * * 是否为null或空字符串
     *
     * @param str 字符串
     * @return boolean 如果是字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    /**
     * * 是否为null或空字符串
     *
     * @param str 字符串
     * @return boolean 如果字符串为空格，仍将返回true
     */
    public static boolean isBank(Object str) {
        return null == str || str.toString().length() == 0 || str.toString().trim().length() == 0;
    }

    /**
     * * 是否非null或非空字符串
     *
     * @param str 字符串
     * @return boolean 如果是null或长度为0,则返回false,否则返回true
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 是否非null或非空字符串
     *
     * @param str 字符串
     * @return boolean 如果是null或长度为0,则返回false,否则返回true
     */
    public static boolean isNotEmpty(String... str) {
    	for(String s:str){
    		if(isBank(s)){
    			return false;
    		}
    	}
    	return true;
    }
    /**
     * 获得文件类型
     *
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName) {
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index);
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^((13[0-9])|(17[0-9])|(147)|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static String trimEmpty(String member_province) {
        if (member_province == null) {
            return "";
        }
        return member_province.trim();
    }

    /*******
     * @param str
     * @return
     * @detail 去除回车，换行，制表符
     */
    public static String replaceOtherChar(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /*****
     * @param obj
     * @param i
     * @return
     * @detail 左右添加空格
     */
    public static String addPadding(Object obj, int length) {
        String str = obj.toString();
        if (str == null)
            return str;
        int strLen = str.length();
        if (strLen < length) {
            while (strLen < length) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str).append("0");//右补0
                str = sb.toString();
                strLen = strLen+1;
            }
            return str;
        }
        return str;
    }
}