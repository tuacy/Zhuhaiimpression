package com.example.example.imageloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.example.R;

import java.io.File;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

	private List<String>   mDatas    = null;
	private String         mDirPath  = null;
	private LayoutInflater mInflater = null;

	public ImageAdapter(Context context, List<String> datas, String dirPath) {
		mDatas = datas;
		mDirPath = dirPath;
		mInflater = LayoutInflater.from(context);
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

		ViewHolder viewHolder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_imageloader_gridview_image, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mImgViImage = (ImageView) convertView.findViewById(R.id.imgVi_loaderImage_image);
			viewHolder.mImgBtnSelectState = (ImageButton) convertView.findViewById(R.id.imgBtn_loaderImage_select_state);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImgViImage.setImageResource(R.mipmap.ic_image_default);
		viewHolder.mImgBtnSelectState.setImageResource(R.mipmap.ic_image_unseleced);
		ImageLoader.getInstance().loaderImage(mDirPath + File.separator + mDatas.get(position), viewHolder.mImgViImage);

		return convertView;
	}

	class ViewHolder {

		ImageView   mImgViImage;
		ImageButton mImgBtnSelectState;

	}
}
