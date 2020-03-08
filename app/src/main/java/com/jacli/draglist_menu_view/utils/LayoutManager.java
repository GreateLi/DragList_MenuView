package com.jacli.draglist_menu_view.utils;



import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class LayoutManager {
	

    
    public static class ScreenConstant {
    	
    	public static int displayWidth;  //屏幕宽度
        public static int displayHeight; //屏幕高度
    	
    }
    
	public static ScreenConstant getScreenConstant(Activity activity) {
		
		 DisplayMetrics displayMetrics = new DisplayMetrics();
		 activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		 ScreenConstant.displayWidth = displayMetrics.widthPixels;
		 ScreenConstant.displayHeight = displayMetrics.heightPixels;
		 return new ScreenConstant();
	}
	

	/**
	 * dip转换px
	 * @param pxValue
	 * @return
	 */
	public static  int dip2px(Context mContext, float pxValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (pxValue * scale + 0.5f);
	}
}
