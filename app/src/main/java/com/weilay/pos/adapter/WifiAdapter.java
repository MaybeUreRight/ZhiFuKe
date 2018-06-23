package com.weilay.pos.adapter;

import java.util.List;

import com.weilay.pos.R;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.util.WifiAdmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WifiAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ScanResult> scanResults;
	private WifiInfo currentWifi;
	private Context mContext;

	@SuppressWarnings("unchecked")
	public WifiAdapter(Context context, List<ScanResult> results) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		scanResults = results;
		this.currentWifi=WifiAdmin.getInstance(mContext).getWifiInfo();
		mContext=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (scanResults != null) {

			return scanResults.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return scanResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		VH vh = null;
		if (view == null) {
			vh = new VH();
			view = inflater.inflate(R.layout.wifi_item_layout, null);
			vh.wifi_name = (TextView) view.findViewById(R.id.wifi_name);
			vh.wifi_connected = (TextView) view
					.findViewById(R.id.wifi_connected);
			vh.wifi_level = (ImageView) view.findViewById(R.id.wifi_level);
			vh.layout = (RelativeLayout) view
					.findViewById(R.id.wifiReativeLayout);
			view.setTag(vh);
		} else {
			vh = (VH) view.getTag();
		}
		vh.wifi_connected.setText("");

		if (position % 2 == 0 | position == 0) {
			vh.layout.setBackgroundColor(Color.parseColor("#E6E6E6"));
		} else {
			vh.layout.setBackgroundColor(Color.WHITE);
		}
		if(scanResults!=null &&  scanResults.size()>=position){
			ScanResult sr = scanResults.get(position);
			String capabilities = sr.capabilities;
			if (!capabilities.contains("WPA") && !capabilities.contains("WPS")
					&& !capabilities.contains("WEP")
					&& capabilities.contains("ESS")) {
				vh.wifi_connected.setText("无密码");
			}
			if (!WeiLayApplication.getSp_wifi().getString(sr.SSID, "").equals("")) {
				vh.wifi_connected.setText("已保存密码");
			}
			/**
			 * Wifi名称需要加双引号,用\(转义字符)添加双引号
			 */
			if (currentWifi!=null && currentWifi.getSSID().equals("\"" + sr.SSID.toString() + "\"")) {
				vh.wifi_connected.setText("已连接");
			}else{
				vh.wifi_connected.setText("");
			}
			vh.wifi_name.setText("" + sr.SSID);

			// 判断信号强度，显示对应的指示图标
			if (Math.abs(sr.level) > 90) {
				vh.wifi_level.setImageResource(R.drawable.wifilevel0);
			} else if (Math.abs(sr.level) > 70) {
				vh.wifi_level.setImageResource(R.drawable.wifilevel1);
			} else if (Math.abs(sr.level) > 60) {
				vh.wifi_level.setImageResource(R.drawable.wifilevel2);
			} else if (Math.abs(sr.level) > 50) {
				vh.wifi_level.setImageResource(R.drawable.wifilevel3);
			} else {
				vh.wifi_level.setImageResource(R.drawable.wifilevel4);
			}
		}
		return view;
	}

	private class VH {
		TextView wifi_name, wifi_connected;
		ImageView wifi_level;
		RelativeLayout layout;
	}

	/*****
	 * @Detail 通知界面改变
	 */
	public void notifyDataSetChange(List<ScanResult> datas) {
		currentWifi=WifiAdmin.getInstance(mContext).getWifiInfo();
		this.scanResults=datas;
		notifyDataSetChanged();
	}
}
