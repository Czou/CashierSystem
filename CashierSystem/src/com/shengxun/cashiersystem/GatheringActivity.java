package com.shengxun.cashiersystem;

import com.zvezda.android.utils.BaseUtils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 收款界面
 * 
 * @author sw
 * @date 2015-4-24
 */
public class GatheringActivity extends BaseActivity {
	/**
	 * 返回按钮
	 */
	TextView gathering_back;
	/**
	 * 总额，现金，找零输入框
	 */
	EditText gathering_total_money, gathering_cash, gathering_change;
	/**
	 * 保存总额,现金，找零数据
	 */
	private static String total_momey = "20", cash = "", change = "";

	/**
	 * 所有按钮
	 */
	private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
			btn_8, btn_9, btn_50, btn_100, btn_200, btn_300, btn_00, btn_spot,
			btn_back_up, btn_ok;

	/**
	 * 设置总额
	 * 
	 * @param str
	 * @auth sw
	 */
	public static void setTotalmoney(String str) {
		total_momey = str;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_gathering_view);

		initWidget();

	}

	/**
	 * 初始化view sw
	 */
	private void initWidget() {
		gathering_back = (TextView) findViewById(R.id.cashier_gathering_back);
		gathering_total_money = (EditText) findViewById(R.id.cashier_gathering_total_money);
		gathering_cash = (EditText) findViewById(R.id.cashier_gathering_cash);
		gathering_change = (EditText) findViewById(R.id.cashier_gathering_change);
		btn_0 = (Button) findViewById(R.id.gathering_btn_0);
		btn_1 = (Button) findViewById(R.id.gathering_btn_1);
		btn_2 = (Button) findViewById(R.id.gathering_btn_2);
		btn_3 = (Button) findViewById(R.id.gathering_btn_3);
		btn_4 = (Button) findViewById(R.id.gathering_btn_4);
		btn_5 = (Button) findViewById(R.id.gathering_btn_5);
		btn_6 = (Button) findViewById(R.id.gathering_btn_6);
		btn_7 = (Button) findViewById(R.id.gathering_btn_7);
		btn_8 = (Button) findViewById(R.id.gathering_btn_8);
		btn_9 = (Button) findViewById(R.id.gathering_btn_9);
		btn_50 = (Button) findViewById(R.id.gathering_btn_50);
		btn_100 = (Button) findViewById(R.id.gathering_btn_100);
		btn_200 = (Button) findViewById(R.id.gathering_btn_200);
		btn_300 = (Button) findViewById(R.id.gathering_btn_300);
		btn_00 = (Button) findViewById(R.id.gathering_btn_00);
		btn_spot = (Button) findViewById(R.id.gathering_btn_spot);
		btn_back_up = (Button) findViewById(R.id.gathering_btn_backup);
		btn_ok = (Button) findViewById(R.id.gathering_btn_ok);

		gathering_back.setOnClickListener(myclick);
		gathering_cash.addTextChangedListener(mytextchange);
		btn_0.setOnClickListener(myclick);
		btn_1.setOnClickListener(myclick);
		btn_2.setOnClickListener(myclick);
		btn_3.setOnClickListener(myclick);
		btn_4.setOnClickListener(myclick);
		btn_5.setOnClickListener(myclick);
		btn_6.setOnClickListener(myclick);
		btn_7.setOnClickListener(myclick);
		btn_8.setOnClickListener(myclick);
		btn_9.setOnClickListener(myclick);
		btn_50.setOnClickListener(myclick);
		btn_100.setOnClickListener(myclick);
		btn_200.setOnClickListener(myclick);
		btn_300.setOnClickListener(myclick);
		btn_spot.setOnClickListener(myclick);
		btn_00.setOnClickListener(myclick);
		btn_back_up.setOnClickListener(myclick);
		btn_ok.setOnClickListener(myclick);

		gathering_total_money.setText(total_momey);

	}

	/**
	 * 点击事件
	 */
	private OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.cashier_gathering_back:
				finish();
				break;
			case R.id.gathering_btn_0:
				cash = cash + btn_0.getText().toString().trim();
				break;
			case R.id.gathering_btn_1:
				cash = cash + btn_1.getText().toString().trim();
				break;
			case R.id.gathering_btn_2:
				cash = cash + btn_2.getText().toString().trim();
				break;
			case R.id.gathering_btn_3:
				cash = cash + btn_3.getText().toString().trim();
				break;
			case R.id.gathering_btn_4:
				cash = cash + btn_4.getText().toString().trim();
				break;
			case R.id.gathering_btn_5:
				cash = cash + btn_5.getText().toString().trim();
				break;
			case R.id.gathering_btn_6:
				cash = cash + btn_6.getText().toString().trim();
				break;
			case R.id.gathering_btn_7:
				cash = cash + btn_7.getText().toString().trim();
				break;
			case R.id.gathering_btn_8:
				cash = cash + btn_8.getText().toString().trim();
				break;
			case R.id.gathering_btn_9:
				cash = cash + btn_9.getText().toString().trim();
				break;
			case R.id.gathering_btn_50:
				cash = cash + btn_50.getText().toString().trim();
				break;
			case R.id.gathering_btn_100:
				cash = cash + btn_100.getText().toString().trim();
				break;
			case R.id.gathering_btn_200:
				cash = cash + btn_200.getText().toString().trim();
				break;
			case R.id.gathering_btn_300:
				cash = cash + btn_300.getText().toString().trim();
				break;
			case R.id.gathering_btn_00:
				cash = cash + btn_00.getText().toString().trim();
				break;
			case R.id.gathering_btn_spot:
				cash = cash + btn_spot.getText().toString().trim();
				break;
			case R.id.gathering_btn_backup:
				if (cash != null && cash.length() > 0) {
					cash = cash.substring(0, cash.length() - 1);
				}
				break;
			case R.id.gathering_btn_ok:
				break;
			default:
				break;
			}
		}
	};

	TextWatcher mytextchange = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			gathering_cash.setText(cash);
			if (BaseUtils.IsNotEmpty(cash)) {
				gathering_change.setText(Double.parseDouble(cash)
						- Double.parseDouble(total_momey) + "");
			} else {
				gathering_change.setText("0");
			}
		}
	};

}
