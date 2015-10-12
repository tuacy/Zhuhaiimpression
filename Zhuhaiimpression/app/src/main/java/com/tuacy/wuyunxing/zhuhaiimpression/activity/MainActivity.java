package com.tuacy.wuyunxing.zhuhaiimpression.activity;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tuacy.wuyunxing.zhuhaiimpression.Constants;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseApplication;
import com.tuacy.wuyunxing.zhuhaiimpression.fragment.LatestNewsMainFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.fragment.PeopleStaffFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkUtils;
import com.tuacy.wuyunxing.zhuhaiimpression.widget.CircleTransformation;
import com.tuacy.wuyunxing.zhuhaiimpression.widget.SimpleListDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


public class MainActivity extends MobileBaseActivity {

	@InjectView(R.id.tool_bar)
	Toolbar        mToolBar;
	@InjectView(R.id.navigation_view)
	NavigationView mNavigationView;
	@InjectView(R.id.drawer_layout)
	DrawerLayout   mDrawerLayout;

	private ImageView mImageViewUserHead;
	private Button    mButtonLogin;
	private TextView  mTextViewUserName;

	private LatestNewsMainFragment mLastNewFragment;
	private PeopleStaffFragment    mPeopleStaffFragment;

	public static QQAuth   mQQAuth  = MobileBaseApplication.getInstance().getQQAuth();
	private       UserInfo mInfo    = null;
	private       Tencent  mTencent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mTencent = Tencent.createInstance(Constants.QQ_APP_ID, MainActivity.this);
		initView();
		mNavigationView.setNavigationItemSelectedListener(mNavigationListener);
		initDrawer();
		setFragments(R.id.menu_people_staff);
		mToolBar.setTitle(R.string.drawer_menu_people_staff);
	}

	@Override
	protected void onStart() {
		super.onStart();
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
		mImageViewUserHead = (ImageView) findViewById(R.id.iv_user_head);
		Picasso.with(mContext).load(R.mipmap.acount).transform(new CircleTransformation()).into(mImageViewUserHead);
		mButtonLogin = (Button) findViewById(R.id.button_qq_login);
		mButtonLogin.setText(R.string.qq_login_in);
		mTextViewUserName = (TextView) findViewById(R.id.tv_user_name);
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

	@OnClick({R.id.button_qq_login,
			  R.id.iv_user_head})
	void viewOnClick(View view) {
		switch (view.getId()) {
			case R.id.button_qq_login:
				qqLogin();
				break;
			case R.id.iv_user_head:
				startActivity(new Intent(this, TestActivity.class));
		}
	}

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

	private void qqLogin() {
		showActionSelectDialog();
//		if (!mQQAuth.isSessionValid()) {
//			mQQAuth.login(this, "all", null);
//			mTencent.login(this, "all", new BaseUiListener() {
//				@Override
//				public void onComplete(Object response) {
//					super.onComplete(response);
//					mButtonLogin.setText(R.string.qq_login_out);
//				}
//
//				@Override
//				protected void doComplete(JSONObject values) {
//					super.doComplete(values);
//					updateUseInfoUI(true);
//				}
//			});
//		} else {
//			mQQAuth.logout(this);
//			mButtonLogin.setText(R.string.qq_login_in);
//			updateUseInfoUI(false);
//		}
	}

	private void updateUseInfoUI(boolean login) {
		if (login) {
			if (mQQAuth != null && mQQAuth.isSessionValid()) {
				mInfo = new UserInfo(this, mQQAuth.getQQToken());
				mInfo.getUserInfo(new IUiListener() {

					@Override
					public void onComplete(Object o) {
						JSONObject json = (JSONObject) o;
						if (json.has("figureurl")) {
							try {
								Picasso.with(mContext)
									   .load(json.getString("figureurl_qq_2"))
									   .transform(new CircleTransformation())
									   .into(mImageViewUserHead);
								mTextViewUserName.setText(json.getString("nickname"));
							} catch (JSONException e) {

							}
						}
					}

					@Override
					public void onError(UiError uiError) {

					}

					@Override
					public void onCancel() {

					}
				});
			}
		} else {
			Picasso.with(mContext).load(R.mipmap.acount).transform(new CircleTransformation()).into(mImageViewUserHead);
			mTextViewUserName.setText("");
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {

		}
	}

	private void showActionSelectDialog() {
		SimpleListDialog.OnItemClickListener onItemClickListener = new SimpleListDialog.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position, int tag) {
				switch (tag) {
					case 0:
						Snackbar.make(getCurrentFocus(), "click", Snackbar.LENGTH_SHORT)
								.setAction("Right", new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
									}
								})
								.show();
						break;
					case 1:
						Snackbar.make(getCurrentFocus(), "click", Snackbar.LENGTH_SHORT)
								.setAction("Right", new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
									}
								})
								.show();
					default:
						break;
				}
			}
		};
		new SimpleListDialog.Builder(MainActivity.this).setDisplayData(getPopupsData(), new String[]{"name",
																									 "icon",
																									 "tag"})
													   .setCanceledOnTouchOutside(true)
													   .setOnItemClickListener(onItemClickListener)
													   .show();
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
