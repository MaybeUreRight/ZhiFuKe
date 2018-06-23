package com.weilay.pos.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.weilay.pos.R;
import com.weilay.pos.app.UrlDefine;
import com.weilay.pos.entity.SendTicketInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SendTicketAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<SendTicketInfo> list_sti;
    private Context context;

    public SendTicketAdapter(Context context, List<SendTicketInfo> list_sti) {
        inflater = LayoutInflater.from(context);
        this.list_sti = list_sti;
        this.context = context;
        // this.isNolmal = type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_sti.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list_sti.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        VH vh = null;
        SendTicketInfo sti = list_sti.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.sendticket_item_layout, null);
            vh = new VH();
            vh.cardinfo_tv = (TextView) view.findViewById(R.id.cardinfo_tv);
            vh.merchantlogo_iv = (ImageView) view
                    .findViewById(R.id.merchantlogo_iv);
            vh.merchantname_tv = (TextView) view
                    .findViewById(R.id.merchantname_tv);
            vh.sendticket_number = (TextView) view
                    .findViewById(R.id.sendticket_number);
            vh.sendticket_stock = (TextView) view
                    .findViewById(R.id.sendticket_stock);
            vh.sendticket_deadline = (TextView) view
                    .findViewById(R.id.sendticket_deadline);
            vh.layout = (RelativeLayout) view.findViewById(R.id.rl_1);
            view.setTag(vh);
        } else {
            vh = (VH) view.getTag();
        }
        setLayout(vh, sti);

        return view;
    }

    private void setLayout(VH vh, SendTicketInfo sti) {
        vh.cardinfo_tv.setText(sti.getCardinfo());
        vh.merchantname_tv.setText(sti.getMerchentname());
//		ImageLoader.getInstance().displayImage(
//				UrlDefine.BASE_URL + sti.getMerchantlogo(), vh.merchantlogo_iv);


        Glide.with(context).load(UrlDefine.BASE_URL + sti.getMerchantlogo()).into(vh.merchantlogo_iv);

        vh.sendticket_stock.setText(sti.getStock() + "张");
        vh.sendticket_deadline.setText("有效期至:" + sti.getDeadline());
        // String color = sti.getColor();
        // vh.layout.setBackgroundColor(Color.parseColor(color != null ? color
        // : "#FFFFFF"));
    }

    class VH {
        ImageView merchantlogo_iv, sendticket_reduce, sendticket_increase;
        TextView merchantname_tv, cardinfo_tv, sendticket_number,
                sendticket_stock, sendticket_deadline;
        RelativeLayout layout;
    }

}
