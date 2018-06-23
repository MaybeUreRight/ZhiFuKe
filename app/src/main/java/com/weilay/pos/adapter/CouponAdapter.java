package com.weilay.pos.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.weilay.dialog.SendCardDialog;
import com.weilay.pos.R;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.ConvertUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CouponAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<CouponEntity> datas;
	private BaseActivity mActivity;
	private Context context;

	public CouponAdapter(BaseActivity context, List<CouponEntity> list_sti) {
		inflater = LayoutInflater.from(context);
		this.datas = list_sti;
		this.context = context;
		this.mActivity = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public CouponEntity getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		VH vh = null;
		final CouponEntity sti = getItem(position);
		if (view == null) {
			view = inflater.inflate(R.layout.item_coupon, null);
			vh=new VH();
			vh.cardinfo_tv = (TextView) view.findViewById(R.id.cardinfo_tv);
			vh.merchantlogo_iv = (ImageView) view.findViewById(R.id.merchantlogo_iv);

			vh.merchantname_tv = (TextView) view.findViewById(R.id.merchantname_tv);
			vh.sendticket_number = (TextView) view.findViewById(R.id.sendticket_number);
			vh.sendticket_stock = (TextView) view.findViewById(R.id.sendticket_stock);
			vh.sendticket_deadline = (TextView) view.findViewById(R.id.sendticket_deadline);
			vh.layout = (RelativeLayout) view.findViewById(R.id.rl_1);
			vh.preferTv = (TextView) view.findViewById(R.id.prefer_tv);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		setLayout(vh, sti);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new SendCardDialog(sti).show(mActivity.getSupportFragmentManager(), "发券");
			}
		});
		return view;
	}

	private void setLayout(VH vh, final CouponEntity sti) {
		vh.cardinfo_tv.setText(sti.getTitle());
		vh.merchantname_tv.setText(sti.getMerchantname());
//		ImageLoader.getInstance().displayImage(UrlDefine.BASE_URL + sti.getMerchantlogo(), vh.merchantlogo_iv);
		Glide.with(context).load(UrlDefine.BASE_URL + sti.getMerchantlogo()).into(vh.merchantlogo_iv);
		vh.sendticket_stock.setText("库存:" + sti.getStock() + "张");
		vh.sendticket_deadline.setText("有效期:" + sti.getDeadline());
		// 发券页面隐藏发券的奖励金额 update by rxwu
		vh.preferTv.setVisibility(View.INVISIBLE);
		vh.preferTv.setText("任务奖励：每领一张可获得" + ConvertUtil.branchToYuan(sti.getMerchantcommission()) + "元奖励");
	}

	class VH {
		ImageView merchantlogo_iv, sendticket_reduce, sendticket_increase;
		TextView merchantname_tv, cardinfo_tv, sendticket_number, sendticket_stock, sendticket_deadline, preferTv;
		RelativeLayout layout;
	}

	public void notityDataSetChange(List<CouponEntity> datas) {
		// TODO Auto-generated method stub
		if (datas == null) {
			datas = new ArrayList<>();
		}
		this.datas = datas;
		notifyDataSetChanged();
	}
}
