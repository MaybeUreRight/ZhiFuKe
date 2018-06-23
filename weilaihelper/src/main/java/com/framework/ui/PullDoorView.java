package com.framework.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/*******
 * 
 * @detail 启动页面动画
 * @author rxwu
 *
 * @date 2016/08/22
 * 
 */
public class PullDoorView extends RelativeLayout {
	private Context mContext;
	private Scroller mScroller;
	private int mScreenHeigh = 0;
	private int mLastDownY = 0;
	private int mCurryY;
	private int mDelY;
	private boolean mCloseFlag = false; // 是否关闭推动页

	public PullDoorView(Context context) {
		super(context);
		mContext = context;
		setupView();
		loadChildView();
	}

	public PullDoorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupView();
		loadChildView();
	}

	public int getScreenHeigh() {
		return this.mScreenHeigh;
	}

	private void setupView() {
		// 有弹跳效果的Interpolator
		Interpolator polator = new BounceInterpolator();
		mScroller = new Scroller(mContext, polator);

		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (mContext.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenHeigh = dm.heightPixels;
	}

	// 得到控件
	private void loadChildView() {
		Log.i("this.child count", "" + this.getChildCount());
		Log.i("this.child count", "" + PullDoorView.this.getChildCount());
	}

	// 推动门的动画
	private void startBounceAnim(int startY, int dy, int duration) {
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();
	}

	// 返回键收起门帘
	public void endBounceAnim(int startY, int dy, int duration) {
		mScroller = new Scroller(mContext);
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();
		mCloseFlag = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastDownY = (int) event.getY();
			return true;

		case MotionEvent.ACTION_MOVE:
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;
			// 上滑有效
			if (mDelY < 0) {
				scrollTo(0, -mDelY);
			}
			break;

		case MotionEvent.ACTION_UP:
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;
			mDelY = (mDelY > mScreenHeigh / 3 ? mScreenHeigh / 3 : mDelY);

			if (Math.abs(mDelY) > mScreenHeigh / 3) {
				// 向上滑动超过大半个屏幕高的时候 开启向上消失动画
				startBounceAnim(this.getScrollY(), mScreenHeigh, 450);
				mCloseFlag = true;
			} else {
				// 向上滑动未超过大半个屏幕高的时候 开启向下弹动动画
				startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else if (mCloseFlag) {
			onPullDoorListener.onEnd();
			this.setVisibility(View.GONE);
		}
	}

	private onPullDoorListener onPullDoorListener;

	public void setOnPullDoorListener(onPullDoorListener listener) {
		this.onPullDoorListener = listener;
	}

	public interface onPullDoorListener {
		/**
		 * 门帘效果结束时触发
		 */
		void onEnd();
	}
}