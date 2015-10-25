package com.example.example.imageloader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.example.R;
import com.example.example.base.MobileBaseActivity;
import com.example.example.imageloader.bean.FolderBean;
import com.example.example.networkstate.NetworkUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageSelectActivity extends MobileBaseActivity implements ListDirPopupWindow.OnPathSelectedListener {

	private static final int MSG_OBTAIN_DATA_SUCCESS = 0x000;

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

	private GridView       mGvImage;
	private TextView       mTvDirPath;
	private TextView       mTvDirCount;
	private RelativeLayout mRlDirSelect;

	private File               mCurrentFile       = null;
	private int                mCurrentImageCount = 0;
	private List<FolderBean>   mFolderList        = new ArrayList<>();
	private ProgressDialog     mProgressDialog    = null;
	private Handler            mUiHandler         = null;
	private List<String>       mImgs              = null;
	private ImageAdapter       mAdapter           = null;
	private ListDirPopupWindow mPopupWindow       = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_select);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		mRlDirSelect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.setAnimationStyle(R.style.DirPopupWindowAnim);
				mPopupWindow.showAsDropDown(mRlDirSelect, 0, 0);
				setAlpha(0.7f);
			}
		});
	}

	private void initData() {
		mUiHandler = new UiHandler(this);
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(ImageSelectActivity.this, "no external storage", Toast.LENGTH_SHORT).show();
			return;
		}
		mProgressDialog = ProgressDialog.show(ImageSelectActivity.this, null, "loading....");
		new Thread() {
			@Override
			public void run() {
				Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver contentResolver = getContentResolver();
				Cursor cursor = contentResolver.query(imgUri, null, MediaStore.Images.Media.MIME_TYPE +
																	" = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? ",
													  new String[]{"image/jpeg",
																   "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
				Set<String> mDirPaths = new HashSet<>();
				while (cursor.moveToNext()) {
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					File parentFile = new File(path).getParentFile();
					if (null == parentFile) {
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();
					if (mDirPaths.contains(dirPath)) {
						continue;
					}
					if (null == parentFile.list()) {
						continue;
					}
					int imageCount = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File file, String s) {
							if (s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".png")) {
								return true;
							}
							return false;
						}
					}).length;
					mDirPaths.add(dirPath);
					FolderBean folderBean = new FolderBean();
					folderBean.setPath(dirPath);
					folderBean.setImgCount(imageCount);
					folderBean.setFristImgPath(path);
					mFolderList.add(folderBean);
					if (imageCount > mCurrentImageCount) {
						mCurrentFile = parentFile;
						mCurrentImageCount = imageCount;
					}
				}
				cursor.close();
				mUiHandler.sendEmptyMessage(MSG_OBTAIN_DATA_SUCCESS);

			}
		}.start();
	}

	private void initView() {
		mGvImage = (GridView) findViewById(R.id.gv_imageselect_image);
		mTvDirPath = (TextView) findViewById(R.id.tv_imageselect_dir_path);
		mTvDirCount = (TextView) findViewById(R.id.tv_imageselect_dir_count);
		mRlDirSelect = (RelativeLayout) findViewById(R.id.rl_imageselect_dir_select);
	}

	private void data2View() {
		if (null == mCurrentFile) {
			Toast.makeText(ImageSelectActivity.this, "no image data", Toast.LENGTH_SHORT).show();
			return;
		}
		mImgs = Arrays.asList(mCurrentFile.list(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				if (s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".png")) {
					return true;
				}
				return false;
			}
		}));
		mCurrentImageCount = mImgs.size();
		mAdapter = new ImageAdapter(ImageSelectActivity.this, mImgs, mCurrentFile.getAbsolutePath());
		mGvImage.setAdapter(mAdapter);
		mTvDirPath.setText(mCurrentFile.getName());
		mTvDirCount.setText(mCurrentImageCount + "");
	}

	private void initPopupWindow() {
		mPopupWindow = new ListDirPopupWindow(ImageSelectActivity.this, mFolderList);
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				setAlpha(1.0f);
			}
		});
		mPopupWindow.setOnPathSelectedListener(this);
	}

	private void setAlpha(float alpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = alpha;
		getWindow().setAttributes(lp);
	}

	@Override
	public void pathSelected(FolderBean folderBean) {
		mCurrentFile = new File(folderBean.getPath());
		data2View();
		mPopupWindow.dismiss();
	}


	static class UiHandler extends Handler {

		WeakReference<ImageSelectActivity> mActivityReference;

		public UiHandler(ImageSelectActivity activity) {
			mActivityReference = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final ImageSelectActivity activity = mActivityReference.get();
			if (activity != null) {
				switch (msg.what) {
					case MSG_OBTAIN_DATA_SUCCESS:
						activity.mProgressDialog.dismiss();
						activity.data2View();
						activity.initPopupWindow();
						break;
				}
			}
		}
	}
}
