package com.shengxun.util;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;

import com.shengxun.cashiersystem.R;
import com.shengxun.cashiersystem.TipUpdateVersionAtivity;
import com.shengxun.constant.C;
import com.shengxun.entity.AppVersion;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
/**
 * 模块描述：软件版本更新管理
 * 2014-10-15 下午5:35:24
 * Write by LILIN
 */
public class CheckVersionManager {
	
	/**
	 * 上下文
	 */
	private static Context mContext=null;
	
	/**
	 * 是否显示检查更新中对话框
	 */
	private static boolean isLoading=false;
	/**
	 * @param mContext   上下文
	 * @param isLoading  是否需要加装对话框
	 */
	public static void checkVersion(Context mContext,boolean isLoading) {
		CheckVersionManager.mContext=mContext;
		CheckVersionManager.isLoading=isLoading;
		ConnectManager.getInstance().getAppUpdateInfo(checkVersionAjaxCallBack);
		if(isLoading){
			C.openProgressDialog(mContext, onKeyListener, R.string.checkVersion);
		}
	}
	private static OnKeyListener onKeyListener = new OnKeyListener()
	{
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
		{
			C.closeProgressDialog();
			return false;
		}
	};
	private static AjaxCallBack<String> checkVersionAjaxCallBack=new AjaxCallBack<String>() {
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			C.closeProgressDialog();
			LG.e(getClass(),"版本更新－－－－－>"+t);
			try{
			if(BaseUtils.IsNotEmpty(t)&&JSONParser.getStringFromJsonString("status", t).equals("1")){
				String data=JSONParser.getStringFromJsonString("data", t);
				String app_version=JSONParser.getStringFromJsonString("app_version", data);
				if(BaseUtils.IsNotEmpty(app_version)){
					AppVersion appVersion=(AppVersion) JSONParser.JSON2Object(app_version, AppVersion.class);
					PackageManager packageManager = mContext.getPackageManager();
	                // getPackageName()是当前类的包名，0代表是获取版本信息
	                PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
	                //String versionName = packInfo.versionName;
	                //当前软件版本
	                int versionCode = packInfo.versionCode;
	                //服务器版本
	                int updateVersionCode = Integer.parseInt(appVersion.version_num);
	                if(versionCode < updateVersionCode)//有版本更新
	                {
	                	Intent intent = new Intent(mContext,TipUpdateVersionAtivity.class);
	                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                	intent.putExtra("function",appVersion.version_content);
	                	//intent.putExtra("must_update", appVersion.must_update);
	                	intent.putExtra("download_url", appVersion.down_url);
	                	mContext.startActivity(intent);
	                }
	                else
	                {
	                	if(isLoading){
	                		C.showShort(mContext.getString(R.string.noFindNewVersion), mContext);
	                	}
	                }
				}
			}else{
				String alert=JSONParser.getStringFromJsonString("error_desc", t)+"";
				if(isLoading){
					if(BaseUtils.IsNotEmpty(alert)){
						C.showShort(alert+"",mContext);
					}else{
						C.showShort("更新失败!",mContext);
					}
				}
			}
			}catch(Exception e){
			e.printStackTrace();
		}
		}
		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			LG.e(getClass(), t+"-"+errorNo+"-"+strMsg);
		}
	   };
}
