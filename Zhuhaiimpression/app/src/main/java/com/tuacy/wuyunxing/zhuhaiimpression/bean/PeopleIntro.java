package com.tuacy.wuyunxing.zhuhaiimpression.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author: tuacy
 * @date: 2015/9/18 16:08
 * @version: V1.0
 */
public class PeopleIntro extends BmobObject {

	private String   mName;
	private String   mUrl;
	private String   mDescriptor;
	private BmobFile mPic;

	public PeopleIntro() {
		this.setTableName("people_intro");
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getDescriptor() {
		return mDescriptor;
	}

	public void setDescriptor(String descriptor) {
		this.mDescriptor = descriptor;
	}

	public BmobFile getPic() {
		return mPic;
	}

	public void setPic(BmobFile pic) {
		this.mPic = pic;
	}
}
