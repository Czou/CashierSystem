package com.shengxun.cashiersystem.app;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.entity.LoginInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.LG;
import com.zvezda.database.utils.ORMOpearationDao;

import android.app.Application;

/**
 * 模块描述：应用程序
 * 2015-4-21 下午1:50:45
 * Write by LILIN
 */
public class ApplicationCS extends Application{

	public LoginInfo loginInfo=null;
	/**
	 * ORM数据库操作封装
	 */
	protected ORMOpearationDao ormOpearationDao=null;
	@Override
	public void onCreate() {
		super.onCreate();
		ormOpearationDao=new ORMOpearationDao(getApplicationContext());
		LG.i(ApplicationCS.class, "收银系统启动------>1:获取联网最新产品信息");
		ConnectManager.getInstance().getProductList(ajaxCallBack);
	}
	private AjaxCallBack<String> ajaxCallBack=new AjaxCallBack<String>() {

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(ApplicationCS.class, "收银系统启动------>2：将最新的数据写入数据库");
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
		}
		
		
	};
}
