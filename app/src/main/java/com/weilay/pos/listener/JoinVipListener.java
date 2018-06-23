package com.weilay.pos.listener;

import com.weilay.pos.entity.JoinVipEntity;

public interface JoinVipListener {
	public void onSuc(JoinVipEntity info);
	public void onErr();
	public void on404(String msg);//没有上架会员卡
}
