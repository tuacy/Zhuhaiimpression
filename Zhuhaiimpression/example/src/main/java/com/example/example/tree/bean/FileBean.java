package com.example.example.tree.bean;

import com.example.example.tree.utils.annotation.TreeNodeId;
import com.example.example.tree.utils.annotation.TreeNodeName;
import com.example.example.tree.utils.annotation.TreeNodePid;

public class FileBean {

	@TreeNodeId
	private int    mId;
	@TreeNodePid
	private int    mPid;
	@TreeNodeName
	private String mName;
	private String mDes;

	public FileBean(int mId, int mPid, String mName) {
		this.mId = mId;
		this.mPid = mPid;
		this.mName = mName;
	}

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public int getPid() {
		return mPid;
	}

	public void setPid(int mPid) {
		this.mPid = mPid;
	}

	public String getDes() {
		return mDes;
	}

	public void setDes(String mDes) {
		this.mDes = mDes;
	}
}
