package com.weilay.listener;

import android.content.DialogInterface;

/**
 * Created by  rxwu on 2016/3/20 0020.
 * <p/>
 * Email：1158577255@qq.com
 * <p/>
 * Detail 提问弹出层的回调接口
 */
public interface DialogAskListener {
    public void okClick(DialogInterface dialog);
    public void cancelClick(DialogInterface dialog);
}
