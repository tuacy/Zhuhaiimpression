package com.example.example.imageloader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.example.R;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {

	private List<String>   mDatas    = null;
	private String         mDirPath  = null;
	private LayoutInflater mInflater = null;
	private Set<Integer>   mSelector = null;

	public ImageAdapter(Context context, List<String> datas, String dirPath) {
		mDatas = datas;
		mDirPath = dirPath;
		mInflater = LayoutInflater.from(context);
		mSelector = new HashSet<>();
	}

	public void setSelector(Set<Integer> selector) {
		mSelector = selector;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_imageloader_gridview_image, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mImgViImage = (ImageView) convertView.findViewById(R.id.imgVi_loaderImage_image);
			viewHolder.mImgBtnSelectState = (ImageButton) convertView.findViewById(R.id.imgBtn_loaderImage_select_state);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final String imagePath = mDirPath + File.separator + mDatas.get(position);
		viewHolder.mImgBtnSelectState.setFocusable(false);
		viewHolder.mImgViImage.setImageResource(R.mipmap.ic_image_default);
		viewHolder.mImgViImage.setColorFilter(null);
		viewHolder.mImgViImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSelector.contains(imagePath.hashCode())) {
					mSelector.remove(imagePath.hashCode());
					viewHolder.mImgViImage.setColorFilter(null);
					viewHolder.mImgBtnSelectState.setImageResource(R.mipmap.ic_image_unseleced);
				} else {
					mSelector.add(imagePath.hashCode());
					viewHolder.mImgViImage.setColorFilter(Color.parseColor("#77000000"));
					viewHolder.mImgBtnSelectState.setImageResource(R.mipmap.ic_image_selected);
				}
			}
		});
		viewHolder.mImgBtnSelectState.setImageResource(R.mipmap.ic_image_unseleced);
		ImageLoader.getInstance().loaderImage(imagePath, viewHolder.mImgViImage);
		if (mSelector.contains((mDirPath + File.separator + mDatas.get(position)).hashCode())) {
			viewHolder.mImgViImage.setColorFilter(Color.parseColor("#77000000"));
			viewHolder.mImgBtnSelectState.setImageResource(R.mipmap.ic_image_selected);
		}

		return convertView;
	}

	class ViewHolder {

		ImageView   mImgViImage;
		ImageButton mImgBtnSelectState;

	}
}
