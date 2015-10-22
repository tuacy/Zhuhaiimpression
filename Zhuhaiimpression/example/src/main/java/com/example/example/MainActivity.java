package com.example.example;

import android.os.Bundle;
import android.widget.ListView;

import com.example.example.base.MobileBaseActivity;
import com.example.example.networkstate.NetworkUtils;
import com.example.example.tree.CustomerTreeAdapter;
import com.example.example.tree.bean.FileBean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends MobileBaseActivity {

	private ListView            mListView;
	private CustomerTreeAdapter mAdapter;
	private List<FileBean>      mDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListView = (ListView) findViewById(R.id.list_view);
		getDatas();
		try {
			mAdapter = new CustomerTreeAdapter(mListView, this, mDatas, 1);
			mListView.setAdapter(mAdapter);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

	public void getDatas() {
		mDatas = new ArrayList<>();
		FileBean bean1 = new FileBean(1, 0, "root 1");
		mDatas.add(bean1);
		FileBean bean2 = new FileBean(2, 0, "root 2");
		mDatas.add(bean2);
		FileBean bean3 = new FileBean(3, 1, "1-1");
		mDatas.add(bean3);
		FileBean bean4 = new FileBean(4, 1, "1-2");
		mDatas.add(bean4);
		FileBean bean5 = new FileBean(5, 1, "1-3");
		mDatas.add(bean5);
		FileBean bean6 = new FileBean(6, 2, "2-1");
		mDatas.add(bean6);
		FileBean bean7 = new FileBean(7, 2, "2-2");
		mDatas.add(bean7);

	}
}
