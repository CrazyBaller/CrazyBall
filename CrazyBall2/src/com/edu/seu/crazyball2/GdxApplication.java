package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;


import com.badlogic.gdx.backends.android.AndroidApplication;


import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;



public class GdxApplication extends AndroidApplication {
    /** Called when the activity is first created. */
	
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics(); // 获取屏幕尺寸
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 得到屏幕的宽度和高度
		if (dm.widthPixels < dm.heightPixels) { // 判断宽度与高度的大小
			SCREEN_WIDTH = dm.widthPixels; // 为SCREEN_WIDTH赋值
			SCREEN_HEIGHT = dm.heightPixels; // 为SCREEN_HEIGHT赋值
		} else {
			SCREEN_WIDTH = dm.heightPixels; // 为SCREEN_WIDTH赋值
			SCREEN_HEIGHT = dm.widthPixels; // 为SCREEN_HEIGHT赋值
		}
        initialize(new GdxAppUI(), false);
    }
}