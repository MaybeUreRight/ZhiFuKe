package com.weilay.pos.listener;

public interface RefundListener {
	public void onSuc();
	public void onErr(String msg);
	public void onRefunded(String msg);//已经退过款
	public void on403();//没有退款权限
}
