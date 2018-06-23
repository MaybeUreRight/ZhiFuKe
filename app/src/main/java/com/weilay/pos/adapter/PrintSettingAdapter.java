package com.weilay.pos.adapter;

import com.weilay.pos.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PrintSettingAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] info;

	public PrintSettingAdapter(Context context, String[] usbPort) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		info = usbPort;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return info[position];
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
			view = inflater.inflate(R.layout.kexiantiaoshi_item_layout, null);
			vh=new VH();
			vh.usb_info = (TextView) view.findViewById(R.id.duankou_info);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		try {
			vh.usb_info.setText(info[position]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return view;
	}

	private class VH {
		TextView usb_info;
	}
}
