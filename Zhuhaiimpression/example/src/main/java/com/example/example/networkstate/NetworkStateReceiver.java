package com.example.example.networkstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;


/**
 * @author: tuacy
 * @date: 2015/9/23 10:12
 * @version: V1.0
 */
public class NetworkStateReceiver extends BroadcastReceiver {

	private final static String ANDROID_NET_CHANGE_ACTION        = "android.net.conn.CONNECTIVITY_CHANGE";

	private static boolean                          mIsNetAvailable     = false;
	private static NetworkUtils.NetworkType         mNetworkType        = NetworkUtils.NetworkType.NONE;
	private static ArrayList<NetworkChangeObserver> mNetChangeObservers = new ArrayList<NetworkChangeObserver>();
	private static BroadcastReceiver                mBroadcastReceiver  = null;

	private static BroadcastReceiver getReceiver() {
		if (null == mBroadcastReceiver) {
			mBroadcastReceiver = new NetworkStateReceiver();
		}
		return mBroadcastReceiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		mBroadcastReceiver = NetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
			if (!NetworkUtils.isNetworkAvailable(context)) {
				mIsNetAvailable = false;
			} else {
				mIsNetAvailable = true;
				mNetworkType = NetworkUtils.getAPNType(context);
			}
			notifyObserver();
		}
	}

	private void notifyObserver() {
		if (!mNetChangeObservers.isEmpty()) {
			int size = mNetChangeObservers.size();
			for (int i = 0; i < size; i++) {
				NetworkChangeObserver observer = mNetChangeObservers.get(i);
				if (observer != null) {
					if (isNetworkAvailable()) {
						observer.onNetConnected(mNetworkType);
					} else {
						observer.onNetDisConnect();
					}
				}
			}
		}
	}

	public static boolean isNetworkAvailable() {
		return mIsNetAvailable;
	}

	public static NetworkUtils.NetworkType getAPNType() {
		return mNetworkType;
	}

	public static void registerObserver(NetworkChangeObserver observer) {
		if (null == mNetChangeObservers) {
			mNetChangeObservers = new ArrayList<NetworkChangeObserver>();
		}
		mNetChangeObservers.add(observer);
	}

	public static void removeRegisterObserver(NetworkChangeObserver observer) {
		if (null != mNetChangeObservers) {
			if (mNetChangeObservers.contains(observer)) {
				mNetChangeObservers.remove(observer);
			}
		}
	}


	public static void registerNetworkStateReceiver(Context mContext) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
	}

	public static void checkNetworkState(Context mContext) {
		Intent intent = new Intent();
		intent.setAction(ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}

	public static void unRegisterNetworkStateReceiver(Context mContext) {
		if (null != mBroadcastReceiver) {
			try {
				mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
			} catch (Exception e) {
			}
		}

	}
}
