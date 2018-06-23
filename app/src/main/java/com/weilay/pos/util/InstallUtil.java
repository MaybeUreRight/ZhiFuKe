package com.weilay.pos.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.framework.utils.L;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

public class InstallUtil {
	/**
	 * 静默安装
	 * 
	 * @param path
	 * @return
	 */
	public static boolean slientInstall(String path) {
		boolean result = false;
		Process process = null;
		OutputStream out = null;
		int value = 0;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.writeBytes("chmod 777 " + path + "\n");
			dataOutputStream
					.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
							+ path);
			// 提交命令
			dataOutputStream.flush();
			// 关闭流操作
			dataOutputStream.close();
			out.close();
			value = process.waitFor();

			L.i("gg", "exitvalue:" + process.exitValue());
			L.i("gg", "waitFor:" + process.waitFor());
			// 代表成功
			if (value == 0) {
				result = true;
			} else if (value == 1) { // 失败
				result = false;
			} else { // 未知情况
				result = false;
			}
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		} catch (InterruptedException e) {
			result = false;
			e.printStackTrace();
		} finally {
			process.destroy();
		}

		return result;
	}

	/**
	 * 静默卸载
	 */
	public static boolean clientUninstall(String packageName) {
		boolean result = false;
		PrintWriter PrintWriter = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");
			PrintWriter = new PrintWriter(process.getOutputStream());
			PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
			PrintWriter.println("pm uninstall " + packageName);
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();
			if (value == 0) {
				result = true;
			} else if (value == 1) { // 失败
				result = false;
			} else { // 未知情况
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}
	private static final String TAG = ".utils.InstallUtil";
	private static final String  apkPath = "/mnt/sdcard/weilai/yuji.apk";

	// 安装assets中的应用
	public static void install(Context context, String name) {
		AssetManager assets = context.getAssets();
		try {
			// 获取assets资源目录下的himarket.mp3,实际上是himarket.apk,为了避免被编译压缩，修改后缀名。
			InputStream stream = assets.open("yuji.mp3");
			if (stream == null) {
				Log.v(TAG, "no file");
				return;
			}

			String folder = "/mnt/sdcard/weilai/";
			File f = new File(folder);
			if (!f.exists()) {
				f.mkdir();
			}
			
			File file = new File(apkPath);
			if(file.exists())
			{
				file.delete();
			}
			// 创建apk文件
			file.createNewFile();
			// 将资源中的文件重写到sdcard中
			// <uses-permission
			// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
			writeStreamToFile(context, stream, file);
			// 安装apk
			// <uses-permission
			// android:name="android.permission.INSTALL_PACKAGES" />
			installApk(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeStreamToFile(Context context, InputStream stream, File file) {
		try {
			//
			OutputStream output = null;
			try {
				output = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				try {
					final byte[] buffer = new byte[1024];
					int read;

					while ((read = stream.read(buffer)) != -1)
						output.write(buffer, 0, read);

					output.flush();
				} finally {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public  static void installApk(Context context) {
		Log.v(TAG, apkPath);
		openUnknowSource(context);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE,true);
		intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	private static void  openUnknowSource(Context context) {
		ContentValues values = new ContentValues();
        values.put("value", 1);
        Cursor cursor = null;
        try{
            int value = 0;
            cursor = context.getContentResolver().query(Settings.Secure.CONTENT_URI, new String[] { "value",},
                 "name=?", 
                 new String[] {Settings.Secure.INSTALL_NON_MARKET_APPS}, null);
            if(cursor != null && cursor.moveToNext()){
                value = cursor.getInt(cursor.getColumnIndex("value"));
            }
            if(cursor != null){
            	cursor.close();
            	cursor = null;
            }

            if(0 == value){
                int i = context.getContentResolver().update(Settings.Secure.CONTENT_URI, values,"name=?", 
                        new String[] {Settings.Secure.INSTALL_NON_MARKET_APPS} );
                if(i > 0){
                    Log.e("", "success");
                }else{
                    Log.e("", "fail");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
        	  if(cursor!=null) {
        		  cursor.close();
        		  cursor=null;
        	  }
        }
	}
}
