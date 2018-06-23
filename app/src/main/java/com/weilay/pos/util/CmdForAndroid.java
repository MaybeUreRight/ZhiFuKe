package com.weilay.pos.util;

import com.framework.utils.L;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class CmdForAndroid {

	public static boolean shella(String cmd,String[] info) {

		try {
			Process suProcess = Runtime.getRuntime().exec(cmd);// su为root权限
			DataOutputStream os = new DataOutputStream(
					suProcess.getOutputStream());
			// Execute commands that require root access
			String[] send_cmd = new String[5];
			send_cmd[0] = "netcfg eth0 up";
			send_cmd[1] = "ifconfig eth0 " + info[0] + " netmask " + info[1];
			send_cmd[2] = "route add default gw " + info[2] + " dev eth0";
			send_cmd[3] = "setprop net.eth0.dns1 " + info[3];
			send_cmd[4] = "setprop net.eth0.dns2 " + info[4];
			for (int i = 1; i < send_cmd.length; i++) {
				os.writeBytes(send_cmd[i] + "\n");
				os.flush();
				os.writeBytes("exit\n");
				os.flush();
			}

		} catch (IOException e) {

			// e.printStackTrace();
			L.i("gg", e.getMessage());
			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			L.i("gg", e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean shella(String cmd,String info) {

		try {
			Process suProcess = Runtime.getRuntime().exec(cmd);// su为root权限
			DataOutputStream os = new DataOutputStream(
					suProcess.getOutputStream());
			// Execute commands that require root access
			os.writeBytes(info + "\n");
			os.flush();
			os.writeBytes("exit\n");
			os.flush();

		} catch (IOException e) {

			// e.printStackTrace();
			L.i("gg", e.getMessage());
			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			L.i("gg", e.getMessage());
			return false;
		}
		return true;
	}

	public static String shellReturn() {
		// 这个类是一个很好用的工具，java中可以执行java命令，android中可以执行shell命令
		Runtime mRuntime = Runtime.getRuntime();
		try {
			// Process中封装了返回的结果和执行错误的结果
			Process mProcess = mRuntime.exec("cat /proc/cpuinfo");
			BufferedReader mReader = new BufferedReader(new InputStreamReader(
					mProcess.getInputStream()));
			StringBuffer mRespBuff = new StringBuffer();
			char[] buff = new char[1024];
			int ch = 0;
			while ((ch = mReader.read(buff)) != -1) {
				mRespBuff.append(buff, 0, ch);
			}
			mReader.close();
			return mRespBuff.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
