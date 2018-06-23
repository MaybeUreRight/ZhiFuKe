package com.weilay.pos.listener;

import java.util.List;

/******
 * @detail 获取会员规则的接口
 * @author rxwu
 *
 */
public interface LoadMemberRulesListener {

	/******
	 * @detail 查询成功
	 * @param rules
	 *            查询到的规则
	 */
	public void loadSuccess(List<String> rules);

	/******
	 * @detail 查询失败
	 * @param msg
	 *            错误的信息
	 */
	public void loadFailed(String msg);
}
