package com.shengxun.cashiersystem.app;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Application;

import com.j256.ormlite.dao.Dao;
import com.shengxun.entity.LoginInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
import com.zvezda.database.utils.ORMOpearationDao;

/**
 * 模块描述：应用程序
 * 2015-4-21 下午1:50:45
 * Write by LILIN
 */
public class ApplicationCS extends Application{

	/**
	 * 登录的收银员信息 
	 */
	public LoginInfo loginInfo=null;
	
	/**
	 * 收银员的卡号
	 */
	public String cashier_card_no=null;
	
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

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(ApplicationCS.class, "收银系统启动------>2：将最新的数据写入数据库");
			try {
			if(BaseUtils.IsNotEmpty(t)&&JSONParser.getStringFromJsonString("status", t).equals("1")){
				String data=JSONParser.getStringFromJsonString("data", t);
				String product_list=JSONParser.getStringFromJsonString("product_list", data);
				ArrayList<ProductInfo> products=(ArrayList<ProductInfo>) JSONParser.JSON2Array(product_list, ProductInfo.class);
				if(products!=null&&products.size()>0){
					Dao<ProductInfo,Integer> productsDao=ormOpearationDao.getDao(ProductInfo.class);
					//productsDao.executeRawNoArgs("DELETE FROM productInfoTable");//删除所有数据
					for(ProductInfo entity:products){
					 productsDao.createIfNotExists(entity);
					}
				}
			}} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
		}
		
		
	};
}
