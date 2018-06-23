package com.weilay.pos;

import java.util.List;

import org.json.JSONObject;

import com.framework.ui.DialogAsk;
import com.framework.ui.DialogConfirm;
import com.framework.utils.InputMoneyFilter;
import com.framework.utils.L;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.weilay.dialog.PermissionDialog;
import com.weilay.listener.DialogAskListener;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.adapter.RechargePreferAdapter;
import com.weilay.pos.adapter.TimeRechargeAdapter;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.MemberEntity;
import com.weilay.pos.entity.MemberTimesLevelEntity;
import com.weilay.pos.entity.RechargePreferEntity;
import com.weilay.pos.listener.OnDataListener;
import com.weilay.pos.listener.ResponseListener;
import com.weilay.pos.printData.PrintOrderData;
import com.weilay.pos.titleactivity.NotTitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.ConvertUtil;
import com.weilay.pos.util.HttpUtils;
import com.weilay.pos.util.KeyboardUtil;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.FormBody;

/******
 * @detail 会员充值页面
 * @author rxwu
 * @date 2016/07/14
 */
public class MemberRechargeActivity extends NotTitleActivity implements OnClickListener, OnTouchListener {

	private LinearLayout member_ll, scanll;
	private FrameLayout keyboard_ll;
	private TextView memberNoTv, memberNameTv, memberAmtTv, memberGiftTv, memeberRechargeGiftTv, memberLevelTv;
	private EditText memberNoEt, rechargeEt;
	private Button searchBtn, rechargeBtn;
	private KeyboardView keyboardview;
	private List<RechargePreferEntity> recharges;
	private String memberCode;
	private MemberTimesLevelEntity currentLevel;
	private ImageView scanIv;

	private GridView rechargePreferGv;
	private RechargePreferAdapter rechargeAdapter;

	// 按次数充值
	private LinearLayout member_ll1;
	private TextView memberNoTv1, memberNameTv1;
	private List<MemberTimesLevelEntity> datas;
	private ListView timeLv;
	private TimeRechargeAdapter timeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentLayout(R.layout.activity_member_recharge);
		if (Utils.getCurOperator() != null && Utils.getCurOperator().getQrcode_lock_1() == 1) {
			new PermissionDialog().show(getSupportFragmentManager(), getLocalClassName());
		}
		initView();
		initEvents();
		initDatas();
	}

	// 初始化界面
	private void initView() {
		// 启动扫描
		startScan();
		getViewfinderView().setScanSize(0.7);
		((TextView) findViewById(R.id.common_head_tv)).setText("会员充值");
		member_ll = (LinearLayout) findViewById(R.id.member_ll);
		scanll = (LinearLayout) findViewById(R.id.scan_ll);
		keyboard_ll = (FrameLayout) findViewById(R.id.keyboard_ll);
		keyboardview = (KeyboardView) findViewById(R.id.keyboard_view);

		memberNoTv = (TextView) findViewById(R.id.member_no_tv);
		memberNameTv = (TextView) findViewById(R.id.member_name_tv);
		memberAmtTv = (TextView) findViewById(R.id.member_amt_tv);
		memberGiftTv = (TextView) findViewById(R.id.member_gift_tv);
		memeberRechargeGiftTv = (TextView) findViewById(R.id.member_recharge_gift_tv);
		memberLevelTv = (TextView) findViewById(R.id.member_level_tv);

		memberNoEt = (EditText) findViewById(R.id.member_search_et);
		rechargeEt = (EditText) findViewById(R.id.member_recharge_et);
		rechargeEt.getEditableText().setFilters(new InputFilter[] { new InputMoneyFilter(100000) });
		searchBtn = (Button) findViewById(R.id.member_search_btn);
		rechargeBtn = (Button) findViewById(R.id.member_recharge_btn);
		scanIv = (ImageView) findViewById(R.id.scan_iv);

		rechargePreferGv = (GridView) findViewById(R.id.recharge_prefer_gv);
		// 空提示
		rechargeAdapter = new RechargePreferAdapter(this);
		TextView emptyView = (TextView) findViewById(R.id.empty_view);
		rechargePreferGv.setEmptyView(emptyView);
		rechargePreferGv.setAdapter(rechargeAdapter);
		memberGiftTv.setFocusable(true);
		memberGiftTv.setFocusableInTouchMode(true);
		memberGiftTv.requestFocus();
		memberGiftTv.requestFocusFromTouch();

		// 初始化按此付费的控件
		member_ll1 = (LinearLayout) findViewById(R.id.member_ll1);
		memberNoTv1 = (TextView) findViewById(R.id.member_no_tv1);
		memberNameTv1 = (TextView) findViewById(R.id.member_name_tv1);
		timeLv = (ListView) findViewById(R.id.times_lv);
		// 空提示
		timeAdapter = new TimeRechargeAdapter(this, datas);
		TextView emptyView1 = (TextView) findViewById(R.id.empty_view1);
		timeLv.setEmptyView(emptyView1);
		timeLv.setAdapter(timeAdapter);
	}

	// 初始化data
	private void initDatas() {
		queryRechagePrefer();
	}

	private void showCamera(boolean scan) {
		if (scan == this.scan) {
			return;
		}
		this.scan = scan;
		if (!scan) {
			scanIv.setImageResource(R.drawable.btn_scan);
			scanll.setVisibility(View.GONE);
			member_ll.setVisibility(View.VISIBLE);
			keyboard_ll.setVisibility(View.VISIBLE);
			rechargeBtn.setVisibility(View.VISIBLE);
		} else {
			scanIv.setImageResource(R.drawable.icon_keyboard);
			scanll.setVisibility(View.VISIBLE);
			member_ll.setVisibility(View.GONE);
			keyboard_ll.setVisibility(View.GONE);
			rechargeBtn.setVisibility(View.GONE);
			restartCerame();
		}
	}

	boolean scan = false;

	// 初始化事件
	private void initEvents() {
		searchBtn.setOnClickListener(this);
		rechargeBtn.setOnClickListener(this);
		memberNoEt.setOnTouchListener(this);
		rechargeEt.setOnTouchListener(this);
		scanIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCamera(!scan);
			}
		});
		rechargeEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String recharge = arg0.toString();// 充值的金额
				double rechargeMoney = 0f;
				try {
					rechargeMoney = Double.parseDouble(recharge);
				} catch (Exception e) {
					// TODO: handle exception
					rechargeMoney = 0;
				}
				if (rechargeMoney == 0) {
					return;
				}
				if (recharges != null) {
					for (int i = 0; i < recharges.size(); i++) {
						double ifAmt = ConvertUtil.getMoeny(recharges.get(i).getRechargeamt() / 100);
						double giftAmt = ConvertUtil.getMoeny(recharges.get(i).getLargessamt() / 100);
						if (rechargeMoney >= ifAmt) {
							memeberRechargeGiftTv.setText(giftAmt + "");
							break;
						} else {
							memeberRechargeGiftTv.setText("0");
						}
					}
				}
			}
		});
		timeLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				currentLevel = datas.get(arg2);
				timeAdapter.setSelect(arg2);
			}

		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		hideKeyBoard();
		switch (arg0.getId()) {
		case R.id.member_search_btn:
			// 点击查找会员信息
			memberCode = memberNoEt.getText().toString();
			if (TextUtils.isEmpty(memberCode)) {
				return;
			}
			queryMemberInfo(memberCode);
			break;
		case R.id.member_recharge_btn:
			// 跳转到支付的页面
			if (memberEntity == null) {
				T.showCenter("请指定充值的会员");
				return;
			}
			if (memberEntity.getMember_card_type() != 1 && TextUtils.isEmpty(rechargeEt.getText())) {
				T.showCenter("请输入充值金额");
				return;
			}
			if (memberEntity.getMember_card_type() == 1 && currentLevel == null) {
				T.showCenter("请选择要充值的次数");
				return;
			}
			String tip = memberEntity.getMember_card_type() == 1 ? ("确定充值" + currentLevel.getTimes() + "次")
					: ("是否充值" + ConvertUtil.getMoeny(rechargeEt.getText()) + "元到会员卡");
			DialogAsk.ask(this, "充值会员", tip, "确定", "取消", new DialogAskListener() {

				@Override
				public void okClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
					final double recharge = ConvertUtil.getMoeny(rechargeEt.getText());
					final double gift = ConvertUtil.getMoeny(memeberRechargeGiftTv.getText());
					Utils.memberRecharge(MemberRechargeActivity.this, memberEntity, recharge, currentLevel, gift,
							new OnDataListener() {

								@Override
								public void onFailed(String msg) {
									// TODO Auto-generated method stub
									T.showCenter("充值失败");
								}

								@Override
								public void onData(Object obj) {
									// TODO Auto-generated method stub
									askComplete("充值成功");
									PrintOrderData.printMemberRecharge(memberEntity, recharge, gift, currentLevel);
								}
							});
				}

				@Override
				public void cancelClick(DialogInterface dialog) {
					// TODO Auto-generated method stub
				}
			});
			break;
		default:
			break;
		}
	}

	/*****
	 * @detail 充值完成后提示
	 * @param content
	 *            提示内容
	 */
	private void askComplete(String content) {
		DialogConfirm.ask(MemberRechargeActivity.this, "充值提示", content, "确定", new DialogConfirmListener() {

			@Override
			public void okClick(DialogInterface dialog) {
				// TODO Auto-generated
				// method stub
				finish();
			}
		});
	}

	KeyboardUtil numberDialog;

	private void hideKeyBoard() {
		if (numberDialog != null) {
			numberDialog.hideKeyboard();
			numberDialog = null;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.member_search_et:
			// if (numberDialog == null || numberDialog.getEdit() != memberNoEt)
			// {
			numberDialog = new KeyboardUtil(MemberRechargeActivity.this, MemberRechargeActivity.this, memberNoEt,
					keyboardview, true);
			numberDialog.showKeyboard();
			// }
			break;
		case R.id.member_recharge_et:
			L.i("-----member_recharge_et:OnTouch");
			// if (numberDialog == null || numberDialog.getEdit() != rechargeEt)
			// {
			numberDialog = new KeyboardUtil(MemberRechargeActivity.this, MemberRechargeActivity.this, rechargeEt,
					keyboardview, true);
			numberDialog.showKeyboard();
			// }
			// showCamera(false);
			break;
		}
		return false;
	}

	MemberEntity memberEntity;// 会员的信息

	/******
	 * @detail 查询会员的基本信息
	 */
	public void queryMemberInfo(String card_no) {
		if (TextUtils.isEmpty(card_no)) {
			return;
		}
		hideKeyBoard();
		showLoading("查询会员信息中");
		FormBody.Builder param = BaseParam.getParams();
		param.add("code", card_no);
		HttpUtils.sendPost(this, param, UrlDefine.URL_GET_MEMBER_IFNO, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				stopLoading();
				if (json != null && json.optInt("code") == 0) {
					// String dataStr=json.optString("data");
					// 如果成功
					final List<MemberEntity> members = new Gson().fromJson(json.optString("data"),
							new TypeToken<List<MemberEntity>>() {
							}.getType());
					if (members != null && members.size() > 0) {
						memberEntity = members.get(0);
					} else {
						memberEntity = null;
					}
					// 检查会员信息是否为空
					if (memberEntity != null) {
						if (memberEntity.getMember_card_type() == 1) {
							// 次数卡
							member_ll.setVisibility(View.GONE);
							member_ll1.setVisibility(View.VISIBLE);
							try {
								datas = new Gson().fromJson(
										json.optJSONArray("data").optJSONObject(0).optString("member_times_level"),
										new TypeToken<List<MemberTimesLevelEntity>>() {
										}.getType());
								timeAdapter.notifyDataSetChange(datas);
							} catch (Exception e) {
								L.e("返回错误结果：" + e.getLocalizedMessage());
							}

						} else {
							// 充值卡
							member_ll.setVisibility(View.VISIBLE);
							member_ll1.setVisibility(View.GONE);
						}
					} else {
						T.showCenter("查无此会员");
						clearMember();
					}

					memberNameTv.setText("会员姓名:" + memberEntity.getNickname());
					memberNoTv.setText("会员编号:" + memberEntity.getMembership_number());
					memberNameTv1.setText("会员姓名:" + memberEntity.getNickname());
					memberNoTv1.setText("会员编号:" + memberEntity.getMembership_number());
					memberAmtTv.setText("会员余额:" + ConvertUtil.branchToYuan(memberEntity.getBalance()) + "元");
					memberGiftTv.setText("赠送余额:" + ConvertUtil.branchToYuan(memberEntity.getGiveamount()) + "元");
					memberLevelTv.setText("会员等级:" + memberEntity.getLevel());
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				stopLoading();
				// T.showCenter("");
				T.showCenter("查找出错（" + msg + ")");
				clearMember();
			}
		});
	}

	private void clearMember() {
		memberNameTv.setText("会员姓名:");
		memberNoTv.setText("会员编号");
		memberAmtTv.setText("会员余额：");
		memberGiftTv.setText("赠送余额:");
		rechargeEt.setText("");
		memeberRechargeGiftTv.setText("0");
		memberNameTv1.setText("会员姓名:");
		memberNoTv1.setText("会员编号");
	}

	/*****
	 * @detail 查询充值赠送规则
	 */
	private void queryRechagePrefer() {
		// 查询充值赠送规则
		FormBody.Builder params = BaseParam.getParams();
		HttpUtils.sendPost(this, params, UrlDefine.URL_RECHARGE_PREFERS, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				recharges = new Gson().fromJson(json.optString("data"), new TypeToken<List<RechargePreferEntity>>() {
				}.getType());
				rechargeAdapter.notifyDataSetChange(recharges);
			}

			@Override
			public void onFailed(int code, String msg) {
				// TODO Auto-generated method stub
				T.showCenter("获取赠送信息失败");
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryMemberInfo(memberCode);
	}

	/******
	 * @detail 处理扫描结果
	 * @param result
	 */
	@Override
	public void handleDecode(Result result) {
		super.handleDecode(result);
		// TODO 处理扫描的结果
		String member_no = result.getText();
		T.showCenter(member_no == null ? "没有内容" : member_no);
		if (memberNoEt != null) {
			memberNoEt.setHint(member_no);
		}
		queryMemberInfo(member_no);
		showCamera(false);
	}

}
