package com.shengxun.cashiersystem;

import com.zvezda.android.utils.AppManager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends BaseActivity {

	Button btn_back, btn_open, btn_close;

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

		btn_back.setOnClickListener(myclick);
		btn_open.setOnClickListener(myclick);
		btn_close.setOnClickListener(myclick);
	}

	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_setting_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_setting_btn_open:

				break;
			case R.id.cashier_setting_btn_close:

				break;
			default:
				break;
			}
		}
	};

}
