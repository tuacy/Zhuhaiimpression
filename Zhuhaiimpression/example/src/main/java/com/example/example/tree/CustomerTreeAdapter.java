package com.example.example.tree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.example.R;
import com.example.example.tree.utils.Node;
import com.example.example.tree.utils.TreeListViewAdapter;

import java.util.List;

/**
 * @author: tuacy
 * @date: 2015/10/22 11:30
 * @version: V1.0
 */
public class CustomerTreeAdapter extends TreeListViewAdapter {

	public CustomerTreeAdapter(ListView listView, Context context, List datas, int defualExpandLevel) throws IllegalAccessException {
		super(listView, context, datas, defualExpandLevel);
	}

	@Override
	public View convertView(Node node, int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInlater.inflate(R.layout.item_tree, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mImageViewIcon = (ImageView) convertView.findViewById(R.id.im_icon);
			viewHolder.mTextViewName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (node.getIcon() == -1) {
			viewHolder.mImageViewIcon.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.mImageViewIcon.setImageResource(node.getIcon());
		}
		viewHolder.mTextViewName.setText(node.getName());
		return convertView;
	}

	class ViewHolder {
		public ImageView mImageViewIcon;
		public TextView  mTextViewName;
	}
}
