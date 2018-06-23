package com.weilay.pos;

import java.util.ArrayList;
import java.util.List;

import com.framework.ui.BaseAdapter;
import com.framework.ui.BaseViewHolder;
import com.weilay.pos.app.MenuDefine;
import com.weilay.pos.titleactivity.TitleActivity;
import com.weilay.pos.util.BootUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

/**************
 * @detail：MoreActivity update by rxwu，修改布局控件，删除退款的回调处理(无用) File
 *                      Name:MoreActivity.java Package:com.weilay.pos Date:
 *                      2016年12月7日下午2:57:37 Author: rxwu
 * 
 */
public class MoreActivity extends TitleActivity {
	private GridView moreGridView;
	private MenuDefine[] menus = new MenuDefine[] { MenuDefine.MORE_MANAGE, MenuDefine.MORE_REFUND,
			/*MenuDefine.MORE_CHARGE,*/ MenuDefine.MORE_SHIFT, MenuDefine.MORE_PRINT/*, MenuDefine.MORE_SHUTDOWN*/ };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_more);
		setTitle("应用中心");
		init();
		reg();
	}

	private void init() {
		moreGridView = (GridView) findViewById(R.id.more_gridview);
		moreGridView.setNumColumns(2);
		List<MenuDefine> menuDatas = new ArrayList<>();
		for (MenuDefine item : menus) {
			menuDatas.add(item);
		}
		moreGridView.setAdapter(new BaseAdapter<MenuDefine>(this, menuDatas) {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				if (arg1 == null) {
					arg1 = LayoutInflater.from(mContext).inflate(R.layout.item_menu_more, arg2, false);
				}
				AbsListView.LayoutParams param = new AbsListView.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, moreGridView.getHeight() / 3);
				arg1.setLayoutParams(param);
				ImageView img = BaseViewHolder.get(arg1, R.id.item_img);
				img.setImageResource(getItem(arg0).getIcon());
				return arg1;
			}

		});
	}

	Intent intent = null;

	private void reg() {
		moreGridView.setHorizontalSpacing(2);
		moreGridView.setVerticalSpacing(2);
		moreGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (menus[arg2]) {
				case MORE_MANAGE:
					intent = new Intent(mContext, EquipmentManagementActivity.class);
					startActivity(intent);
					break;
				case MORE_REFUND:
					RefundActivity.actionStart(mContext);
					break;
				case MORE_CHARGE:
					intent = new Intent(mContext, SendRedPackInputActivity.class);
					startActivity(intent);
					break;
				case MORE_SHIFT:
					intent = new Intent(mContext, ShiftActivity.class);
					startActivity(intent);
					break;
				case MORE_PRINT:
					intent = new Intent(mContext, PrintActivity.class);
					startActivity(intent);
					break;
				case MORE_SHUTDOWN:
					BootUtils.shutdownOrReboot(mContext);
					break;
				default:
					break;
				}

			}

		});
	}
}
