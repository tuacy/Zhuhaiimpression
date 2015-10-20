package com.tuacy.wuyunxing.zhuhaiimpression.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Set;

public abstract class MobileBaseAdapter<T> extends BaseAdapter {

	protected Context      mContext    = null;
	protected List<T>      mDatas      = null;
	protected Set<Integer> mSelections = null;
	private   int          mLayoutId   = 0;

	public MobileBaseAdapter(Context context, int layoutId) {
		this(context, layoutId, null);
	}

	public MobileBaseAdapter(Context context, int layoutId, List<T> datas) {
		mContext = context;
		mDatas = datas;
		mLayoutId = layoutId;
	}

	public void setDatas(List<T> datas) {
		mDatas = datas;
		notifyDataSetChanged();
	}

	public void setSelections(Set<Integer> selections) {
		mSelections = selections;
		notifyDataSetChanged();
	}

	public void clearSelections() {
		mSelections.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent, mLayoutId, position);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}

	public abstract void convert(ViewHolder viewHolder, T t);
}
