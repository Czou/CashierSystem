package com.shengxun.cashiersystem;

import java.sql.SQLException;
import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseConnection;
import com.shengxun.cashiersystem.app.ApplicationCS;
import com.shengxun.constant.C;
import com.shengxun.entity.LoginInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.service.BackgroundService;
import com.shengxun.util.AndroidAdjustResizeUtil;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

/**
 * 模块描述：收银系统登录界面 2015-4-14 下午12:21:52 Write by LILIN
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity {

	private EditText user_name = null;
	private EditText user_password = null;
	private TextView user_login = null;
	private TextView user_reset = null;
	private String database_path;
	/**
	 * 商品同步回调结果
	 */
	public String SYN_RESULT;
	/**
	 * 开始更新时间　
	 */
	private long startTime;
	/**
	 * 商品数据库操作dao
	 */
	public static Dao<ProductInfo, Integer> productDao;
	SQLiteDatabase database;
	// true为同步，false为不同步
	public static boolean isLoadingData = true;
	private static boolean isLoginIn = true;
	private ArrayList<ProductInfo> ppp;

	/**
	 * 提供给系统退出时使用，若是退出的则不会再进行数据同步
	 * 
	 * @param isLogin
	 * @auth shouwei
	 */
	public static void setIsLoginIn(boolean isLogin) {
		isLoginIn = isLogin;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		AndroidAdjustResizeUtil.assistActivity(mActivity);
		user_name = (EditText) this.findViewById(R.id.user_name);
		user_password = (EditText) this.findViewById(R.id.user_password);
		user_login = (TextView) this.findViewById(R.id.user_login);
		user_reset = (TextView) this.findViewById(R.id.user_reset);
		user_reset.setOnClickListener(onClickListener);
		user_login.setOnClickListener(onClickListener);
		
		
		// // 测试使用账号,发布时请注释
		user_name.setText("T00010088");
		user_password.setText("532614");
		// 获取数据库地址
		database_path = getDatabasePath(C.DATABASE_NAME).getAbsolutePath();
		if (isLoginIn) {
			startTime = System.currentTimeMillis();
			C.openProgressDialog(mActivity, null, "正在同步数据信息，请耐心等待...");
			// 启动服务更新区域地址信息
			//registerBroad();
			BackgroundService.openService(mActivity, ormOpearationDao);
			// 获取商品数据
			productDao = ormOpearationDao.getDao(ProductInfo.class);

			if (BaseUtils.isNetworkAvailable(mActivity)) {
				ConnectManager.getInstance().getProductList(
						sp.getSValue(applicationCS.LAST_SYN_TIME, ""),
						productAjaxCallBack);
			} else {
				C.closeProgressDialog();
				C.showDialogAlert("当前网络不可用", mActivity);
			}
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 确认登录
			case R.id.user_login: {
				String str_user_name = user_name.getText().toString();
				String str_user_password = user_password.getText().toString();
				if (BaseUtils.IsNotEmpty(str_user_name)
						&& BaseUtils.IsNotEmpty(str_user_password)) {
					applicationCS.cashier_card_no = str_user_name;
					String login_code = C.getDesStr(str_user_name + "#"
							+ str_user_password, C.DES_KEY);
					if (BaseUtils.isNetworkAvailable(mActivity)) {
						C.openProgressDialog(mActivity, null, "正在登录，请耐心等待...");
						ConnectManager.getInstance().getLoginResult(login_code,
								loginAjaxCallBack);
					} else {
						C.showDialogAlert("当前网络不可用", mActivity);
						C.closeProgressDialog();
					}
				} else {
					C.showDialogAlert(
							""
									+ resources
											.getString(R.string.cashier_system_alert_no_login),
							mActivity);
				}
			}
				break;
			// 重置
			case R.id.user_reset: {
				user_name.setText("");
				user_password.setText("");
			}
				break;
			}
		}
	};
	// 登录回调
	AjaxCallBack<String> loginAjaxCallBack = new AjaxCallBack<String>() {

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				applicationCS.loginInfo = (LoginInfo) JSONParser.JSON2Object(
						data, LoginInfo.class);
				if (applicationCS.loginInfo != null
						&& applicationCS.loginInfo.cashier_info != null) {
					if (applicationCS.loginInfo.cashier_info.c_status == 1) {
						applicationCS.mc_id = applicationCS.loginInfo.mc_id;

						// 写入锁屏密码
						String lockPsd = user_password.getText().toString()
								.trim();
						C.CURRENT_LOCK_PSD = lockPsd;
						sp.setValue(C.SHARED_LOCK_PSD, lockPsd);

						goActivity(MainActivity.class);
						AppManager.getAppManager().finishActivity(mActivity);
					} else {
						C.showDialogAlert(
								""
										+ resources
												.getString(R.string.cashier_system_alert_login_no_permissions),
								mActivity);
					}
				}

			} else {
				String error_msg = JSONParser.getStringFromJsonString(
						"error_desc", t);
				if (BaseUtils.IsNotEmpty(error_msg)) {
					C.showDialogAlert(error_msg, mActivity);
				} else {
					C.showDialogAlert(
							""
									+ resources
											.getString(R.string.cashier_system_alert_login_fail),
							mActivity);
				}
			}
			C.closeProgressDialog();
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showDialogAlert(
					""
							+ resources
									.getString(R.string.cashier_system_alert_login_fail),
					mActivity);
			C.closeProgressDialog();
		}
	};

	DataBackMessage dbm = null;// 广播

	/**
	 * 注册广播
	 */
	private void registerBroad() {
		IntentFilter intentFilter = new IntentFilter(
				BackgroundService.ACTION_DATA_STATUS);
		dbm = new DataBackMessage();
		registerReceiver(dbm, intentFilter);
	}

	private void unRegisterBroad() {
		if (null != dbm)
			unregisterReceiver(dbm);
	}

	/**
	 * 接受广播里数据写入成功没
	 * 
	 */
	private class DataBackMessage extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int code = intent.getIntExtra(BackgroundService.KEY_CODE, 0);
			long consumeTime = System.currentTimeMillis() - startTime;
			if (1 == code) {
				LG.e(getClass(), "数据库更新时间  总共=====> " + consumeTime);
				unRegisterBroad();
				BackgroundService.closeService();
			} else if (0 == code) {
				LG.e(getClass(), "数据库更新失败  总共=====> " + consumeTime);
				unRegisterBroad();
				BackgroundService.closeService();
			}
		}

	}

	private AjaxCallBack<String> productAjaxCallBack = new AjaxCallBack<String>() {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "更新商品------->" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				String product_list = JSONParser.getStringFromJsonString(
						"product_list", data);
				String last_syn_time = JSONParser.getStringFromJsonString(
						"syn_time", data);
				ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) JSONParser
						.JSON2Array(product_list, ProductInfo.class);
				if (products != null && products.size() > 0) {
					updateProductDatabase(products, last_syn_time);
				} else {
					handler.sendEmptyMessage(3);
				}
			}else if(JSONParser.getStringFromJsonString("status", t).equals("0")){
				String msg = JSONParser.getStringFromJsonString("error_desc", t);
				if(BaseUtils.IsNotEmpty(msg)){
					C.showDialogAlert(msg, mActivity);
				}else{
					handler.sendEmptyMessage(0);
				}
			} else {
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
//					database = SQLiteDatabase.openOrCreateDatabase(
//							database_path, null);
					// 第一次更新数据，全部更新
					if (!BaseUtils.IsNotEmpty(sp.getSValue(
							ApplicationCS.LAST_SYN_TIME, ""))) {
						
						AndroidDatabaseConnection db = new AndroidDatabaseConnection(
								ormOpearationDao.openDataHelper()
										.getReadableDatabase(), true);
						db.setAutoCommit(false);
						LG.i(ApplicationCS.class, "收银系统启动------>2：数据全部更新");
						 productDao.executeRawNoArgs("delete from productInfosTable");//
						 //删除所有数据
						 for (ProductInfo entity : products) {
							 productDao.create(entity);
						 }
						 db.commit(null);
//						database.beginTransaction();
//						database.execSQL("delete from productInfosTable");
//						for (int i = 0; i < products.size(); i++) {
//							ProductInfo entity = products.get(i);
//							database.execSQL("insert into productInfosTable (op_id,zqp_id,op_market_price,op_promote_market_price,op_promote_number,op_promote_start_date,op_promote_end_date,op_is_promote,op_number,op_status,op_is_for_show,qp_name,qy_id,op_bar_code,img_url) values ("
//									+ entity.op_id
//									+ ",'"
//									+ entity.zqp_id
//									+ "',"
//									+ entity.op_market_price
//									+ ","
//									+ entity.op_promote_market_price
//									+ ","
//									+ entity.op_promote_number
//									+ ",'"
//									+ entity.op_promote_start_date
//									+ "','"
//									+ entity.op_promote_end_date
//									+ "',"
//									+ entity.op_is_promote
//									+ ","
//									+ entity.op_number
//									+ ","
//									+ entity.op_status
//									+ ","
//									+ entity.op_is_for_show
//									+ ",'"
//									+ entity.qp_name
//									+ "',"
//									+ entity.qy_id
//									+ ",'"
//									+ entity.op_bar_code
//									+ "','"
//									+ entity.img_url + "')");
//						}
//						database.setTransactionSuccessful();
						handler.sendEmptyMessage(1);
					} else {
						LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据");
						String product_ids = "";
						AndroidDatabaseConnection db = new AndroidDatabaseConnection(
								ormOpearationDao.openDataHelper()
										.getReadableDatabase(), true);
						db.setAutoCommit(false);
						 for (ProductInfo entity : products) {
							 productDao.createOrUpdate(entity);
							 product_ids += entity.op_id + ",";
						 }
						 db.commit(null);
						 
//						database.beginTransaction();
//						for (int i = 0; i < products.size(); i++) {
//							ProductInfo entity = products.get(i);
//							product_ids += entity.op_id + ",";
//							database.execSQL("replace into productInfosTable (op_id,zqp_id,op_market_price,op_promote_market_price,op_promote_number,op_promote_start_date,op_promote_end_date,op_is_promote,op_number,op_status,op_is_for_show,qp_name,qy_id,op_bar_code,img_url) values ("
//									+ entity.op_id
//									+ ",'"
//									+ entity.zqp_id
//									+ "',"
//									+ entity.op_market_price
//									+ ","
//									+ entity.op_promote_market_price
//									+ ","
//									+ entity.op_promote_number
//									+ ",'"
//									+ entity.op_promote_start_date
//									+ "','"
//									+ entity.op_promote_end_date
//									+ "',"
//									+ entity.op_is_promote
//									+ ","
//									+ entity.op_number
//									+ ","
//									+ entity.op_status
//									+ ","
//									+ entity.op_is_for_show
//									+ ",'"
//									+ entity.qp_name
//									+ "',"
//									+ entity.qy_id
//									+ ",'"
//									+ entity.op_bar_code
//									+ "','"
//									+ entity.img_url + "')");
//						}
//						database.setTransactionSuccessful();
						Message msg = new Message();
						msg.what = 2;
						msg.obj = product_ids;
						handler.sendMessage(msg);
					}
					sp.setValue(ApplicationCS.LAST_SYN_TIME, last_syn_time);
					LG.i(getClass(), "最后更新时间 －－－－－－－－－－ >" + last_syn_time);
				} catch (Exception e) {
					// TODO: handle exception
					handler.sendEmptyMessage(0);
				} finally {
					//database.endTransaction();
					//database.close();
				}
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 成功
			if (msg.what == 1) {
				long loadTime = System.currentTimeMillis() - startTime;
				LG.e(getClass(), "商品更新总时间：" + loadTime);
				Toast.makeText(
						getApplicationContext(),
						resources
								.getString(R.string.cashier_system_alert_syn_product_succeed),
						Toast.LENGTH_LONG).show();
				C.closeProgressDialog();
			}
			// 失败
			else if (msg.what == 0) {
				C.showDialogAlert(
						resources
								.getString(R.string.cashier_system_alert_syn_product_falied),
						mActivity);
				C.closeProgressDialog();
			}
			// 调用同步信息接口
			else if (msg.what == 2) {
				LG.i(ApplicationCS.class, "收银系统启动------>2：增量更新数据结束调用回调");
				ConnectManager.getInstance().productSynCallback(
						(String) msg.obj, ajaxCallBack);
			}
			// 数据为空时
			else if (msg.what == 3) {
				C.showDialogAlert("暂无商品", mActivity);
				C.closeProgressDialog();
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
