package com.weilay.pos;

import java.util.ArrayList;
import java.util.List;

import com.weilay.dialog.WifiDialog;
import com.weilay.pos.adapter.WifiAdapter;
import com.weilay.pos.entity.WifiEntity;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.WifiAdmin;

import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WifiActivity extends TitleActivity implements OnClickListener {

	

	private ListView wifi_listview;
	private WifiAdapter wifiAdapter;
	private List<ScanResult> scanList = new ArrayList<ScanResult>();
	private ScanResult sr;
	private WifiAdmin wifiadmin;

	private WifiDialog wifi_dialog;

	private TextView wifi_shuaxin;
	private ImageView addBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.wifi_layout);
		setTitle("无线网络");
		wifiadmin = WifiAdmin.getInstance(this);
		wifiadmin.OpenWifi();
		if (getIntent().getBooleanExtra("isLogin", false)) {// 如果是从登陆界面跳转进来,则隐藏home按钮
			getLl_homel().setVisibility(View.INVISIBLE);
			getLl_message().setVisibility(View.INVISIBLE);
		} else {
			getLl_homel().setVisibility(View.VISIBLE);
			getLl_message().setVisibility(View.VISIBLE);
		}
		init();
		loadWifis(false, true);
		refershHanlder.sendEmptyMessageDelayed(1,5000);
		reg();
	}

	/******
	 * @Detail 初始化控件
	 * @return void
	 * @param
	 * @detail
	 */
	private void init() {
		addBtn = (ImageView) findViewById(R.id.add_wifi);
		wifi_listview = (ListView) findViewById(R.id.wifi_listview);
		wifiAdapter = new WifiAdapter(WifiActivity.this, scanList);
		wifi_listview.setAdapter(wifiAdapter);
		TextView empty_view = (TextView) findViewById(R.id.empty_view);
		wifi_listview.setEmptyView(empty_view);
		wifi_shuaxin = (TextView) findViewById(R.id.wifi_shuaxin);

	}

	private void reg() {
		addBtn.setVisibility(View.VISIBLE);
		addBtn.setOnClickListener(this);
		wifi_shuaxin.setOnClickListener(this);
		
		wifi_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				sr = scanList.get(position);
				WifiEntity wifi=WifiEntity.parseScanResult(sr);
				wifi_dialog=new WifiDialog(mContext,wifi);
				wifi_dialog.setTitle("当前WIFI:" + sr.SSID);
				wifi_dialog.show();
			}
		});
	}
	

	/*****
	 * 定时刷新
	 */
	Handler refershHanlder = new Handler() {
		public void handleMessage(Message msg) {
			loadWifis(true,false);
			sendEmptyMessageDelayed(0, 5000);// 自己调用自己，实现定时刷新
		};
	};

	/****
	 * @return void
	 * @param tip
	 *            是否显示扫描中
	 * @detail
	 */
	private void loadWifis(boolean isRefresh,boolean tip) {
		if (tip) {
			showLoading(isRefresh?"Wifi刷新中!请稍候...":"Wifi扫描中!请稍候...");
		}
		new AsyncTask<Void, Void, List<ScanResult>>() {
			@Override
			protected List<ScanResult> doInBackground(Void... arg0) {
				 try {  
                     Thread.sleep(1000);  
                 } catch (Exception e) {  
                     e.printStackTrace();  
                 }  
				int count=0;
				if(scanList!=null){
					scanList.clear();
				}
				while(count<3 && (scanList==null || scanList.size()==0)){
					scanList = wifiadmin.getWifiList();
					count++;
				}
				count=0;
				return scanList;
			}
			@Override
			protected void onPostExecute(List<ScanResult> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				wifiAdapter.notifyDataSetChange(scanList);
				stopLoading();
			}
		}.execute();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (wifi_dialog != null) {
			wifi_dialog.dismiss();
		}
		if(refershHanlder!=null){
			refershHanlder.removeMessages(0);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadWifis(false,true);
	}

	


	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.add_wifi:
			wifi_dialog=new WifiDialog(mContext,null);
			wifi_dialog.setTitle("手动添加隐藏wifi");
			wifi_dialog.show();
			break;
		case R.id.wifi_shuaxin:
			loadWifis(true,true);
			break;
		default:
			break;
		}

	}

}
