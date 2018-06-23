package com.framework.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/********
 * File Name:BasePagerAdapter.java
 * Package:com.framework.ui	
 * Date: 2016年12月1日下午2:08:15
 * Author: rxwu
 * Detail:BasePagerAdapter
 */
public  class BasePagerAdapter extends PagerAdapter{
	private List<View>mViews;
	private Activity mActivity;
	public  BasePagerAdapter(Activity activity,List<View> Views) {
		// TODO Auto-generated constructor stub
		this.mViews=Views;
		this.mActivity=activity;
	}
	public  BasePagerAdapter(Activity activity,View[] views) {
		// TODO Auto-generated constructor stub
		mViews=new ArrayList<>();
		if(views!=null){
			for(View item:views){
				mViews.add(item);
			}
		}
		this.mActivity=activity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViews.size();
	}
    
    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  
        // TODO Auto-generated method stub  
        container.addView(mViews.get(position));  
        return mViews.get(position);  
    }  
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
	public void notifyDataSetChange(List<View>views){
		if(views==null){
			views=new ArrayList<>();
		}
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChange(View[]views){
		mViews=new ArrayList<>();
		if(views!=null){
			for(View item:views){
				mViews.add(item);
			}
		}
		notifyDataSetChanged();
	}

}
