package com.weilay.pos;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.RefundPrintAdapter;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.CheckOutEntity;
//import com.weilay.pos.entity.RefundPrint;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.ServerTimeUtils;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.FormBody;

public class RefundPrintActivity extends TitleActivity {
	private ListView refundprint_listview;
	private RefundPrintAdapter adapter;
	private Dialog dayin_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.refundprint_layout);
		setTitle("退款存单");
		send_refundprint(ServerTimeUtils.getServerTime() - 15 * 24 * 60 * 60
				* 1000, ServerTimeUtils.getServerTime());
		dayin_dialog = new Dialog(RefundPrintActivity.this,
				android.R.style.Animation);
		dayin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dayin_dialog.setContentView(R.layout.refundprint_dayin_layout);
	}

	private void init(List<CheckOutEntity> refundPrints) {
		adapter = new RefundPrintAdapter(this, refundPrints);
		refundprint_listview = (ListView) findViewById(R.id.refundprint_listview);
		TextView empty_view = (TextView) findViewById(R.id.empty_view);
		refundprint_listview.setEmptyView(empty_view);
		refundprint_listview.setAdapter(adapter);
	}

	private void reg(final List<CheckOutEntity> refundPrints) {
		refundprint_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final CheckOutEntity rp = refundPrints.get(position);
				StringBuffer refundStr = new StringBuffer();
				refundStr.append("退款时间:" + rp.getTxtime() + "\n");
				refundStr.append("设备编码:" + rp.getSn() + "\n");
				refundStr.append("退款方式:" + rp.getPayMethod().getValue() + "\n");
				refundStr.append("退款单号:" + rp.getRefundno() + "\n");
				refundStr.append("商户单号:\n" + rp.getTx_no() + "\n");
				refundStr.append("退款金额:" + ConvertUtil.branchToYuan(rp.getRefundamount()) + "元");
				DialogAsk.ask(mContext, "退款订单打印", refundStr.toString(), "打印",
						"返回", new DialogAskListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								printRefund(rp);
							}

							@Override
							public void cancelClick(DialogInterface dialog) {
							}
						});
			}
		});
	}
	/****
	 * @detail 打印退款单
	 */
	private void printRefund(CheckOutEntity rp) {
		USBComPort usbComPort = new USBComPort();
		if (usbComPort.printOutRP(rp)) {

		} else {
			T.showCenter("打印失败!找不到打印机.");
		}
	}


	private void send_refundprint(long startTime, long endTime) {
		showLoading("请稍候...");
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("starttime", String.valueOf(startTime));
		builder.add("endtime", String.valueOf(endTime));
		HttpUtils.sendPost(mContext, builder, UrlDefine.URL_REFUNDLOG,
				new ResponseListener() {

					@Override
					public void onSuccess(JSONObject json) {
						// TODO Auto-generated method stub
						stopLoading();
						try {
							Type type = new TypeToken<List<CheckOutEntity>>() {
							}.getType();
							List<CheckOutEntity> refundPrints = new Gson()
									.fromJson(json.getString("data"), type);
							if (refundPrints != null) {
								init(refundPrints);
								reg(refundPrints);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}

					@Override
					public void onFailed(int code, String msg) {
						// TODO Auto-generated method stub
						stopLoading();
						DialogConfirm.ask(mContext, "退款提示", msg, "确定",
								new DialogConfirmListener() {

									@Override
									public void okClick(DialogInterface dialog) {
										// TODO Auto-generated method stub

									}
								});
					}
				});
	}

}
