package com.shengxun.cashiersystem.app;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Application;

import com.j256.ormlite.dao.Dao;
import com.shengxun.constant.C;
import com.shengxun.entity.LoginInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ConnectManager;
import com.shengxun.util.DeviceID;
import com.shengxun.util.MD5Util;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
import com.zvezda.data.utils.DataSP;
import com.zvezda.database.utils.ORMOpearationDao;

/**
 * 模块描述：应用程序 2015-4-21 下午1:50:45 Write by LILIN
 */
public class ApplicationCS extends Application {

	/**
	 * 登录的收银员信息
	 */
	public LoginInfo loginInfo = null;

	/**
	 * 收银员的卡号
	 */
	public String cashier_card_no = null;

	/**
	 * 收银机的编号
	 */
	public String mc_id = null;

	/**
	 * ORM数据库操作封装
	 */
	protected ORMOpearationDao ormOpearationDao = null;
	/**
	 * 产品数据上次的同步时间
	 */
	public static final String LAST_SYN_TIME="last_syn_time";
	public DataSP sp=null;
	@Override
	public void onCreate() {
		super.onCreate();
		ormOpearationDao = new ORMOpearationDao(getApplicationContext(),C.DATABASE_NAME);
		//修改机器码
		C.MACHINE_CODE=DeviceID.getDeviceID(this);
		C.VERIFY_CODE=MD5Util.GetMD5Code(""+C.SOB_CODE+"#"+C.SOB_PASSWORD+"#"+C.MACHINE_CODE+"");
		LG.e(getClass(), ""+DeviceID.getDeviceID(this));
		LG.i(ApplicationCS.class, "收银系统启动------>1:获取联网最新产品信息");
		sp=new DataSP(this, "CashierSystem");
		ConnectManager.getInstance().getProductList(sp.getSValue(LAST_SYN_TIME, ""),productAjaxCallBack);
	}

	private AjaxCallBack<String> productAjaxCallBack = new AjaxCallBack<String>() {

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			try {
				if (BaseUtils.IsNotEmpty(t)&& JSONParser.getStringFromJsonString("status", t).equals("1")) {
					String data = JSONParser.getStringFromJsonString("data", t);
					String product_list = JSONParser.getStringFromJsonString("product_list", data);
					String last_syn_time = JSONParser.getStringFromJsonString("last_syn_time", data);
					ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) JSONParser.JSON2Array(product_list, ProductInfo.class);
					//如果增量数据不为空的话
					if (products != null && products.size() > 0) {
						Dao<ProductInfo, Integer> productsDao = ormOpearationDao.getDao(ProductInfo.class);
						//第一次更新数据，全部更新
						if(BaseUtils.IsNotEmpty(sp.getSValue(LAST_SYN_TIME, ""))){
							LG.i(ApplicationCS.class, "收银系统启动------>2：数据全部更新");

							productsDao.executeRawNoArgs("DELETE FROM productInfosTable");//删除所有数据
							for (ProductInfo entity : products) {
								productsDao.create(entity);
							}
						}else{
							LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据");
							String product_ids="";
							for (ProductInfo entity : products) {
								productsDao.createOrUpdate(entity);
								product_ids+=entity.op_id;
							}
							LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据结束调用回调");
							AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {

								@Override
								public void onSuccess(String t) {
									super.onSuccess(t);
									if (BaseUtils.IsNotEmpty(t)&&
										JSONParser.getStringFromJsonString("status", t).equals("1")) {
										String data = JSONParser.getStringFromJsonString("data", t);
										String result = JSONParser.getStringFromJsonString("result", data);
										LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据结束调用回调result->"+result);
									}
								}
								
							};
							ConnectManager.getInstance().productSynCallback(product_ids, ajaxCallBack);
						}
						sp.setValue(LAST_SYN_TIME, last_syn_time);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
		}

	};
}
