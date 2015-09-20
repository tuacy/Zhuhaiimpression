package com.tuacy.wuyunxing.zhuhaiimpression.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author: tuacy
 * @date: 2015/9/18 16:08
 * @version: V1.0
 */
public class PeopleIntro extends BmobObject {

	private String name;
	private String url;
	private String descriptor;
	private BmobFile picture;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public BmobFile getPic() {
		return picture;
	}

	public void setPic(BmobFile pic) {
		this.picture = pic;
	}
}
