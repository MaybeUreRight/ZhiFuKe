package com.weilay.pos.print;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.framework.utils.L;
import com.framework.utils.StringUtil;
import com.google.gson.Gson;
import com.p5.printer.PrinterNative;
import com.rxwu.helper.USBProtolHelper;
import com.weilay.pos.PaySelectActivity2;
import com.weilay.pos.R;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.PayType;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.MachineEntity;
import com.weilay.pos.entity.PayTypeEntity;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.BeepManager;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.InterceptUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ComPort {
	private final String OCR_HEADER = "<ocr_filter_header>";
	private final String OCR_TAIL = "hello";
	private final String OCR_WX_HEADER = "<ocr_weixin_header>";
	private final String OCR_WX_TAIL = "wx_tail";
	private final String OCR_ZFB_HEADER = "<ocr_zhifubao_header>";
	private final String OCR_ZFB_TAIL = "zfb_tail";
	private final String OCR_CASH_HEADER = "<ocr_cash_header>";
	private final String OCR_CASH_TAIL = "cash_tail";
	private final String OCR_VIP_HEADER = "<ocr_vip_header>";
	private final String OCR_VIP_TAIL = "vip_tail";

	private String uploadFile_url = "API/UploadFile";
	private final int UPLOADFILE = 888;
	private BaseActivity activity;
	private Client client;
	private MachineEntity machineInfo;
	private String date_type = "G";
	private String keyword = "";
	InterceptUtil iu;
	static boolean COM_USE = false;
	static boolean USB_USE = false;
	public USBProtolHelper helper = null;
	private TessOCR mTessOCR;
	PrinterNative pn;
	private byte[] printBuffer = new byte[512];
	boolean isReading = false;

	private StringBuffer saveBuffer;

	public InterceptUtil getInterceptUtil() {
		if (iu == null) {
			return iu = new InterceptUtil();
		}
		return iu;
	}

	public TessOCR getmTessOCR() {
		if (mTessOCR == null) {
			return mTessOCR = new TessOCR();
		}
		return mTessOCR;
	}

	private Context mContext;

	public ComPort(Context context) {
		this.mContext = context;
		client = new Client(mContext);

	}

	/***********
	 * @detail 启动
	 */
	public void openMainCom() {
		if (isReading) {
			closeMainCom();
		}
		isReading = true;
		startThread();// 启动监听
	}

	/********
	 * @detail 关闭
	 */
	public void closeMainCom() {
		isReading = false;
		if (pn != null) {
			try{
				pn.close();
				pn = null;
			}catch(Exception ex){
				L.e(ex.getLocalizedMessage());
			}
		}
	}

	/******
	 * @detail 获取订单的识别格式
	 */
	private void getMachineInfo() {
		getmachineing = true;
		Utils.queryOrderRead(new OnDataListener<Object>() {

			@Override
			public void onFailed(String msg) {
				// T.showCenter("获取订单识别信息失败，无法自动识别支付金额");
				machineInfo = null;
				getmachineing = false;
			}

			@Override
			public void onData(Object obj) {
				// 得到订单识别信息时候，才启动读串口
				machineInfo = (MachineEntity) obj;
				L.e("---" + new Gson().toJson(machineInfo).toString());
				getmachineing = false;
				PosDefine.CURRENT_APGRADE_MODE = machineInfo.getUpgradetype();
			}
		});
	}

	// 启动线程
	private void startThread() {
		// new 串口打印监听工具
		pn = new PrinterNative();
		// 打开监听的线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pn.openUE();
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isReading) {
					try {
						pn.finishpoll();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 收到打印内容
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isReading) {
					int len = pn.poll(printBuffer);
					SendInfo(printBuffer, len);
				}
			}
		}).start();
	}

	private void saveFile(String toSaveString, String filePath) {
		try {
			File saveFile = new File(filePath);
			if (!saveFile.exists()) {
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(saveFile, true);
			outStream.write(toSaveString.getBytes());
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			L.i("gg", e.getMessage());
		}
	}

	private int streamType = 0;
	StringBuffer bs = null;
	boolean getmachineing = false;
	boolean isIntercept = false;
	private int uploadSecond = 3;

	private String hex_file = Environment.getExternalStorageDirectory() + "/tmp/hexFile.txt";
	private Timer uploadTimer;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case UPLOADFILE:
				if (uploadSecond <= 0) {
					saveFile(saveBuffer.toString(), hex_file);
					if (!isIntercept) {// 无法识别才上传
						Log.i("gg", "keyWord:" + machineInfo.getKeyword());
						uploadFile(hex_file, machineInfo.getKeyword());// 上传数据
					}
					saveBuffer = null;// 清空buffer数据 防止重复上传
					if (uploadTimer != null) {
						uploadTimer.cancel();
					}
					uploadTimer = null;
				} else {
					uploadSecond--;
				}
				break;

			default:
				break;
			}

		};
	};

	private void uploadFileBegin() {
		if (uploadTimer == null) {
			uploadTimer = new Timer();
			uploadTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					// uploadFile(hex_file);// 将打印指令上传至服务器
					SendMessage(UPLOADFILE, "");
					Log.i("gg", "距离文件上传还剩下" + uploadSecond + "秒");
				}
			}, 1000, 1000);
		}
	}

	private BeepManager beepManager;

	private void playBeep() {
		if (beepManager == null) {
			beepManager = new BeepManager(WeiLayApplication.app, R.raw.beep);
			beepManager.playBeep();
		} else {
			beepManager.playBeep();
		}
	}

	/*****
	 * @ detail 根据打印的信息生成支付的实体
	 * 
	 * @param ret
	 * @return
	 */
	private PayTypeEntity getPayType(String ret) {
		PayTypeEntity payType = new PayTypeEntity();
		// 默认快捷键
		if (ret.indexOf(OCR_HEADER) != -1) {
			try {
				ret = ret.substring(ret.indexOf(OCR_HEADER) + OCR_HEADER.length(), ret.indexOf(OCR_TAIL));
				Log.i("gg", "amount:" + ret);
				if (!StringUtil.isEmpty(ret)) {
					double amount = Double.valueOf(ret);
					if (amount > 0) {
						payType.setAmount(amount);
						payType.setPayType(PayType.WEIXIN);
						payType.setTx_no(GetUtil.getOutTradeNo());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			// 微信快捷键
		} else if (ret.indexOf(OCR_WX_HEADER) != -1) {
			try {
				ret = ret.substring(ret.indexOf(OCR_WX_HEADER) + OCR_WX_HEADER.length(), ret.indexOf(OCR_WX_TAIL));
				Log.i("gg", "OCR_WX_HEADER amount:" + ret);
				if (!StringUtil.isEmpty(ret)) {
					double amount = Double.valueOf(ret);
					if (amount > 0) {
						payType.setAmount(amount);
						payType.setPayType(PayType.WEIXIN);
						payType.setTx_no(GetUtil.getOutTradeNo());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			// 支付宝快捷键
		} else if (ret.indexOf(OCR_ZFB_HEADER) != -1) {
			try {
				ret = ret.substring(ret.indexOf(OCR_ZFB_HEADER) + OCR_ZFB_HEADER.length(), ret.indexOf(OCR_ZFB_TAIL));
				Log.i("gg", "OCR_ZFB_HEADER amount:" + ret);
				if (!StringUtil.isEmpty(ret)) {
					double amount = Double.valueOf(ret);
					if (amount > 0) {
						payType.setAmount(amount);
						payType.setPayType(PayType.ALIPAY);
						payType.setTx_no(GetUtil.getOutTradeNo());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else if (ret.indexOf(OCR_CASH_HEADER) != -1) {
			try {
				ret = ret.substring(ret.indexOf(OCR_CASH_HEADER) + OCR_CASH_HEADER.length(),
						ret.indexOf(OCR_CASH_TAIL));
				Log.i("gg", "OCR_CASH_HEADER amount:" + ret);
				if (!StringUtil.isEmpty(ret)) {
					double amount = Double.valueOf(ret);
					if (amount > 0) {
						payType.setAmount(amount);
						payType.setPayType(PayType.CASH);
						payType.setTx_no(GetUtil.getOutTradeNo());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else if (ret.indexOf(OCR_VIP_HEADER) != -1) {
			try {
				ret = ret.substring(ret.indexOf(OCR_VIP_HEADER) + OCR_VIP_HEADER.length(), ret.indexOf(OCR_VIP_TAIL));
				Log.i("gg", "OCR_VIP_HEADER amount:" + ret);
				if (!StringUtil.isEmpty(ret)) {
					double amount = Double.valueOf(ret);
					if (amount > 0) {
						payType.setAmount(amount);
						payType.setPayType(PayType.CHUZHIKA);
						payType.setTx_no(GetUtil.getOutTradeNo());
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return payType;
	}

	// 心跳的传输文
	public static final String CONST_HEARTBEAT = "wEilaY@165.~X&/$";
	long lastHeartBeartTime = System.currentTimeMillis();// 记录心跳的间隔时间

	@SuppressWarnings("deprecation")
	private void SendInfo(byte[] comData, int dataLen) {
		String ret = new String(comData, 0, dataLen).trim();
		Log.i("gg", "Sendinfo:" + ret);
		if (ret.startsWith(CONST_HEARTBEAT)) {
			L.d("keep heatbeart;inteval->" + (System.currentTimeMillis() - lastHeartBeartTime) + "ms");
			lastHeartBeartTime = System.currentTimeMillis();

			return;
		}
		if(!Utils.isLogin) {
			Log.i("gg", "没有登录，接收没启动");
			return;
		}
		// 1.默认快捷键. 2.微信支付快捷键. 3.支付宝快捷键. 4.现金支付 5.会员卡支付
		if (ret.indexOf(OCR_HEADER) != -1 | ret.indexOf(OCR_WX_HEADER) != -1 | ret.indexOf(OCR_ZFB_HEADER) != -1
				| ret.indexOf(OCR_CASH_HEADER) != -1 | ret.indexOf(OCR_VIP_HEADER) != -1) {
			// update by rxwu at 2017-06-27
			//new SaveLogTask().execute(ret);
			PayTypeEntity payType = getPayType(ret);
			if (payType != null && payType.getAmount() > 0) {
				playBeep();
				Intent intent = new Intent(mContext, PaySelectActivity2.class);
				intent.putExtra(PosDefine.INTENTE_PAY_INFO, payType);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Log.i("gg", "ocr Paytype:" + payType.getPayType());
				mContext.startActivity(intent);
			}
			return;// 如果插件能识别,就不执行字符识别
		} else {
			try {
				WeiLayApplication.getUsbHelper().write(comData, dataLen);
			} catch (Exception ex) {
				L.e("gg", ex.getLocalizedMessage());
			}
		}
		// -----------------------------------------------非插件的字符识别
		if (machineInfo == null && !getmachineing) {
			getMachineInfo();
			return;
		}
		try {
			date_type = machineInfo.getRecognizetype();
			keyword = machineInfo.getKeyword();

			byte[] bRce = ComBean(comData, dataLen);
			if (saveBuffer == null) {
				saveBuffer = new StringBuffer();
			}
			if (StringUtil.isEmpty(keyword)) {
				return;
			}
			saveBuffer.append(MyFunc.ByteArrToHex(comData, 0, comData.length));
			uploadSecond = 3;
			uploadFileBegin();// 准备倒计时三秒后上传
			if (date_type.equals("C")) // (this.decode_type)
			{
				for (int i = 0; i < bRce.length - 1; i++) {
					if (bRce[i] == 0x1c && bRce[i + 1] == 0x26) { // chinese
						// mode
						streamType = 1;
						break;
					}
				}
				String ret2 = "";
				if (streamType == 1) { // chinese mode
					// String s = MyFunc.ByteArrToHex(bRce, 0, bRce.length);
					// Log.i("original", s);
					ret2 = getInterceptUtil().decodeString(bRce, 0);
					// Log.i("text", ret);
				} else {
					if (bRce.length > 10) {
						for (int i = bRce.length - 10; i < bRce.length - 1; i++) {
							if (bRce[i] == 0x1b && bRce[i + 1] == 0x6d) {
								break;
							}
						}
					}
					ret2 = new String(bRce, "GB2312");
				}
				Log.i("gg", "ret2:" + ret2);

				double amountx = getInterceptUtil().getTotalAmount(keyword, ret2);
				Log.i("gg", "txt amount:" + amountx);
				if (amountx > 0) {
					isIntercept = true;
					Intent intent = new Intent(mContext, PaySelectActivity2.class);
					PayTypeEntity payType = new PayTypeEntity();
					payType.setAmount(amountx);
					payType.setTx_no(GetUtil.getOutTradeNo());
					intent.putExtra(PosDefine.INTENTE_PAY_INFO, payType);
					mContext.startActivity(intent);
				} else {
					isIntercept = false;
				}
				// else {
				//
				// double amount = 0;
				// Boolean cutPaper = false;
				// this.receivedByteArr =
				// ArrayUtils.addAll(this.receivedByteArr,
				// bRce);
				// if (bRce.length > 10) {
				// for (int i = bRce.length - 10; i < bRce.length - 1; i++) {
				// if (bRce[i] == 0x1b && bRce[i + 1] == 0x6d
				// || bRce[i] == 0x1b && bRce[i + 1] == 0x42
				// || bRce[i] == 0x1b && bRce[i + 1] == 0x43) {
				// cutPaper = true;
				// break;
				// }
				// }
				// }
				// int len = bRce.length;
				// if (cutPaper || len >= 5 && bRce[len - 5] == 27
				// && bRce[len - 4] == 67 && bRce[len - 3] == 4
				// && bRce[len - 2] == 2 && bRce[len - 1] == 3) { // 0x1B
				// amount = getInterceptUtil().decode(getActivity(),
				// this.receivedByteArr, getmTessOCR(), machineInfo);
				// Log.i("gg", "amount:" + amount);
				// if (amount > 0) {
				// isIntercept = true;
				// Intent intent = new Intent(getActivity(),
				// PaySelectActivity2.class);
				// // intent.putExtra(PosDefine.INTERCEPT_PAYAMOUNT,
				// // amount);
				// // intent.putExtra(PosDefine.ISINTERCEPT, true);
				// PayTypeEntity payType = new PayTypeEntity();
				// payType.setAmount(ConvertUtil.getMoeny(amount));
				// payType.setTx_no(GetUtil.getOutTradeNo());
				// intent.putExtra(PosDefine.INTENTE_PAY_INFO, payType);
				// getActivity().startActivity(intent);
				// this.receivedByteArr = new byte[0];
				// getInterceptUtil().setIdx_bmp(0);
				// } else {
				// isIntercept = false;
				// }
				// }
				//
				// }
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public byte[] ComBean(byte[] buffer, int size) {
		byte[] bRec = new byte[size];
		for (int i = 0; i < size; i++) {
			bRec[i] = buffer[i];
		}
		return bRec;
	}

	private void uploadFile(String filePath, String keyword) {

		Call call = client.uploadFile(uploadFile_url, filePath, GetUtil.getimei(), keyword);
		if (call != null) {
			call.enqueue(new Callback() {

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					Log.i("gg", "uploadfile-->IOE:" + arg1.toString());
					deleteTempFile();
				}

				@Override
				public void onResponse(Call arg0, Response res) throws IOException {
					// TODO Auto-generated method stub
					String res_info = res.body().string();
					Log.i("gg", "res_info-->uploadFile:" + res_info);
					deleteTempFile();
				}
			});
		} else {
			deleteTempFile();
			T.showCenter("网络异常!");
		}
	}

	public String printHexString(byte[] b) {
		String s = new String();
		String buff = new String();
		String a = "";
		for (int i = 0; i < b.length; i++) {
			s = String.format("%02x ", b[i]);
			buff += s;
			if (i + 1 % 16 == 0) {
				L.d("TAG", buff);
				buff = "";
			} else if (i + 1 % 8 == 0) {
				buff += "   ";
			}
		}
		L.d("gg", buff);

		return a;
	}

	/*
	 * delete temp files
	 */
	private void deleteTempFile() {
		try {
			String path = Environment.getExternalStorageDirectory() + "/tmp/";
			File dir = new File(path);
			if (dir.exists()) {
				if (dir.isDirectory()) {
					String[] children = dir.list();
					File f;
					for (int i = 0; i < children.length; i++) {
						f = new File(path + children[i]);
						f.delete();
					}
				}
			} else {
				dir.mkdir();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			L.i("gg", e.getMessage());
		}
	}

	/*
	 * public void printk() { File file = new File("/proc/sys/kernel/printk");
	 * OutputStream outputStream = null; try { outputStream = new
	 * FileOutputStream(file); outputStream.write("0".getBytes()); } catch
	 * (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } finally { try { outputStream.flush();
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (Exception ex) {
	 * 
	 * } } }
	 */
	private void SendMessage(int resId, Object obj) {
		Message message = new Message();
		message.arg1 = resId;
		message.obj = obj;
		handler.sendMessage(message);
	}
}