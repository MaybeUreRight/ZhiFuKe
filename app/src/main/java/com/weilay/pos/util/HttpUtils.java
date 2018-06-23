package com.weilay.pos.util;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weilay.pos.app.Client;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.listener.BaseResponseListener;
import com.weilay.pos.listener.NetCodeEnum;
import com.weilay.pos.listener.ResponseImageListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.listener.StringResponseListener;
import com.weilay.pos.titleactivity.BaseActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/********
 * @detail 网络请求工具封装
 * @author Administrator
 * 
 */
public class HttpUtils {
	private static Client client;

	public static Client getClient() {
		if (client == null) {
			client = new Client(WeiLayApplication.app);
		}
		return client;
	}

	public static void downloadImage(final BaseActivity activity,
			final String url, final ResponseImageListener listener) {
		FormBody.Builder params = BaseParam.getBaseParams();
		Call call = getClient().toserver(params, url);

		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, final IOException ioe) {
					// TODO Auto-generated method stub
					Log.i("gg", "onFailure:" + ioe.toString() + "---url:" + url);
					if (ioe instanceof java.io.InterruptedIOException) {
						sendError(activity, NetCodeEnum.TIMEOUT, listener);
					} else if (ioe instanceof UnknownHostException) {
						sendError(activity, NetCodeEnum.UNKNOWHOST, listener);
					} else {
						sendError(activity, NetCodeEnum.OTHERCODE, listener);
					}
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stu
					try {
						final Bitmap bitmap = BitmapFactory.decodeStream(res
								.body().byteStream());
						if (bitmap != null) {
							if (listener != null && activity != null) {
								activity.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										listener.loadSuccess(bitmap);
									}
								});
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						sendError(activity, NetCodeEnum.NOIMAGE, listener);
					}
				}
			});
		} else {
			sendError(activity, NetCodeEnum.NETWORK_UNABLE, listener);
		}
	}

	public static void sendPost(final BaseActivity activity,
			FormBody.Builder params, final String url,
			final ResponseListener listener) {
		Call call = getClient().toserver(params, url);

		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, final IOException ioe) {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					Log.i("gg", "onFailure:" + ioe.toString() + "---url:" + url);
					if (ioe instanceof java.net.SocketTimeoutException
							|| ioe instanceof java.io.InterruptedIOException) {
						sendError(activity, NetCodeEnum.TIMEOUT, listener);
					} else if (ioe instanceof UnknownHostException) {
						sendError(activity, NetCodeEnum.UNKNOWHOST, listener);
					} else {
						sendError(activity, NetCodeEnum.OTHERCODE, listener);
					}
				}

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					Log.i("gg", "res_info:" + res_info);
					try {
						final JSONObject jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {
							if (listener != null && activity != null) {
								activity.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										listener.onSuccess(jo);
									}
								});
							}
						} else {
							sendError(activity, jo.optInt("code"),
									jo.optString("message"), listener);
						}
					} catch (JSONException e) {
						sendError(activity, NetCodeEnum.NOJSON, listener);
					}
				}
			});
		} else {
			sendError(activity, NetCodeEnum.NETWORK_UNABLE, listener);
		}
	}

	/********
	 * @Detail 不需要传入activity对象的网络请求方法，可以确保回掉在主线程上运行
	 * @param params
	 * @param url
	 * @param listener
	 */
	public static void sendPost(FormBody.Builder params, final String url,
			final ResponseListener listener) {

		Call call = getClient().toserver(params, url);

		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					Log.i("gg", "res_info:" + res_info);
					try {
						final JSONObject jo = new JSONObject(res_info);
						if (jo.getString("code").equals("0")) {
							if (listener != null) {
								WeiLayApplication.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										listener.onSuccess(jo);
									}
								});
							}
						} else {
							if (listener != null) {
								WeiLayApplication.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										listener.onFailed(jo.optInt("code"),
												jo.optString("message"));
									}
								});

							}
						}
					} catch (

					JSONException e) {
						// TODO Auto-generated catch block
						if (listener != null) {
							WeiLayApplication.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									listener.onFailed(NetCodeEnum.NOJSON,
											"返回不是标准的json格式");
								}
							});

						}
					}
				}

				@Override
				public void onFailure(Call arg0, final IOException ioe) {
					// TODO Auto-generated method stub
					Log.i("gg", "onFailure:" + ioe.toString() + "---url:" + url);

					if (listener != null) {
						WeiLayApplication.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (ioe instanceof java.net.SocketTimeoutException
										|| ioe instanceof java.io.InterruptedIOException) {
									listener.onFailed(NetCodeEnum.TIMEOUT,
											"网络请求超时");
								} else if (ioe instanceof UnknownHostException) {
									listener.onFailed(NetCodeEnum.UNKNOWHOST,
											"服务器连接超时");
								} else {
									listener.onFailed(NetCodeEnum.FAILED,
											"请求失败");
								}
							}
						});

					}

				}
			});

		} else {
			if (listener != null) {
				WeiLayApplication.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.networkError();
					}

				});
			}
		}
	}
	
	/*****
	 * @detail 只返回string格式的数据
	 * @return void
	 * @param 
	 * @detail
	 */
	public static void sendStrPost(FormBody.Builder params, final String url,
			final StringResponseListener listener) {

		Call call = getClient().toserver(params, url);

		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response res)
						throws IOException {
					// TODO Auto-generated method stub
					try{
						listener.onSuccess(res.body().string());
					}catch(Exception ex){
						listener.onFailed(NetCodeEnum.FAILED,"获取不到返回数据");
					}
					
				}

				@Override
				public void onFailure(Call arg0, final IOException ioe) {
					// TODO Auto-generated method stub
					Log.i("gg", "onFailure:" + ioe.toString() + "---url:" + url);

					if (listener != null) {
						WeiLayApplication.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (ioe instanceof java.net.SocketTimeoutException
										|| ioe instanceof java.io.InterruptedIOException) {
									listener.onFailed(NetCodeEnum.TIMEOUT,
											"网络请求超时");
								} else if (ioe instanceof UnknownHostException) {
									listener.onFailed(NetCodeEnum.UNKNOWHOST,
											"服务器连接超时");
								} else {
									listener.onFailed(NetCodeEnum.FAILED,
											"请求失败");
								}
							}
						});

					}

				}
			});

		} else {
			if (listener != null) {
				WeiLayApplication.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.networkError();
					}

				});
			}
		}
	}

	private static void sendError(BaseActivity act, final int what,
			final String msg, final ResponseListener listener) {
		if (act == null || listener == null) {
			return;
		}
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listener.onFailed(what, msg);
			}
		});
	}

	private static void sendError(final BaseActivity act, final NetCodeEnum what,
			final BaseResponseListener listener) {
		if (act == null || listener == null) {
			return;
		}
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				act.stopLoading();
				switch (what) {
				case NOJSON:
					listener.onFailed(what, "服务器返回数据格式有误");
					break;
				case UNKNOWHOST:
					listener.onFailed(what, "无法连接服务器!");
					break;
				case TIMEOUT:
					listener.timeOut();
					break;
				case NETWORK_UNABLE:
					listener.networkError();
					break;
				case FAILED:
					listener.onFailed(what, "请求失败");
					break;
				default:
					listener.onFailed(NetCodeEnum.OTHERCODE, "请求失败");
					break;
				}
			}
		});
	}
}
