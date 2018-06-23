package com.weilay.pos.adapter;

import java.util.List;

import com.framework.ui.BaseViewHolder;
import com.weilay.pos.R;
import com.weilay.pos.app.PayType;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PayAdapter extends com.framework.ui.BaseAdapter<PayType>{
	//private Activity mContext;
	private PayType currentSelect=PayType.WEIXIN;
	public PayAdapter(Activity context, List<PayType> datas) {
		super(context, datas);
	//	this.mContext=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1==null){
			arg1=LayoutInflater.from(context).inflate(R.layout.pay_item,null);
		}
		ImageView payIcon=BaseViewHolder.get(arg1,R.id.pay_icon);
		payIcon.setImageResource(datas.get(arg0).getIcon());
		payIcon.setEnabled(currentSelect==datas.get(arg0)?false:true);
		return arg1;
	}

	public void setSelect(PayType payType) {
		// TODO Auto-generated method stub
		currentSelect=payType;
		List temps=datas;
		notifyDataSetChange(temps);
		
	}

}
