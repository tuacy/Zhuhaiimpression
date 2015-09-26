package com.tuacy.wuyunxing.zhuhaiimpression.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.fragment.LatestNewsMainFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.fragment.PeopleStaffFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkUtils;
import com.tuacy.wuyunxing.zhuhaiimpression.widget.CircleTransformation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;


public class MainActivity extends MobileBaseActivity {

	@InjectView(R.id.tool_bar)
	Toolbar        mToolBar;
	@InjectView(R.id.navigation_view)
	NavigationView mNavigationView;
	@InjectView(R.id.drawer_layout)
	DrawerLayout   mDrawerLayout;

	private LatestNewsMainFragment mLastNewFragment;
	private PeopleStaffFragment    mPeopleStaffFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		initView();
		mNavigationView.setNavigationItemSelectedListener(mNavigationListener);
		initDrawer();
		setFragments(R.id.menu_people_staff);
		mToolBar.setTitle(R.string.drawer_menu_people_staff);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
		BmobUser.logOut(this);
	}

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {
		snackbar(mDrawerLayout, R.string.no_network_connect);
	}

	private void initView() {
		ImageView userHead = (ImageView) findViewById(R.id.iv_user_head);
		Picasso.with(mContext).load(R.mipmap.acount).transform(new CircleTransformation()).into(userHead);
	}

	private void initDrawer() {
		setSupportActionBar(mToolBar);
		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open,
																	   R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};
		drawerToggle.syncState();
		mDrawerLayout.setDrawerListener(drawerToggle);
	}

	private NavigationView.OnNavigationItemSelectedListener mNavigationListener = new NavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.menu_people_staff:
					setFragments(menuItem.getItemId());
					mToolBar.setTitle(menuItem.getTitle());
					menuItem.setChecked(true);
					break;
				case R.id.menu_last_new:
					setFragments(menuItem.getItemId());
					mToolBar.setTitle(menuItem.getTitle());
					menuItem.setChecked(true);
					break;
				case R.id.sub_menu_about:
					Snackbar.make(getCurrentFocus(), "click" + menuItem.getTitle(), Snackbar.LENGTH_SHORT)
							.setAction("Right", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
								}
							})
							.show();
					break;
				case R.id.sub_menu_setting:
					Snackbar.make(getCurrentFocus(), "click" + menuItem.getTitle(), Snackbar.LENGTH_SHORT)
							.setAction("Right", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
								}
							})
							.show();
					break;
			}
			mDrawerLayout.closeDrawers();
			return true;
		}
	};

	private void setFragments(int id) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		hideFragments(transaction);
		switch (id) {
			case R.id.menu_people_staff:
				if (null == mPeopleStaffFragment) {
					mPeopleStaffFragment = new PeopleStaffFragment();
					transaction.add(R.id.drawer_content, mPeopleStaffFragment);
				} else {
					transaction.show(mPeopleStaffFragment);
				}
				break;
			case R.id.menu_last_new:
				if (null == mLastNewFragment) {
					mLastNewFragment = new LatestNewsMainFragment();
					transaction.add(R.id.drawer_content, mLastNewFragment);
				} else {
					transaction.show(mLastNewFragment);
				}
				break;
		}
		transaction.commit();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (mPeopleStaffFragment != null) {
			transaction.hide(mPeopleStaffFragment);
		}
		if (mLastNewFragment != null) {
			transaction.hide(mLastNewFragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
