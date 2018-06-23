package com.weilay.pos.adapter;

import com.weilay.pos.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenDianAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] date_array;
	private String[] jine;



	public MenDianAdapter(Context context, String[] date_array) {
		inflater = LayoutInflater.from(context);
		this.date_array = date_array;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return date_array.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return date_array[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		VH vh = null;
		
		if (view == null) {
			view = inflater.inflate(R.layout.mendian_item_layout, null);
			vh=new VH();
			vh.mendian_date = (TextView) view.findViewById(R.id.mendian_date);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		vh.mendian_date.setText(date_array[position]);
		return view;
	}

	private class VH {
		TextView mendian_date;
	}

}
