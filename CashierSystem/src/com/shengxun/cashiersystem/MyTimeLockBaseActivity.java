package com.shengxun.cashiersystem;

import java.util.Timer;
import java.util.TimerTask;

import com.shengxun.util.MyLockScreenUtil;
import com.zvezda.android.utils.LG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 含有计时进入锁屏功能的基础Activity,所有需要锁屏保护的页面都继承自这个activity
 * @author sw
 * @date 2015-6-17
 */
public class MyTimeLockBaseActivity extends BaseActivity{
	
	MyLockScreenUtil mLock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLock = MyLockScreenUtil.getInstance(mActivity,handler);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//屏幕发生操作，则重新计时
		mLock.setCurrentTimeCount(0);
		return super.dispatchTouchEvent(ev);
	}
	/**
	 * handler
	 */
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == MyLockScreenUtil.HANDLER_OK){
				goLockActivity();
			}
		};
	};
	/**
	 * 跳转方法，可供MainActivity使用,用于同步操作，避免重复跳转
	 * @auth shouwei
	 */
	public void goLockActivity(){
		mLock.closeTimer();
		Intent it = new Intent(mActivity,LockActivity.class);
		startActivityForResult(it, MyLockScreenUtil.LOCK_REQUESTCODE);
	}
	/**
	 * 接收LockActivity回伟参数
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LG.i(getClass(), "onactivityresult =====>"+requestCode+","+resultCode+",");
		if(resultCode == RESULT_OK){
			if(requestCode == MyLockScreenUtil.LOCK_REQUESTCODE){
				mLock.openTimer();
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HOME){
			if(mLock != null){
				mLock.closeTimer();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(mLock!=null){
			mLock.setCurrentTimeCount(0);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLock!=null){
			mLock.setCurrentTimeCount(0);
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mLock!=null){
			mLock.setCurrentTimeCount(0);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(mLock==null){
			mLock = MyLockScreenUtil.getInstance(mActivity, handler);
		}
		mLock.openTimer();
	}
}
