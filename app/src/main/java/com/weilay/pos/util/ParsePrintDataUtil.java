package com.weilay.pos.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/******
 * @Detail 解析打印的内容
 * @author rxwu
 * @date 2016/07/08
 *
 */
public class ParsePrintDataUtil {
	
	
	/********
	 * @detail 获取打印数据中的金额,如果解析不到金额，则返回-1
	 * @param keyword 后台设置的关键词
	 * @param s 解析的内容
	 * @return
	 */
	public static float getMoney(String keyword,String s){
		if(s==null || keyword==null || "".equals(s) || s.indexOf(keyword)==-1){
			return -1f;                                       
		}
		//1.查找到关键词的位置
		int index=s.indexOf(keyword);
		//2.截取关键词之后的字符串,并去除所有的空格字符
		s=s.substring(index,s.length()).replaceAll(" ","");
		//3.获取截取到字符串之后的第一组数字（位置第一个）
	    Pattern p=Pattern.compile("([0-9.]+)");   
	    Matcher m=p.matcher(s);     
	    if(m.find()){
	    	return Float.parseFloat(m.group(0));
	    }
		return -1f;
	}
	
	
	
	
	
	/**********
	 * @detail 判断是否已经打印结束
	 * @param printData 收到要打印的数据
	 */
	public static boolean printEnd(byte[] printData){
//		L.e("gg","----"+printData);
		int len = printData.length;
		if (len > 10) {
			for (int i =len- 10; i <len - 1; i++) {
				if (printData[i] == 0x1b && printData[i + 1] == 0x6d) {
					if (len >= 5 && printData[len - 5] == 27
							&& printData[len - 4] == 67 && printData[len - 3] == 4
							&& printData[len - 2] == 2 && printData[len - 1] == 3) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/*****
	 * @detail 合并两个byte[]
	 */
	public static byte[] addBytes(byte[]before,byte[]add){
		if(before==null){
			before=new byte[]{};
		}
		if(add==null){
			return before;
		}
		byte[]temps=new byte[before.length+add.length];
		for(int i=0;i<before.length;i++){
			temps[i]=before[i];
		}
		
		for(int j=0;j<add.length;j++){
			temps[before.length+j]=add[j];
		}
		return temps;
	}
	
	
	
	
	
	/*******
	 * @detail 将base64bit的图片格式转换为string来解析
	 */
	
	public void convertBase64ToStr(byte[]bytes){
		
	}
}
