package com.weilay.dialog;

import com.framework.utils.InputMoneyFilter;
import com.framework.utils.StringUtil;
import com.weilay.pos.R;
import com.weilay.pos.util.KeyboardUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.text.InputFilter;
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
public class NumberDialog extends AlertDialog.Builder {
    private Context context;
    private EditText curEditText;
    KeyboardView keyboard;
    public boolean isShowing=false;
    private KeyboardUtil util;
    private String title;
    private TextView editText;
    private InputMoneyFilter mFilter;
    private OnFinish mFinish;
    private boolean mCopy=true;
    public static interface OnFinish{
    	public void onFinish(String num);
    }
    public void setOnFinish(OnFinish finish){
    	this.mFinish=finish;
    }
    public NumberDialog(Context context, int theme, final TextView editText,String title) {
        super(context, theme);
        this.context=context;
        this.title=title;
        this.editText=editText;
    }
    public NumberDialog(Context context, int theme, final TextView editText,String title,boolean copy) {
        super(context, theme);
        this.context=context;
        this.title=title;
        this.editText=editText;
        this.mCopy=copy;
    }




    public void showKeyBoard(){
        util=new KeyboardUtil((Activity)context,context,curEditText,keyboard,true);
        util.showKeyboard();
    }

    @Override
    public AlertDialog create() {
        dialog=super.create();
        View dialogView=LayoutInflater.from(context).inflate(R.layout.dialog_number,null);
        keyboard=(KeyboardView)dialogView.findViewById(R.id.dialog_number_keyboard);
        curEditText=(EditText)dialogView.findViewById(R.id.dialog_number_edittext);
        ImageView button=(ImageView) dialogView.findViewById(R.id.dialog_close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView titleTv=((TextView)dialogView.findViewById(R.id.dialog_numbere_title));
        titleTv.setText(title);
        titleTv.requestFocus();
        //隐藏键盘
        if(editText!=null){
        	if(mCopy){
        		 curEditText.setText(editText.getText());
        	}else{
        		curEditText.setHint(editText.getText());
        	}
           
        }
        if(mFilter!=null){
        	curEditText.setFilters(new InputFilter[]{mFilter});
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
	            try{
	            	String text=curEditText.getText().toString();
	            	text=StringUtil.isBank(text)?editText.getText().toString():text;
	                editText.setText(text);
	            	if(mFinish!=null){
	            		mFinish.onFinish(text);
	            	} 
	            	dismiss();
	            }catch (Exception e) {
					// TODO: handle exception
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





	public void setFilter(InputMoneyFilter filter) {
		// TODO Auto-generated method stub
		this.mFilter=filter;
	}




}
