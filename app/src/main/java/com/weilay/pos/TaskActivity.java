package com.weilay.pos;

import java.util.ArrayList;
import java.util.List;

import com.framework.utils.L;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.db.CouponDBHelper;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.fragment.CouponTaskFragment;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/*******
 * @detail 任务卡券页面（卡券互投）
 * @author Administrator
 *
 */
public class TaskActivity extends NotTitleActivity implements OnClickListener {
	private CouponTaskFragment friendCouponFragment;// , couponFragment;//
													// 朋友券，普通券
	private List<CouponEntity> datas = new ArrayList<>();
	private List<CouponEntity> firend_datas = new ArrayList<>();
	private List<CouponEntity> default_datas = new ArrayList<>();

	private static final int FRIEND_COUPON = 1;// 朋友卡券
	private static final int DEFAULT_COUPON = 2;// 普通卡券

	private TextView normel_tv, firend_tv;
	private View normalLine, friendLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_task);
		initViews();
		initDatas();
		initEvents();
	}

	/*****
	 * @detail 初始化视图
	 */
	private void initViews() {
		((TextView) findViewById(R.id.common_head_tv)).setText("发券任务");
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (friendCouponFragment == null) {
			friendCouponFragment = new CouponTaskFragment();
			transaction.add(R.id.coupon_fragment, friendCouponFragment);
		}
		transaction.commitAllowingStateLoss();
		normel_tv = (TextView) findViewById(R.id.normal_coupon);
		firend_tv = (TextView) findViewById(R.id.friend_coupon);
		normalLine = findViewById(R.id.normal_view);
		friendLine = findViewById(R.id.friend_view);
		setSelect(FRIEND_COUPON);
	}

	private StringBuffer coupons = new StringBuffer();

	/*****
	 * @detail 初始化数据
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		// update by rxwu at 2016/08/10 修改卡券互投的获取方式
		// showLoading("正在获取卡券任务");
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				L.d("--init coupons num is:" + (datas == null ? 0 : datas.size()));
				List<String> temps = CouponDBHelper.findAllMessages();
				if (temps == null || temps.size() <= 0) {
					return null;
				}
				for (String item : temps) {
					coupons.append("," + item);
				}
				L.d("--pushservices 查询的卡券是：" + coupons);
				return coupons.substring(1, coupons.length());
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				loadCoupons(result);

			}
		}.execute();
	}

	/******
	 * @detail 读取所有的卡券任务
	 * @param cids
	 */
	public void loadCoupons(String cids) {
		if (cids == null || TextUtils.isEmpty(cids)) {
			T.showCenter("暂无卡券任务");
			return;
		}
		Utils.loadTaskCoupon(this, cids, new OnDataListener() {

			@Override
			public void onFailed(String msg) {
				// TODO Auto-generated method stub
				T.showCenter("暂无卡券任务");
			}

			@Override
			public void onData(Object obj) {
				// TODO Auto-generated method stub
				datas = (List<CouponEntity>) obj;
				if (datas == null) {
					return;
				}
				firend_datas.clear();
				default_datas.clear();
				for (CouponEntity item : datas) {
					if(item!=null){
						if(item.getStock()==0){
							//库存为0，自动清除
							T.showCenter(item.getCardinfo()+"已经过期或者库存不足");
							CouponDBHelper.delteCoupon(item);
							return;
						}
						switch (item.getType()) {
						case CardTypeEnum.FRIEND_CASH:
						case CardTypeEnum.FRIEND_GIFT:
							firend_datas.add(item);
							break;
						default:
							default_datas.add(item);
							break;
						}
					}
					
				}
				setDatas();
			}
		});
	}

	private void initEvents() {
		normel_tv.setOnClickListener(this);
		firend_tv.setOnClickListener(this);
	}

	/****
	 * @detail 设置当前显示的页面
	 * @param what
	 */
	private void setSelect(int what) {
		initialise();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		hideFragment(transaction);
		switch (what) {
		case FRIEND_COUPON:
			firend_tv.setTextColor(Color.GREEN);
			friendLine.setVisibility(View.VISIBLE);
			transaction.show(friendCouponFragment);
			break;
		/*
		 * default: normel_tv.setTextColor(Color.GREEN);
		 * normalLine.setVisibility(View.VISIBLE);
		 * transaction.show(couponFragment); break;
		 */
		}
		transaction.commitAllowingStateLoss();
		setDatas();
	}

	@SuppressLint("ResourceAsColor")
	private void initialise() {
		normel_tv.setTextColor(Color.WHITE);
		firend_tv.setTextColor(Color.WHITE);
		normalLine.setVisibility(View.GONE);
		friendLine.setVisibility(View.GONE);
	}

	private void setDatas() {
		if (friendCouponFragment != null) {
			friendCouponFragment.setDatas(firend_datas);
		}
	}

	private void hideFragment(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (friendCouponFragment != null) {
			transaction.hide(friendCouponFragment);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.normal_coupon:
			setSelect(DEFAULT_COUPON);
			break;
		case R.id.friend_coupon:
			setSelect(FRIEND_COUPON);
			break;
		}
	}

}
