package com.weilay.pos.listener;

/******
 * @detail 任务接受监听
 * @author rxwu
 * @date 2016/08/10
 *
 */
public interface TaskReceiverListener {
	public void receiver();

	public void cancel();
}
