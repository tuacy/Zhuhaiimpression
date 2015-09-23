package com.tuacy.wuyunxing.zhuhaiimpression.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.adapter.FragmentAdapter;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author: tuacy
 * @date: 2015/9/18 17:36
 * @version: V1.0
 */
public class LatestNewsMainFragment extends MobileBaseFragment {
    @InjectView(R.id.last_new_tablayout)
    TabLayout mTabLayout;
    @InjectView(R.id.last_new_viewpager)
    ViewPager mViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_last_new, null);
        ButterKnife.inject(this, fragmentView);
        initView();
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void initView() {
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.software));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.software_test));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.stock));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.comprehensive));
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new LatestNewsImageFragment());
        fragments.add(new PeopleStaffFragment());
        fragments.add(new PeopleStaffFragment());
        fragments.add(new PeopleStaffFragment());
        List<String> titles = new ArrayList<String>();
        titles.add(getResources().getString(R.string.software));
        titles.add(getResources().getString(R.string.software_test));
        titles.add(getResources().getString(R.string.stock));
        titles.add(getResources().getString(R.string.comprehensive));
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }
}
