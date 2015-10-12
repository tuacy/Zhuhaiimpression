package com.tuacy.wuyunxing.zhuhaiimpression.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tuacy.wuyunxing.zhuhaiimpression.R;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SimpleListPopupsAdapter extends BaseAdapter {

	private Context                        mContext;
	private List<? extends Map<String, ?>> mData;
	private String[]                       mKeys;

	public SimpleListPopupsAdapter(Context context, List<? extends Map<String, ?>> data, String[] keys) {
		mContext = context;
		mData = data;
		mKeys = keys;
	}

	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		if (mData == null) {
			return null;
		}
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_list_popups_item, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		Map<String, ?> map = (Map<String, ?>) getItem(position);
		if (map != null && mKeys.length > 0) {
			holder.mName.setText((Integer) map.get(mKeys[0]));
			if (mKeys.length > 1) {
				Integer resId = (Integer) map.get(mKeys[1]);
				if (resId != null) {
					holder.mIcon.setImageResource((Integer) map.get(mKeys[1]));
				} else {
					holder.mIcon.setVisibility(View.GONE);
				}
			} else {
				holder.mIcon.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	static final class ViewHolder {

		@InjectView(R.id.simple_list_popups_icon)
		ImageView mIcon;

		@InjectView(R.id.simple_list_popups_name)
		TextView mName;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}
