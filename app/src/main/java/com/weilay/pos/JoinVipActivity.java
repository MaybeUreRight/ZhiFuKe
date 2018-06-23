package com.weilay.pos;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.framework.ui.DialogConfirm;
import com.google.zxing.WriterException;
import com.weilay.listener.DialogConfirmListener;
import com.weilay.pos.app.Client;
import com.weilay.pos.app.Config;
import com.weilay.pos.app.PayAction;
import com.weilay.pos.app.PosDefine;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.entity.JoinVipEntity;
import com.weilay.pos.listener.JoinVipListener;
import com.weilay.pos.listener.LoadMemberRulesListener;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BaseParam;
import com.weilay.pos.util.EncodingHandler;
import com.weilay.pos.util.T;
import com.weilay.pos.util.Utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import okhttp3.FormBody;

public class JoinVipActivity extends TitleActivity {

    private String characterSet;

    private final int VIP_SUC = 9000;
    private final int VIP_ERR = 9001;
    private ImageView vip_card_qr;

    private Client client;
    private String url = "API/updateMemberBonus";
    private String vip_url = "API/putInQRCode";
    private SharedPreferences sp_login;
    private TextView vipRecharge_tv, card_name, card_info, card_time,
            vipHintTv;
    private ImageView card_logo;
    // private JoinVipInfo jvi;
    private RelativeLayout layout;

    /**
     * private Handler handler = new Handler() { public void
     * handleMessage(android.os.Message msg) { stopLoading(); switch (msg.arg1)
     * { case VIP_SUC: // if (jvi != null) { // createQR(jvi.getUrl2qrcode());
     * // card_name.setText(jvi.getName()); //
     * card_info.setText(jvi.getCardinfo()); // ImageLoader imageLoader =
     * ImageLoader.getInstance(); // imageLoader.displayImage( //
     * UrlDefine.BASE_URL + jvi.getLogo(), card_logo);// 会员图标 //
     * imageLoader.loadImage(UrlDefine.BASE_URL + jvi.getBgurl(), // new
     * ImageLoadingListener() {// 会员背景 // // @Override // public void
     * onLoadingStarted(String arg0, // View arg1) { // // TODO Auto-generated
     * method stub // // } // // @Override // public void onLoadingFailed(String
     * arg0, // View arg1, FailReason arg2) { // // TODO Auto-generated method
     * stub // // } // // @SuppressLint("NewApi") // @Override // public void
     * onLoadingComplete(String arg0, // View arg1, Bitmap bitmap) { // // TODO
     * Auto-generated method stub // BitmapDrawable drawable = new
     * BitmapDrawable( // bitmap); // layout.setBackground(drawable); // } // // @Override
     * // public void onLoadingCancelled(String arg0, // View arg1) { // // TODO
     * Auto-generated method stub // // } // }); // //
     * card_time.setText(jvi.getStarttime() + " 至 " // + jvi.getEndtime()); // }
     * // break; case VIP_ERR: T.showCenter("获取会员信息失败!"); break; case 404:
     * DialogConfirm.ask(JoinVipActivity.this, "会员提示", msg.obj == null ?
     * "您还没有上架会员卡" : msg.obj.toString(), "确定", new DialogConfirmListener() {
     *
     * @Override public void okClick(DialogInterface dialog) { // TODO
     * Auto-generated method stub finish(); } });
     * <p>
     * break; default: break; }
     * <p>
     * }; };
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.joinvip_layout);
        setTitle("会员");
        client = new Client(JoinVipActivity.this);
        sp_login = WeiLayApplication.getSp_login();
        init();
        reg();
        initDatas();
        send_Vipcard();
    }

    private void initDatas() {
        Utils.getMemberUpgradeRules(this, new LoadMemberRulesListener() {

            @Override
            public void loadSuccess(List<String> rules) {
                // TODO Auto-generated method stub
                StringBuffer content = new StringBuffer();
                for (String rule : rules) {
                    content.append("☞  " + rule + "\n");
                }
                vipHintTv.setText(content.toString());
            }

            @Override
            public void loadFailed(String msg) {
                // TODO Auto-generated method stub
                // T.showCenter("获取会员升级规则失败");
            }
        });
    }

    private void init() {
        vip_card_qr = (ImageView) findViewById(R.id.vip_card_qr);
        vipRecharge_tv = (TextView) findViewById(R.id.vip_recharge);
        card_name = (TextView) findViewById(R.id.card_name);
        card_info = (TextView) findViewById(R.id.vip_card_info);
        card_time = (TextView) findViewById(R.id.vip_card_time);
        card_logo = (ImageView) findViewById(R.id.vip_card_logo);
        vipHintTv = (TextView) findViewById(R.id.vip_hint);
        layout = (RelativeLayout) findViewById(R.id.prl_1);
        card_name.setText(Config.COMPANY_NAME);
        card_info.setText(Config.COMPANY_NAME + "玫瑰特权蓝卡");
        card_logo.setImageResource(Config.LOGO_RES);

    }

    private int membertype = 1;//0 表示金额充值 1表示次数充值

    private void reg() {
        vipRecharge_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(JoinVipActivity.this, (membertype == 0 ?
                        MemberRechargeActivity.class : MemberRechargeActivity.class));
                intent.putExtra(PosDefine.INTENT_PAY_ACTION,
                        PayAction.MEMBER_RECHARGE_PAY);
                intent.putExtra("payType", "vipRecharge");
                startActivity(intent);
            }
        });
    }

    private void send_Vipcard() {
        showLoading("正在获取会员信息...");
        FormBody.Builder builder = BaseParam.getParams();

        Utils.JoinVip(mContext, builder, new JoinVipListener() {

            @Override
            public void onSuc(JoinVipEntity jvi) {
                // TODO Auto-generated method stub
                stopLoading();
                if (jvi != null) {
                    createQR(jvi.getUrl2qrcode());
                    card_name.setText(jvi.getName());
                    card_info.setText(jvi.getCardinfo());
                    Glide.with(JoinVipActivity.this)
                            .load(UrlDefine.BASE_URL + jvi.getLogo())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    layout.setBackground(resource);
                                    return false;
                                }
                            })
                            .into(card_logo);

                    card_time.setText(jvi.getStarttime() + " 至 " + jvi.getEndtime());
                }
            }

            @Override
            public void onErr() {
                // TODO Auto-generated method stub
                stopLoading();
                T.showCenter("获取会员信息失败!");
            }

            @Override
            public void on404(String msg) {
                // TODO Auto-generated method stub
                stopLoading();
                DialogConfirm.ask(JoinVipActivity.this, "会员提示",
                        msg == null ? "您还没有上架会员卡" : msg, "确定",
                        new DialogConfirmListener() {

                            @Override
                            public void okClick(DialogInterface dialog) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
            }
        });
    }

    private void createQR(String QRurl) {
        // 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
        Bitmap qrcodeBitmap;
        try {
            qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 700);
            vip_card_qr.setImageBitmap(qrcodeBitmap);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
