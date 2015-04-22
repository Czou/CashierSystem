package com.shengxun.util;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.shengxun.constant.C;
import com.shengxun.constant.U;
/**
 * 连接服务器的连接管理
 * @author LILIN
 * 下午4:23:35
 */
public class ConnectManager {
	private static ConnectManager instance=null;
	private FinalHttp finalHttp;
	public static ConnectManager getInstance(){
		if(instance==null){
			instance=new ConnectManager();
		}
		return instance;
	}
	
	public ConnectManager() {
		finalHttp = new FinalHttp();
		finalHttp.configRequestExecutionRetryCount(2);
	}
	
	
	
	/*****************登录接口 LILIN  Start********************/
	
	/**
	 * @param login_code  des_crypt(cardno#password)
		des加密秘钥051jks~~
		des加密向量 array(0x35, 0x41, 0x43, 0x38, 0x35, 0x30, 0x35, 0x32)
	 * @param ajaxCallBack
	 */
	public void getLoginResult(String login_code,AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		//每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);
		
		params.put("login_code", login_code);
		finalHttp.configCharset("UTF-8");
		finalHttp.get(U.CASHIER_SYSTEM_LOGIN, params, ajaxCallBack);
	}
	/*****************登录接口 LILIN  End ********************/

	
	}