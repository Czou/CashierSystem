package com.shengxun.service;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.shengxun.cashiersystem.app.ApplicationCS;
import com.shengxun.constant.C;
import com.shengxun.entity.AreaInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
import com.zvezda.database.utils.ORMOpearationDao;

/**
 * 模块描述： 后台服务，写入区域地理信息，启动检测有无更新的版本 2015-5-18 下午2:46:49 Write by LILIN
 */
@SuppressLint("HandlerLeak")
public class BackgroundService extends Service {
	/**
	 * 数据加载到哪一步
	 */
	public static final String ACTION_DATA_STATUS = "com.shengxun.service.datastatus";
	public static final String KEY_CODE = "code";
	public static final String KEY_MESSAGE = "message";
	private static BackgroundService bs = null;
	private static Context context = null;
	/**
	 * ORM数据库操作封装
	 */
	protected static ORMOpearationDao ormOpearationDao = null;

	private Thread updateThread = null;

	/**
	 * 打开服务 并获取信息
	 * 
	 * @param context
	 */
	public static void openService(Context context, ORMOpearationDao ormDao) {
		// 避免重复打开
		if (bs == null) {
			BackgroundService.context = context;
			// ormOpearationDao=new ORMOpearationDao(context,C.DATABASE_NAME);
			if (ormDao != null) {
				ormOpearationDao = ormDao;
			} else {
				ormOpearationDao = new ORMOpearationDao(context,
						C.DATABASE_NAME);
			}
			Intent it = new Intent();
			// 显示意图
			it.setClass(context, BackgroundService.class);
			// 隐式意图
			// it.setAction(SACTION);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(it);

		}
	}

	/**
	 * 关闭服务
	 */
	public static void closeService() {
		if (bs != null) {
			if (BackgroundService.context == null)
				BackgroundService.context = bs.getApplicationContext();
			Intent it = new Intent();
			it.setClass(BackgroundService.context, BackgroundService.class);
			// it.setAction(SACTION);
			bs.stopService(it);
			BackgroundService.context = null;
			bs = null;
		}
	}

	/**
	 * 发送广播
	 * 
	 * @param m
	 */
	private void sendBroad(int m) {
		Intent intent = new Intent();
		intent.setAction(ACTION_DATA_STATUS);
		intent.putExtra(KEY_CODE, m);
		switch (m) {
		case 1:
			intent.putExtra(KEY_MESSAGE, "最新的地理区域信息写入数据库更新完成!");
			LG.i(getClass(), "最新的地理区域信息写入数据库更新完成!");
			break;
		}
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LG.i(getClass(), "开启后台服务-------->");
		if (null == bs)
			bs = BackgroundService.this;
		try {
			ConnectManager.getInstance()
					.getAreaResult("", "", areaAjaxCallBack);
		} catch (Exception e) {
			LG.e(getClass(),
					getClass().getSimpleName() + "后台服务初始化数据异常--->"
							+ e.toString());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LG.i(getClass(), "关闭后台服务-------->");
		BackgroundService.context = null;
		bs = null;
	}

	public void updateData(final String t) {
		updateThread = new Thread(new Runnable() {
			@Override
			public void run() {

				String data = JSONParser.getStringFromJsonString("data", t);
				String areasStr = JSONParser.getStringFromJsonString(
						"area_list", data);
				@SuppressWarnings("unchecked")
				ArrayList<AreaInfo> areas = (ArrayList<AreaInfo>) JSONParser
						.JSON2Array(areasStr, AreaInfo.class);
				if (areas == null || areas.size() <= 0) {
					C.showShort("区域数据为空", context);
					return;
				}
				try {
					AndroidDatabaseConnection db = new AndroidDatabaseConnection(
							ormOpearationDao.openDataHelper()
									.getReadableDatabase(), true);
					Dao<AreaInfo, Integer> areaDao = ormOpearationDao
							.getDao(AreaInfo.class);
					db.setAutoCommit(false);
					// 只写
					// areaDao.executeRawNoArgs("DELETE FROM areaInfoTable");//删除所有数据
					for (AreaInfo ai : areas) {
						areaDao.createIfNotExists(ai);
					}
					LG.e(getClass(), "同步区域信息完成-------");
					db.commit(null);
					updateThread.interrupt();
				} catch (SQLException e) {
					e.printStackTrace();
					C.showLong("写入区域信息失败", context);
				} 
				BackgroundService.closeService();
			}
		});
		updateThread.start();
	}

	private AjaxCallBack<String> areaAjaxCallBack = new AjaxCallBack<String>() {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			//LG.i(ApplicationCS.class, "收银系统启动------>3：将最新的地理区域信息写入数据库" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				// 将josn解析放入到线程中去,这样不会堵塞ui线程
				updateData(t);
				// 广播发送(废弃)
				// sendBroad(1);
			} else {
				// sendBroad(0);
				C.showShort(
						JSONParser.getStringFromJsonString("error_desc", t),
						context);
			}
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			// sendBroad(0);
			C.showShort("区域信息获取失败", context);
			ormOpearationDao.closeDataHelper();
		}

	};
}
