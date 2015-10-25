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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FolderBean that = (FolderBean) o;

		if (mImgCount != that.mImgCount) {
			return false;
		}
		if (mPath != null ? !mPath.equals(that.mPath) : that.mPath != null) {
			return false;
		}
		if (mFirstImgPath != null ? !mFirstImgPath.equals(that.mFirstImgPath) : that.mFirstImgPath != null) {
			return false;
		}
		return !(mName != null ? !mName.equals(that.mName) : that.mName != null);

	}

	@Override
	public int hashCode() {
		int result = mPath != null ? mPath.hashCode() : 0;
		result = 31 * result + (mFirstImgPath != null ? mFirstImgPath.hashCode() : 0);
		result = 31 * result + (mName != null ? mName.hashCode() : 0);
		result = 31 * result + mImgCount;
		return result;
	}
}
