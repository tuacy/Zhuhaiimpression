package com.example.example.tree.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;


public abstract class TreeListViewAdapter<T> extends BaseAdapter {

	protected Context        mCoantext;
	protected List<Node>     mAllNodes;
	protected List<Node>     mVisibleNodes;
	protected LayoutInflater mInlater;
	protected ListView       mListView;

	public TreeListViewAdapter(ListView listView, Context context, List<T> datas, int defualExpandLevel) throws IllegalAccessException {
		mCoantext = context;
		mInlater = LayoutInflater.from(context);
		mAllNodes = TreeHelper.getSortNodes(datas, defualExpandLevel);
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		mListView = listView;
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				expandOrCollapse(position);
			}
		});
	}

	private void expandOrCollapse(int position) {
		Node node = mVisibleNodes.get(position);
		if (null != node) {
			if (!node.isLeaf()) {
				node.setExpand(!node.isExpand());
				mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public int getCount() {
		return mVisibleNodes.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Node node = mVisibleNodes.get(position);
		convertView = convertView(node, position, convertView, parent);
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		return convertView;
	}

	public abstract View convertView(Node node, int position, View convertView, ViewGroup parent);
}
