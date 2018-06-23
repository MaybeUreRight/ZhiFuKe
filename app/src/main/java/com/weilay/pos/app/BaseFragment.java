package com.weilay.pos.app;

import java.lang.reflect.Field;

import com.framework.utils.L;
import com.weilay.pos.titleactivity.BaseActivity;
import com.weilay.pos.util.ACache;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public View mRootView;
	public BaseActivity mContext;
	public ACache mCache;
	private boolean reload = false;

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = (BaseActivity) getActivity();
		mCache = ACache.get(mContext);
		View view = initViews(inflater, container);
		initDatas();
		initEvents();
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public abstract View initViews(LayoutInflater inflater, ViewGroup container);

	public abstract void initDatas();

	public abstract void initEvents();

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		L.d(getClass() + "--onPause");
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		L.d(getClass() + "--onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		L.d(getClass().getSimpleName() + "-----停止");
		// stopDatas();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		L.d(getClass().getSimpleName() + (hidden ? "隐藏" : "显示"));
		if (!hidden && reload) {
			initDatas();
		}
		super.onHiddenChanged(hidden);
	}

	/**********
	 * @detail 取消网络请求的时候的调用函数，由继承基本片段方法的子类重写方便处理界面的UI
	 */
	public void cancelHttpRequest() {

	}

	public <T extends View> T getViewById(int id) {
		return (T) mRootView.findViewById(id);
	}

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
