package com.tuacy.wuyunxing.zhuhaiimpression.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.tuacy.wuyunxing.zhuhaiimpression.Constants;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.activity.ImageDetailActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.activity.PeopleStaffDetailActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.animation.OvershootInLeftAnimator;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.bean.Image;
import com.tuacy.wuyunxing.zhuhaiimpression.tools.VolleyImageCache;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by tuacy on 2015/9/20.
 */
public class LatestNewsImageFragment extends MobileBaseFragment
	implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener {

	@InjectView(R.id.recycler_view_img)
	RecyclerView     mRecyclerViewImg;
	@InjectView(R.id.BGA_Refresh_Layout)
	BGARefreshLayout mRefreshLayout;

	private View         mFragmentView = null;
	private ImageAdapter mAdapter      = null;

	private int mCurrentPage = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.fragment_new_image, null);
		ButterKnife.inject(this, mFragmentView);
		initView();
		return mFragmentView;
	}

	private void initView() {
		mRefreshLayout.setDelegate(this);
		mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));

		mRecyclerViewImg.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		mRecyclerViewImg.setItemAnimator(new OvershootInLeftAnimator());
		mAdapter = new ImageAdapter(mRecyclerViewImg);
		mAdapter.setOnRVItemClickListener(this);
		mRecyclerViewImg.setAdapter(mAdapter);
		beginRefreshing();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	private void queryData(int page, int pageNumber, final int actionType) {
		BmobQuery peopleIntroQuery = new BmobQuery<Image>();
		peopleIntroQuery.setLimit(pageNumber);
		peopleIntroQuery.setSkip(page * pageNumber);
		peopleIntroQuery.findObjects(mContext, new FindListener<Image>() {
			@Override
			public void onSuccess(List<Image> list) {

				if (list.size() > 0) {
					if (Constants.PULL_REFRESH == actionType) {
						/** refresh */
						mRefreshLayout.endRefreshing();
						mAdapter.setDatas(list);
						mCurrentPage = 0;
						mRecyclerViewImg.smoothScrollToPosition(0);
					} else {
						/** load more */
						mRefreshLayout.endLoadingMore();
						mAdapter.addMoreDatas(list);
					}
					mCurrentPage++;
				} else {
					if (Constants.PULL_REFRESH == actionType) {
						mRefreshLayout.endRefreshing();
					} else {
						mRefreshLayout.endLoadingMore();
					}
					snackbar(mFragmentView, R.string.no_more_data);
				}

			}

			@Override
			public void onError(int i, String s) {
				if (Constants.PULL_REFRESH == actionType) {
					mRefreshLayout.endRefreshing();
				} else {
					mRefreshLayout.endLoadingMore();
				}
				snackbar(mFragmentView, R.string.get_data_error);
			}
		});
	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
		queryData(0, Constants.ONE_PAGE_NUMBER, Constants.PULL_REFRESH);
	}

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
		queryData(mCurrentPage, Constants.ONE_PAGE_NUMBER, Constants.PULL_LOAD_MORE);
		return true;
	}

	@Override
	public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
		Image image = mAdapter.getDatas().get(i);
		Bundle bundle = new Bundle();
		bundle.putCharSequence(ImageDetailActivity.IMAGE_URL, image.getImage().getFileUrl(mContext));
		bundle.putCharSequence(ImageDetailActivity.IMAGE_AUTHOR, image.getAuthor());
		bundle.putCharSequence(ImageDetailActivity.IMAGE_DESCRIPTOR, image.getDescriptor());
		bundle.putCharSequence(ImageDetailActivity.IMAGE_TITLE, image.getTitle());
		goActivity(ImageDetailActivity.class, bundle);
	}


	private void beginRefreshing() {
		mRefreshLayout.beginRefreshing();
	}


	private class ImageAdapter extends BGARecyclerViewAdapter<Image> {

		public ImageAdapter(RecyclerView recyclerView) {
			super(recyclerView, R.layout.item_new_image);
		}

		@Override
		protected void fillData(BGAViewHolderHelper bgaViewHolderHelper, int i, Image image) {
			bgaViewHolderHelper.setText(R.id.text_view_image_descriptor, image.getDescriptor());
			VolleyImageCache.networkImageViewUse((NetworkImageView) bgaViewHolderHelper.getView(R.id.network_image_view_new),
												 image.getImage().getFileUrl(mContext));
		}
	}
}
