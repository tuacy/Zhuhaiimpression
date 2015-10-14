package com.example.example.measurespec;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author: tuacy
 * @date: 2015/10/13 9:30
 * @version: V1.0
 */
public class MeasureSpecViewTest extends View {

	private static final String TAG = "TestView";

	public MeasureSpecViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MeasureSpecViewTest(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeasureSpecViewTest(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onMeasure:" + getTag());
		setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec, false),
							 getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec, true));
	}

	public static int getDefaultSize(int size, int measureSpec, boolean width) {
		int result = size;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		Log.i(TAG ,"size = " + size +  " specSize " + specSize + " " + width);
		switch (specMode) {
			case MeasureSpec.UNSPECIFIED:
				Log.i(TAG, "specMode:MeasureSpec.UNSPECIFIED");
				result = size;
				break;
			case MeasureSpec.AT_MOST:
				Log.i(TAG, "specMode:MeasureSpec.AT_MOST");
				result = specSize;
				break;
			case MeasureSpec.EXACTLY:
				Log.i(TAG, "specMode:MeasureSpec.EXACTLY");
				result = specSize;
				break;
		}
		return result;
	}
}
