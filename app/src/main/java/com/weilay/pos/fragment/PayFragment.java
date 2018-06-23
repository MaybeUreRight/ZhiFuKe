package com.weilay.pos.fragment;

import com.weilay.pos.R;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.util.T;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PayFragment extends mFragment {
	private static final String INTENT_KEY = "paytype_key";
	private PayTypeEntity mPay;

	public static PayFragment newInstance(PayTypeEntity pay) {
		PayFragment newFragment = new PayFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(INTENT_KEY, pay);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		if (args != null) {
			this.mPay = (PayTypeEntity) args.getSerializable(INTENT_KEY);
			if (mPay == null) {
				T.showCenter("没有支付权限");
			}
		}
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_pay, null);
		}
		return mRootView;
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
