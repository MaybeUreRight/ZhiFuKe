package com.weilay.pos;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import com.framework.utils.NetworkUtils;
import com.weilay.pos.app.Client;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.WifiUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class NetworkTestActivity extends TitleActivity {
	private TextView Network_info, testNetwork_tv;
	private ScrollView scrollView;
	private ConnectivityManager mConnectivityManager;
	private WifiManager wifiManager;
	private Client client;
	private boolean ping_state;
	private int level;// WiFI信号强度

	private Handler handler=new Handler(){public void handleMessage(android.os.Message msg){if(msg.what==0){Network_info.append("服务器连接成功!"+""+"\n");Network_info.append("网络检测结束!");}if(msg.what==1){Network_info.append("服务器连接失败!"+"\n");Network_info.append("网络检测结束!");}};};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.network_diagnostics_layout);
		setTitle("网络诊断");
		client = new Client(NetworkTestActivity.this);
		init();
		reg();
	}

	@SuppressLint("ServiceCast")
	private void init() {
		testNetwork_tv = (TextView) findViewById(R.id.test_network);
		Network_info = (TextView) findViewById(R.id.network_info);
		scrollView = (ScrollView) findViewById(R.id.scroll_test);

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

	}

	private void reg() {

		testNetwork_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// NetworkTest test = new NetworkTest();
				// if (test.isNetworkConnected(NetworkTestActivity.this)) {
				// SimpleDateFormat formatter = new SimpleDateFormat(
				// "yyyy年MM月dd日 HH:mm:ss ");
				// Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				// String date = formatter.format(curDate);
				// L.i("gg", "" + date);
				// Network_info.append(date + "\n");
				// Network_info.append("当前网络连接正常!.....\n");
				// } else {
				// Network_info.append("当前网络连接异常!.....\n");
				//
				// }
				// if (test.isWifiConnected(NetworkTestActivity.this)) {
				// Network_info.append("当前WIFI网络连接正常!.....\n\n");
				// } else {
				// Network_info.append("当前WIFI网络连接异常!.....\n\n");
				// }
				Network_info.setText("");
				String ipaddress = "";
				String netmask = "";
				String gateway = "";
				String dns1 = "";
				String dns2 = "";
				Network_info.append("[WIFI]:正在检测WIFI.....\n");
				if (wifiManager.isWifiEnabled()) {
					Network_info.append("[WIFI]:WIFI已开启.....\n");
					Network_info.append("[WIFI]:正在检测WIFI网络.....\n");
					if (WifiUtils.isWifiConnected(mContext)) {
						WifiInfo wifiInfo = wifiManager.getConnectionInfo();
						String wifiStr = "";
						level = wifiInfo.getRssi();
						if (level <= 0 && level >= (-50)) {
							wifiStr = "非常好";
						} else if (level < (-50) && level >= (-70)) {
							wifiStr = "良好";
						} else if (level < (-70) && level >= (-90)) {
							wifiStr = "弱";
						}
						Network_info.append("[WIFI]:当前WIFI网络连接正常!.....\n");
						Network_info.append("[WIFI]:MAC:" + wifiInfo.getMacAddress() + "\n");
						Network_info.append("[WIFI]:IP:" + FormatIP(wifiInfo.getIpAddress()) + "\n");
						Network_info.append("[WIFI]:WIFI名称:" + wifiInfo.getSSID() + "\n");
						Network_info.append("[WIFI]:信号强度:" + wifiStr + "\n\n");
					} else {
						Network_info.append("[WIFI]:当前WIFI网络未连接.....\n");
					}

				} else {
					Network_info.append("[WIFI]:当前WIFI未开启!.....\n\n");
				}
				Network_info.append("[网线]:正在检测网线.....\n");
				if(NetworkUtils.checkEthernet(mContext)){
					Network_info.append("[网线]:网线已连接.....\n");
				}else{
					Network_info.append("[网线]:网线未连接.....\n");
				}
				ipaddress =getLocalIpAddress()+"\n";// GetUtil.execCommand("getprop dhcp.eth0.ipaddress");
				if (!"".equals(ipaddress)) {
					netmask = GetUtil.execCommand("getprop dhcp.eth0.mask");
					gateway = GetUtil.execCommand("getprop dhcp.eth0.gateway");
					dns1 = GetUtil.execCommand("getprop net.eth0.dns1");
					dns2 = GetUtil.execCommand("getprop net.eth0.dns2");
					Network_info.append("[网线]:IP:" + ipaddress + "\n".trim());
					Network_info.append("[网线]:网关:" + gateway + "\n".trim());
					Network_info.append("[网线]:子网掩码:" + netmask + "\n".trim());
					Network_info.append("[网线]:dns1:" + dns1 + "\n".trim());
					Network_info.append("[网线]:dns2:" + dns2 + "\n\n".trim());
				} else {
					Network_info.append("[网线]:设备不存在.....\n\n");
				}
				Network_info.append("正在访问服务器.....\n");
				ping();
			}
		});
	};

	private String getLocalIpAddress() {
		  try {  
	            String ipv4;  
	            List<NetworkInterface>  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());  
	            for (NetworkInterface ni: nilist)   
	            {  
	                List<InetAddress>  ialist = Collections.list(ni.getInetAddresses());  
	                for (InetAddress address: ialist){  
	                    if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress()))   
	                    {   
	                    	
	                        return ipv4;  
	                    }  
	                }  
	   
	            }  
	   
	        } catch (SocketException ex) {  
	        }  
	        return null;  
	}

	public static String FormatIP(int IpAddress) {
		return Formatter.formatIpAddress(IpAddress);
	}

	private void ping() {
		FormBody.Builder builder = BaseParam.getParams();
		Call call = client.toserver(builder, "");
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException ioe) {
					// TODO Auto-generated method stub

					if (ioe.toString() != null) {
						handler.sendEmptyMessage(1);
					}
				}

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					// TODO Auto-generated method stub

					if (res != null) {
						// L.i("gg", "res:" + res.body().string());
						handler.sendEmptyMessage(0);

					}
				}
			});
		} else {
			handler.sendEmptyMessage(44);
			Network_info.append("服务器连接失败!" + "\n");
			Network_info.append("网络检测结束!");
			Toast.makeText(this, "网络异常!", 0).show();
		}
	}
}
