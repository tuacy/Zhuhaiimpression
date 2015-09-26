package com.tuacy.wuyunxing.zhuhaiimpression.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseActivity;
import com.tuacy.wuyunxing.zhuhaiimpression.networkstate.NetworkUtils;
import com.tuacy.wuyunxing.zhuhaiimpression.tools.VolleyImageCache;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tuacy on 2015/9/26.
 */
public class ImageDetailActivity extends MobileBaseActivity {

	public static final String IMAGE_URL        = "image_url";
	public static final String IMAGE_AUTHOR     = "image_author";
	public static final String IMAGE_DESCRIPTOR = "image_descriptor";
	public static final String IMAGE_TITLE      = "image_title";

	@InjectView(R.id.network_image_view_new)
	NetworkImageView        mImageView;
	@InjectView(R.id.toolbar)
	Toolbar                 mToolbar;
	@InjectView(R.id.author_content_info)
	TextView                mImageAuthorView;
	@InjectView(R.id.descriptor_content_info)
	TextView                mImageDescriptorView;
	@InjectView(R.id.collapsing_toolbar)
	CollapsingToolbarLayout mCollapsingToolbar;

	private String mImageUrl        = null;
	private String mImageAuthor     = null;
	private String mImageDescriptor = null;
	private String mImageTitle      = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
		ButterKnife.inject(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mImageUrl = bundle.getString(IMAGE_URL);
			mImageAuthor = bundle.getString(IMAGE_AUTHOR);
			mImageDescriptor = bundle.getString(IMAGE_DESCRIPTOR);
			mImageTitle = bundle.getString(IMAGE_TITLE);
		}
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
	}

	private void initView() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (null != mImageTitle) {
			mCollapsingToolbar.setTitle(mImageTitle);
		}
		if (null != mImageUrl) {
			VolleyImageCache.networkImageViewUse(mImageView, mImageUrl);
		}
		if (null != mImageAuthor) {
			mImageAuthorView.setText("\t" + mImageAuthor);
		}
		if (null != mImageDescriptor) {
			mImageDescriptorView.setText("\t" + mImageDescriptor);
		}
	}

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}
}
