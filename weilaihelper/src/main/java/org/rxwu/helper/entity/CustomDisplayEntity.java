package org.rxwu.helper.entity;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Created by rxwu on 2016/4/13.
 * <p>
 * Email:1158577255@qq.com
 * <p>
 * detail: 客显操作的实体工具类
 */
public final class CustomDisplayEntity  {
    private List<byte[]> cmds = new ArrayList<byte[]>();
    private static final int NUM_COUNT = 15;
    private static final byte[] CMD_CLEAR =new byte[]{0x0C};//清除命令
    private static final byte[]SWAP =new byte[]{0x0D};//回车

    //命令的类型
    public static final int TYPE_PRICE = 1;
    public static final int TYPE_SUM = 2;
    public static final int TYPE_INCOME = 3;
    public static final int TYPE_FIND = 4;


    private static final byte[] PRICE = {0x1B, 0x73, 0x31};//单价
    private static final byte[] SUM = {0x1B, 0x73, 0x32};//合计
    private static final byte[] INCOME = {0x1B, 0x73, 0x33};//收款
    private static final byte[] FIND = {0x1B, 0x73, 0x34};//找零

    /**********
     * @param type  显示的类型
     * @param money 显示金额
     * @detail
     */
    public CustomDisplayEntity(int type, String money) {
        cmds.clear();
        cmds.add(parseMoneyCMD(money + ""));
        cmds.add(SWAP);
        switch (type) {
            case TYPE_PRICE:
                cmds.add(PRICE);
                break;
            case TYPE_FIND:
                cmds.add(FIND);
                break;

            case TYPE_INCOME:
                cmds.add(INCOME);
                break;

            case TYPE_SUM:
                cmds.add(SUM);
                break;
        }
    }

    public List<byte[]> getCmds() {
        return cmds;
    }


    /***********
     * @param money
     * @return
     * @detail 将要显示的金额字符串转换成可以识别的hex16进制数
     */
    private byte[] parseMoneyCMD(String money) {
        byte[] temp = new byte[NUM_COUNT];
        char[] nums = money.toCharArray();
        temp[0] = 0x0C;
        temp[1] = 0x1B;
        temp[2] = 0x51;
        temp[3] = 0x41;
        int bu_count = NUM_COUNT - 4 - nums.length;
        if (0 < bu_count) {
            for (int i = 0; i < bu_count; i++) {
                //补空格，数字向右对齐
                temp[4 + i] = 0x20;
            }
        }
        try {
            int length = NUM_COUNT - nums.length;
            for (int i = 0; i < nums.length; i++) {
                String hexStr = Integer.toHexString(nums[i]);//得到的设置为十六进制的
                int ten = Integer.parseInt(hexStr, 16);
                temp[length + i] = Byte.parseByte(String.valueOf(ten));
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return temp;
    }

}
