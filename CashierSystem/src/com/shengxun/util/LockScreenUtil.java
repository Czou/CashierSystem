package com.shengxun.util;

import com.shengxun.service.AdminReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * 调用系统锁屏管理工具
 * @author sw
 * @date 2015-6-17
 */
public class LockScreenUtil {
	
	DevicePolicyManager policyManager;
	ComponentName componentName;
	static Activity mActivity;
	private LockScreenUtil(Activity mActivity){
		policyManager = (DevicePolicyManager) mActivity.getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(mActivity, AdminReceiver.class);
	}

	static LockScreenUtil mLockScreen  = null;
	public static  LockScreenUtil getInstance(Activity activity){
		if(mLockScreen==null){
			mLockScreen = new LockScreenUtil(activity);
		}
		mActivity = activity;
		return mLockScreen;
	}
	/**
	 * 锁屏
	 * @auth shouwei
	 */
	public void mylock(){
	       
		activeManage();
	    boolean active = policyManager.isAdminActive(componentName);   
	    if(!active){//若无权限   
	        activeManage();//去获得权限
	        policyManager.lockNow();//并锁屏   
	    }
	    if (active) {
	            policyManager.lockNow();//直接锁屏   
	    }   
	}  
	/**
	 * 激活服务
	 * @auth shouwei
	 */
	public void activeManage() {   
	        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器   
	        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);   
	           
	        //权限列表   
	        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);   
	  
	        //描述(additional explanation)   
	        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");   
	  
	        mActivity.startActivityForResult(intent, 0);
	}
}
