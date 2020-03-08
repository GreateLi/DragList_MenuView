package com.jacli.draglist_menu_view.note;

import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MyLinerLay extends LinearLayout {

	private LinearLayout rLayout;  // 上层显示的//
	private View bottomView;  // 左滑动 显示的//
	private int viewWidth = -1;
	public float move;
	private float start;
	private boolean isRight = true;
	private int sreenWidth;
	private float x;

	public interface RightScrollDeleteItem{
		void deleteItem();
		boolean isScrollLeft();
	}
	private RightScrollDeleteItem mRightScrollDeleteItem;
	public void setRightScrollDeleteItem(RightScrollDeleteItem rightScrollDeleteItem) {
		mRightScrollDeleteItem = rightScrollDeleteItem;
	}
	
	@SuppressWarnings("deprecation")
	public MyLinerLay(Context context) {
		super(context);
		WindowManager manager = (WindowManager) context
				.getSystemService(Service.WINDOW_SERVICE);
		sreenWidth = manager.getDefaultDisplay().getWidth();
		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (viewWidth == -1) {
					viewWidth = bottomView.getWidth();
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	public MyLinerLay(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager manager = (WindowManager) context
				.getSystemService(Service.WINDOW_SERVICE);
		sreenWidth = manager.getDefaultDisplay().getWidth();
		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (viewWidth == -1&&bottomView!=null) {
					viewWidth = bottomView.getWidth();
				}
				return true;
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		rLayout = (LinearLayout) getChildAt(0);
		LayoutParams params = (LayoutParams) rLayout.getLayoutParams();
		//params.width = sreenWidth-52;
		rLayout.setLayoutParams(params);
		bottomView = getChildAt(1);
	}

	public void move(MotionEvent event) {
		x = event.getRawX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("TAG", "move--DOWN");
			start = event.getRawX();
			break;
		case MotionEvent.ACTION_UP:
			Log.i("TAG", "move--up");
			float movea = x - start;
			if (movea != 0 && movea < -viewWidth / 2f) {
				if(null!=mRightScrollDeleteItem)
				{
					// 如果是已删除状态，不要向左滚动
					if(!mRightScrollDeleteItem.isScrollLeft())
					{
						scrollTo(0, 0);
						move=0;
						isRight = true;
						return;
					}
					else
					{
						scrollTo(viewWidth, 0);
						move=-viewWidth;
						// new MyAsyncTask().execute(viewWidth,1);
						isRight = false;
					}
				}else
				{
					scrollTo(viewWidth, 0);
					move=-viewWidth;
					// new MyAsyncTask().execute(viewWidth,1);
					isRight = false;
				}
				
			}

			else {

				// new MyAsyncTask().execute(0,2);
				if (movea > sreenWidth -viewWidth+10&& isRight)
				 	if(null!=mRightScrollDeleteItem)
					{
				 		mRightScrollDeleteItem.deleteItem();
						Log.i("MyLinerLay", "mRightScrollDeleteItem");
					}
					Log.i("MyLinerLay", "mRightScrollDeleteItem movea "+movea +" "+sreenWidth *0.3);
					scrollTo(0, 0);
					move=0;
					isRight = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			move = x - start;
			if (isRight) {
				if (move <= 0 && move >= -viewWidth)
				{
//					if(null!=mRightScrollDeleteItem)
//					{
//						// 如果是已删除状态，不要向左滚动
//						if(!mRightScrollDeleteItem.isScrollLeft())
//						{
//							scrollTo(0, 0);
//							move=0;
//							isRight = true;
//							return;
//						}
//					}
					
				 	//scrollTo(-(int) move, 0);
				}
					
			} else {
				 //if (move >= 0 && move <= viewWidth)
					//  scrollTo(viewWidth - (int) move, 0);
			}
			/*
			 * else if(move<-viewWidth) move=-viewWidth; else if(move>0) move=0;
			 */
			break;
		}
	}

	public void getBack() {
		if (!isRight)
			new MyAsyncTask().execute();
	}

	private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {

			int i = 0;
			for (i = viewWidth; i >= 0; i -= 6) {
				publishProgress(i);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (i > 0)
				publishProgress(i);
			isRight = true;

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			scrollTo(values[0], 0);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}

	}

}
