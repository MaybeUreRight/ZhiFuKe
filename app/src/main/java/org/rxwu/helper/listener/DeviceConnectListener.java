package org.rxwu.helper.listener;
	


/******
 * @detail 设备连接回调监听
 * @author Administrator
 *
 */
public interface DeviceConnectListener {
	/******
	 * @detail 设备连接成功
	 */
	public void connectSuccess();
	/******
	 * @detail 设备连接出错
	 * @msg 连接出错信息
	 */
	public void connectError(String msg);
	
	
	/******
	 * @detail 设备连接中..
	 */
	public void connecting();
	
	/******
	 * @detail 用户主动取消连接
	 */
	public void connectCancel();
}
