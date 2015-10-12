package com.example.example;

import android.os.Bundle;

import com.example.example.base.MobileBaseActivity;
import com.example.example.networkstate.NetworkUtils;


public class MainActivity extends MobileBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

}
