package org.rxwu.helper.listener;


/*******
 * @detail 副屏显示监听类
 * @author rxwu
 * @date 2016.4.22
 *
 */
public interface CustomDisplayListener {
	/*****
	 *@detail 显示成功
	 */
	public void showSuccess();
	/*****
	 *@detail 显示失败
	 *@param msg 错误的信息
	 */
	public void showError(String msg);
}
