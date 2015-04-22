package com.shengxun.cashiersystem.app;

import com.shengxun.entity.LoginInfo;

import android.app.Application;

/**
 * 模块描述：应用程序
 * 2015-4-21 下午1:50:45
 * Write by LILIN
 */
public class ApplicationCS extends Application{

	public LoginInfo loginInfo=null;
	@Override
	public void onCreate() {
		super.onCreate();
	}
  
}
