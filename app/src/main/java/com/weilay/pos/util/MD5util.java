package com.weilay.pos.util;

import java.security.MessageDigest;

/**
 * User: rizenguo
 * Date: 2014/10/23
 * Time: 15:43
 */
public class MD5util {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 杞崲瀛楄妭鏁扮粍涓?16杩涘埗瀛椾覆
     * @param b 瀛楄妭鏁扮粍
     * @return 16杩涘埗瀛椾覆
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 杞崲byte鍒?16杩涘埗
     * @param b 瑕佽浆鎹㈢殑byte
     * @return 16杩涘埗鏍煎紡
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5缂栫爜
     * @param origin 鍘熷瀛楃涓?
     * @return 缁忚繃MD5鍔犲瘑涔嬪悗鐨勭粨鏋?
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

}
