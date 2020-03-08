package com.jacli.draglist_menu_view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class MyEnableEditText extends EditText {
	private boolean isTouch= false;
	
	public void setMyTouchEvent(boolean t)
	{
		isTouch = t;
	}

	public MyEnableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyEnableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyEnableEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//return false;
//		// TODO Auto-generated method stub
		if(!isTouch)
		return isTouch;
		
		 return super.onTouchEvent(event);
	}
}