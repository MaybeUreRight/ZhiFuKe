package com.weilay.pos.app;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.weilay.pos.util.NetworkTest;

import android.content.Context;
import android.util.Log;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class Client {
	private OkHttpClient client;
	private NetworkTest networkTest;
	private Context mContext;
	private String server_url = UrlDefine.BASE_URL;
	

	public Client(Context context) {
		client = new OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS).addInterceptor(new RetryIntercepter(0)).retryOnConnectionFailure(false).build();
		networkTest = new NetworkTest();
		this.mContext = context;
	}
	/**
	 * 重试拦截器
	 */
	public class RetryIntercepter implements Interceptor {

	    public int maxRetry;//最大重试次数
	    private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

	    public RetryIntercepter(int maxRetry) {
	        this.maxRetry = maxRetry;
	    }

	    @Override
	    public Response intercept(Chain chain) throws IOException {
	        Request request = chain.request();
	        System.out.println("retryNum=" + retryNum);
	        Response response = chain.proceed(request);
	        while (!response.isSuccessful() && retryNum < maxRetry) {
	            retryNum++;
	            System.out.println("retryNum=" + retryNum);
	            response = chain.proceed(request);
	        }
	        return response;
	    }
	}
	public Call toserver(FormBody.Builder builder, String url) {
		Log.i("gg", "toserver_url:" + server_url + url);
		RequestBody mRequestBody = builder.build();

		Request request = new Request.Builder().url(server_url + url)
				.post(mRequestBody).build();
		final Request copy = request.newBuilder().build();

		final Buffer buffer = new Buffer();
		try {
			copy.body().writeTo(buffer);
			Log.i("gg", "request:" + buffer.readUtf8());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Call call = client.newCall(request);

		if (networkTest.isNetworkConnected(mContext)) {
			return call;
		}
		return null;
	}

	public Call down_apk(String url, String path) {
		client = new OkHttpClient();
		final Request request = new Request.Builder().url(server_url + url)
				.build();
		Call call = client.newCall(request);

		if (networkTest.isNetworkConnected(mContext)) {
			return call;
		}
		return null;

	}

	public Call down_qrPhoto(String url) {
		client = new OkHttpClient();
		final Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		if (networkTest.isNetworkConnected(mContext)) {
			return call;
		}
		return null;
	}

	public Call down_version(String url) {
		client = new OkHttpClient();
		final Request request = new Request.Builder().url(server_url + url)
				.build();
		Call call = client.newCall(request);
		if (networkTest.isNetworkConnected(mContext)) {
			return call;
		}
		return null;
	}

	public Call upload(String url, String path, String sn, String flag) {
		client = new OkHttpClient();
		File file = new File(path);
		String fileName = file.getName();
		RequestBody fileBody = RequestBody.create(
				MediaType.parse("application/octet-stream"), file);
		MultipartBody.Builder mb = new okhttp3.MultipartBody.Builder();

		RequestBody requestBody = mb
				.setType(MultipartBody.FORM)
				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"sn\""),
						RequestBody.create(null, sn))
				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"flag\""),
						RequestBody.create(null, flag))

				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"uploadedfile\";filename=\""
										+ fileName + "\""), fileBody).build();

		Request request = new Request.Builder().url(server_url + url)
				.post(requestBody).build();
		Call call = client.newCall(request);

		if (networkTest.isNetworkConnected(mContext)) {
			Log.i("gg", "toserverUrl:" + server_url + url);
			return call;
		}
		return null;
	}

	public Call uploadFile(String url, String path, String sn,String keyword) {
		client = new OkHttpClient();
		File file = new File(path);
		String fileName = file.getName();
		RequestBody fileBody = RequestBody.create(
				MediaType.parse("application/octet-stream"), file);
		MultipartBody.Builder mb = new okhttp3.MultipartBody.Builder();
		RequestBody requestBody = mb
				.setType(MultipartBody.FORM)
				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"sn\""),
						RequestBody.create(null, sn))
							.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"keyword\""),
						RequestBody.create(null, keyword))
				.addPart(
						Headers.of("Content-Disposition",
								"form-data; name=\"uploadedfile\";filename=\""
										+ fileName + "\""), fileBody).build();
		Request request = new Request.Builder().url(server_url + url)
				.post(requestBody).build();
		Call call = client.newCall(request);
		Log.i("gg", "toserverUrl:" + server_url + url);
		if (networkTest.isNetworkConnected(mContext)) {
			return call;
		}
		return null;
	}
}
