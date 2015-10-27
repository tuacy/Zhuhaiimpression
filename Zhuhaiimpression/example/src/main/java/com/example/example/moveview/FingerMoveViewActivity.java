package com.example.example.moveview;

import android.os.Bundle;
import android.util.Log;

import com.example.example.R;
import com.example.example.base.MobileBaseActivity;
import com.example.example.moveview.bean.MoveViewBean;
import com.example.example.moveview.widget.FingerMovePanel;
import com.example.example.networkstate.NetworkUtils;

import java.util.List;


/**
 * @author: tuacy
 * @date: 2015/10/27 10:08
 * @version: V1.0
 */
public class FingerMoveViewActivity extends MobileBaseActivity {

	private FingerMovePanel mMovePanel;


	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finger_move_view);
		initView();
	}

	private void initView() {
		mMovePanel = (FingerMovePanel) findViewById(R.id.panel_move);
		MoveViewBean moveViewBean0 = new MoveViewBean("aaa", "aaaa", 448, 624, 0, 0);
		MoveViewBean moveViewBean1 = new MoveViewBean("bbb", "bbb", 447, 545, 0, 0);
		mMovePanel.addMoveView(moveViewBean0);
		mMovePanel.addMoveView(moveViewBean1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		List<MoveViewBean> list = mMovePanel.getMoveViewBeanList();
		for (MoveViewBean bean : list) {
			Log.d("vae_tag", "left:" + bean.getLeft() + "  top:" + bean.getTop());
		}
	}
}
