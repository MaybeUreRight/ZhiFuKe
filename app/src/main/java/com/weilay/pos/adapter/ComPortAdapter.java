
package com.weilay.pos.adapter;

import com.weilay.pos.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComPortAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] duankou_info;

	public ComPortAdapter(Context context, String[] entryValues) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		duankou_info = entryValues;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return duankou_info.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return duankou_info[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		VH vh =null;
		if (view == null) {
			view = inflater.inflate(R.layout.kexiantiaoshi_item_layout, null);
			vh= new VH();
			vh.duankouInfo_tv = (TextView) view.findViewById(R.id.duankou_info);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		vh.duankouInfo_tv.setText(duankou_info[position]);
		return view;
	}

	private class VH {
		TextView duankouInfo_tv;
	}
}
