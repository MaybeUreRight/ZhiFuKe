package com.weilay.pos.fragment;

import java.util.ArrayList;

import com.weilay.pos.R;
import com.weilay.pos.SendTicketBeginAvtivity;
import com.weilay.pos.adapter.SendTicketAdapter;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.entity.SendTicketInfo;
import com.weilay.pos.titleactivity.SendTickectFragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class NormalCouponFragment extends SendTickectFragment {
	private ListView nc_listview;
	private ArrayList<SendTicketInfo> list_sti;
	private ArrayList<SendTicketInfo> normalList;
	private SendTicketInfo sti;
	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.sendticket_layout, container, false);
		nc_listview = (ListView) view.findViewById(R.id.sendticket_listview);
		TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
		nc_listview.setEmptyView(emptyView);
		list_sti = getArguments().getParcelableArrayList("normalcoupon");
		normalList = new ArrayList<SendTicketInfo>();
		if (list_sti != null) {
			for (int i = 0; i < list_sti.size(); i++) {
				SendTicketInfo for_sti = list_sti.get(i);
				switch (for_sti.getType()) {
				case CardTypeEnum.FRIEND_CASH:

					break;
				case CardTypeEnum.FRIEND_GIFT:

					break;
				default:
					normalList.add(for_sti);
					break;
				}

			}
		}

		SendTicketAdapter adapter = new SendTicketAdapter(getActivity(), normalList);
		nc_listview.setAdapter(adapter);
		nc_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				sti = normalList.get(position);
				// view.setTag(position);
				// listview_init(view);
				if (sti != null) {
					Intent intent = new Intent(getActivity(), SendTicketBeginAvtivity.class);
					// Bundle bundle = new Bundle();
					// bundle.putParcelable("sti", sti);
					intent.putExtra("sti", sti);
					startActivity(intent);
				}
			}
		});
		return view;
	}
	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		
	}
	
	
}
