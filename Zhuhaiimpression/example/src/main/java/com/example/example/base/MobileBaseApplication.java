package com.example.example.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: tuacy
 * @date: 2015/9/18 15:56
 * @version: V1.0
 */
public class MobileBaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

	public static final String TAG = "VolleyPatterns";

	private static MobileBaseApplication sInstance;
	private final List<Activity> mActivities = new ArrayList<Activity>();


	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

	public static synchronized MobileBaseApplication getInstance() {
		return sInstance;
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

	}

	protected void deinitializeApplication() {
	}
}
