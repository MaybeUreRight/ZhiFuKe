package com.weilay.pos.adapter;

import java.util.ArrayList;
import java.util.List;

import com.weilay.pos.R;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.util.ConvertUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class VipInfoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<CouponEntity> mList_vc;
	private int last_position = -1;// 保存上次点击的item的position

	public VipInfoAdapter(Context context, List<CouponEntity> list_vc) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		mList_vc = list_vc;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList_vc.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList_vc.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	View lastView = null;
	VH vh;

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		vh = new VH();
		CouponEntity vc = mList_vc.get(position);
		String couponType = vc.getType();
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.vipinfo_item_layout, null);
			vh.coupon_amount = (TextView) view.findViewById(R.id.coupon_amount);
			vh.coupon_info = (TextView) view.findViewById(R.id.coupon_info);
			vh.coupon_type = (TextView) view.findViewById(R.id.coupon_type);
			vh.coupon_select_iv = (Button) view.findViewById(R.id.coupon_select_iv);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		double amount = Double.valueOf(vc.getAmount());
		switch (couponType) {
		case "DISCOUNT":
			vh.coupon_amount.setText(amount * 10 + "折");
			vh.coupon_type.setText("折扣券");
			break;

		case "CASH":
			vh.coupon_amount.setText(ConvertUtil.branchToYuan(amount) + "元");
			vh.coupon_type.setText("优惠券");
			break;
		}
		vh.coupon_info.setText(vc.getInfo());
		return view;
	}

	class VH {
		TextView coupon_amount, coupon_info, coupon_type;
		Button coupon_select_iv;
	}
	
	public void notityDataSetChange(List<CouponEntity>coupons){
		if(coupons==null){
			coupons=new ArrayList<>();
		}
		this.mList_vc=coupons;
		notifyDataSetChanged();
	}

}
