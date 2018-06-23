package com.weilay.pos.adapter;

import com.weilay.pos.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShiftRecordAdapter extends BaseAdapter {
	private LayoutInflater inflater;

	private String[] date_array;
	public ShiftRecordAdapter(Context context, String[] strlist) {
		inflater = LayoutInflater.from(context);
		this.date_array = strlist;
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
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		String date  = date_array[position];
		
		VH vh = null;
		if (view == null) {
			
			view = inflater.inflate(R.layout.shift_record_item_layout, null);
			vh=new VH();
			vh.jb_date = (TextView) view.findViewById(R.id.jiaoban_date);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		vh.jb_date.setText(date);
		return view;
	}

	private class VH {
		TextView jb_date;
	}

}
