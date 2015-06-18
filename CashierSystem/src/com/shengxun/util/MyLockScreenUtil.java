package com.shengxun.util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;

import com.shengxun.cashiersystem.MyTimeLockBaseActivity;
import com.shengxun.constant.C;
import com.zvezda.android.utils.LG;

/**
 * 自定义计时器进行锁屏管理工具
 * 
 * @author sw
 * @date 2015-6-17
 */
public class MyLockScreenUtil {

	// 计时器
	Timer timer = null;
	// 计算秒数
	int timeCount;

	// 计时任务
	MyTimerTask mTimerTask;

	static MyLockScreenUtil mLockutil;
	static Activity mActivity;
	static Handler mhandler;
	// startactivityforresult的请求code
	public static final int LOCK_REQUESTCODE = 10001;
	// handler
	public static final int HANDLER_OK = 10002;

	private MyLockScreenUtil() {
		timer = new Timer();
		openTimer();
	}

	public static MyLockScreenUtil getInstance(Activity activity,
			Handler handler) {
		if (mLockutil == null) {
			mLockutil = new MyLockScreenUtil();
		}
		mActivity = activity;
		mhandler = handler;
		return mLockutil;
	}

	public void setCurrentTimeCount(int timeCount) {
		this.timeCount = timeCount;
	}

	/**
	 * 计时任务
	 * 
	 * @author sw
	 * @date 2015-6-17
	 */
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
//			LG.i(getClass(), "计时总秒数 ========= >" + timeCount);
			if(isRunningForeground()){
				timeCount++;
				if (timeCount >= C.MAX_LOCKDOWN_SECOND) {
					mhandler.sendEmptyMessage(HANDLER_OK);
					//closeTimer();
				}
			}
		}
	}

	/**
	 * 开启计时任务
	 * 
	 * @auth shouwei
	 */
	public void openTimer() {
		timeCount = 0;
		closeTimer();
		// 每一秒钟触发一次
		mTimerTask = new MyTimerTask();
		timer.schedule(mTimerTask, 0, 1000);
	}

	/**
	 * 取消计时任务
	 * @auth shouwei
	 */
	public void closeTimer() {
		if (timer != null && mTimerTask != null) {
			mTimerTask.cancel();
		}
	}

	public boolean isRunningForeground() {
		String packageName = getPackageName(mActivity);
		String topActivityClassName = getTopActivityName(mActivity);
		if (packageName != null && topActivityClassName != null
				&& topActivityClassName.startsWith(packageName)) {
			return true;
		} else {
			return false;
		}
	}

	public String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager
				.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	public String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}
}
