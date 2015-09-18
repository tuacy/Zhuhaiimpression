package com.tuacy.wuyunxing.zhuhaiimpression.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author: tuacy
 * @date: 2015/9/18 17:35
 * @version: V1.0
 */
public class PeopleStaffFragment extends MobileBaseFragment {

    @InjectView(R.id.people_staff_list_view)
    PullToRefreshListView mPeopleStaffListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_people_staff, null);
        ButterKnife.inject(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
