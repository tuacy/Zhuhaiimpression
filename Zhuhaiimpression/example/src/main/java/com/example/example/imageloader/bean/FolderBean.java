package com.example.example.imageloader.bean;

import java.io.File;

public class FolderBean {

	private String mPath;
	private String mFirstImgPath;
	private String mName;
	private int    mImgCount;

	public String getPath() {
		return mPath;
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
		int lastIndexOf = this.mPath.lastIndexOf(File.separator);
		mName = this.mPath.substring(lastIndexOf + 1);
	}

	public String getFristImgPath() {
		return mFirstImgPath;
	}

	public void setFristImgPath(String mFristImgPath) {
		this.mFirstImgPath = mFristImgPath;
	}

	public String getName() {
		return mName;
	}

	public int getImgCount() {
		return mImgCount;
	}

	public void setImgCount(int mImgCount) {
		this.mImgCount = mImgCount;
	}
}
