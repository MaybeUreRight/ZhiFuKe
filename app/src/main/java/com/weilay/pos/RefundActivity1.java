package com.weilay.pos;
/*package com.weilay.pos;

import com.framework.ui.DialogConfirm;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.listener.RefundListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.GetUtil;
import com.weilay.pos.util.Utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import okhttp3.FormBody;

public class RefundActivity extends TitleActivity {
	private TextView refund_danhao, refund_jine, refund_enter;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.refund_layout);
		setTitle("退款");
		intent = getIntent();
		init();
		reg();
	}

	private void init() {
		refund_danhao = (TextView) findViewById(R.id.refund_danhao);
		refund_jine = (TextView) findViewById(R.id.refund_jine);
		refund_enter = (TextView) findViewById(R.id.refund_enter);
	}

	private Bundle bundle = null;
	private String tx_no;
	private String jine;
	private String refundType;// 退款平台

	private void reg() {
		if (intent != null) {
			bundle = intent.getBundleExtra("orderQuery");
			tx_no = bundle.getString("tx_no");
			jine = bundle.getString("totalAmount");
			refundType = bundle.getString("refundType");
			refund_danhao.setText(tx_no);
			refund_jine.setText("可退总额:" + ConvertUtil.getMoeny(jine) / 100
					+ "元");
		}

		refund_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// progressDialog.show();
				showLoading("请稍候...");
				send_refund(tx_no, jine, refundType);
			}
		});
	}
	private void send_refund(String tx_no, String amount, String type) {
		FormBody.Builder builder = BaseParam.getParams();
		builder.add("tx_no", tx_no);
		builder.add("refundno", GetUtil.getRefundOutTradeNo());
		builder.add("totalAmount", amount);
		builder.add("paytype", type);
		builder.add("remarks", "");
		Utils.SendRefund(mContext, builder, new RefundListener() {

			@Override
			public void onSuc() {
				// TODO Auto-generated method stub
				stopLoading();
				DialogConfirm.ask(mContext, "退款提示", "退款成功", "确定",
						new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
							}
						});
			}

			@Override
			public void onRefunded(String msg) {
				// TODO Auto-generated method stub
				stopLoading();
				DialogConfirm.ask(mContext, "退款提示",msg, "确定",
						new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								RefundActivity.this.finish();
							}
						});
			}

			@Override
			public void onErr(String msg) {
				// TODO Auto-generated method stub
				DialogConfirm.ask(mContext, "提示", "退款失败("+msg+")", "确定",
						new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								RefundActivity.this.finish();
							}
						});
			}

			@Override
			public void on403() {
				// TODO Auto-generated method stub
				DialogConfirm.ask(mContext, "提示", "该商户没有退款权限,请到微信官方申请退款权限!",
						"确定", new DialogConfirmListener() {

							@Override
							public void okClick(DialogInterface dialog) {
								// TODO Auto-generated method stub
								RefundActivity.this.finish();
							}
						});
			}
		});
	}

}
*/