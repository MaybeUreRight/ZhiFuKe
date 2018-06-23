
package com.weilay.pos.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rxwu on 2016/3/24.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail: 双击点击事件
 */
public abstract class OnDbClickListener implements View.OnTouchListener {
    private int count = 0;
    private long firClick = 0, secClick = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            count++;
            if (count == 1) {
                firClick = System.currentTimeMillis();

            } else if (count == 2) {
                secClick = System.currentTimeMillis();
                if (secClick - firClick < 1000) {
                    //双击事件
                    onDBClick(v, event);
                }
                count = 0;
                firClick = 0;
                secClick = 0;

            }
        }
        return true;
    }
    public abstract  void onDBClick(View v, MotionEvent event);
}

