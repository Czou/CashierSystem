package com.shengxun.util;

//Workaround to get adjustResize functionality for input methos when the fullscreen mode is on
//found by Ricardo
//taken from http://stackoverflow.com/a/19494006

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
/**
 * 解决android全屏下windosSoftInputmode:adjustResize失效的问题
 * 使用方法，在setcontentview之后调用assistActivity
 * @date 2015-5-8
 */
public class AndroidAdjustResizeUtil {

	// For more information, see
	// https://code.google.com/p/android/issues/detail?id=5497
	// To use this class, simply invoke assistActivity() on an Activity that
	// already has its content view set.

	public static void assistActivity(Activity activity) {
		new AndroidAdjustResizeUtil(activity);
	}

	private View mChildOfContent;
	private int usableHeightPrevious;
	private FrameLayout.LayoutParams frameLayoutParams;

	private AndroidAdjustResizeUtil(Activity activity) {
		FrameLayout content = (FrameLayout) activity
				.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);
		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						possiblyResizeChildOfContent();
					}
				});
		frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
				.getLayoutParams();
	}

	private void possiblyResizeChildOfContent() {
		int usableHeightNow = computeUsableHeight();
		if (usableHeightNow != usableHeightPrevious) {
			int usableHeightSansKeyboard = mChildOfContent.getRootView()
					.getHeight();
			int heightDifference = usableHeightSansKeyboard - usableHeightNow;
			if (heightDifference > (usableHeightSansKeyboard / 4)) {
				// keyboard probably just became visible
				frameLayoutParams.height = usableHeightSansKeyboard
						- heightDifference;
			} else {
				// keyboard probably just became hidden
				frameLayoutParams.height = usableHeightSansKeyboard;
			}
			mChildOfContent.requestLayout();
			usableHeightPrevious = usableHeightNow;
		}
	}

	private int computeUsableHeight() {
		Rect r = new Rect();
		mChildOfContent.getWindowVisibleDisplayFrame(r);
		return (r.bottom - r.top);
	}

}
