package edu.gatech.constraints.demo;

import android.util.Log;

public class Functions {
	public static void d(String msg){
		Log.d(Constants.APP_TAG, msg);
	}
	
	public static void d(int d){
		Log.d(Constants.APP_TAG, d+"");
	}
	
	public static void d(float f){
		Log.d(Constants.APP_TAG, f+"");
	}
}
