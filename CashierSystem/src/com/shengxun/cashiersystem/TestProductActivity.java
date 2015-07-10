package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;

import com.j256.ormlite.dao.Dao;
import com.shengxun.cashiersystem.app.ApplicationCS;
import com.shengxun.constant.C;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class TestProductActivity extends BaseActivity {
	Dao<ProductInfo, Integer> productDao ;
	String SYN_RESULT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);

		C.openProgressDialog(mActivity, null, "数据同步中...");
		
		productDao = ormOpearationDao.getDao(ProductInfo.class);
		
		ConnectManager.getInstance().getProductList(
				applicationCS.LAST_SYN_TIME, productAjaxCallBack);
	}

	private AjaxCallBack<String> productAjaxCallBack = new AjaxCallBack<String>() {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "更新商品------->" + t);
			try {
				if (BaseUtils.IsNotEmpty(t)
						&& JSONParser.getStringFromJsonString("status", t)
								.equals("1")) {
					String data = JSONParser.getStringFromJsonString("data", t);
					String product_list = JSONParser.getStringFromJsonString(
							"product_list", data);
					String last_syn_time = JSONParser.getStringFromJsonString(
							"syn_time", data);
					ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) JSONParser
							.JSON2Array(product_list, ProductInfo.class);
					updateProductDatabase(products, last_syn_time);
				}else{
					handler.sendEmptyMessage(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(0);
			}
		}
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			handler.sendEmptyMessage(0);
		};
	};
	
	// 增量更新数据库
		private void updateProductDatabase(final ArrayList<ProductInfo> products,
				final String last_syn_time) {
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						if (products != null && products.size() > 0) {
							// productDao =
							// ormOpearationDao.getDao(ProductInfo.class);
							// 第一次更新数据，全部更新
							if (!BaseUtils.IsNotEmpty(sp.getSValue(
									ApplicationCS.LAST_SYN_TIME, ""))) {
								LG.i(ApplicationCS.class, "收银系统启动------>2：数据全部更新");
								productDao
										.executeRawNoArgs("delete from productInfosTable");// 删除所有数据
								for (ProductInfo entity : products) {
									productDao.create(entity);
								}
								handler.sendEmptyMessage(1);
							} else {
								LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据");
								String product_ids = "";
								for (ProductInfo entity : products) {
									productDao.createOrUpdate(entity);
									product_ids += entity.op_id + ",";
								}
								Message msg = new Message();
								msg.what = 2;
								msg.obj = product_ids;
								handler.sendMessage(msg);
							}
							sp.setValue(ApplicationCS.LAST_SYN_TIME, last_syn_time);
							LG.i(getClass(), "最后更新时间 －－－－－－－－－－ >" + last_syn_time);
						}
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(0);
					}
				}
			}.start();

		}
		
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				// 成功
				if (msg.what == 1) {
					Toast.makeText(getApplicationContext(), "数据同步成功",
							Toast.LENGTH_LONG).show();
					C.closeProgressDialog();
				}
				// 失败
				else if (msg.what == 0) {
					Toast.makeText(getApplicationContext(), "数据同步失败，可进入设置中手动更新",
							Toast.LENGTH_LONG).show();
					C.closeProgressDialog();
				}
				// 调用同步信息接口
				else if (msg.what == 2) {
					LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据结束调用回调");
					ConnectManager.getInstance().productSynCallback(
							(String) msg.obj, ajaxCallBack);
				}
			};
		};

		AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				LG.i(getClass(), "产品同步回调---->" + t);
				if (BaseUtils.IsNotEmpty(t)
						&& JSONParser.getStringFromJsonString("status", t).equals(
								"1")) {
					String data = JSONParser.getStringFromJsonString("data", t);
					SYN_RESULT = JSONParser.getStringFromJsonString("result", data);
					LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据结束调用回调result->"
							+ SYN_RESULT);
					if (SYN_RESULT.equals("ok")) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(0);
					}
				}
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				handler.sendEmptyMessage(0);
			};
		};
}
