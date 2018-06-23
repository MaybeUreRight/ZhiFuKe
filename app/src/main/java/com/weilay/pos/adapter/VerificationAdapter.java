package com.weilay.pos.adapter;

import java.util.List;

import com.weilay.pos.entity.VerificationSheet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VerificationAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<VerificationSheet> hxd_list;

	public VerificationAdapter(Context context, List<VerificationSheet> hxd_list) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.hxd_list = hxd_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hxd_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return hxd_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private String lastDay;
	private String thatDay;

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		VH vh =null;
		VerificationSheet dxd = hxd_list.get(position);
		if (view == null) {
			view = inflater
					.inflate(
							com.weilay.pos.R.layout.verificationsheet_item_layout,
							null);
			vh= new VH();
			vh.number_tv = (TextView) view
					.findViewById(com.weilay.pos.R.id.hxd_number);
			vh.date_tv = (TextView) view
					.findViewById(com.weilay.pos.R.id.hxd_date);
			vh.classify_rl = (RelativeLayout) view
					.findViewById(com.weilay.pos.R.id.rl_1);
			vh.title_date = (TextView) view
					.findViewById(com.weilay.pos.R.id.title_date);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		if (position > 0) {
			lastDay = hxd_list.get(position - 1).getCtime().substring(8, 10);
			thatDay = dxd.getCtime().substring(8, 10);
			Log.i("gg", "lastDay:" + lastDay + ",thatDay:" + thatDay);
			if (lastDay.equals(thatDay)) {
				vh.classify_rl.setVisibility(View.GONE);
			} else {
				vh.classify_rl.setVisibility(View.VISIBLE);
			}
		}
		vh.title_date.setText(dxd.getCtime().substring(0, 10));
		vh.number_tv.setText("核销单号:" + dxd.getCode());
		vh.date_tv.setText("核销日期:" + dxd.getCtime());
		return view;
	}

	private class VH {
		TextView number_tv, date_tv, title_date;
		RelativeLayout classify_rl;
	}

}
