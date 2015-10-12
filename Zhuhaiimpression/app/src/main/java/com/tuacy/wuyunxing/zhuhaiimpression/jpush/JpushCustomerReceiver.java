package com.tuacy.wuyunxing.zhuhaiimpression.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * @author: tuacy
 * @date: 2015/10/12 10:54
 * @version: V1.0
 */
public class JpushCustomerReceiver extends BroadcastReceiver {

	private static final String TAG = "JpushCustomerReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[JpushCustomerReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[JpushCustomerReceiver] Receive Registration Id : " + regId);
			//send the Registration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[JpushCustomerReceiver] Receive push customer message: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[JpushCustomerReceiver] Receive push notification");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[JpushCustomerReceiver] Receive push notification ID: " + notifactionId);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[JpushCustomerReceiver] Use click the notification");
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[JpushCustomerReceiver] Use receive RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[JpushCustomerReceiver]" + intent.getAction() + " connected state change to " + connected);
		} else {
			Log.d(TAG, "[JpushCustomerReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
