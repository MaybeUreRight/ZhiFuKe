package com.weilay.pos.fragment;

import java.util.ArrayList;
import java.util.List;

import com.weilay.pos.R;
import com.weilay.pos.adapter.CouponAdapter;
import com.weilay.pos.app.BaseFragment;
import com.weilay.pos.entity.CouponEntity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CouponTaskFragment extends BaseFragment {
	private ListView taskList;
	private CouponAdapter adapter = null;
	private List<CouponEntity> datas = new ArrayList<>();

	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_task, null);
		}
		taskList = getViewById(R.id.task_list);
		View emptyView = getViewById(R.id.empty_view);
		taskList.setEmptyView(emptyView);
		adapter = new CouponAdapter(mContext, datas);
		taskList.setAdapter(adapter);
		return mRootView;
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub

	}

	public void setDatas(List<CouponEntity> datas) {
		// TODO Auto-generated method stub
		if (datas != null && adapter != null) {
			adapter.notityDataSetChange(datas);
		}
	}

}
