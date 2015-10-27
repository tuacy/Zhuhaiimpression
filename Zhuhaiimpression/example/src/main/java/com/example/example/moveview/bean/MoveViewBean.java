package com.example.example.moveview.bean;

/**
 * @author: tuacy
 * @date: 2015/10/27 10:11
 * @version: V1.0
 */
public class MoveViewBean {

	/**
	 * icon name
	 */
	private String mName;
	private String mTag;
	private int    mLeft;
	private int    mRight;
	private int    mTop;
	private int    mBottom;

	public MoveViewBean(String mName, String mTag, int mLeft, int mTop, int mRight, int mBottom) {
		this.mName = mName;
		this.mTag = mTag;
		this.mLeft = mLeft;
		this.mRight = mRight;
		this.mTop = mTop;
		this.mBottom = mBottom;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getTag() {
		return mTag;
	}

	public void setTag(String mTag) {
		this.mTag = mTag;
	}

	public int getLeft() {
		return mLeft;
	}

	public void setLeft(int mLeft) {
		this.mLeft = mLeft;
	}

	public int getRight() {
		return mRight;
	}

	public void setRight(int mRight) {
		this.mRight = mRight;
	}

	public int getTop() {
		return mTop;
	}

	public void setTop(int mTop) {
		this.mTop = mTop;
	}

	public int getBottom() {
		return mBottom;
	}

	public void setBottom(int mBottom) {
		this.mBottom = mBottom;
	}
}
