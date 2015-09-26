package com.tuacy.wuyunxing.zhuhaiimpression.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by tuacy on 2015/9/20.
 */
public class Image extends BmobObject {

	private BmobFile image;
	private String   author;
	private String   descriptor;
	private String 	title;
	private BmobDate time;

	public BmobDate getTime() {
		return time;
	}

	public void setTime(BmobDate time) {
		this.time = time;
	}

	public BmobFile getImage() {
		return image;
	}

	public void setImage(BmobFile image) {
		this.image = image;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
