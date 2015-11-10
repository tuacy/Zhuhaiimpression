package com.example.example;

public class OutSideClass {

	private int mA;

	public int getValue() {
		return mA;
	}

	public static class InnerClass {
		public  int get() {
			return 2;
		}
	}
}
