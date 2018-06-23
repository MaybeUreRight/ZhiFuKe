package com.weilay.pos.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author 
 *数据转换工具
 */
public class MyFunc {	
	
	//-------------------------------------------------------
	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num)
	{
		return num & 0x1;
	}
    //-------------------------------------------------------
    static public int HexToInt(String inHex)//Hex字符串转int
    {
    	return Integer.parseInt(inHex, 16);
    }
    //-------------------------------------------------------
    static public byte HexToByte(String inHex)//Hex字符串转byte
    {
    	return (byte)Integer.parseInt(inHex,16);
    }
    //-------------------------------------------------------
    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
    	return String.format("%02x", inByte).toUpperCase();
    }
    //-------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=inBytArr.length;
		for (int i = 0; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString(); 
	}
  //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr,int offset,int byteCount)//字节数组转转hex字符串，可选长度
	{
    	StringBuilder strBuilder=new StringBuilder();
		int j=byteCount;
		for (int i = offset; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]) + " ");
		}
		return strBuilder.toString();
	}
	//-------------------------------------------------------
	//转hex字符串转字节数组
    static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
	{
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen)==1)
		{//奇数
			hexlen++;
			result = new byte[(hexlen/2)];
			inHex="0"+inHex;
		}else {//偶数
			result = new byte[(hexlen/2)];
		}
	    int j=0;
		for (int i = 0; i < hexlen; i+=2)
		{
			result[j]=HexToByte(inHex.substring(i,i+2));
			j++;
		}
	    return result; 
	}
    
    static public String hex2Str(String str) throws UnsupportedEncodingException {
        String strArr[] = str.split("\\ "); // 分割拿到形如 xE9 的16进制数据
        byte[] byteArr = new byte[strArr.length - 1];
        for (int i = 1; i < strArr.length; i++) {
            Integer hexInt = Integer.valueOf(strArr[i], 16); //.decode("0x" + strArr[i]);
            byteArr[i - 1] = hexInt.byteValue();
        }
 
        return new String(byteArr, "GB2312");
    }
    
    static public String intString2Hex(String intStr)
    {
//    	String ret = "", tmp;
//    	if(intStr.length() % 2 != 0) intStr += "0";
//    	int len = intStr.length()/2;
//    	int v;
//    	
//    	for(int i = 0; i < len; i++){
//    		tmp = intStr.substring(i * 2, i * 2 + 2);
//    		v = Integer.valueOf(tmp);
//    		ret += Integer.toHexString(v);
//    	}
    	Long l = Long.valueOf(intStr);
    	    	
    	return Long.toHexString(l).toUpperCase();
    }
    
    public static void readFileByBytes(String fileName){ 
    	File file = new File(fileName); 
    	InputStream in = null; 
    	try {
	    	// 一次读一个字节 
	    	in = new FileInputStream(file); 
	    	int tempbyte; 
	    	
	    	while ((tempbyte = in.read()) != -1) { 
	    		System.out.write(tempbyte); 
	    	} 
	    	
	    	in.close(); 
    	} catch (IOException e) { 
	    	e.printStackTrace(); 
	    	//return; 
    	}
    }
}