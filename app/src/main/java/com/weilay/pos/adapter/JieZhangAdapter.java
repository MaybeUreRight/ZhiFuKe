package com.weilay.pos.adapter;

import java.util.List;

import com.weilay.pos.R;
import com.weilay.pos.entity.CheckOutEntity;
import com.weilay.pos.util.ConvertUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JieZhangAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CheckOutEntity> jz_list;

	public JieZhangAdapter(Context context, List<CheckOutEntity> jz_list) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.jz_list = jz_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jz_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jz_list.get(position);
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
		VH vh = null;
		CheckOutEntity jz = jz_list.get(position);

		if (view == null) {
			view = inflater.inflate(R.layout.checkout_item_layout, null);
			vh=new VH();
			vh.jz_date = (TextView) view.findViewById(R.id.jiezhang_date);
			vh.jz_danhao = (TextView) view.findViewById(R.id.jiezhang_danhao);
			vh.jz_paytype = (TextView) view.findViewById(R.id.jiezhang_paytype);
			vh.jz_amount = (TextView) view.findViewById(R.id.jiezhang_amount);
			vh.rl_date = (RelativeLayout) view.findViewById(R.id.rl_1);
			vh.jz_title_date = (TextView) view.findViewById(R.id.title_date);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		if (position > 0) {
			lastDay = jz_list.get(position - 1).getTxtime().substring(8, 10);
			thatDay = jz.getTxtime().substring(8, 10);
//			Log.i("gg", "lastDay:" + lastDay + ",thatDay:" + thatDay);
			if (lastDay.equals(thatDay)) {
				vh.rl_date.setVisibility(View.GONE);
			} else {
				vh.rl_date.setVisibility(View.VISIBLE);
			}
		} else {
			vh.rl_date.setVisibility(View.VISIBLE);
		}
		vh.jz_title_date.setText(jz.getTxtime().substring(0, 10));
		vh.jz_date.setText(jz.getTxtime());
		vh.jz_danhao.setText("商户单号:" + jz.getTx_no());
		vh.jz_amount.setText("￥" + ConvertUtil.getMoeny(jz.getTotalamountYuan()));
		try{
			vh.jz_paytype.setText(jz.getPayMethod().getValue());
		}catch(Exception ex)
		{}
		
		return view;
	}

	private class VH {
		TextView jz_date, jz_paytype, jz_danhao, jz_amount, jz_title_date;
		RelativeLayout rl_date;
	}
}
