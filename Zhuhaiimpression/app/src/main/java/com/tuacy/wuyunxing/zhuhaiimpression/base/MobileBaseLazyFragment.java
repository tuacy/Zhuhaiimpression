package com.tuacy.wuyunxing.zhuhaiimpression.base;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.tuacy.library.base.BaseLazyFragment;

/**
 * @author: tuacy
 * @date: 2015/9/22 19:35
 * @version: V1.0
 */
public abstract class MobileBaseLazyFragment extends BaseLazyFragment {

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
