package com.jacli.draglist_menu_view.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.jacli.draglist_menu_view.MainActivity;
import com.jacli.draglist_menu_view.R;
import com.jacli.draglist_menu_view.note.bean.newItemNoteContentParam.NotesBean;
import com.jacli.draglist_menu_view.utils.CallbackInterface;
import com.jacli.draglist_menu_view.utils.DateUtils;
import com.jacli.draglist_menu_view.view.MyEnableEditText;
import com.jacli.draglist_menu_view.note.MyLinerLay.RightScrollDeleteItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/***
 * 自定义可拖拽ListView适配器
 */
@SuppressLint("UseSparseArrays")
public class DragListAdapter extends BaseAdapter {
	
	private List<NotesBean> beans  ;// 数据源
	private LayoutInflater mInflater;
	private Context context;
	private Animation animation;
	private int mIndex;
	private View parentView;
	
	private int yearData;
	private int monthData;
	private int dayData;
	private int hourData;
	private int minuteData;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

	private boolean isActivityUp = false;
	private boolean isInit = true;
	protected float lastX;
	private boolean ischange = true;// 定义一个记录是否用原始记录还是文本监听改变记录的标识符
	
	public static String NOTE_CONTENT = "";
    List<CheckBox> checkBoxArray = new ArrayList<>() ;
    private String circulationString="";
    
    List<RadioButton> RadioButtonArray = new ArrayList<>() ;

    private boolean isCreateTastPermission = false;
 
    private boolean isAddNote = false;
	

	public void setParentView(View view) {
		parentView = view;
	}


	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}
	
	public void setAddState(boolean isAddNote) {
		this.isAddNote = isAddNote;
	}
	////////////////////////////////////

	private Context mContext; // 上下文
	//private List<String> dataList;// 数据源
	private Map<Integer,MyLinerLay> lins=new HashMap<Integer,MyLinerLay>();// 数据源
	//private ArrayList<String> copyList = new ArrayList<String>();
	private List<NotesBean> copyList = new ArrayList< >();// 数据源
	private int mInvisilePosition = -1;// 用来标记不可见Item的位置
	private boolean isChanged = true;// 是否发生改变标识
	private boolean mShowItem = false;// 是否显示拖拽Item的内容标识
	/**
	 * 移动方向的标记，-1为默认值，0表示向下移动，1表示向上移动
	 */
	private int mLastFlag = -1;    
	private int mHeight;
	@SuppressWarnings("unused")
	private int mDragPosition = -1;  //拖动位置
	@SuppressWarnings("unused")
	private boolean isSameDragDirection = true;// 是否为相同方向拖动的标记
	 

	/** DragListAdapter构造方法 */
	public DragListAdapter(Context context, List<NotesBean> beans) {
		this.mContext = context;
		 
		this.context =context;
		this.beans = beans;
		mInflater = LayoutInflater.from(context);
	 
		animation = AnimationUtils.loadAnimation(context, R.anim.slide_right);
	}

	/** 获取Item总数 */
	@Override
	public int getCount() {
		return beans.size();
	}

	/** 获取ListView中Item项 */
	@Override
	public MyLinerLay getItem(int position) {
		return lins.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	

	/**
	 * 设置是否显示下降的Item
	 * 
	 * @param showItem
	 */
	public void showDropItem(boolean showItem) {
		this.mShowItem = showItem;
	}

	/**
	 * 设置不可见项的位置标记
	 * 
	 * @param position
	 */
	public void setInvisiblePosition(int position) {
		mInvisilePosition = position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		/***
		 * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
		 * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
		 */
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.draglist_item, null);

		//initItemView(position, convertView);// 初始化Item视图

		//final MyEnableEditText titleTv = (MyEnableEditText) convertView
		//		.findViewById(R.id.drag_item_title_tv);
		//titleTv.setText(beans.get(position).getNotesContent());
		
		////////////////////////////////////////////////////////////////////
		final NotesBean bean = beans.get(position);
		mIndex = position;
		final ViewHolder viewHolder;
        
		View leftLayout = convertView.findViewById(R.id.left_layout);
		viewHolder = new ViewHolder();
		viewHolder.content = (MyEnableEditText) convertView
				.findViewById(R.id.note_content);
		viewHolder.image_mic = (ImageView) convertView
				.findViewById(R.id.image_mic);
		viewHolder.clock_setting = (Button) convertView
				.findViewById(R.id.clock_setting);
		viewHolder.btnTask = (Button) convertView
				.findViewById(R.id.task);
		
		viewHolder.btnTarget = (Button) convertView
				.findViewById(R.id.target);
		View imgDrag =   convertView
				.findViewById(R.id.drag_item_image);
		
	 
	 
		View alarm_layout = convertView.findViewById(R.id.alarm_layout);
		if("1".equals( bean.getRemind()))
		{
			alarm_layout.setVisibility(View.VISIBLE);
			TextView alarmTime = ((TextView)convertView.findViewById(R.id.alert_time));
			ImageView alarm_img = ((ImageView)convertView.findViewById(R.id.alarm_img));
			alarmTime.setText(bean.getRemindTime());
			if(!DateUtils.judgeCurrTime(bean.getRemindTime()))
			{
				alarmTime.setTextColor(Color.RED);
				alarm_img.setImageResource(R.drawable.alarm_repeat_over);
			}
			
		}
		else
		{
			alarm_layout.setVisibility(View.INVISIBLE);
		}
		viewHolder.content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!ischange)
					if (beans.size() > 0) {
						// if(!bean.getNotesContent().equals(s.toString()))
						if (viewHolder.content.hasFocus()) {
							System.out.println("aftertextchanged "
									+ s.toString() + " " + position);
							//beans.get(position).setNotesContent(s.toString());
							//beans.get(position).setPosition(position);
							//beans.get(position).setChanged(true);
//							PreManager.putString(context,
//									MainActivity.EDITTEXT_CONETENT_CHANGED,
//									s.toString());
						} else {
							//bean.setChanged(false);
						}
					}
			}
		});

		ischange = true;
	 
		final View swipelayout =  convertView;
	 

		if ("0".equals(bean.getNotesType())) {
			if("0".equals(bean.getNotesStatus()))
			{
				viewHolder.content.setText(bean.getNotesContent());
				 viewHolder.content.setFocusable(false);
				 
			}else
			{
				setTextDeleteState(viewHolder.content,bean.getNotesContent());
				 viewHolder.content.setFocusable(false);
			}
		    
			viewHolder.image_mic.setVisibility(View.GONE);
			if(isAddNote&&position==0)
			{
				 viewHolder.content.setMyTouchEvent(true);
				 viewHolder.content.setFocusable(true);
				 viewHolder.content.setFocusableInTouchMode(true);
				 viewHolder.content.requestFocus();
				 InputMethodManager imm1 =  (InputMethodManager)((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
				 if(imm1 != null) {   
					    imm1.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(),
		                           0);   
		                      
				 }
				//if(!viewHolder.content.isFocused() )
				{
					
					 
					//InputMethodManager imm = (InputMethodManager)   viewHolder.content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					////imm.showSoftInputFromInputMethod(viewHolder.content.getWindowToken(),0);  
					//打开软键盘
					 InputMethodManager imm = (InputMethodManager) viewHolder.content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					 imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
				}
		
				isAddNote = false;
			}
		
			// viewHolder.content.setEnabled(true);
		} else {
			viewHolder.image_mic.setVisibility(View.VISIBLE);
			viewHolder.btnTarget.setVisibility(View.INVISIBLE);
			viewHolder.btnTask.setVisibility(View.INVISIBLE);
			// viewHolder.content.setEnabled(false);
			// viewHolder.content.setText(bean.getCreateTime());
			viewHolder.content.setMyTouchEvent(false);
			//viewHolder.content.setFocusable(false);
			//viewHolder.content.setFocusableInTouchMode(false);
			if (bean.getCreateTime()>0)
			{
//				viewHolder.content.setText(DateUtils.timeStampToStr(Long
//				.parseLong(bean.getCreateTime())));
		
		       String strTimeStamp = DateUtils.timeStampToStr( bean.getCreateTime());
		      if("0".equals(bean.getNotesStatus()))
		      {
			     viewHolder.content.setText(strTimeStamp);
		      }else
		      {
			    setTextDeleteState(viewHolder.content,strTimeStamp);
			    viewHolder.content.setFocusable(false);
		      }
			}	
		}
 
//		if(null == loginBean)
//		{
//			getCreateTastPermission();
//		}
		
		if(!isCreateTastPermission)
		{
			viewHolder.btnTarget.setVisibility(View.INVISIBLE);
		}
		 
		ischange = false;
		viewHolder.btnTarget.setOnClickListener(new MyOnClickListener(bean ,convertView));
		viewHolder.btnTask.setOnClickListener(new MyOnClickListener(bean ,convertView) );
		viewHolder.clock_setting.setOnClickListener(new MyOnClickListener(bean ,convertView)) ;
  
		viewHolder.content.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (v.getId()) {  
		        case R.id.note_content:  
		            v.getParent().requestDisallowInterceptTouchEvent(true);  
		            switch (event.getAction()) {  
		                case MotionEvent.ACTION_UP:
		                    v.getParent().requestDisallowInterceptTouchEvent(false);  
		                    break;  
		             }
		            }
				return false;
			}
		});
		viewHolder.content
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (beans.size() > 0)
 
							if (!hasFocus  ) {
								viewHolder.content.setMyTouchEvent(false); 
								 //beans.get(position).setNotesContent(viewHolder.content.getText().toString());
								//newItemNoteContentParam.NotesBean itemNote = beans
								//		.get(position);
								String noteid = bean.getNotesId();
								//if (itemNote.getNotesContent()!=null &&itemNote.getNotesContent().length() > 0)
								if(TextUtils.isEmpty(viewHolder.content.getText().toString()))
								{
									if(!viewHolder.content.isFocused())
									{
										 //emoveItem(position);
									//	notifyDataSetChanged();
									}
									
								}
								else
								{
									if(!viewHolder.content.getText().toString().equals(bean.getNotesContent()))
									{
										beans.get(position).setNotesContent(viewHolder.content.getText().toString());
										editItemData(bean.getNotesContent(),
												noteid,position);
									}
									
								}
							}
							else
							{
								viewHolder.content.setMyTouchEvent(true); 
//								InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);  
//								imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							}
					}
				});

 
		
		RightScrollDeleteItem scrollDeleteItem = new RightScrollDeleteItem()
		{

			@Override
			public void deleteItem() {
			//	MediaPlayUtils.getInstance().startRawMedia(context, NoteFragment.keybroadResouceID[2]);
				//Toast.makeText(context, "position"+position, 100).show();
				if(viewHolder.content.isFocused())
				{
					return;
				}
				if("0".equals(bean.getNotesStatus()))
				{
					if ("0".equals(bean.getNotesType()))   
					{
						setTextDeleteState(viewHolder.content,bean.getNotesContent());
					}
					else
					{
						String strTimeStamp = DateUtils.timeStampToStr( bean.getCreateTime());
						setTextDeleteState(viewHolder.content,strTimeStamp);
					}
					
					 bean.setNotesStatus("1");

				}
				else
				{
//					// TODO Auto-generated method stub
					((MainActivity) context).simpleDialogSelect_(
							"你确定要删除这条记录", new CallbackInterface() {

								@Override
								public void select(boolean selected) {
									// TODO Auto-generated method stub
									if (selected) {

										DeleteItemData(position);
									}

								}

							});
				}
			
			}

			@Override
			public boolean isScrollLeft() {
				// TODO Auto-generated method stub

				//Toast.makeText(context, "position"+position, 100).show();
				if("0".equals(bean.getNotesStatus()))
				{
					return true;
				}
				if ("0".equals(beans.get(position).getNotesType()))   
				{
					viewHolder.content.setText(bean.getNotesContent());
					 
				}
				else
				{
					String strTimeStamp = DateUtils.timeStampToStr( bean.getCreateTime());
					viewHolder.content.setText(strTimeStamp);
				}
				
				bean.setNotesStatus("0");
				// updateItemState(bean);
				return false;
			}
			
		};
		
		((MyLinerLay)convertView).setRightScrollDeleteItem(scrollDeleteItem);
		lins.put(position,(MyLinerLay)convertView);
		if (isChanged) {// 判断是否发生了改变

			if (position == mInvisilePosition) {

				if (!mShowItem) {// 在拖拽过程不允许显示的状态下，设置Item内容隐藏

					// 因为item背景为白色，故而在这里要设置为全透明色防止有白色遮挡问题（向上拖拽）
					convertView.findViewById(R.id.drag_item_layout)
							.setBackgroundColor(0x0000000000);

					// 隐藏Item上面的内容
					int invis = View.INVISIBLE;
					convertView.findViewById(R.id.drag_item_image)
							.setVisibility(invis);
					convertView.findViewById(R.id.drag_item_close_layout)
							.setVisibility(invis);
					leftLayout.setVisibility(invis);
				}
			}

			if (mLastFlag != -1) {

				if (mLastFlag == 1) {

					if (position > mInvisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, -mHeight);
						convertView.startAnimation(animation);// 开启动画
					}

				} else if (mLastFlag == 0) {

					if (position < mInvisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, mHeight);
						convertView.startAnimation(animation);// 开启动画
					}
				}
			}
		}
		
		convertView.findViewById(R.id.drag_item_image)
		.setVisibility(View.VISIBLE);
		return convertView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		//lins.clear();
	}



	/**
	 * 删除指定的Item
	 * 
	 * @param pos
	 *            // 要删除的下标
	 */
	public void removeItem(int pos) {
		if (beans != null && beans.size() > pos) {
			beans.remove(pos);
			lins.remove(pos);
			this.notifyDataSetChanged();// 刷新适配器
		}
	}
	
	/*	*//***
	 * 设置文本带删除状态
	 * 
	 * @param  
	 *             
	 * @param endPosition
	 *             
	 */ 
	private void setTextDeleteState(MyEnableEditText edit,String content)
	{
		Spannable spanStrikethrough = new SpannableString(content);
        StrikethroughSpan stSpan = new StrikethroughSpan();  //设置删除线样式
        spanStrikethrough.setSpan(stSpan, 0, spanStrikethrough.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        edit.setText(spanStrikethrough);
      
       
	}

/*	*//***
	 * 动态修改ListView的方位.
	 * 
	 * @param startPosition
	 *            点击移动的position
	 * @param endPosition
	 *            松开时候的position
	 *//*
	public void exchange1(int startPosition, int endPosition) {
		Object startObject = getItem(startPosition);

		if (startPosition < endPosition) {// 向下拖拽
			dataList.add(endPosition + 1, (String) startObject);
			dataList.remove(startPosition);
		} else {// 向上拖拽或者不动
			dataList.add(endPosition, (String) startObject);
			dataList.remove(startPosition + 1);
		}
		isChanged = true;
	}*/

	/**
	 * 动态修改Item内容
	 * 
	 * @param startPosition
	 *            // 开始的位置
	 * @param endPosition
	 *            // 当前停留的位置
	 */
	public void exchangeCopy(int startPosition, int endPosition) {
		Object startObject = getCopyItem(startPosition);

		if (startPosition < endPosition) {// 向下拖拽
			copyList.add(endPosition + 1, (NotesBean) startObject);
			copyList.remove(startPosition);
		} else {// 向上拖拽或者不动
			copyList.add(endPosition, (NotesBean) startObject);
			copyList.remove(startPosition + 1);
		}
		isChanged = true;
	}

	/**
	 * 获取镜像(拖拽)Item项
	 * 
	 * @param position
	 * @return
	 */
	public Object getCopyItem(int position) {
		return copyList.get(position);
	}

	/**
	 * 添加拖动项
	 * 
	 * @param start
	 *            // 要进行添加的位置
	 * @param obj
	 */
	public void addDragItem(int start, Object obj) {
		beans.remove(start);// 删除该项
		beans.add(start, (NotesBean) obj);// 添加删除项
	}

	public void copyList() {
		copyList.clear();// 清空集合
		for (NotesBean str : beans) {// 遍历数据源
			copyList.add(str);// 添加至拷贝集合
		}
	}

	public void pastList() {
		beans.clear();// 清空集合
		for (NotesBean str : copyList) {// 遍历拷贝集合
			beans.add(str);// 添加至数据源集合
		}
	}

	/**
	 * 设置是否为相同方向拖动的标记
	 * 
	 * @param value
	 */
	public void setIsSameDragDirection(boolean value) {
		isSameDragDirection = value;
	}

	/**
	 * 设置拖动方向标记
	 * 
	 * @param flag
	 */
	public void setLastFlag(int flag) {
		mLastFlag = flag;
	}

	/**
	 * 设置高度
	 * 
	 * @param value
	 */
	public void setHeight(int value) {
		mHeight = value;
	}
	
	

	/**
	 * 设置当前拖动位置
	 * 
	 * @param position
	 */
	public void setCurrentDragPosition(int position) {
		mDragPosition = position;
	}

	/**
	 * 从自身出现的动画
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Animation getFromSelfAnimation(int x, int y) {
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);// 平移动画
		translateAnimation
				.setInterpolator(new AccelerateDecelerateInterpolator());// 设置插值器
		translateAnimation.setFillAfter(true);
		translateAnimation.setDuration(1000);
		translateAnimation.setInterpolator(new AccelerateInterpolator());// 设置插值器
		return translateAnimation;
	}
	
	private void editItemData(String data, String noteId, final int position) {

		if(TextUtils.isEmpty(data))
		{
			return;
		}
		if (noteId == null) {
//			((BaseActivity) context).showLoadingDialog();
//			HttpUtils.getInstance().post(
//					true,
//					context,
//					URLUtils.URL_NODE_ADD,
//					URLTools.getInStance(context).TheNotesAdd("0", data),
//
//					new MyResponseHandler<ItemNoteContentParam>(
//							new ItemNoteContentParam(), false,
//							(BaseActivity) context) {
//
//						@Override
//						protected void onEvent(Event event) {
//							((BaseActivity) context).dismissLoadingDialog();
//							if (event.isSuccess()) {
//								ItemNoteContentParam note = (ItemNoteContentParam) event
//										.getReturnParamAtIndex(0);
//								beans.get(position).setNotesId(note.getNotesId());
//							} else {
//								CommonUtil.showException(event);
//							}
//						}
//					});

		} else {
//			((BaseActivity) context).showLoadingDialog();
//			HttpUtils.getInstance().post(
//					true,
//					context,
//					URLUtils.URL_NODE_UPDATE,
//					URLTools.getInStance(context).TheNotesUpdate(noteId, data),
//
//					new MyResponseHandler<ItemNoteContentParam>(
//							new ItemNoteContentParam(), false,
//							(BaseActivity) context) {
//
//						@Override
//						protected void onEvent(Event event) {
//							((BaseActivity) context).dismissLoadingDialog();
//							if (event.isSuccess()) {
//								PreManager.putString(context,
//										NoteFragment.EDITTEXT_CONETENT_CHANGED,
//										"");
//
//							} else {
//								CommonUtil.showException(event);
//							}
//						}
//					});
		}
	}

	private void DeleteItemData(final int position) {
		String noteid = beans.get(position).getNotesId();
		 final NotesBean note = beans.get(position);
		
		if (null != noteid) {
//			((BaseActivity) context).showLoadingDialog();
//			HttpUtils.getInstance().post(
//					true,
//					context,
//					URLUtils.URL_NODE_UPDATE,
//					URLTools.getInStance(context).TheNotesUpdateItemState(note.getNotesId(), note.getNotesContent(),"2"),
//
//					new MyResponseHandler<NoteStateUpdateParam>(
//							new NoteStateUpdateParam(), false,
//							(BaseActivity) context) {
//
//						@Override
//						protected void onEvent(Event event) {
//							((BaseActivity) context).dismissLoadingDialog();
//							if (event.isSuccess()) {
////								PreManager.putString(context,
////										NoteFragment.EDITTEXT_CONETENT_CHANGED,
////										"");
//	                           Log.e("updateItemstate","noteid"+note.getNotesId()+" states"+note.getNotesStatus());
//	                           beans.remove(position);
// 					    	   notifyDataSetChanged();
//							} else {
//								CommonUtil.showException(event);
//							}
//						}
//					});

		} else {
			beans.remove(position);
			notifyDataSetChanged();
		}

	}

	static class ViewHolder {
		public MyEnableEditText content;
		public Button clock_setting;
		public Button btnTarget;
		public Button btnTask;
		public ImageView image_mic;
	}
 

	public class MyOnClickListener implements OnClickListener {

		NotesBean note ;
		View convertView;
		public MyOnClickListener(NotesBean bean ,View convertView)
		{
			note  = bean;
			this.convertView = convertView;
			
		}
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.target: {
				 
			//((MainActivity)	context).createTarget(note.getNotesContent(),1);
			}
				break;
			case R.id.task: {
				//((MainActivity)	context).createTarget(note.getNotesContent(),2);
				//Intent intent = new Intent(context,TaskCreateActivity.class);
				//intent.putExtra(NOTE_CONTENT, note.getNotesContent());
				//context.startActivity(intent);
			}
				break;
			case R.id.clock_setting: {

                
			}
				break;
				
			}
		}

	}
	
	
	
	//private void setNoteRemind(final String noteid,String circulation,final String remindTime,String remind,final String NotesContent)

	

	


	
	public class myCheckChangeListener implements OnCheckedChangeListener
	{
		private int checkViewID;
        public myCheckChangeListener(int id)
        {
        	this.checkViewID = id;
        }
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)
			// TODO Auto-generated method stub
			setRadioButtonState(); 
		}
	}
	
	private void setCheckBoxState()
	{
		for (int i = 0; i < checkBoxArray.size(); i++) {
			((CheckBox)checkBoxArray.get(i)).setChecked(false);
		}
	}
	private void setRadioButtonState()
	{
		for (int i = 0; i < RadioButtonArray.size(); i++) {
			((RadioButton)RadioButtonArray.get(i)).setChecked(false);
		}
	}
	
	private String getTimerString ()
	{
		circulationString = "";
		for (int i = 0; i < checkBoxArray.size(); i++) {
			if(((CheckBox)checkBoxArray.get(i)).isChecked())
			{
				circulationString+=((i+1)+",");
			}
		}
		
	 
		if(circulationString.equals(""))
		for (int i = 0; i < RadioButtonArray.size(); i++) {
			if(((RadioButton)RadioButtonArray.get(i)).isChecked())
			{
				if(0==i)
				{
					circulationString+="10";
				}
				else if(1==i)
				{
					circulationString+="11";
				}
				else if(2==i)
				{
					circulationString+="12";
				}
				else if(3==i)
				{
					circulationString+="13";
				}
				else if(4==i)
				{
					circulationString+="14";
				}
			}
		}
		if(circulationString.equals(""))
		{
			circulationString="0"; 
		}
		return circulationString;
	}
	

    
	private boolean dealwith_circulation (NotesBean bean)
	{
		if(null!=bean.getRemindTime()&&1==bean.getCirculation().length())
		{
		    int key = Integer.valueOf(bean.getCirculation());
			switch(key)
			{
			case 0:// 0 不用处理了;
				break;
			case 1:
			
			case 2:
			   
			case 3:
			 
			case 4:
				 
			case 5:
			 
			case 6:
				 
			case 7:
				checkBoxArray.get(key-1).setChecked(true);
			  
				break;
			}
	 
		}
		else if(bean.getCirculation().length()==2)
		{
			 int type = Integer.valueOf(bean.getCirculation());
			 switch(type)
			 {
			 case 10:
			 
			 case 11:
			 case 12:
          
			 case 13:
            
			 case 14:
             {
            	 RadioButtonArray.get(type-10).setChecked(true);
			 }
			  break;
				 
			 }
		}
		else if(bean.getCirculation().length()>2)// 大于二个字符 如： 1，2，3,如包含当前星期几，返回真
		{
			if(bean.getCirculation().subSequence(1, 2).equals(","))
			{
				  String timeStr="";
				  String[] split = bean.getCirculation().split(",");
				  for(int i =0;i<split.length;i++)
			      {
			        	timeStr = split[i];
			        	 int type = Integer.valueOf(timeStr);
			        	 if(type-1>=0)
			        	checkBoxArray.get(type-1).setChecked(true);
			      }
	
			}
		}
	 
		return false;
		
	}
	
 
}
