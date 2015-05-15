package com.shengxun.util;
import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.BitmapUtils;
import com.zvezda.android.utils.LG;
/**
 * 缓存地址管理
 * @author LILIN
 * 上午11:18:01
 */
public class CacheM {
	/**
	 * 缓存标识地址
	 */
	private final static String CACHE_NAME="_realconvenient";
	/**
	 * 系统图片缓存路径
	 */
	public static String getImageCachePath(Context context){
		String  cachePath=null;
		if (BaseUtils.checkSDCard())
		{
			cachePath = Environment.getExternalStorageDirectory() + "/ImageCachePath"+CACHE_NAME;
		}
		else
		{
			cachePath = Environment.getDataDirectory().getPath() + "/ImageCachePath"+CACHE_NAME;
		}
		File file = new File(cachePath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		return cachePath;
	}
	/**
	 * 系统软件更新缓存路径
	 */
	public static String getDownloadApkCachePath(Context context){

		String appCachePath=null;
		
		String apkDownPath=""+System.currentTimeMillis();
		/**
		 * 内存卡是否可用
		 */
		if(BaseUtils.checkSDCard()){
			appCachePath=Environment.getExternalStorageDirectory()+ "/AppCachePath"+CACHE_NAME+"/"+apkDownPath;
		}else{
			appCachePath = Environment.getDataDirectory().getPath()+ "/AppCachePath"+CACHE_NAME+"/"+apkDownPath;
		}
		File file = new File(appCachePath);
		if(!file.exists())
		{
			file.mkdirs();
		}
		LG.e(CacheM.class, "当前DownloadApkCachePath缓存地址------->"+appCachePath);
		return appCachePath;
	}
	/**
	 * 压缩上传图片
	 * Write By LILIN
	 * 2014-5-14
	 * @param context
	 * @param path
	 * @return
	 */
	public static String getCompressImagePath(Context context,String path){
		try{
			File file=new File(path);
			int size=(int) file.length();
			LG.e(CacheM.class,"压缩前File大小------------->"+size);
			//当图片小于200K就不压缩
			if (size > 200000) {
				String factUpImagePath=BitmapUtils.SaveBitmap(BitmapUtils.readCompressBitmap(path, 480f, 800f),getImageCachePath(context), "mxsj_"+System.currentTimeMillis()+".jpg");
				File file1=new File(factUpImagePath);
				int size1=(int) file1.length();
				LG.e(CacheM.class,"压缩后File大小------------->"+size1);
				return factUpImagePath; 
			}else{
				return path;
			}
		}catch(Exception e){
			e.printStackTrace();
			return path;
		}
	}
}
