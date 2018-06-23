package com.weilay.pos;

import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.GetUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class NetworkInfoActivity extends TitleActivity {
	private TextView network_type, network_ip, network_netmask,
			network_gateway, network_dns1, network_dns2;
	private WifiManager wifiManager;
	private SharedPreferences sp;
	@SuppressLint("ServiceCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.networkinfo_layout);
		setTitle("网络信息");
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		sp = ((WeiLayApplication) getApplication()).getSp_ip();
		init();
		reg();
	}

	private void init() {
		network_type = (TextView) findViewById(R.id.networkinfo_type);
		network_ip = (TextView) findViewById(R.id.networkinfo_ip);
		network_netmask = (TextView) findViewById(R.id.networkinfo_netmask);
		network_gateway = (TextView) findViewById(R.id.networkinfo_gateway);
		network_dns1 = (TextView) findViewById(R.id.networkinfo_dns1);
		network_dns2 = (TextView) findViewById(R.id.networkinfo_dns2);
		String ipaddress = null;
		String netmask = null;
		String gateway = null;
		String dns1 = null;
		String dns2 = null;
		if (getWifiManager()) {
			network_type.setText("当前的网络连接方式为:Wifi");
			ipaddress = GetUtil.execCommand("getprop dhcp.wlan0.ipaddress");
			netmask = GetUtil.execCommand("getprop dhcp.wlan0.mask");
			gateway = GetUtil.execCommand("getprop dhcp.wlan0.gateway");
			dns1 = GetUtil.execCommand("getprop dhcp.wlan0.dns1");
			dns2 = GetUtil.execCommand("getprop dhcp.wlan0.dns2");

		} else if (sp.getBoolean("wirednetwork", false)) {
			network_type.setText("当前的网络连接方式为:以太网");
			ipaddress = sp.getString("ipaddress", "0");
			netmask = sp.getString("netmask", "0");
			gateway = sp.getString("gateway", "0");
			dns1 = sp.getString("dns1", "0");
			dns2 = sp.getString("dns2", "0");
		} else {
			network_type.setText("当前的网络连接方式为:以太网");
			ipaddress = GetUtil.execCommand("getprop dhcp.eth0.ipaddress");
			netmask = GetUtil.execCommand("getprop dhcp.eth0.mask");
			gateway = GetUtil.execCommand("getprop dhcp.eth0.gateway");
			dns1 = GetUtil.execCommand("getprop dhcp.eth0.dns1");
			dns2 = GetUtil.execCommand("getprop dhcp.eth0.dns2");
		}
		network_ip.setText(ipaddress);
		network_netmask.setText(netmask);
		network_gateway.setText(gateway);
		network_dns1.setText(dns1);
		network_dns2.setText(dns2);
	}

	private void reg() {

	}

	private boolean getWifiManager() {
		if (wifiManager.isWifiEnabled()) {
			return true;
		}
		return false;
	}
}
