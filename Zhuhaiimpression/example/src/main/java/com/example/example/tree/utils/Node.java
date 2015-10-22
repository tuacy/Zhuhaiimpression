package com.example.example.tree.utils;


import java.util.ArrayList;
import java.util.List;

public class Node {

	private int    mId;
	/**
	 * 根节点的pid
	 */
	private int    mPid;
	private String mName;

	/**
	 * 树的层级
	 */
	private int     mLevel;
	/**
	 * 该节点是否展开
	 */
	private boolean mExpand;

	/**
	 * 当前节点的图标
	 */
	private int  mIcon;
	/**
	 * 该节点的父节点
	 */
	private Node mParent;
	/**
	 * 该节点的子节点
	 */
	private List<Node> mChildren = new ArrayList<Node>();

	public Node(int mId, int mPid, String mName) {
		this.mId = mId;
		this.mPid = mPid;
		this.mName = mName;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public int getPid() {
		return mPid;
	}

	public void setPid(int pid) {
		this.mPid = pid;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public int getLevel() {
		return null == mParent ? 0 : mParent.getLevel() + 1;
	}

	public void setLevel(int level) {
		this.mLevel = level;
	}

	public boolean isExpand() {
		return mExpand;
	}

	public void setExpand(boolean expand) {
		this.mExpand = expand;
		if (!expand) {
			for (Node node: mChildren) {
				node.setExpand(false);
			}
		}
	}

	public int getIcon() {
		return mIcon;
	}

	public void setIcon(int icon) {
		this.mIcon = icon;
	}

	public Node getParent() {
		return mParent;
	}

	public void setParent(Node parent) {
		this.mParent = parent;
	}

	public List<Node> getChildren() {
		return mChildren;
	}

	public void setChildren(List<Node> children) {
		this.mChildren = children;
	}

	/**
	 * 是否是根节点
	 */
	public boolean isRoot() {
		return null == mParent;
	}

	public boolean isParentExpand() {
		if (null == mParent) {
			return false;
		}
		return mParent.isExpand();
	}

	public boolean isLeaf() {
		return 0 == mChildren.size();
	}
}
