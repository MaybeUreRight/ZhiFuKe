package com.weilay.pos.listener;


import com.weilay.pos.entity.PayTypeEntity;

/******
 * @detail 卡券核销
 * @author Administrator
 *
 */
public interface CardUseListener {
	/*****
	 * @detail 使用成功
	 */
	public void success(PayTypeEntity result);

	/******
	 * @Detail 使用失败
	 */
	public void failed(String msg);
	

}
