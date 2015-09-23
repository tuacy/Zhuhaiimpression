package com.tuacy.library.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author: tuacy
 * @date: 2015/9/22 19:17
 * @version: V1.0
 */
public abstract class BaseLazyFragment extends Fragment {
	
	private boolean isPrepared;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPrepare();
	}

	/**
	 * first time onResume call onUserVisible and call onFirstUserVisible avoid repetitive operation
	 */
	private boolean isFirstResume = true;

	@Override
	public void onResume() {
		super.onResume();
		if (isFirstResume) {
			isFirstResume = false;
			return;
		}
		if (getUserVisibleHint()) {
			onUserVisible();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (getUserVisibleHint()) {
			onUserInvisible();
		}
	}

	private boolean isFirstVisible   = true;
	private boolean isFirstInvisible = true;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (isFirstVisible) {
				isFirstVisible = false;
				initPrepare();
			} else {
				onUserVisible();
			}
		} else {
			if (isFirstInvisible) {
				isFirstInvisible = false;
				onFirstUserInvisible();
			} else {
				onUserInvisible();
			}
		}
	}

	private synchronized void initPrepare() {
		if (isPrepared) {
			onFirstUserVisible();
		} else {
			isPrepared = true;
		}
	}

	/**
	 * when fragment is visible for the first time, here we can do some initialized work or refresh data only once
	 */
	protected abstract void onFirstUserVisible();

	/**
	 * this method like the fragment's lifecycle method onResume() (switch back or onResume)
	 */
	protected abstract void onUserVisible();

	/**
	 * when fragment is invisible for the first time
	 */
	private void onFirstUserInvisible() {
		// here we do not recommend do something
	}

	/**
	 * this method like the fragment's lifecycle method onPause() (Switch off or onPause)
	 */
	protected abstract void onUserInvisible();
}
