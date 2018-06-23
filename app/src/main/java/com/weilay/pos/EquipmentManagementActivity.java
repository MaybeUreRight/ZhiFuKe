package com.weilay.pos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.utils.L;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.app.Client;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BarClose;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.T;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EquipmentManagementActivity extends TitleActivity {

	private final int DOWN_LOADING = 9999;
	private ImageView shebeijiance_iv, wangluoguanli_iv, shebeixinxi_iv,
			shebeizhuangtai_iv, xitonggengxin_iv, dayinshezhi_iv;
	private ProgressDialog progressDialog;
	private Client client;
	private String version;
	private String apk_path;// apk存放路径
	private String apkPublic_url = "Public/apk/F_manage.apk";
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 3:
				cancelProgress();
				final String path = GetUtil.getpath() + "/weilay"; // 安装包存放地址
				if (version != null) {
					double serverVersion = Double.valueOf(version);
					double localVersion = Double.valueOf(GetUtil
							.getversionCode(EquipmentManagementActivity.this));
					L.i("gg", "本地版本号:" + localVersion);
					if (serverVersion > localVersion) {
						DialogAsk.ask(EquipmentManagementActivity.this, "更新提示",
								"发现新版本!", "更新", "取消", new DialogAskListener() {

									@Override
									public void okClick(DialogInterface dialog) {
										// TODO Auto-generated method stub
										downApk(apkPublic_url, path);

										dialog.dismiss();
									}

									@Override
									public void cancelClick(
											DialogInterface dialog) {
										// TODO Auto-generated method stub
										L.i("gg", "取消更新!");
										dialog.dismiss();
									}
								});

					} else {
						DialogConfirm.ask(EquipmentManagementActivity.this,
								"更新提示", "当前为最新版本!", "确定",
								new DialogConfirmListener() {

									@Override
									public void okClick(DialogInterface dialog) {
										// TODO Auto-generated method stub

									}
								});
					}
				}
				break;
			case 4:
				cancelProgress();

				break;

			case 6:
				cancelProgress();
				// CmdForAndroid.shella("pm clear eu.chainfire.supersu");//
				// 清除root应用的缓存
				Intent intent = new Intent("com.example.weilayitem");
				intent.putExtra("path", apk_path);
				startActivity(Intent.createChooser(intent, "weilayitem"));
				break;
			case DOWN_LOADING:
				progressDialog.setProgress((int)msg.obj);
				progressDialog.setMessage("正在下载!请稍等..." + ((int)msg.obj) + "%");
				progressDialog.show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.equipment_management_layout);

		client = new Client(EquipmentManagementActivity.this);
		setTitle("设备管理");
		/* Title_item_tv.setOnTouchListener(new OnDbClickListener() {
	            @Override
	            public void onDBClick(View v, MotionEvent event) {
	            	  BarClose.showBar(mContext);
	            	  Intent intent =  new Intent(Settings.ACTION_SETTINGS);
	                  startActivity(intent);
	                  
	            }
	        });*/
		init();
		reg();
	}

	private void init() { 
		shebeijiance_iv = (ImageView) findViewById(R.id.shebeijiance_iv);
		wangluoguanli_iv = (ImageView) findViewById(R.id.wangluoguanli_iv);
		shebeixinxi_iv = (ImageView) findViewById(R.id.shebeixinxi_iv);
		shebeizhuangtai_iv = (ImageView) findViewById(R.id.shebeizhuangtai_iv);
		xitonggengxin_iv = (ImageView) findViewById(R.id.xitonggengxin_iv);
		dayinshezhi_iv = (ImageView) findViewById(R.id.dayinshezhi_iv);

		progressDialog = new ProgressDialog(this, R.style.loading_dialog);
		progressDialog
				.setIndeterminateDrawable(EquipmentManagementActivity.this
						.getResources().getDrawable(R.anim.loading1));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("正在检查!请稍等...");
	}

	private void reg() {
		shebeijiance_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(EquipmentManagementActivity.this,
						DetectionActivity.class);
				startActivity(i);
			}
		});
		wangluoguanli_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EquipmentManagementActivity.this,
						NetWorkActivity.class);
				startActivity(i);
			}
		});
		shebeixinxi_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EquipmentManagementActivity.this,
						EquipmentInfoActivity.class);
				startActivity(i);
			}
		});
		shebeizhuangtai_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// progressDialog.show();
				Intent intent = new Intent(EquipmentManagementActivity.this,
						EquipmentState.class);
				startActivity(intent);

			}
		});
		xitonggengxin_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// showLoading("正在获取版本信息!请稍等...");
				//if (WeiLayApplication.UPGRADE) {
					progressDialog.setMessage("正在获取版本信息!请稍等...");
					progressDialog.show();
					View dialog_view = progressDialog.getWindow()
							.getDecorView();
					GetUtil.setDialogText(dialog_view);
					getversion();// 获取服务器版本
//				} else {
//					T.showLong("无需升级");
//				}
			}
		});
		dayinshezhi_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EquipmentManagementActivity.this,
						PrintSettingActivity.class);
				startActivity(intent);
			}
		});

	}

	private void getversion() {
		Call call = client.down_version("Public/apk/F_manageversion.txt");
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException ioe) {
					if (ioe.toString() != null) {
						// handler.sendEmptyMessage(4);
						
					}
					sendMessage(4, "");
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					L.i("EquipmentMangement", "服务器版本为:" + res_info);
					version = res_info;
					sendMessage(3, "");

				}
				
			});
		} else {
			T.showCenter("网络异常!");
			sendMessage(4, "");
		}
	}

	private void downApk(final String url, final String path) {
		// showLoading("正在下载!请稍等...");
		progressDialog.setMessage("正在下载!请稍等...0%");
		progressDialog.show();

		Call call = client.down_apk(url, path);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					sendMessage(4, "");
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					InputStream is = null;
					byte[] buf = new byte[2048];
					int len = 0;
					FileOutputStream fos = null;
					try {
						is = res.body().byteStream();
						File file = new File(path);
						File apk_file = apk_file = new File(path + "/"
								+ GetUtil.getFileName(url));
						if (file.exists()) {
							if (apk_file.exists()) {// 判断文件是否存在
								apk_file.delete();
							}
						} else {
							file.mkdirs();
						}

						fos = new FileOutputStream(apk_file);
						long contentLength = res.body().contentLength();
						long remainLength = 0;
						while ((len = is.read(buf)) != -1) {
							if (contentLength > 0) {
								remainLength+=len;
								sendMessage(DOWN_LOADING,
										(int) (remainLength*100 / contentLength));
							}
							fos.write(buf, 0, len);
						}
						fos.flush();
						// 如果下载文件成功，第一个参数为文件的绝对路径
						apk_path = apk_file.getAbsolutePath();

						// handler.sendEmptyMessage(6);
						sendMessage(6, "");
					} catch (IOException e) {

					} finally {
						try {
							if (is != null)
								is.close();
						} catch (IOException e) {
						}
						try {
							if (fos != null)
								fos.close();
						} catch (IOException e) {
						}
					}
				}
			});
		} else {
			T.showCenter("网络异常!");
		}
	}

	private void cancelProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}

	private void sendMessage(int resId, Object obj) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = obj;
		handler.sendMessage(message);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BarClose.closeBar();
		L.e("-----------------------------------");
	}
}

