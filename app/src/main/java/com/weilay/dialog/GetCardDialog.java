package com.weilay.dialog;

import com.weilay.pos.PaySelectActivity2;
import com.weilay.pos.R;
import com.weilay.pos.app.CardTypeEnum;
import com.weilay.pos.db.CouponDBHelper;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.listener.TaskReceiverListener;
import com.weilay.pos.titleactivity.BaseDialogFragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/******
 * @detail 使用优惠卡券
 * @author rxwu
 *
 */
public class GetCardDialog extends BaseDialogFragment implements OnClickListener {
	CouponEntity coupon = null;

	private TaskReceiverListener taskReceiverListener = new TaskReceiverListener() {

		@Override
		public void cancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void receiver() {
			// TODO Auto-generated method stub

		}
	};

	public void setTaskReceiverListener(TaskReceiverListener taskReceiverListener) {
		if (taskReceiverListener != null) {
			this.taskReceiverListener = taskReceiverListener;
		}
	}

	Intent intent = null;
	private Button okBtn, cancelBtn;
	private TextView cardTypeTv, cardNoTv, cardInfoTv, cardDateTv, midNameTv, closeBtn;

	public GetCardDialog(CouponEntity coupon) {
		// TODO Auto-generated constructor stub

		this.coupon = coupon;
		// 初始化付款的信息
	}

	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		mRootView = inflater.inflate(R.layout.dialog_get_card, container);
		// ((TextView)findViewById(R.id.common_head_tv)).setText("会员充值");
		intent = new Intent(getActivity(), PaySelectActivity2.class);
		okBtn = getViewById(R.id.btn_sure);
		cancelBtn = getViewById(R.id.btn_cancel);
		midNameTv = getViewById(R.id.mid_name);
		cardTypeTv = getViewById(R.id.card_type);
		cardNoTv = getViewById(R.id.card_no);
		cardDateTv = getViewById(R.id.card_date);
		cardInfoTv = getViewById(R.id.card_info);
		closeBtn = getViewById(R.id.btn_close);

		return mRootView;
	}

	@Override
	public void initDatas() {
		cardNoTv.setText(coupon.getInfo());
		cardDateTv.setText(coupon.getDeadline() == null ? "" : coupon.getDeadline());
		cardInfoTv.setText(coupon.getNotice() == null ? "" : coupon.getNotice());
		cardTypeTv.setText(CardTypeEnum.getTypeName(coupon.getType()));
		midNameTv.setText(coupon.getMerchantname() == null ? "" : coupon.getMerchantname());
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_sure:
			// 确认接收任务，将卡券任务保存到卡券中心中
			CouponDBHelper.saveCoupons(coupon);
			taskReceiverListener.receiver();
			dismiss();// 关闭弹窗
			break;
		case R.id.btn_cancel:
			dismiss();
			taskReceiverListener.cancel();
			break;
		default:
			dismiss();
			break;
		}
	}
}
