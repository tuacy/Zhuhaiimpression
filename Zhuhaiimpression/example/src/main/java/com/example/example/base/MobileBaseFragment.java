package com.example.example.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author: tuacy
 * @date: 2015/9/18 17:31
 * @version: V1.0
 */
public abstract class MobileBaseFragment extends Fragment {

	protected Context mContext;

	public MobileBaseFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	final protected void goActivity(Class<?> clazz) {
		Intent intent = new Intent(mContext, clazz);
		startActivity(intent);
	}

	final protected void goActivity(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(mContext, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	final protected void goActivityThenKill(Class<?> clazz) {
		Intent intent = new Intent(mContext, clazz);
		startActivity(intent);
		getActivity().finish();
	}

	final protected void goActivityThenKill(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(mContext, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		getActivity().finish();
	}

	final protected void goActivityForResult(Class<?> clazz, int requestCode) {
		Intent intent = new Intent(getActivity(), clazz);
		startActivityForResult(intent, requestCode);
	}

	final protected void goActivityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
		Intent intent = new Intent(getActivity(), clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	final protected void snackbar(View view, int strId) {
		Snackbar.make(view, strId, Snackbar.LENGTH_SHORT).show();
	}

	final protected void snackbar(View view, int strId, int duration) {
		Snackbar.make(view, strId, duration).show();
	}

	final protected void snackbar(View view, String str) {
		Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show();
	}

	final protected void snackbar(View view, String str, int duration) {
		Snackbar.make(view, str, duration).show();
	}

}
