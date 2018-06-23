package com.weilay.dialog;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.R;
import com.weilay.pos.app.Config;
import com.weilay.pos.entity.CouponEntity;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.printData.PrintOrderData;
import com.weilay.pos.titleactivity.BaseDialogFragment;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.QRCodeUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/*******
 * @detail 发送卡券的弹窗
 * @author rxwu
 *
 */
public class SendCardDialog extends BaseDialogFragment implements OnClickListener {
	private CouponEntity coupon;

	public SendCardDialog(CouponEntity coupon) {
		if (coupon == null) {
			T.showCenter("获取不到卡券的信息");
			dismiss();
			return;
		}
		this.coupon = coupon;
	}

	// 发券商户名称，卡券的标题、卡券的卡号、有效期、卡券使用须知、优惠说明、奖励说明
	private TextView midNameTv, couponTitleTv, cardstockTv, deadlineTv, cardInfoTv, cardPreferTv, cardMissionTv;
	private ImageView cardCodeIv;// 发券二维码
	private Button cancelBtn, sureBtn;

	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.dialog_send_card, container);
		}
		midNameTv = getViewById(R.id.mid_name_tv);
		couponTitleTv = getViewById(R.id.card_title_tv);
		cardstockTv = getViewById(R.id.card_stock_tv);
		cardInfoTv = getViewById(R.id.card_info_tv);
		deadlineTv=getViewById(R.id.card_date_title);
		cardPreferTv = getViewById(R.id.card_prefer_tv);
		cardMissionTv = getViewById(R.id.card_mission_tv);
		cardCodeIv = getViewById(R.id.coupon_code_iv);
		cancelBtn = getViewById(R.id.btn_cancel);
		sureBtn = getViewById(R.id.btn_sure);
		return mRootView;
	}

	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		initCard(coupon);
		midNameTv.setText(coupon.getMerchantname());
		couponTitleTv.setText("卡券名称：" + coupon.getTitle());
		cardstockTv.setText("库存：" + coupon.getStock());
		deadlineTv.setText("有效日期："+coupon.getDeadline());
		cardInfoTv.setText(coupon.getInfo());
		cardPreferTv.setText(coupon.getNotice());
		cardMissionTv.setText("任务奖励：每领一张可获得" + ConvertUtil.getMoeny(coupon.getMerchantcommission() / 100) + "元奖励");
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		sureBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_sure:
			if (coupon.getStock() >= 0) {
				DialogAsk.ask(mContext, "发券提示", "是否确认发券", "确定", "取消", new DialogAskListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						PrintOrderData.printCoupon(coupon);
					}

					@Override
					public void cancelClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						// dialog.dismiss();
					}
				});
			} else {
				DialogConfirm.ask(mContext, "卡券使用提示", "抱歉，卡券库存不足", "确定", null);
			}
			break;
		default:
			break;
		}

	}

	/*******
	 * @detail 读取卡券的二维码
	 */
	private void loadCard() {
		if (coupon.getStock() >= 0) {
			new AsyncTask<Void, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(Void... arg0) {
					// TODO Auto-generated method stub
					return QRCodeUtil.createQRImage(coupon.getUrl2qrcode(), 400, 400, null, null);
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (result != null) {
						cardCodeIv.setImageBitmap(result);
						cardCodeIv.setScaleType(ScaleType.FIT_XY);
					} else {
						cardCodeIv.setImageResource(Config.LOGO_RES);
					}
				}
			}.execute();
		} else {
			DialogConfirm.ask(mContext, "卡券使用提示", "抱歉，卡券库存不足", "确定", null);
		}
	}

	/*****
	 * @detail 发卡券列表
	 * @param coupon
	 */
	private void initCard(final CouponEntity coupon) {
		if (coupon == null) {
			return;
		}
		// TODO PRINTER
		mContext.showLoading("正在获取卡券信息");
		Utils.getCouponQRCode(mContext, coupon.getId(), new OnDataListener() {

			@Override
			public void onFailed(String msg) {
				// TODO Auto-generated method stub
				mContext.stopLoading();
				DialogConfirm.ask(mContext, "获取卡券信息提示", "获取卡券信息失败", "确定", new DialogConfirmListener() {

					@Override
					public void okClick(DialogInterface dialog) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
			}

			@Override
			public void onData(Object obj) {
				// TODO Auto-generated method stub
				mContext.stopLoading();
				String code = obj.toString();
				coupon.setUrl2qrcode(code);
				loadCard();
			}
		});
	}

}
