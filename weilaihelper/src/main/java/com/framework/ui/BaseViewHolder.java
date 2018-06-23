package com.framework.ui;

import android.util.SparseArray;
import android.view.View;


/**
 * Created by rxwu on 2016/4/27.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:
 */
public class BaseViewHolder{
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder =

                (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new
                    SparseArray();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView =

                    view.findViewById(id);
            viewHolder.put(id,

                    childView);
        }
        return (T) childView;
    }
}
