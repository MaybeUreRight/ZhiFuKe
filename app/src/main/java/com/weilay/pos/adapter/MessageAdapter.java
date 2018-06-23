package com.weilay.pos.adapter;

import java.util.ArrayList;
import java.util.List;

import com.framework.ui.DialogAsk;
import com.framework.utils.TimeUtils;
import com.weilay.listener.DialogAskListener;
import com.weilay.pos.R;
import com.weilay.pos.app.MessageType;
import com.weilay.pos.db.MessageDBHelper;
import com.weilay.pos.entity.MessageEntity;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.ServerTimeUtils;
import com.weilay.pos.util.T;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private Activity mContext;
	private List<MessageEntity> datas;

	public MessageAdapter(Activity context, List<MessageEntity> datas) {
		this.mContext = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
	}

	@Override
	public MessageEntity getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	int count=0;
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		final MessageEntity sti = getItem(arg0);
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_message, null);
			vh = new ViewHolder();
			vh.noTv=(TextView)view.findViewById(R.id.message_id_tv);
			vh.titleTv = (TextView) view.findViewById(R.id.message_title_tv);
			vh.contentTv = (TextView) view.findViewById(R.id.message_content_tv);
			vh.timeTv = (TextView) view.findViewById(R.id.message_time_tv);
			vh.deleteIv = (ImageView) view.findViewById(R.id.message_delete_iv);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		try{
			++count;
			vh.noTv.setText(count+".");
			switch (sti.getType()) {
			case MessageType.GLOBAL:
				vh.titleTv.setText(sti.getTitle() == null ? "" : sti.getTitle());
				vh.contentTv.setText(sti.getData());
				break;
			case MessageType.CARD:
				double price = ConvertUtil.getMoeny(sti.getData());
				String priceStr = "每领一张可获得" + ConvertUtil.branchToYuan(price) + "元奖励";
				vh.titleTv.setText("你有新的卡券信息");
				vh.contentTv.setText(priceStr);
				break;
			default:
				break;
			}
			vh.timeTv.setText("" + TimeUtils.parseComeTime(ServerTimeUtils.server_time, sti.getTime() * 1000));
			vh.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub\
					DialogAsk.ask(mContext, "删除提示", "确定要删除这条信息吗?", "确定", "取消", new DialogAskListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							// TODO Auto-generated method stub
							MessageDBHelper.deleteMessage(sti);
							datas.remove(sti);
							notifyDataSetChange(datas);
							T.showShort("删除成功");
						}

						@Override
						public void cancelClick(DialogInterface dialog) {
						}
					});

				}
			});
		}catch(Exception ex){
			
		}
		
		return view;
	}

	class ViewHolder {
		TextView titleTv, contentTv, timeTv,noTv;
		ImageView deleteIv;// , stateTv;
	}

	/*****
	 * @detail 通知数据
	 * @param datas
	 */
	public void notifyDataSetChange(List<MessageEntity> datas) {
		if (datas == null) {
			datas = new ArrayList<>();
		}
		this.datas = datas;
		notifyDataSetChanged();
	}

}
