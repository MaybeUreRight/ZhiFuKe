package com.weilay.pos.titleactivity;

import java.lang.reflect.Field;

import com.weilay.pos.app.BaseFragment;

import android.support.v4.app.Fragment;

/******
 * @detail 片段基类抽象
 * @author rxwu
 * @date 2016/08/22
 *
 */
public abstract class SendTickectFragment extends BaseFragment{

	/*******
	 * @detail 重写onDetach,避免发生activity has been destoryed 错误
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
