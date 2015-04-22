package com.shengxun.cashiersystem;
import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.shengxun.cashiersystem.app.ApplicationCS;
import com.zvezda.android.utils.AppManager;
/**
 * 基础Activity
 * @author LILIN
 * 上午11:58:22
 */
public class BaseActivity extends FragmentActivity{
	/**
	 * 当前活动上下文
	 */
	protected Activity mActivity;
	/**
	 * 资源管理
	 */
	protected Resources resources;
	/**
	 * 应用
	 */
	protected ApplicationCS applicationCS;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		mActivity = this;
		resources=mActivity.getResources();
		applicationCS=(ApplicationCS) mActivity.getApplication();
		//将该Activity添加到栈，方便管理
		AppManager.getAppManager().addActivity(mActivity);
	}
	
	protected void goActivity(Class <?> clazz){
		startActivity(new Intent(mActivity, clazz));
		//切换动画
	}
	protected void goActivity(Class <?> clazz,Serializable  obj){
		Intent intent=new Intent(mActivity, clazz);
		intent.putExtra("DATA", obj);
		startActivity(intent);
	}
	
}
