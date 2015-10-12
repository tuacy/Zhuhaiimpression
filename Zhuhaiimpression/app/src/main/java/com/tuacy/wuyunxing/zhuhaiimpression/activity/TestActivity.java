package com.tuacy.wuyunxing.zhuhaiimpression.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkUtils;
import com.tuacy.wuyunxing.zhuhaiimpression.widget.SimpleListPopupsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: tuacy
 * @date: 2015/10/12 17:45
 * @version: V1.0
 */
public class TestActivity extends MobileBaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_popups_content);
		ListView listView = (ListView) findViewById(R.id.simple_list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		listView.setAdapter(new SimpleListPopupsAdapter(this, getPopupsData(), new String[]{"name",
																							"icon",
																							"tag"}));
	}

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

	private List<? extends Map<String, ?>> getPopupsData() {
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("name", R.string.photo);
		map.put("tag", 0);
		list.add(map);

		map = new HashMap<String, Integer>();
		map.put("name", R.string.video);
		map.put("tag", 1);
		list.add(map);

		return list;
	}
}
