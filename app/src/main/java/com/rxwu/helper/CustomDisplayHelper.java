package com.rxwu.helper;

import java.io.IOException;

import org.rxwu.helper.entity.CustomDisplayEntity;
import org.rxwu.helper.listener.CustomDisplayListener;

import android.util.Log;

/**
 * Created by rxwu on 2016/4/13.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:客显操作工具类
 */
public final class CustomDisplayHelper extends BaseSerialPortHelper<CustomDisplayEntity>{
	
	private CustomDisplayListener customDisplayListener;
	private CustomDisplayEntity entity=null;
	
	
    public CustomDisplayListener getCustomDisplayListener() {
		return customDisplayListener;
	}

	public void setCustomDisplayListener(CustomDisplayListener customDisplayListener) {
		this.customDisplayListener = customDisplayListener;
	}

	///dev/ttyS3 2400 device 设备 booter　波特率
    public CustomDisplayHelper(String device,int booter) {
        super(device,booter);
    }
    
    public void setListener(int what,String msg){
    	if(customDisplayListener==null)
    		return;
    	switch (what) {
		case ERROR:
			customDisplayListener.showError(msg);
			break;
		case SUCCESS:
			customDisplayListener.showSuccess();
			break;
		default:
			break;
		}
    }
    String errorMsg="";
    @Override
    protected Integer doInBackground(Integer... params) {
        try {
        	if(entity==null)
        	{
        		errorMsg="获取不到显示内容";
        		return ERROR;
        	}
            if(params==null || mOutputStream==null){
            	errorMsg="连接断开";
            	return ERROR;
            }
            for(byte[]cmd:entity.getCmds()){
                mOutputStream.write(cmd);
            }
        } catch (IOException e) {
        	errorMsg="其他错误："+e.getMessage()+":"+e.getLocalizedMessage();
            return ERROR;
        }
        return SUCCESS;
    }
    
    @Override
    protected void onPostExecute(Integer result) {
    	// TODO Auto-generated method stub
    	super.onPostExecute(result);
    	setListener(result,errorMsg);
    }
    //发送
    public void show(CustomDisplayEntity entity) {
    	this.entity=entity;
    	super.start();
    	
    }
    
  

    private static final byte[] PRICE = new byte[]{0x1B, 0x73, 0x31};
    private static final char POINT = 0x0D;
    private static final char EMPTY = 0x20;

    private static final int NUM_COUNT = 15;//最多可显示14个字符

    public byte[] parseMoneyCMD(String money) {
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
