package com.tuacy.wuyunxing.zhuhaiimpression.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuacy.wuyunxing.zhuhaiimpression.R;

import java.util.List;
import java.util.Map;

public class SimpleListDialog extends Dialog {

	public SimpleListDialog(Context context) {
		this(context, R.style.Dialog);
	}

	public SimpleListDialog(Context context, int theme) {
		super(context, theme);
	}

	void apply(final Builder builder) {

		if (builder.mShowTitle) {
			setTitle(builder.mTitle);
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		setContentView(R.layout.list_popups_content);
		ListView listView = (ListView) findViewById(R.id.simple_list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (builder.mListener != null) {
					Map<String, ?> map = builder.mData.get(position);
					Integer tag = (Integer) map.get(builder.mKeys[2]);
					builder.mListener.onItemClick(view, position, tag);
				}
				dismiss();
			}
		});
		listView.setAdapter(new SimpleListPopupsAdapter(getContext(), builder.mData, builder.mKeys));

		setCanceledOnTouchOutside(builder.mCanceledOnTouchOutside);
	}

	public interface OnItemClickListener {

		void onItemClick(View view, int position, int tag);
	}

	public static class Builder {

		Context mContext;
		boolean mCanceledOnTouchOutside = true;
		int     mTheme                  = R.style.Dialog;
		int mTitle;
		boolean mShowTitle = false;
		List<? extends Map<String, ?>> mData;
		/**
		 * mKeys[0]: item name resId. mKeys[1]: item icon resId. mKeys[2]: item tag for onItemClick.
		 */
		String[]                       mKeys;
		OnItemClickListener            mListener;

		public Builder(Context context) {
			mContext = context;
		}

		public Builder(Context context, int theme) {
			mContext = context;
			mTheme = theme;
		}

		public Builder setTitle(int titleId) {
			mShowTitle = true;
			mTitle = titleId;
			return this;
		}

		public Builder setCanceledOnTouchOutside(boolean cancel) {
			mCanceledOnTouchOutside = cancel;
			return this;
		}

		public Builder setDisplayData(List<? extends Map<String, ?>> data, String[] keys) {
			mData = data;
			mKeys = keys;
			return this;
		}

		public Builder setOnItemClickListener(OnItemClickListener l) {
			mListener = l;
			return this;
		}

		public SimpleListDialog build() {
			SimpleListDialog dialog = new SimpleListDialog(mContext, mTheme);
			dialog.apply(this);
			return dialog;
		}

		public SimpleListDialog show() {
			SimpleListDialog dialog = build();
			dialog.show();
			return dialog;
		}
	}
}
