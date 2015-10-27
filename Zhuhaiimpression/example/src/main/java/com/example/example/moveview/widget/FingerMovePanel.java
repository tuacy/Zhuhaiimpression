package com.example.example.moveview.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.example.R;
import com.example.example.moveview.bean.MoveViewBean;

import java.util.ArrayList;
import java.util.List;

public class FingerMovePanel extends FrameLayout implements View.OnTouchListener {

	private int                mLastX    = 0;
	private int                mLastY    = 0;
	private List<MoveViewBean> mViewList = new ArrayList<>();

	public FingerMovePanel(Context context) {
		super(context);
	}

	public FingerMovePanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FingerMovePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void reInitViews(List<MoveViewBean> viewList) {
		mViewList = viewList;
		for (MoveViewBean moveViewBean : mViewList) {
			ImageView imageView = new ImageView(this.getContext());
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
																	   ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.setMargins(moveViewBean.getLeft(), moveViewBean.getTop(), moveViewBean.getRight(), moveViewBean.getBottom());
			imageView.setLayoutParams(lp);
			imageView.setImageResource(R.mipmap.ic_launcher);
			imageView.setClickable(true);
			imageView.setOnTouchListener(this);
			imageView.setLayoutParams(lp);
			imageView.setTag(moveViewBean.getTag());
			this.addView(imageView);
		}
	}

	public List<MoveViewBean> getMoveViewBeanList() {
		return mViewList;
	}

	public void addMoveView(MoveViewBean moveBean) {
		mViewList.add(moveBean);
		ImageView imageView = new ImageView(this.getContext());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
																   ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(moveBean.getLeft(), moveBean.getTop(), moveBean.getRight(), moveBean.getBottom());
		imageView.setLayoutParams(lp);
		imageView.setImageResource(R.mipmap.ic_launcher);
		imageView.setClickable(true);
		imageView.setOnTouchListener(this);
		imageView.setLayoutParams(lp);
		imageView.setTag(moveBean.getTag());
		this.addView(imageView);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastX = (int) event.getRawX();
				mLastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int dx = (int) event.getRawX() - mLastX;
				int dy = (int) event.getRawY() - mLastY;
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
				int lastLeftMargin = lp.leftMargin;
				int lastTopMargin = lp.topMargin;

				int rawLeftMargin = lastLeftMargin + dx;
				int rawTopMargin = lastTopMargin + dy;

				if (rawLeftMargin < 0) {
					rawLeftMargin = 0;
				}
				if (rawLeftMargin + v.getWidth() > this.getWidth()) {
					rawLeftMargin = this.getWidth() - v.getWidth();
				}

				if (rawTopMargin < 0) {
					rawTopMargin = 0;
				}
				if (rawTopMargin + v.getHeight() > this.getHeight()) {
					rawTopMargin = this.getHeight() - v.getHeight();
				}
				updateMoveViewBeanList((String) v.getTag(), rawLeftMargin, rawTopMargin, 0, 0);
				lp.setMargins(rawLeftMargin, rawTopMargin, 0, 0);
				v.setLayoutParams(lp);
				mLastX = (int) event.getRawX();
				mLastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return false;
	}

	private void updateMoveViewBeanList(String tag, int left, int top, int right, int bottom) {
		for(MoveViewBean moveViewBean : mViewList) {
			if (moveViewBean.getTag().equals(tag)) {
				moveViewBean.setLeft(left);
				moveViewBean.setTop(top);
				moveViewBean.setRight(right);
				moveViewBean.setBottom(bottom);
			}
		}
	}
}
