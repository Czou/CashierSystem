package com.shengxun.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by Savion on 2015/7/2.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    public DBHelper(Context context,String name,int version){
        super(context,name,null,version);
        Log.e("savion","DBHelper-------");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        Log.e("savion","DBHelper onCreate---------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        Log.e("savion","DBHelper onUpgrade---------");
    }

    @Override
    public void close() {
        super.close();
        Log.e("savion","DBHelper close---------");
    }
}
