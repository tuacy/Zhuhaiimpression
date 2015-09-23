package com.tuacy.wuyunxing.zhuhaiimpression.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tuacy.library.adapter.ListViewDataAdapter;
import com.tuacy.library.adapter.ViewHolderBase;
import com.tuacy.library.adapter.ViewHolderCreator;
import com.tuacy.wuyunxing.zhuhaiimpression.Constants;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.activity.PeopleStaffDetailActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.bean.PeopleIntro;
import com.tuacy.wuyunxing.zhuhaiimpression.tools.VolleyImageCache;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

/**
 * @author: tuacy
 * @date: 2015/9/18 17:35
 * @version: V1.0
 */
public class PeopleStaffFragment extends MobileBaseFragment {

	@InjectView(R.id.people_staff_list_view)
	PullToRefreshListView mPeopleStaffPullToRefreshListView;

	private View           mFragmentView;
	private ILoadingLayout mILoadingLayout;

	private ListViewDataAdapter<PeopleIntro> mListViewAdapter = null;

	private int mCurrentPage = 0;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.fragment_people_staff, null);
		ButterKnife.inject(this, mFragmentView);
		initPullToRefresh();
		initAdapter();
		return mFragmentView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	private void initPullToRefresh() {
		mILoadingLayout = mPeopleStaffPullToRefreshListView.getLoadingLayoutProxy();
		mILoadingLayout.setLastUpdatedLabel("");
		mILoadingLayout.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
		mILoadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
		mILoadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
		mPeopleStaffPullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					mILoadingLayout.setLastUpdatedLabel("");
					mILoadingLayout.setPullLabel(getString(R.string.pull_to_refresh_top_pull));
					mILoadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_top_refreshing));
					mILoadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_top_release));
				} else if (firstVisibleItem + visibleItemCount + 1 == totalItemCount) {
					mILoadingLayout.setLastUpdatedLabel("");
					mILoadingLayout.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
					mILoadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
					mILoadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
				}
			}
		});

		mPeopleStaffPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				queryData(0, Constants.PULL_REFRESH);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				queryData(mCurrentPage, Constants.PULL_LOAD_MORE);
			}
		});
		mPeopleStaffPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String name = mListViewAdapter.getDataList().get(position - 1).getName();
				String url = mListViewAdapter.getDataList().get(position - 1).getUrl();
				Bundle bundle = new Bundle();
				bundle.putCharSequence(PeopleStaffDetailActivity.PEOPLE_NAME, name);
				bundle.putCharSequence(PeopleStaffDetailActivity.PEOPLE_URL, url);
				goActivity(PeopleStaffDetailActivity.class, bundle);
			}
		});

		mPeopleStaffPullToRefreshListView.setRefreshing();
	}

	private void initAdapter() {
		mListViewAdapter = new ListViewDataAdapter<PeopleIntro>(new ViewHolderCreator<PeopleIntro>() {
			@Override
			public ViewHolderBase<PeopleIntro> createViewHolder(int position) {
				return new ViewHolderBase<PeopleIntro>() {

					NetworkImageView mPeopleHead;
					TextView mPeopleName;
					TextView mPeopleDescriptor;

					@Override
					public View createView(LayoutInflater layoutInflater) {
						View convertView = layoutInflater.inflate(R.layout.item_people_staff, null);
						mPeopleHead = ButterKnife.findById(convertView, R.id.iv_people_head);
						mPeopleName = ButterKnife.findById(convertView, R.id.tv_people_name);
						mPeopleDescriptor = ButterKnife.findById(convertView, R.id.tv_people_descriptor);
						return convertView;
					}

					@Override
					public void showData(int position, PeopleIntro itemData) {
						BmobFile peopleHead = itemData.getPic();
						String url = peopleHead.getFileUrl(mContext);
						VolleyImageCache.networkImageViewUse(mPeopleHead, url);
						mPeopleName.setText("\t" + itemData.getName());
						mPeopleDescriptor.setText("\t" + itemData.getDescriptor());
					}
				};
			}
		});
		ListView peopleStaffListView = mPeopleStaffPullToRefreshListView.getRefreshableView();
		peopleStaffListView.setAdapter(mListViewAdapter);
	}

	private void queryData(int page, final int actionType) {
		BmobQuery peopleIntroQuery = new BmobQuery<PeopleIntro>();
		peopleIntroQuery.setLimit(Constants.ONE_PAGE_NUMBER);
		peopleIntroQuery.setSkip(page * Constants.ONE_PAGE_NUMBER);
		peopleIntroQuery.findObjects(mContext, new FindListener<PeopleIntro>() {
			@Override
			public void onSuccess(List<PeopleIntro> list) {
				mPeopleStaffPullToRefreshListView.onRefreshComplete();
				if (list.size() > 0) {
					if (Constants.PULL_REFRESH == actionType) {
						mCurrentPage = 0;
						mListViewAdapter.getDataList().clear();
					}
					mListViewAdapter.getDataList().addAll(list);
					mListViewAdapter.notifyDataSetChanged();
					mCurrentPage++;
				} else {
					snackbar(mFragmentView, R.string.no_more_data);
				}
			}

			@Override
			public void onError(int i, String s) {
				mPeopleStaffPullToRefreshListView.onRefreshComplete();
				snackbar(mFragmentView, R.string.get_data_error);
			}
		});
	}
}
