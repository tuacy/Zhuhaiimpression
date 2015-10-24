package com.example.example.imageloader;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.example.R;
import com.example.example.imageloader.bean.FolderBean;

import java.util.List;

/**
 * Created by tuacy on 2015/10/25.
 */
public class ListDirPopupWindow extends PopupWindow {


	public interface OnPathSelectedListener {

		public void pathSelected(FolderBean folderBean);
	}

	private int                    mWidth;
	private int                    mHeigth;
	private View                   mContentView;
	private ListView               mListView;
	private List<FolderBean>       mDatas;
	private Context                mContext;
	private OnPathSelectedListener mPathSelectedListener;

	public void setOnPathSelectedListener(OnPathSelectedListener listener) {
		mPathSelectedListener = listener;
	}

	public ListDirPopupWindow(Context context, List<FolderBean> data) {
		calculateWidthAndHeight(context);
		mContentView = LayoutInflater.from(context).inflate(R.layout.popup_folder_select, null);
		mDatas = data;
		mContext = context;
		setContentView(mContentView);
		setWidth(mWidth);
		setHeight(mHeigth);
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (null != mPathSelectedListener) {
					mPathSelectedListener.pathSelected(mDatas.get(position));
				}
			}
		});
	}

	private void initData() {
		PopupListAdapter adapter = new PopupListAdapter(mContext, mDatas);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		mListView = (ListView) mContentView.findViewById(R.id.lstVi_loaderImage_folder);

	}

	private void calculateWidthAndHeight(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		mWidth = displayMetrics.widthPixels;
		mHeigth = (int) (displayMetrics.heightPixels * 0.8);
	}

	private class PopupListAdapter extends BaseAdapter {

		private List<FolderBean> mDatas;
		private LayoutInflater   mInflater;

		public PopupListAdapter(Context context, List<FolderBean> data) {
			mDatas = data;
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
				convertView = mInflater.inflate(R.layout.item_popup_folder, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mImgVi = (ImageView) convertView.findViewById(R.id.imgVi_loaderImage_folder);
				viewHolder.mTvFolderName = (TextView) convertView.findViewById(R.id.tv_loaderImage_folder_name);
				viewHolder.mTvFolderImageCount = (TextView) convertView.findViewById(R.id.tv_loaderImage_folder_image_count);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mImgVi.setImageResource(R.mipmap.ic_dir_image_default);
			FolderBean folderBean = (FolderBean) getItem(position);
			ImageLoader.getInstance().loaderImage(folderBean.getFristImgPath(), viewHolder.mImgVi);
			viewHolder.mTvFolderName.setText(folderBean.getName());
			viewHolder.mTvFolderImageCount.setText(folderBean.getImgCount() + "");
			return convertView;
		}

		class ViewHolder {

			ImageView mImgVi;
			TextView  mTvFolderName;
			TextView  mTvFolderImageCount;

		}
	}
}
