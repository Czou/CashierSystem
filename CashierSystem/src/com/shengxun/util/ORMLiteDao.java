package com.shengxun.util;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Savion on 2015/7/2.
 */
public class ORMLiteDao {
    //数据库助手
    DBHelper mHelper;
    //上下文
    Context context;
    //数据库名
    final static String DB_NAME="test.db";
    //数据库版本
    final static int VERSION=1;

    DatabaseConnection mDatabaseconnection;

    public ORMLiteDao(Context context){
        this.context = context;
    }
    /**
     * 打开helper
     */
    public void openDataHelper(){
        if(mHelper==null){
            try {
                mHelper = new DBHelper(context,DB_NAME,VERSION);
                mDatabaseconnection = mHelper.getConnectionSource().getReadWriteConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭helper
     */
    public void closeDataHelper(){
        if(mDatabaseconnection!=null){
            try {
                mDatabaseconnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(mHelper!=null){
            mHelper.close();
        }
    }

    /**
     * 获得操作dao
     */
    public <T> Dao<T,Integer> getDao(Class<T> clazz){
        openDataHelper();
        Dao<T,Integer> dao =null;
        try {
            dao = mHelper.getDao(clazz);
            TableUtils.createTableIfNotExists(mHelper.getConnectionSource(),clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }
}
