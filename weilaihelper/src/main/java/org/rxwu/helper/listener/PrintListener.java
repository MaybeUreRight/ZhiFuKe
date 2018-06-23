package org.rxwu.helper.listener;

/**
 * Created by rxwu on 2016/4/8.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:打印回调接口
 */
public interface PrintListener {
    //打印成功
    public void printSuccess();

    //打印失败
    public void printError(String msg);

    //打印中
    public void printIng();
}
	