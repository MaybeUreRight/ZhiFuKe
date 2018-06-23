package com.weilay.pos.listener;

import com.weilay.pos.entity.PayTypeEntity;

public interface PayListener {

	// 支付成功
	public void onSuccess(PayTypeEntity printPayInfo);

	// 支付失败
	public void onFailed(String msg);

	// 支付中
	public void paying(PayTypeEntity paytype);

}
