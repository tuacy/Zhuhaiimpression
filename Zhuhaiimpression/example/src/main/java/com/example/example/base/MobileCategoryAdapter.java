package com.example.example.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MobileCategoryAdapter extends BaseAdapter {

	public static final int TITLE_POSITION  = 0;
	public static final int VIEW_TYPE_TITLE = 0;

	private List<Category> categories = new ArrayList<Category>();
	private Context mContext;
	private int     mTitleLayoutId;

	public MobileCategoryAdapter(Context context) {
		mContext = context;
	}

	public MobileCategoryAdapter(Context context, int titleLayoutId) {
		mContext = context;
		mTitleLayoutId = titleLayoutId;
	}

	public void setTitleLayout(int titleLayoutId) {
		mTitleLayoutId = titleLayoutId;
	}

	public void addCategory(MobileBaseAdapter adapter) {
		categories.add(new Category(adapter));
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setExpand(int categoryIndex, boolean expand) {
		if (categoryIndex >= 0 && categoryIndex < categories.size()) {
			categories.get(categoryIndex).setExpand(expand);
			notifyDataSetChanged();
		}
	}

	public boolean isExpand(int categoryIndex) {
		if (categoryIndex >= 0 && categoryIndex < categories.size()) {
			return categories.get(categoryIndex).isExpand();
		}
		return false;
	}

	public List getCategoryDatas(int position) {
		for (Category category : categories) {
			if (position == TITLE_POSITION) {
				return null;
			}

			int size = category.getAdapter().getCount() + 1;
			if (position < size) {
				return category.getAdapter().getDatas();
			}
			position -= size;
		}

		return null;
	}

	@Override
	public int getCount() {
		int total = 0;

		for (Category category : categories) {
			total += category.getAdapter().getCount() + 1;
		}

		return total;
	}

	@Override
	public Object getItem(int position) {
		for (Category category : categories) {
			if (position == TITLE_POSITION) {
				return category;
			}

			int size = category.getAdapter().getCount() + 1;
			if (position < size) {
				return category.getAdapter().getItem(position - 1);
			}
			position -= size;
		}

		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		int total = 1;

		for (Category category : categories) {
			total += category.getAdapter().getViewTypeCount();
		}

		return total;
	}

	@Override
	public int getItemViewType(int position) {
		int typeOffset = 1;

		for (Category category : categories) {
			if (position == TITLE_POSITION) {
				return VIEW_TYPE_TITLE;
			}

			int size = category.getAdapter().getCount() + 1;
			if (position < size) {
				return typeOffset + category.getAdapter().getItemViewType(position - 1);
			}
			position -= size;
			typeOffset += category.getAdapter().getViewTypeCount();
		}

		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int categoryIndex = 0;

		for (Category category : categories) {
			if (position == TITLE_POSITION) {
				ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent, mTitleLayoutId, position);
				convertTitleView(viewHolder, categoryIndex);
				return viewHolder.getConvertView();
			}

			int size = category.getAdapter().getCount() + 1;
			if (position < size) {
				return category.getAdapter().getView(position - 1, convertView, parent);
			}
			position -= size;
			categoryIndex++;
		}

		return null;
	}

	protected abstract void convertTitleView(ViewHolder viewHolder, int categoryIndex);

	public static abstract class CategoryListClickListener implements AdapterView.OnItemClickListener {

		private MobileCategoryAdapter mAdapter;

		public CategoryListClickListener() {
		}

		public void setAdapter(MobileCategoryAdapter adapter) {
			this.mAdapter = adapter;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int categoryIndex = 0;
			for (Category category : mAdapter.getCategories()) {
				if (position == TITLE_POSITION) {
					onTitleClick(parent, view, categoryIndex);
					return;
				}

				int size = category.getAdapter().getCount() + 1;
				if (position < size) {
					onItemClick(parent, view, categoryIndex, position - 1);
					return;
				}
				position -= size;
				categoryIndex++;
			}
		}

		/**
		 * @param parent        The AdapterView where the click happened.
		 * @param view          The view within the AdapterView that was clicked (this will be a view provided by the adapter)
		 * @param categoryIndex index of category
		 */
		public abstract void onTitleClick(AdapterView<?> parent, View view, int categoryIndex);

		/**
		 * @param parent        The AdapterView where the click happened.
		 * @param view          The view within the AdapterView that was clicked (this will be a view provided by the adapter)
		 * @param categoryIndex index of category
		 * @param position      position in category array
		 */
		public abstract void onItemClick(AdapterView<?> parent, View view, int categoryIndex, int position);
	}

	public static class Category {

		private MobileBaseAdapter mAdapter;

		public Category(MobileBaseAdapter adapter) {
			mAdapter = adapter;
		}

		public void setAdapter(MobileBaseAdapter adapter) {
			mAdapter = adapter;
		}

		public MobileBaseAdapter getAdapter() {
			return mAdapter;
		}

		public boolean isExpand() {
			return mAdapter.isExpand();
		}

		public void setExpand(boolean expand) {
			mAdapter.setExpand(expand);
		}
	}
}
