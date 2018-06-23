package com.framework.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/****
 * @detail network utils
 * @author Administrator
 *
 */
public class NetworkUtils {
	/*****
	 * @detail check network useful
	 * @return if network useable return true,then return false
	 */
	public static boolean isNetworkable(Context context){
		ConnectivityManager mConnMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnMan.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		return info.isConnected();
	}
	/*********
	 * @detail 检查是否有接入网线
	 * @param context
	 * @return
	 */
	public static boolean checkEthernet(Context context)
	{
	        ConnectivityManager conn =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = conn.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	        return networkInfo.isConnected();
	}
}
