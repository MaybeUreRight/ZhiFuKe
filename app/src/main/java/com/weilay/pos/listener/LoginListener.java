package com.weilay.pos.listener;

import com.weilay.pos.entity.OperatorEntity;

public interface LoginListener {
	public void loginSuccess(OperatorEntity operator);

	public void loginFailed(String msg,int code);
}
