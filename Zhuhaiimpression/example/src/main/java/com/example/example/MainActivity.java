package com.example.example;

import android.os.Bundle;

import com.example.example.base.MobileBaseActivity;


public class MainActivity extends MobileBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new OutSideClass.InnerClass().get();
	}

}
