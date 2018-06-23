package com.weilay.pos.adapter;

import java.util.List;

import com.framework.ui.BaseAdapter;
import com.weilay.pos.R;
import com.weilay.pos.entity.MemberTimesLevelEntity;
import com.weilay.pos.util.ConvertUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/******
 * File Name:TimeRechargeAdapter.java
 * Package:com.weilay.pos.adapter	
 * Date: 2017年4月13日下午1:09:53
 * Author: rxwu
 * Detail:TimeRechargeAdapter 会员卡次数充值卡的适配器
 */
public class TimeRechargeAdapter extends BaseAdapter<MemberTimesLevelEntity>{
	
	public TimeRechargeAdapter(Activity context, List<MemberTimesLevelEntity> datas) {
		super(context, datas);
	}	
	private int selectIndex=-1;
	public void setSelect(int index){
		this.selectIndex=index;
		notifyDataSetInvalidated();
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder=null;
		if(arg1==null){
			arg1=LayoutInflater.from(context).inflate(R.layout.item_time_recharge, arg2,false);
			holder=new ViewHolder();
			holder.vipIcon=(ImageView)arg1.findViewById(R.id.vip_icon);
			holder.expdateTv=(TextView)arg1.findViewById(R.id.expdate_tv);
			holder.timeTv=(TextView)arg1.findViewById(R.id.time_tv);
			holder.moneyTv=(TextView)arg1.findViewById(R.id.money_tv);
			arg1.setTag(holder);
		}else{
			holder=(ViewHolder)arg1.getTag();
		}
		MemberTimesLevelEntity item=getItem(arg0);
		holder.vipIcon.setEnabled(arg0==selectIndex);
		holder.expdateTv.setText("有效期:"+item.getPeriod());
		holder.timeTv.setText(item.getTimes()+"次");
		holder.moneyTv.setText(ConvertUtil.branchToYuan(item.getLevel_amount())+"元");
		return arg1;
	}
	class ViewHolder{
		ImageView vipIcon;
		TextView timeTv,expdateTv,moneyTv;
	}
	

}
