package com.tuacy.wuyunxing.zhuhaiimpression.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkChangeObserver;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkStateReceiver;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkUtils;

/**
 * @author: tuacy
 * @date: 2015/9/18 15:56
 * @version: V1.0
 */
public abstract class MobileBaseActivity extends AppCompatActivity {

	protected NetworkChangeObserver mNetworkChangeObserver = null;

	public enum TransitionMode {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		SCALE,
		FADE,
		ZOOM
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (null != getOverridePendingTransitionMode()) {
			switch (getOverridePendingTransitionMode()) {
				case LEFT:
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					break;
				case RIGHT:
					overridePendingTransition(R.anim.right_in, R.anim.right_out);
					break;
				case TOP:
					overridePendingTransition(R.anim.top_in, R.anim.top_out);
					break;
				case BOTTOM:
					overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
					break;
				case SCALE:
					overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
					break;
				case FADE:
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					break;
			}
		}
		super.onCreate(savedInstanceState);

		mNetworkChangeObserver = new NetworkChangeObserver() {
			@Override
			public void onNetConnected(NetworkUtils.NetworkType type) {
				super.onNetConnected(type);
				onNetworkConnected(type);
			}

			@Override
			public void onNetDisConnect() {
				super.onNetDisConnect();
				onNetworkDisConnected();
			}
		};

		NetworkStateReceiver.registerObserver(mNetworkChangeObserver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetworkStateReceiver.removeRegisterObserver(mNetworkChangeObserver);
	}


	/**
	 * network connected
	 */
	protected abstract void onNetworkConnected(NetworkUtils.NetworkType type);

	/**
	 * network disconnected
	 */
	protected abstract void onNetworkDisConnected();


	/**
	 * set the activity override pending transition mode
	 */
	protected TransitionMode getOverridePendingTransitionMode() {
		return null;
	}


	final protected void goActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

	final protected void goActivity(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	final protected void goActivityThenKill(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		finish();
	}

	final protected void goActivityThenKill(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}

	final protected void goActivityForResult(Class<?> clazz, int requestCode) {
		Intent intent = new Intent(this, clazz);
		startActivityForResult(intent, requestCode);
	}

	final protected void goActivityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
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
