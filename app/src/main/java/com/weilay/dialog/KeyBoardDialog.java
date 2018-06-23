package com.weilay.dialog;


import com.weilay.pos.R;
import com.weilay.pos.util.KeyboardUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rxwu on 2016/5/11.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:数字键盘
 */
public class KeyBoardDialog extends AlertDialog.Builder{
    public boolean isShowing=false;
    private Context context;
    private EditText curEditText;
    KeyboardView keyboard;
    private KeyboardUtil util;
    private String title;
    private EditText editText;
    public KeyBoardDialog(Context context, int theme, final EditText editText, String title) {
        super(context, theme);
        this.context=context;
        this.title=title;
        this.editText=editText;
    }

    /****
     * @detail 显示键盘
     */
    public void showKeyBoard(){
        util=new KeyboardUtil((Activity)context,context,curEditText,keyboard,false);
        util.showKeyboard();
    }

    @Override
    public AlertDialog create() {
        dialog=super.create();
        View dialogView=LayoutInflater.from(context).inflate(R.layout.dialog_keyboard,null);
        ImageView button=(ImageView) dialogView.findViewById(R.id.dialog_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        keyboard=(KeyboardView)dialogView.findViewById(R.id.dialog_keyboard);
        curEditText=(EditText)dialogView.findViewById(R.id.dialog_edittext);
        TextView titleTv=((TextView)dialogView.findViewById(R.id.dialog_title));
        titleTv.setText(title);
        titleTv.requestFocus();
        //隐藏键盘
        if(editText!=null){
            curEditText.setText(editText.getText());
        }
        showKeyBoard();
        curEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoard();
            }
        });
        util.setOnFinishListener(new KeyboardUtil.OnFinishListener(){
            @Override
            public void onFinish() {
                if(editText!=null){
                    editText.setText(curEditText.getText());
                    dismiss();
                }
            }
        });
        setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(editText!=null){
                    editText.setText(curEditText.getText());
                }
            }
        });
        dialog.setView(dialogView,0,0,0,0);
        return dialog;
    }

    AlertDialog dialog=null;
    @Override
    public AlertDialog show() {
        isShowing=true;
        return super.show();
    }
    public void dismiss(){
        isShowing=false;
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}