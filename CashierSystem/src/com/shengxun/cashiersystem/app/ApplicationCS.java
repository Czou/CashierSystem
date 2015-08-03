package com.shengxun.cashiersystem.app;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.shengxun.constant.C;
import com.shengxun.entity.LoginInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.service.MyScreenService;
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

	public static Dao<ProductInfo, Integer> productDao;
	/**
	 * 产品数据上次的同步时间
	 */
	public static final String LAST_SYN_TIME = "last_syn_time";

	public static DataSP sp = null;

	public static String SYN_RESULT = null;

	@Override
	public void onCreate() {
		super.onCreate();
		ormOpearationDao = new ORMOpearationDao(getApplicationContext(),
				C.DATABASE_NAME);
		// 修改机器码
		C.MACHINE_CODE =  DeviceID.getDeviceID(this);//"35703a430a4e23ce54:e4:bd:8b:cf:ff";// //
		C.VERIFY_CODE = MD5Util.GetMD5Code("" + C.SOB_CODE + "#"
				+ C.SOB_PASSWORD + "#" + C.MACHINE_CODE + "");

		LG.e(ApplicationCS.class, "========" + DeviceID.getDeviceID(this));
		LG.i(ApplicationCS.class, "收银系统启动------>1:获取联网最新产品信息");
		sp = new DataSP(this, C.SHARED_PREFENCE_NAME);
	}

//	private AjaxCallBack<String> productAjaxCallBack = new AjaxCallBack<String>() {
//		@SuppressWarnings("unchecked")
//		@Override
//		public void onSuccess(String t) {
//			super.onSuccess(t);
//			LG.i(getClass(), "更新商品------->" + t);
//			try {
//				if (BaseUtils.IsNotEmpty(t)
//						&& JSONParser.getStringFromJsonString("status", t)
//								.equals("1")) {
//					String data = JSONParser.getStringFromJsonString("data", t);
//					String product_list = JSONParser.getStringFromJsonString(
//							"product_list", data);
//					String last_syn_time = JSONParser.getStringFromJsonString(
//							"syn_time", data);
//					ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) JSONParser
//							.JSON2Array(product_list, ProductInfo.class);
//					// 如果增量数据不为空的话
//					if (products != null && products.size() > 0) {
//
//						productDao = ormOpearationDao.getDao(ProductInfo.class);
//						// 第一次更新数据，全部更新
//						if (!BaseUtils.IsNotEmpty(sp.getSValue(
//								ApplicationCS.LAST_SYN_TIME, ""))) {
//							LG.i(ApplicationCS.class, "收银系统启动------>2：数据全部更新");
//							productDao
//									.executeRawNoArgs("DELETE FROM productInfosTable");// 删除所有数据
//							for (ProductInfo entity : products) {
//								productDao.create(entity);
//							}
//							Toast.makeText(getApplicationContext(), "数据同步成功",
//									Toast.LENGTH_LONG).show();
//						} else {
//							LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据");
//							String product_ids = "";
//							for (ProductInfo entity : products) {
//								productDao.createOrUpdate(entity);
//								product_ids += entity.op_id + ",";
//							}
//							LG.i(ApplicationCS.class,
//									"收银系统启动------>2：增量更新数据结束调用回调");
//							AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {
//								@Override
//								public void onSuccess(String t) {
//									super.onSuccess(t);
//									LG.i(getClass(), "产品同步回调---->" + t);
//									if (BaseUtils.IsNotEmpty(t)
//											&& JSONParser
//													.getStringFromJsonString(
//															"status", t)
//													.equals("1")) {
//										String data = JSONParser
//												.getStringFromJsonString(
//														"data", t);
//										SYN_RESULT = JSONParser
//												.getStringFromJsonString(
//														"result", data);
//										LG.i(ApplicationCS.class,
//												"收银系统启动------>2：增量更新数据结束调用回调result->"
//														+ SYN_RESULT);
//										if (SYN_RESULT.equals("ok")) {
//											Toast.makeText(
//													getApplicationContext(),
//													"数据同步成功", Toast.LENGTH_LONG)
//													.show();
//										} else {
//											Toast.makeText(
//													getApplicationContext(),
//													"数据同步失败，可进入设置中手动更新",
//													Toast.LENGTH_LONG).show();
//										}
//									}
//								}
//							};
//							ConnectManager.getInstance().productSynCallback(
//									product_ids, ajaxCallBack);
//						}
//						sp.setValue(ApplicationCS.LAST_SYN_TIME, last_syn_time);
//						LG.i(getClass(), "最后更新时间 －－－－－－－－－－ >" + last_syn_time);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				Toast.makeText(getApplicationContext(), "数据同步失败，可进入设置中手动更新",
//						Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onFailure(Throwable t, int errorNo, String strMsg) {
//			super.onFailure(t, errorNo, strMsg);
//			Toast.makeText(getApplicationContext(), "数据同步失败，可进入设置中手动更新",
//					Toast.LENGTH_LONG).show();
//		}
//	};

}
