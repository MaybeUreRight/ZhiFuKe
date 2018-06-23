package com.rxwu.helper;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;

import org.rxwu.helper.entity.PrintEntity;
import org.rxwu.helper.listener.DeviceConnectListener;
import org.rxwu.helper.listener.PrintListener;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by rxwu on 2016/4/6.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:无线打印工具类
 */
public class WifiPrintHelper extends AsyncTask<Void, String, Boolean> {
	private static final  String TAG="------com.weilai.helper-----";
    private  String PRINT_IP ="";
    private  int PRINT_PORT = 9100;
    private static Socket socket = null;
    private static DataOutputStream dos = null;
    private PrintEntity content;
    private PrintListener printListener;
    private DeviceConnectListener deviceConnectListener;
    
    

    public PrintListener getPrintListener() {
		return printListener;
	}

	public void setPrintListener(PrintListener printListener) {
		this.printListener = printListener;
	}

	public DeviceConnectListener getDeviceConnectListener() {
		return deviceConnectListener;
	}

	public void setDeviceConnectListener(DeviceConnectListener deviceConnectListener) {
		this.deviceConnectListener = deviceConnectListener;
	}

	public WifiPrintHelper(String IP,int PORT) {
        PRINT_IP=IP;
        PRINT_PORT=PORT;
    }

    /*
     * 向打印机发送打印内容,开始打印
	 */
    public void printout(PrintEntity content) throws UnsupportedEncodingException {
        this.content = content;//初始化内容
        execute();//执行打印
    }
    private static final int success = 1;
    private static final int error = 2;
    private static final int ing = 3;
    private static final int cancel = 4;

    private void setListener(int type) {
        if (printListener != null) {
            switch (type) {
                case success:
                	printListener.printSuccess();
                    break;
                case error:
                	printListener.printError("打印失败，请检查网络或者无线打印机状态，无法建立链接");
                    break;
                case ing:
                	printListener.printIng();
                    break;
            }
        }
    }
    /**
     *@detail 连接的监听
     * @param type
     */
    private void setConnectListener(int type,String msg) {
        if (deviceConnectListener != null) {
            switch (type) {
                case success:
                	deviceConnectListener.connectSuccess();
                    break;
                case error:
                	deviceConnectListener.connectError("打印失败，请检查网络或者无线打印机状态，无法建立链接");
                    break;
                case ing:
                	deviceConnectListener.connecting();
                    break;
                case cancel:
                	deviceConnectListener.connectCancel();
                	break;
            }
        }
    }
    private DataOutputStream getDataOutputStream() {
    	setConnectListener(ing,"连接中");
        try {
            if (dos == null || socket == null || !socket.isConnected()) {
                socket = new Socket(PRINT_IP, PRINT_PORT);
                dos = new DataOutputStream(socket.getOutputStream());
                setConnectListener(success,"连接成功");
            }
            setConnectListener(error,"连接失败");
        } catch (Exception ex) {
        	 setConnectListener(error,"连接失败:"+ex.getMessage()+";"+ex.getLocalizedMessage());
            Log.d(TAG,ex.getMessage());
        }
        return dos;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (content == null) return false;
        dos = getDataOutputStream();
        setListener(ing);
        try {
            //RESET PRINTER
            byte[] a = new byte[2];
            a[0] = 0x1b;
            a[1] = 0x40;
            dos.write(a);

            //CHARACTOR SET
            byte[] b = new byte[3];
            b[0] = 0x1B;
            b[1] = 0x52;
            b[2] = 0x40;
            dos.write(b);

            //换行
            byte[] line = new byte[2];
            line[0] = 0x0d;
            line[1] = 0x0a;

            //根据换行符切换
            List<byte[]> contents = content.getContents();
            for (byte[] content:contents) {
                if(content=="[swap_line]".getBytes()){
                    dos.write(line);
                }else{
                    dos.write(content);
                }
            }
            byte[] c = new byte[1];
            c[0] = 0x0c;
            dos.write(c);

            for (int i = 0; i < 7; i++) {
                dos.write(line);
            }
            //切纸
            a[0] = 0x1B;
            a[1] = 0x69;
            dos.write(a);

            byte[] d = new byte[4];
            d[0] = 0x1b;
            d[1] = 0x42;
            d[2] = 0x02;
            d[3] = 0x02;
            dos.write(d);
        } catch (Exception e) {
            setListener(error);
            Log.d(TAG,e.getMessage());
        }
        return true;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        //打印成功
        setListener(success);

    }

}