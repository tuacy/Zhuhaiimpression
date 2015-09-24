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
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by tuacy on 2015/9/20.
 */
public class LatestNewsImageFragment extends MobileBaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

	@InjectView(R.id.recycler_view_img)
	RecyclerView     mRecyclerViewImg;
	@InjectView(R.id.BGA_Refresh_Layout)
	BGARefreshLayout mRefreshLayout;

	private View mFragmentView;

	private ImageAdapter mAdapter;
	private List<Image>  mList;

	private int mCurrentPage = 0;


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

		mRefreshLayout.setDelegate(this);
		mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(mContext, true));

		mRecyclerViewImg.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		mRecyclerViewImg.setItemAnimator(new OvershootInLeftAnimator());
		mRecyclerViewImg.setAdapter(mAdapter);
		if (mList.size() == 0) {
			queryData(0, Constants.ONE_PAGE_NUMBER, Constants.PULL_REFRESH);
		} else {
			mAdapter.notifyDataSetChanged();
		}
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
						mList.clear();
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
						mCurrentPage = 0;
					} else {
						int insertFrom = mList.size();
						mList.addAll(list);
						int insertTo = mList.size();
						mAdapter.notifyItemRangeInserted(insertFrom, insertTo);
					}
					mCurrentPage ++;
				} else {
					snackbar(mFragmentView, R.string.no_more_data);
				}
				mRefreshLayout.endRefreshing();
			}

			@Override
			public void onError(int i, String s) {
				snackbar(mFragmentView, R.string.get_data_error);
			}
		});
	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
		Log.d("vae_tag", "onBGARefreshLayoutBeginRefreshing");
		queryData(0, Constants.ONE_PAGE_NUMBER, Constants.PULL_REFRESH);
	}

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
		Log.d("vae_tag", "onBGARefreshLayoutBeginLoadingMore");
		queryData(mCurrentPage, Constants.ONE_PAGE_NUMBER, Constants.PULL_LOAD_MORE);
		return true;
	}


	private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

		private List<Image>    mListSource;
		private LayoutInflater mInflater;

		public void setListSource(List<Image> list) {
			this.mListSource = list;
		}

		public List<Image> getListSource() {
			return mListSource;
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
			return mListSource == null ? 0 : mListSource.size();
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
