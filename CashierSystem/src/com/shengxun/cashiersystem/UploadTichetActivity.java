package com.shengxun.cashiersystem;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.constant.C;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class UploadTichetActivity extends MyTimeLockBaseActivity {

	/**
	 * 输入框
	 */
	private EditText ticket_number, ticket_money, ticket_card_num;
	/**
	 * 按钮
	 */
	private Button btn_upload_ok, btn_upload_back;
	/**
	 * 付款方式1.现金(目前只支持现金)　2.信用卡　3.储蓄卡　4.储值卡 默认１
	 */
	private int pay_way = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cashier_upload_ticket_view);
	}

	/**
	 * 初始化控件
	 * 
	 * @auth shouwei
	 */
	private void initWidget() {
		ticket_card_num = (EditText) findViewById(R.id.cashier_upload_ticket_card_num);
		ticket_money = (EditText) findViewById(R.id.cashier_upload_ticket_money);
		ticket_number = (EditText) findViewById(R.id.cashier_upload_ticket_num);
		btn_upload_back = (Button) findViewById(R.id.cashier_upload_ticket_back);
		btn_upload_ok = (Button) findViewById(R.id.cashier_upload_ticket_ok);
		btn_upload_back.setOnClickListener(myclick);
		btn_upload_ok.setOnClickListener(myclick);
	}

	private void uploadTicket() {
		String str_card_num = ticket_card_num.getText().toString().trim();
		String str_ticket_num = ticket_number.getText().toString().trim();
		String str_ticket_money = ticket_money.getText().toString().trim();
		if (!BaseUtils.IsNotEmpty(str_card_num)) {
			C.showDialogAlert("消费人卡号不能为空", mActivity);
			return;
		}
		if (BaseUtils.IsNotEmpty(str_ticket_num)) {
			C.showDialogAlert("小票号不能为空", mActivity);
			return;
		}
		if (BaseUtils.IsNotEmpty(str_ticket_money)) {
			C.showDialogAlert("小票金额不能为空", mActivity);
			return;
		}
		if (!BaseUtils.isNumber(str_ticket_money)) {
			C.showDialogAlert("小票金额格式错误", mActivity);
			return;
		}

		if (BaseUtils.isNetworkAvailable(mActivity)) {
			C.openProgressDialog(mActivity, null, "正在上传...");
			ConnectManager.getInstance().uploadTicketCallback(str_card_num,
					applicationCS.cashier_card_no, str_ticket_num,
					str_ticket_money, pay_way + "", uploadCallBack);
		} else {
			C.showDialogAlert("当前网络不可用", mActivity);
		}

	}

	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 返回
			case R.id.cashier_upload_ticket_back:
				AppManager.getAppManager().finishActivity(
						UploadTichetActivity.this);
				break;
			// 上传
			case R.id.cashier_upload_ticket_ok:
				uploadTicket();
				break;
			default:
				break;
			}
		}
	};

	AjaxCallBack<String> uploadCallBack = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			C.closeProgressDialog();
			LG.i(getClass(), "上传返回信息--->"+t);
			if(JSONParser.getStringFromJsonString("status", t).equals("1")){
				C.showDialogAlert("上传成功,3秒后将自动关闭此窗口", mActivity);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						AppManager.getAppManager().finishActivity(
								UploadTichetActivity.this);
					}
				}, 3000);
				
			}else if(JSONParser.getStringFromJsonString("status", t).equals("0")){
				String msg = JSONParser.getStringFromJsonString("error_desc", t);
				if(BaseUtils.IsNotEmpty(msg)){
					C.showDialogAlert(msg, mActivity);
				}else{
					C.showDialogAlert("上传失败", mActivity);
				}
			}else{
				C.showDialogAlert("上传失败", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			C.showDialogAlert("上传失败", mActivity);
		};
	};

}
