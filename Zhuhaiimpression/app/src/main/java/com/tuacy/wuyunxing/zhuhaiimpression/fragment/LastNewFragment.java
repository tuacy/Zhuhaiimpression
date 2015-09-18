package com.tuacy.wuyunxing.zhuhaiimpression.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseFragment;

/**
 * @author: tuacy
 * @date: 2015/9/18 17:36
 * @version: V1.0
 */
public class LastNewFragment extends MobileBaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		TextView view = new TextView(getActivity());
		view.setText("test");
		view.setGravity(Gravity.CENTER);
		view.setTextSize(24);
		return view;
	}
}
