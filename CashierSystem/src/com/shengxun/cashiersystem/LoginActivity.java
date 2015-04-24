package com.shengxun.cashiersystem;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.constant.C;
import com.shengxun.entity.LoginInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;

/**
 * 模块描述：收银系统登录界面
 * 2015-4-14 下午12:21:52
 * Write by LILIN
 */
public class LoginActivity extends BaseActivity{

	private EditText user_name=null;
	private EditText user_password=null;
	
	private TextView user_login=null;
	private TextView user_reset=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		user_name=(EditText) this.findViewById(R.id.user_name);
		user_password=(EditText) this.findViewById(R.id.user_password);
		user_login=(TextView) this.findViewById(R.id.user_login);
		user_reset=(TextView) this.findViewById(R.id.user_reset);
		user_reset.setOnClickListener(onClickListener);
		user_login.setOnClickListener(onClickListener);
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
							
					}else{
						C.showShort(""+resources.getString(R.string.cashier_system_alert_no_login), mActivity);
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
		AjaxCallBack<String> loginAjaxCallBack=new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if(BaseUtils.IsNotEmpty(t)&&JSONParser.getStringFromJsonString("status", t).equals("1")){
					String data=JSONParser.getStringFromJsonString("data", t);
					applicationCS.loginInfo=(LoginInfo) JSONParser.JSON2Object(data, LoginInfo.class);
					if(applicationCS.loginInfo!=null&&applicationCS.loginInfo.cashier_info!=null){
						if(applicationCS.loginInfo.cashier_info.c_status==1){
							goActivity(MainActivity.class);
							AppManager.getAppManager().finishActivity(mActivity);
						}else{
							C.showShort(""+resources.getString(R.string.cashier_system_alert_login_no_permissions), mActivity);

						}
					}
					
				}else{
					C.showShort(""+resources.getString(R.string.cashier_system_alert_login_fail), mActivity);
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				C.showShort(""+resources.getString(R.string.cashier_system_alert_login_fail), mActivity);

			}
			
		};
}
