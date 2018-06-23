package com.framework.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * Created by  rxwu on 2016/3/20 0020.
 * <p/>
 * Email：1158577255@qq.com
 * <p/>
 * Detail  自定义基础适配器
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    public List<T> datas = null;
    public Activity context;
    public BaseAdapter(Activity context,List<T> datas) {
        this.datas = datas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return null == datas ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChange(List datas) {
        if (datas == null) {
            this.datas = new ArrayList();
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
    }

}
