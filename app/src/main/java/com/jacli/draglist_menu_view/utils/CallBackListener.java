package com.jacli.draglist_menu_view.utils;


/**
 * 网络服务回调接口
 *
 */
public interface CallBackListener {

	public void onComplete(Object object, String methodName);

	public void onError(Object object, Throwable error);

}
