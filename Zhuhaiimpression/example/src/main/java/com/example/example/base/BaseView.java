package com.example.example.base;

/**
 * @author: tuacy
 * @date: 2015/9/23 9:50
 * @version: V1.0
 */
public interface BaseView {

	void showLoading(String msg);

	void hideLoading();

	void showError(String msg);

	void showException(String msg);

	void showNetError();
}
