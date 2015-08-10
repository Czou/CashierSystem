//package com.shengxun.util;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.support.ConnectionSource;
//
///**
// * Created by Savion on 2015/7/2.
// */
//public class DBHelper extends OrmLiteSqliteOpenHelper {
//	private static final String DATABASE_NAME = "cashier_system";
//	private final String PRODUCT_TABLE = "productInfosTable";
//	private final String AREA_TABLE = "areaTable";
//	int version = 1;
//
//	public DBHelper(Context context) {
//		super(context, DATABASE_NAME, null, 1);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase sqLiteDatabase,
//			ConnectionSource connectionSource) {
//		Log.e("savion", "DBHelper onCreate---------");
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase sqLiteDatabase,
//			ConnectionSource connectionSource, int i, int i1) {
//		Log.e("savion", "DBHelper onUpgrade---------");
//	}
//
//	@Override
//	public void close() {
//		super.close();
//		Log.e("savion", "DBHelper close---------");
//	}
//}
