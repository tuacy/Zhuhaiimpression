package com.tuacy.wuyunxing.zhuhaiimpression.base;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tencent.connect.auth.QQAuth;
import com.tuacy.wuyunxing.zhuhaiimpression.Constants;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * @author: tuacy
 * @date: 2015/9/18 15:56
 * @version: V1.0
 */
public class MobileBaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

	public static final String TAG = "VolleyPatterns";

	private static MobileBaseApplication sInstance;
	private final List<Activity> mActivities = new ArrayList<Activity>();

	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;
	private QQAuth       mQQAuth;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

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

	public QQAuth getQQAuth() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mQQAuth == null) {
			synchronized (MobileBaseApplication.class) {
				if (mQQAuth == null) {
					mQQAuth = QQAuth.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
				}
			}
		}
		return mQQAuth;
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
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			String id = appInfo.metaData.getString("bmob_application_id");
			Bmob.initialize(this, id);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private int mActivityCount = 0;

	public List<Activity> getActivities() {
		return mActivities;
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		if (mActivityCount == 0) {
			initializeApplication();
		}
		mActivities.add(activity);
		mActivityCount++;
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		mActivityCount--;
		mActivities.remove(activity);
		if (mActivityCount == 0) {
			deinitializeApplication();
		}
	}

	public void exit() {
		for (int i = mActivities.size() - 1; i >= 0; i--) {
			Activity activity = mActivities.get(i);
			activity.finish();
		}
	}

	protected void initializeApplication() {
		globalSettings();
		/** init JPush */
		JPushInterface.init(getApplicationContext());
	}

	protected void deinitializeApplication() {
		JPushInterface.stopPush(getApplicationContext());
	}
}
