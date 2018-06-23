package com.weilay.listener;

import android.content.DialogInterface;


/**
 * Created by rxwu on 2016/4/15.
 * <p>
 * Email:1158577255@qq.com
 * <p>
 * detail: 列表弹窗的项点击事件
 */
public interface DialogItemSelectedListener {
    /*******
     * @param which  点击了哪一项
     * @param dialog 当前的弹窗对象
     * @detail 点击回调函数
     */
    public void onItemSelect(int which, DialogInterface dialog);
}
