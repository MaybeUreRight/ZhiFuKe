package com.weilay.pos.listener;

import com.weilay.pos.entity.CouponEntity;

public interface GetCouponListener {
	// 接受数据
	public void onData(CouponEntity entity);

	// 失败的情况
	public void onFailed(String msg);
}
