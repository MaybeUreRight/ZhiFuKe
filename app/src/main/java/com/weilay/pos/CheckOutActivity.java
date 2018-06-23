package com.weilay.pos;

import java.lang.annotation.Annotation;
import java.util.List;

import com.framework.ui.DialogConfirm;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.JieZhangAdapter;
import com.weilay.pos.entity.CheckOutEntity;
import com.weilay.pos.entity.OperatorEntity;
import com.weilay.pos.listener.CheckOutListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.USBComPort;
import com.weilay.pos.util.Utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.FormBody;

/********
 * @detail 结账存单页面
 * @author Administrator
 * 
 */
public class CheckOutActivity extends TitleActivity {
	private ListView jiezhang_listview;
	private JieZhangAdapter adapter;
	private CheckOutEntity jz;
	private Dialog dayin_dialog;
	private TextView nextBtn,perviosBtn;
	private LinearLayout moreLl;
	private int curretPage=1;
	private int pageSize=100;
	private boolean hasMore=true;
	private TextView jz_dayin_date, jz_dayin_sn, jz_dayin_paytype,
			jz_dayin_danhao, jz_dayin_jine, jz_dayin_print, jz_dayin_cancel,
			jz_dayin_refund;
	private CountDownTimer countDownTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.checkout_layout);
		setTitle("结账存单");
		send_jiezhang();
		// ------------
		dayin_dialog = new Dialog(CheckOutActivity.this,
				android.R.style.Animation);
		dayin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dayin_dialog.setContentView(R.layout.checkout_dayin_layout);
	}

	private void init(List<CheckOutEntity> list) {
		adapter = new JieZhangAdapter(this, list);
		jiezhang_listview = (ListView) findViewById(R.id.jiezhang_listview);
		TextView empty_view = (TextView) findViewById(R.id.empty_view);
		jiezhang_listview.setEmptyView(empty_view);
		jiezhang_listview.setAdapter(adapter);
		moreLl=(LinearLayout)findViewById(R.id.more_ll);
		nextBtn=(TextView)findViewById(R.id.btn_next);
		perviosBtn=(TextView)findViewById(R.id.btn_pervios);
		perviosBtn.setVisibility(View.GONE);
		if(!hasMore && curretPage<=1){
			moreLl.setVisibility(View.GONE);
		}else{
			moreLl.setVisibility(View.VISIBLE);
			nextBtn.setVisibility(hasMore?View.VISIBLE:View.GONE);
			perviosBtn.setVisibility((curretPage>1)?View.VISIBLE:View.GONE);
		}
	}

	private void reg(final List<CheckOutEntity> list) {
		jiezhang_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				dayin_dialog.show();
				jz = list.get(position);
				dialog_init();
				dialog_reg();
			}
		});
		//加载更多的结账存单记录
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				nextPage();
			}
		});
		//加载上一页的内容
		perviosBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				perviosPage();
			}
		});
	}
	private void nextPage(){
		curretPage=curretPage+1;
		send_jiezhang();
	}
	private void perviosPage(){
		curretPage=curretPage-1;
		send_jiezhang();
	}
	private void dialog_init() {
		Window w = dayin_dialog.getWindow();
		jz_dayin_date = (TextView) w.findViewById(R.id.jiezhang_dayin_date);
		jz_dayin_sn = (TextView) w.findViewById(R.id.jiezhang_dayin_sn);
		jz_dayin_paytype = (TextView) w
				.findViewById(R.id.jiezhang_dayin_paytype);
		jz_dayin_danhao = (TextView) w.findViewById(R.id.jiezhang_dayin_danhao);
		jz_dayin_jine = (TextView) w.findViewById(R.id.jiezhang_dayin_jine);
		jz_dayin_print = (TextView) w.findViewById(R.id.jiezhang_dayin_print);
		jz_dayin_cancel = (TextView) w.findViewById(R.id.jiezhang_dayin_cancel);
		jz_dayin_refund = (TextView) w.findViewById(R.id.jiezhang_dayin_refund);
	}

	private void dialog_reg() {
		if(jz==null){
			return;
		}
		//判断是否已经退过款
		if(jz.isRefund()){
			jz_dayin_refund.setVisibility(View.INVISIBLE);
		}else{
			OperatorEntity operator=Utils.getCurOperator();
			int  permission=operator.getPermission(jz.getPayMethod());
			switch (jz.getPayMethod()) {
			//只支持微信退款(小微支付不支持退款，这里判断是否小薇支付的条件是当前的支付权限不支持刷卡支付)
			case WEIXIN:
				jz_dayin_refund.setVisibility(permission==OperatorEntity.PAY_PERMISSION_ALL?View.VISIBLE:View.INVISIBLE);
				break;
			case ALIPAY:
				jz_dayin_refund.setVisibility(View.VISIBLE);
				break;
			default:
				jz_dayin_refund.setVisibility(View.INVISIBLE);
				break;
			}
		}
		jz_dayin_date.setText(jz.getTxtime());
		jz_dayin_sn.setText(jz.getSn());
		jz_dayin_paytype.setText(jz.getPayMethod().getValue());
		jz_dayin_danhao.setText(jz.getTx_no());
		jz_dayin_jine.setText("￥"
				+ ConvertUtil.getMoeny(jz.getTotalamountYuan()));
		jz_dayin_print.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//jz.setTitle("结账存单");
				USBComPort usbport = new USBComPort();
				if (usbport.printOutJZ(jz)) {
					final Dialog dialog = DialogConfirm.ask(
							CheckOutActivity.this, "打印提示", "打印成功", "确定",
							new DialogConfirmListener() {
								@Override
								public void okClick(DialogInterface dialog) {
									dayin_dialog.dismiss();
									if (countDownTimer != null) {
										countDownTimer.cancel();
									}
								}
							});
					countDownTimer = new CountDownTimer(3000, 1000) {
						@Override
						public void onTick(long millisUntilFinished) {
						}

						@Override
						public void onFinish() {
							dialog.dismiss();
							dayin_dialog.dismiss();
						}
					};
					countDownTimer.start();
				} else {
					T.showCenter("打印失败!找不到打印机.");
				}
			}

		});

		jz_dayin_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dayin_dialog.dismiss();
			}
		});
		jz_dayin_refund.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RefundActivity.actionCallBack(mContext, jz,1);
			}
		});
	}

	
	/*****
	 * @return void
	 * @param 
	 * @detail 获取结账存单的信息
	 */
	private void send_jiezhang() {
		showLoading("请稍候...");
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("page", ""+curretPage);
		builder.add("pageSize", ""+pageSize);
		Utils.SendCheckOut(mContext, builder, new CheckOutListener() {

			@Override
			public void onErr() {
				stopLoading();
			}

			@Override
			public void onSuc(Object obj) {
				stopLoading();
				List<CheckOutEntity> list = (List<CheckOutEntity>) obj;
				if(list==null || list.size()==0){
					//没有更多数据了
					if(curretPage>1){
						curretPage=curretPage-1;
					}
					hasMore=false;
				}else if(list.size()<pageSize){
					//没有更多数据s
					hasMore=false;
				}else{
					hasMore=true;
				}
				init(list);
				reg(list);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if(resultCode==RESULT_OK){
			T.showCenter("退款成功");
			send_jiezhang();
		}else if(requestCode==RESULT_CANCELED){
			T.showCenter("退款失败");
		}else{
			T.showCenter("退款取消");
		}
		if(dayin_dialog!=null){
			dayin_dialog.dismiss();
			
		}
	}
}
