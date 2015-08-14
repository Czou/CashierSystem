package com.shengxun.cashiersystem.app;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
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
		C.MACHINE_CODE ="35703a430a4e23ce54:e4:bd:8b:cf:ff";// DeviceID.getDeviceID(this);//"35703a430a4e23ce54:e4:bd:8b:cf:ff";//DeviceID.getDeviceID(this);
		Log.i("savion","mark == "+DeviceID.getDeviceID(this));
		C.VERIFY_CODE = MD5Util.GetMD5Code("" + C.SOB_CODE + "#"
				+ C.SOB_PASSWORD + "#" + C.MACHINE_CODE + "");

		LG.e(ApplicationCS.class, "========" + DeviceID.getDeviceID(this));
		LG.i(ApplicationCS.class, "收银系统启动------>1:获取联网最新产品信息");
		sp = new DataSP(this, C.SHARED_PREFENCE_NAME);
	}
}
