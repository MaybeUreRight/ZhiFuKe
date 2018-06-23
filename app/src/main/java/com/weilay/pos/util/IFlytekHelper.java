package com.weilay.pos.util;


import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

import android.content.Context;
import android.util.Log;


/**
 * Created by rxwu on 2017/8/3.
 * @detail 科大讯飞语音封装工具
 */

public class IFlytekHelper {
    private static final String APP_ID="59784ec4";
    private static IFlytekHelper _instance;
    private Context mContext;
    public SpeechUtility sPeechUtility;
    public static IFlytekHelper init(Context context){
        if(_instance==null){
            _instance=new IFlytekHelper(context);
        }
        return _instance;
    }
    public IFlytekHelper(Context context){
        this.mContext=context;
        // 启动讯飞语音合成工具
  	  sPeechUtility = SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "="+APP_ID+",engine_mode=plus");
      if (sPeechUtility != null) {
          sPeechUtility.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
          sPeechUtility.setParameter(SpeechConstant.VOICE_NAME, "王老师");
      }
        //检查是否已经安装
        checkInstall(context);
    }

    /*******
     * @detail 检查是否安装了
     * @param context
     */
    public static  void checkInstall(final Context context){
        final SpeechUtility utility = SpeechUtility.getUtility();
        if( null != utility ){
            if (!utility.checkServiceInstalled()) {
                //安装语记
                InstallUtil.install(context, "yuji");
            }else {
            	T.showCenter("已安装语音插件");
                mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
                    @Override
                    public void onInit(int code) {
                        Log.d("mTtsInitListener", "InitListener init() code = " + code);
                        if (code != ErrorCode.SUCCESS) {
                            T.showCenter("初始化失败,错误码："+code);
                        } else {
                            mTts.setParameter(SpeechConstant.VOICE_NAME,"王老师");
                            mTts.setParameter(SpeechConstant.VOLUME,"100");
                            // 初始化成功，之后可以调用startSpeaking方法
                            // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                            // 正确的做法是将onCreate中的startSpeaking调用移至这里
                            speaking(context,"   ");
                        }
                    }
                });
            }
        }
    }

    private static SpeechSynthesizer mTts;

    public static  void speaking(Context context,String content){
        if(mTts==null){
        
            // 初始化合成对象
            mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
                @Override
                public void onInit(int code) {
                    Log.d("mTtsInitListener", "InitListener init() code = " + code);
                    if (code != ErrorCode.SUCCESS) {
                        T.showCenter("初始化失败,错误码："+code);
                    } else {
                        mTts.setParameter(SpeechConstant.VOICE_NAME,"王老师");
                   	 	
                        // 初始化成功，之后可以调用startSpeaking方法
                        // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                        // 正确的做法是将onCreate中的startSpeaking调用移至这里
                    }
                }
            });
        }
        if(mTts!=null){
            mTts.startSpeaking(content,null);
        }
    }

}