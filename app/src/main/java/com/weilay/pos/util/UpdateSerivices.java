package com.weilay.pos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.utils.L;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.R;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.titleactivity.BaseActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateSerivices {
	private static boolean upgrade = false;
	public BaseActivity mActivity;
	private final int DOWN_LOADING = 9999;
	private Client client;
	private String version;
	private String apk_path;// apk存放路径
	final String path = GetUtil.getpath() + "/weilay";
	private String apk_url = Config.apk_url;   
	private String version_url = Config.version_url;
	private ProgressDialog progressDialog;

	public UpdateSerivices(BaseActivity act) {
		this.mActivity = act;
		client = new Client(act);
		progressDialog = new ProgressDialog(mActivity, R.style.loading_dialog);
		progressDialog.setMax(100);
		progressDialog.setIndeterminateDrawable(mActivity.getResources().getDrawable(R.anim.loading1));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("正在检查!请稍等...");
	}

	/*****
	 * @Detail 检查版本更新
	 */
	public void checkVersion() {
		if (upgrade)
			// 如果正在升级中，或者应用的升级模式选择的是手动升级，那么不自动提醒
			return;
		upgrade = true;
		Call call = client.down_version(version_url);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException ioe) {
					// TODO Auto-generated method stub
					if (ioe.toString() != null) {
						// handler.sendEmptyMessage(4);
						sendMessage(4, "");
					}
				}

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					String res_info = res.body().string();
					L.i("gg", "服务器版本为:" + res_info);
					version = res_info;
					// handler.sendEmptyMessage(3);
					sendMessage(3, "");
				}

			});
		} else {

		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 3:
				final String path = GetUtil.getpath() + "/weilay"; // 安装包存放地址
				if (version != null) {
					double serverVersion = Double.valueOf(version);
					double localVersion = Double.valueOf(GetUtil.getversionCode(mActivity));
					if (serverVersion > localVersion) {
						upgrade = true;
						upgrade(path);
					} else {
						upgrade = false;
					}
				} else {
					upgrade = false;
				}

				break;
			case 4:
				upgrade = false;
				break;
			case 6:
				cancelProgress();
				upgrade = true;
				if (mActivity.isFinishing()) {
					return;
				}
				upgrade = false;// 升级完成
				Intent intent = new Intent("com.example.weilayitem");
				intent.putExtra("path", apk_path);
				mActivity.startActivity(Intent.createChooser(intent, "weilayitem"));
				break;
			case DOWN_LOADING:
				progressDialog.setProgress((int) msg.obj);
				progressDialog.setMessage("正在下载!请稍等..." + ((int) msg.obj) + "%");
				progressDialog.show();
				break;
			}
		};
	};

	// 强制升级
	private void upgrade(final String path) {
		boolean force = PosDefine.CONSTANT_APGRADE_AUTO.equals(Utils.getCurOperator().getUpgradetype());
		if (force) {
			DialogAsk.ask(mActivity, "升级提示", "发现新的版本，请升级（也可以到设备设备管理中的系统更新手动升级）", "马上升级", "暂不升级",
					new DialogAskListener() {

						@Override
						public void okClick(DialogInterface dialog) {
							downApk(apk_url, path);
						}

						@Override
						public void cancelClick(DialogInterface dialog) {
							// TODO Auto-generated method stub

						}
					});
			
		} else {
			DialogConfirm.ask(mActivity, "升级提示", "发现新的版本,请升级", "马上升级", new DialogConfirmListener() {

				@Override
				public void okClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					downApk(apk_url, path);
				}
			});
		}

	}

	private void cancelProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}

	/*****
	 * @detail 下载apk
	 * @return void
	 * @param
	 * @detail
	 */
	private void downApk(final String url, final String path) {
		L.i("正从" + url + "下载新的版本");
		progressDialog.setMessage("正在下载!请稍等...0%");
		progressDialog.show();
		View dialog_view = progressDialog.getWindow()
				.getDecorView();
		GetUtil.setDialogText(dialog_view);
		Call call = client.down_apk(url, path);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					sendMessage(4, "");
				}

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					// TODO Auto-generated method stub
					InputStream is = null;
					byte[] buf = new byte[2048];
					int len = 0;
					FileOutputStream fos = null;
					try {
						is = res.body().byteStream();
						File file = new File(path);
						File apk_file = new File(path + "/" + GetUtil.getFileName(url));
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
							if (len > 0) {
								remainLength += len;
								sendMessage(DOWN_LOADING, (int) (remainLength * 100 / contentLength));
							}
							fos.write(buf, 0, len);
						}
						fos.flush();
						// 如果下载文件成功，第一个参数为文件的绝对路径
						apk_path = apk_file.getAbsolutePath();
						// handler.sendEmptyMessage(6);
						sendMessage(6, "");
					} catch (IOException e) {
						upgrade = false;
					} finally {
						try {
							if (is != null)
								is.close();
							if (fos != null)
								fos.close();
						} catch (IOException e) {
						}
					}
				}
			});
		} else {

		}
	}

	private void sendMessage(int resId, Object obj) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = obj;
		handler.sendMessage(message);
	}

}
