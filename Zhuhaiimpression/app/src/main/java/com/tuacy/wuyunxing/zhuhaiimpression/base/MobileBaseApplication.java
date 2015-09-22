package com.tuacy.wuyunxing.zhuhaiimpression.base;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tuacy.wuyunxing.zhuhaiimpression.Constants;

import cn.bmob.v3.Bmob;

/**
 * @author: tuacy
 * @date: 2015/9/18 15:56
 * @version: V1.0
 */
public class MobileBaseApplication extends Application {

	public static final String TAG = "VolleyPatterns";
	/**
	 * A singleton instance of the application class for easy access in other places
	 */
	private static MobileBaseApplication sInstance;

	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		globalSettings();
	}

	/**
	 * @return ApplicationController singleton instance
	 */
	public static synchronized MobileBaseApplication getInstance() {
		return sInstance;
	}

	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			synchronized (MobileBaseApplication.class) {
				if (mRequestQueue == null) {
					mRequestQueue = Volley.newRequestQueue(getApplicationContext());
				}
			}
		}
		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then it is used else Default TAG is used.
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}


	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to specify a TAG so that the pending/ongoing requests can be
	 * cancelled.
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	private void globalSettings() {
		Bmob.initialize(this, Constants.BMOB_APPLICATION_ID);
	}
}
