package com.weilay.pos;

import java.util.ArrayList;
import java.util.List;

import com.framework.ui.BaseAdapter;
import com.framework.utils.StringUtil;
import com.google.zxing.common.StringUtils;
import com.weilay.pos.app.BroadcastStart;
import com.weilay.pos.app.WeiLayApplication;
import com.weilay.pos.util.BarClose;
import com.weilay.pos.util.BootUtils;
import com.weilay.pos.util.CmdForAndroid;
import com.weilay.pos.util.T;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/*****
 * @detail 系统测试
 * @author rxwu
 *
 */
public class SystemTestActivity extends ListActivity{
	private List<String>datas=new ArrayList<>();
	private TextAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		datas.clear();
		BarClose.showBar(this);
		//datas.add("压力测试");
		datas.add("视频播放");
		datas.add("重启测试");
		adapter=new TextAdapter(this,datas);
		setListAdapter(adapter);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		switch (position) {
//		case 0://压力测试
//			CmdForAndroid.shella("su", "am start -n com.android.calculator2/.Calculator");
//			break;
		case 0://视频播放
			startActivity(new Intent(this,VideoActivity.class));
			break;
		case 1://重启测试
			showInputDialog();
			break;
		default:
			break;
		}
	}
	
	private void showInputDialog(){
		final EditText inputServer = new EditText(this);
		inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入重启的次数").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            	
            	try{
            		int boot=Integer.parseInt(inputServer.getText().toString());
            		BroadcastStart.saveBoot(boot);
            		T.showCenter("即将重启");
            		BootUtils.reboot();
            	}catch (Exception e) {
					// TODO: handle exception
            		T.showCenter("输入格式有误");
				}
             }
        });
        builder.show();
	}
	
	class TextAdapter extends BaseAdapter<String>{
		public TextAdapter(Activity context, List<String> datas) {
			super(context, datas);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder=null;
			if(convertView==null){
				convertView=LayoutInflater.from(context).inflate(R.layout.item_systemtest,null);
				holder=new ViewHolder();
				holder.testTv=(TextView)convertView.findViewById(R.id.systemtest_tv);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder)convertView.getTag();
			}
			holder.testTv.setText(datas.get(position));
			return convertView;
		}
		class ViewHolder{
			TextView testTv;
		}
		
	}
	
	
	

}
