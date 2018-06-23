package com.weilay.pos.adapter;

import java.util.List;

import com.weilay.pos.R;
import com.weilay.pos.entity.CheckOutEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RefundPrintAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CheckOutEntity> rp_list;

	public RefundPrintAdapter(Context context, List<CheckOutEntity> rp_list) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.rp_list = rp_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rp_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rp_list.get(position);
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
		CheckOutEntity pr = rp_list.get(position);
		if (view == null) {
			view = inflater.inflate(R.layout.refundprint_item_layout, null);
			 vh=new VH();
			vh.rp_date = (TextView) view.findViewById(R.id.refundprint_date);
			vh.rp_paytype = (TextView) view.findViewById(R.id.refundprint_paytype);
			vh.rp_danhao = (TextView) view.findViewById(R.id.refundprint_danhao);
			vh.rp_refunddanhao = (TextView) view.findViewById(R.id.refundprint_refunddanhao);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		if (position > 0) {
			lastDay = rp_list.get(position - 1).getTxtime().substring(8, 10);
			thatDay = pr.getTxtime().substring(8, 10);
			if (lastDay.equals(thatDay)) {
				vh.rp_date.setVisibility(View.GONE);
			} else {
				vh.rp_date.setVisibility(View.VISIBLE);
			}
		} else {
			vh.rp_date.setVisibility(View.VISIBLE);
		}
		vh.rp_date.setText(pr.getTxtime() == null ? "" : pr.getTxtime().substring(0, 10));
		vh.rp_paytype.setText("退款方式:" + pr.getPaytype() == null ? ""
				: (pr.getPaytype().replaceAll("支付", "退款").replaceAll("退款宝退款", "支付宝退款")));
		vh.rp_danhao.setText("商户单号:" + pr.getTx_no());
		vh.rp_refunddanhao.setText("退款单号:" + pr.getRefundno());
		return view;
	}

	private class VH {
		TextView rp_date, rp_danhao, rp_refunddanhao, rp_paytype;
	}
}
