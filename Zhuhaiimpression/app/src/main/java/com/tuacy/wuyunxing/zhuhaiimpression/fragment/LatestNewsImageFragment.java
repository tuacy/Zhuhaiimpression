package com.tuacy.wuyunxing.zhuhaiimpression.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.tuacy.wuyunxing.zhuhaiimpression.Constants;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.animation.OvershootInLeftAnimator;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseFragment;
import com.tuacy.wuyunxing.zhuhaiimpression.bean.Image;
import com.tuacy.wuyunxing.zhuhaiimpression.tools.VolleyImageCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by tuacy on 2015/9/20.
 */
public class LatestNewsImageFragment extends MobileBaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

	@InjectView(R.id.superRecyclerView_img)
	SuperRecyclerView mRecyclerViewImg;

	private View mFragmentView;

	private ImageAdapter mAdapter;
	private List<Image>  mList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new ArrayList<Image>();
		mAdapter = new ImageAdapter(mContext, mList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.fragment_new_image, null);
		ButterKnife.inject(this, mFragmentView);
		return mFragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mRecyclerViewImg.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		mRecyclerViewImg.getRecyclerView().setItemAnimator(new OvershootInLeftAnimator());
		mRecyclerViewImg.setRefreshListener(this);
		mRecyclerViewImg.setupMoreListener(this, Constants.ONE_PAGE_NUMBER);
		mRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
													 android.R.color.holo_green_light, android.R.color.holo_red_light);
		mRecyclerViewImg.setAdapter(mAdapter);
		if (mList.size() == 0) {
			onRefresh();
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onRefresh() {
		queryData(0, Constants.ONE_PAGE_NUMBER, Constants.PULL_REFRESH);
	}

	@Override
	public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
		queryData(numberOfItems / Constants.ONE_PAGE_NUMBER, Constants.ONE_PAGE_NUMBER, Constants.PULL_LOAD_MORE);
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
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
					snackbar(mFragmentView, R.string.no_more_data);
				}
				mRecyclerViewImg.hideMoreProgress();
				mRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
			}

			@Override
			public void onError(int i, String s) {
				mRecyclerViewImg.hideMoreProgress();
				mRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
				snackbar(mFragmentView, R.string.get_data_error);
			}

		});
	}


	private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

		private List<Image>    mListSource;
		private LayoutInflater mInflater;

		public void setListSource(List<Image> list) {
			this.mListSource = list;
		}

		public ImageAdapter(Context context, List<Image> list) {
			super();
			this.mListSource = list;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View contentView = mInflater.inflate(R.layout.item_new_image, parent, false);
			ViewHolder holder = new ViewHolder(contentView);
			return holder;
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, final int position) {
			BmobFile imageFile = mListSource.get(position).getImage();
			String url = imageFile.getFileUrl(mContext);
			VolleyImageCache.networkImageViewUse(holder.mPeopleHead, url);
			holder.mImageDescriptor.setText(mListSource.get(position).getDescriptor());
		}

		@Override
		public int getItemCount() {
			return mListSource.size();
		}


		class ViewHolder extends RecyclerView.ViewHolder {

			NetworkImageView mPeopleHead;
			TextView         mImageDescriptor;

			public ViewHolder(View convertView) {
				super(convertView);
				mPeopleHead = (NetworkImageView) convertView.findViewById(R.id.network_image_view_new);
				mImageDescriptor = (TextView) convertView.findViewById(R.id.text_view_image_descriptor);
			}
		}

	}

}
