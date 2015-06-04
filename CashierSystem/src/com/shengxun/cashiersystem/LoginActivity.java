package com.shengxun.cashiersystem;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.constant.C;
import com.shengxun.entity.LoginInfo;
import com.shengxun.service.BackgroundService;
import com.shengxun.util.AndroidAdjustResizeUtil;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

/**
 * 模块描述：收银系统登录界面
 * 2015-4-14 下午12:21:52
 * Write by LILIN
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity{

	private EditText user_name=null;
	private EditText user_password=null;
	
	private TextView user_login=null;
	private TextView user_reset=null;
	
	private long startTime;
	
	public static boolean isLoadingData=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		AndroidAdjustResizeUtil.assistActivity(mActivity);
		user_name=(EditText) this.findViewById(R.id.user_name);
		user_password=(EditText) this.findViewById(R.id.user_password);
		user_login=(TextView) this.findViewById(R.id.user_login);
		user_reset=(TextView) this.findViewById(R.id.user_reset);
		user_reset.setOnClickListener(onClickListener);
		user_login.setOnClickListener(onClickListener);
		
		//测试使用账号
		user_name.setText("T00010088");
		user_password.setText("532614");
		if(isLoadingData){
			startTime = System.currentTimeMillis();
			//启动服务更新
			C.openProgressDialog(mActivity, null, "正在同步数据信息，请耐心等待...");
			registerBroad();
			BackgroundService.openService(mActivity);
		}
	}
	
	
	private OnClickListener onClickListener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
				//确认登录
			case R.id.user_login:
				{
					String str_user_name=user_name.getText().toString();
					String str_user_password=user_password.getText().toString();
					if(BaseUtils.IsNotEmpty(str_user_name)
					  &&BaseUtils.IsNotEmpty(str_user_password)){
						applicationCS.cashier_card_no=str_user_name;
						String login_code=C.getDesStr(str_user_name+"#"+str_user_password, C.DES_KEY);
						ConnectManager.getInstance().getLoginResult(login_code, loginAjaxCallBack);
						C.openProgressDialog(mActivity, null, "正在登录，请耐心等待...");
							
					}else{
						C.showDialogAlert(""+resources.getString(R.string.cashier_system_alert_no_login), mActivity);
					}
				}
				break;
				//重置
			case R.id.user_reset:
				{
					user_name.setText("");
					user_password.setText("");
				}
				break;
			}
		}
		};
		//登录回调
		AjaxCallBack<String> loginAjaxCallBack=new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if(BaseUtils.IsNotEmpty(t)&&JSONParser.getStringFromJsonString("status", t).equals("1")){
					String data=JSONParser.getStringFromJsonString("data", t);
					applicationCS.loginInfo=(LoginInfo) JSONParser.JSON2Object(data, LoginInfo.class);
					if(applicationCS.loginInfo!=null&&applicationCS.loginInfo.cashier_info!=null){
						if(applicationCS.loginInfo.cashier_info.c_status==1){
							applicationCS.mc_id=applicationCS.loginInfo.mc_id;
							goActivity(MainActivity.class);
							AppManager.getAppManager().finishActivity(mActivity);
						}else{
							C.showDialogAlert(""+resources.getString(R.string.cashier_system_alert_login_no_permissions), mActivity);

						}
					}
					
				}else{
					C.showDialogAlert(""+resources.getString(R.string.cashier_system_alert_login_fail), mActivity);
				}
				C.closeProgressDialog();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				C.showDialogAlert(""+resources.getString(R.string.cashier_system_alert_login_fail), mActivity);
				C.closeProgressDialog();
			}
			
		};
		
		
		
		DataBackMessage dbm = null;//广播
		/**
		 * 注册广播
		 */
		private void registerBroad(){
			IntentFilter intentFilter = new IntentFilter(BackgroundService.ACTION_DATA_STATUS);
			dbm = new DataBackMessage();
			registerReceiver(dbm, intentFilter);
		}
		private void unRegisterBroad(){
			if(null != dbm)
				unregisterReceiver(dbm);
		}
		/**
		 * 接受广播里数据写入成功没
		 *
		 */
		private class DataBackMessage extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				int code = intent.getIntExtra(BackgroundService.KEY_CODE, 0);
				long consumeTime = System.currentTimeMillis()-startTime;
				if(1 == code){
					LG.e(getClass(), "数据库更新时间  总共=====> "+consumeTime);
					unRegisterBroad();
					BackgroundService.closeService();
					C.closeProgressDialog();
				}else if(0 == code){
					LG.e(getClass(), "数据库更新失败  总共=====> "+consumeTime);
					unRegisterBroad();
					BackgroundService.closeService();
					C.closeProgressDialog();
				}
			}
			
		}
}
