package com.weilay.pos.adapter;

import java.util.ArrayList;
import java.util.List;

import com.weilay.pos.R;
import com.weilay.pos.entity.RechargePreferEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/******
 * @detail 充值优惠金额
 * @author rxwu
 * @date 2016/07/25
 *
 */
public class RechargePreferAdapter extends BaseAdapter {
	private List<RechargePreferEntity> datas = new ArrayList<>();
	private Context mContext;

	public RechargePreferAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
	}

	@Override
	public RechargePreferEntity getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewholder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.member_recharge_prefer, null);
			viewholder = new ViewHolder();
			viewholder.preferitem = (TextView) arg1.findViewById(R.id.member_recharge_item_tv);
			arg1.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) arg1.getTag();
		}
		viewholder.preferitem.setText((arg0+1)+"、"+getItem(arg0).getName());
		return arg1;
	}

	/******
	 * @detail viewHolder
	 * @author Administrator
	 *
	 */
	class ViewHolder {
		TextView preferitem;
	}

	public void notifyDataSetChange(List<RechargePreferEntity> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}
}
