package com.tuacy.wuyunxing.zhuhaiimpression.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tuacy on 2015/9/20.
 */
public class PeopleStaffDetail extends MobileBaseActivity {

    public static final String PEOPLE_NAME = "people_name";
    public static final String PEOPLE_URL = "people_url";

    @InjectView(R.id.tool_bar)
    Toolbar mToolBar;
    @InjectView(R.id.tv_nda_content)
    WebView mWebView;

    private String mPeopleName = null;
    private String mPeopleUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_staff_detail);
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPeopleName = bundle.getString(PEOPLE_NAME);
            mPeopleUrl = bundle.getString(PEOPLE_URL);
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    private void initView() {
        mToolBar.setTitle(mPeopleName);
        setSupportActionBar(mToolBar);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(mPeopleUrl);
    }

}
