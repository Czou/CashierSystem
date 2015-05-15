package com.shengxun.cashiersystem;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;

import com.shengxun.externalhardware.led.JBLEDInterface;
import com.zvezda.android.utils.AppManager;

/**
 * 设置界面
 * @author sw
 * @date 2015-5-15
 */
public class SettingActivity extends BaseActivity {

	Button btn_back, btn_open, btn_close;
	
	Switch sw_open;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_setting_view);

		initWidget();
	}

	private void initWidget() {

		btn_back = (Button) findViewById(R.id.cashier_setting_back);
		btn_open = (Button) findViewById(R.id.cashier_setting_btn_open);
		btn_close = (Button) findViewById(R.id.cashier_setting_btn_close);

		btn_open.setOnClickListener(myclick);
		btn_close.setOnClickListener(myclick);
		btn_back.setOnClickListener(myclick);
	}

	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_setting_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			 //打开客显
			case R.id.cashier_setting_btn_open:
				JBLEDInterface.openLed();
				break;
			// 关闭客显
			case R.id.cashier_setting_btn_close:
				JBLEDInterface.closeLed();
				break;
			default:
				break;
			}
		}
	};

}
