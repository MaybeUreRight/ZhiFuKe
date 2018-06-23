package com.weilay.pos.adapter;

import com.weilay.pos.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PrintPaperAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] printpaper_info;

	public PrintPaperAdapter(Context context, String[] printpaper) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		printpaper_info = printpaper;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return printpaper_info.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return printpaper_info[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		VH vh = null;
		if (view == null) {
			vh=new VH();
			view = inflater.inflate(R.layout.printsetting_printpaper_layout,
					null);
			vh.printpaper_info = (TextView) view
					.findViewById(R.id.duankou_info);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		vh.printpaper_info.setText(printpaper_info[position]);
		return view;
	}

	private class VH {
		TextView printpaper_info;
	}
}
