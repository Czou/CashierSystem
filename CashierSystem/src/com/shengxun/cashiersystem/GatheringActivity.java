package com.shengxun.cashiersystem;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.shengxun.entity.ProductInfo;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
	private static String cash = "", change = "";

	private double totalMoney = 0;
	/**
	 * 所有按钮
	 */
	private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
			btn_8, btn_9, btn_50, btn_100, btn_200, btn_300, btn_00, btn_spot,
			btn_back_up, btn_ok;
	/**
	 * 接收传递过来的商品列表
	 */
	private ArrayList<ProductInfo> goodsList;
	/**
	 * 记录是否已经有了小数点
	 */
	private boolean hasSpot = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_gathering_view);

		initWidget();
		initWidgetData();
	}

	/**
	 * 初始化view sw
	 */
	private void initWidget() {
		gathering_back = (TextView) findViewById(R.id.cashier_gathering_back);
		gathering_total_money = (EditText) findViewById(R.id.cashier_gathering_total_money);
		gathering_cash = (EditText) findViewById(R.id.cashier_gathering_cash);
		gathering_cash.setText("");
		// 设置不显示输入法
		gathering_cash.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		gathering_cash.setTextIsSelectable(true);
		//设置光标位置
		gathering_cash.setSelection(gathering_cash.length());
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

	}

	/**
	 * 初始化控件显示数据
	 * 
	 * @auth sw
	 */
	private void initWidgetData() {
		// 获得传递商品信息列表
		goodsList = (ArrayList<ProductInfo>) getIntent().getSerializableExtra(
				"DATA");
		if (goodsList == null) {
			return;
		}
		// 计算总额
		for (int i = 0; i < goodsList.size(); i++) {
			totalMoney += (goodsList.get(i).buy_number)
					* (goodsList.get(i).op_market_price);
		}
		LG.i(getClass(), "totalMoney -------->" + totalMoney);
		gathering_total_money.setText(totalMoney + "");
	}

	/**
	 * 点击事件
	 */
	private OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 记录光标位置
			int index = gathering_cash.getSelectionStart();

			switch (v.getId()) {
			case R.id.cashier_gathering_back:
				finish();
				break;
			case R.id.gathering_btn_0:
				addStringToEditText(btn_0.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_1:
				addStringToEditText(btn_1.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_2:
				addStringToEditText(btn_2.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_3:
				addStringToEditText(btn_3.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_4:
				addStringToEditText(btn_4.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_5:
				addStringToEditText(btn_5.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_6:
				addStringToEditText(btn_6.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_7:
				addStringToEditText(btn_7.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_8:
				addStringToEditText(btn_8.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_9:
				addStringToEditText(btn_9.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_50:
				addStringToEditText(btn_50.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_100:
				addStringToEditText(btn_100.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_200:
				addStringToEditText(btn_200.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_300:
				addStringToEditText(btn_300.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_00:
				addStringToEditText(btn_00.getText().toString().trim(), index);
				break;
			case R.id.gathering_btn_spot:
				if (!hasSpot) {
					addStringToEditText(btn_spot.getText().toString().trim(),
							index);
					hasSpot = true;
				}
				break;
			case R.id.gathering_btn_backup:
				delStringFromEditText(index);
				break;
			case R.id.gathering_btn_ok:
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 在指定位置插入字符
	 * @param str
	 * @param index
	 * @auth sw
	 */
	private void addStringToEditText(String str, int index) {
		if (index < 0 || index >= cash.length()) {
			gathering_cash.append(str);
		} else {
			gathering_cash.getText().insert(index, str);
		}
		cash = gathering_cash.getText().toString().trim();
	}
	/**
	 * 删除当前光标所处位置字符
	 * @auth sw
	 */
	private void delStringFromEditText(int index){
		//光标当前位置不在第一位并且金额不为空
		if (index>0&&BaseUtils.IsNotEmpty(cash)) {
			// 判断删除的是否是小数点
			if (cash.substring(index - 1, index).equals(".")) {
				hasSpot = false;
			}
			cash = gathering_cash.getText().delete(index - 1, index)
					.toString();
			gathering_cash.setText(cash);
			gathering_cash.setSelection(index-1);
		}
	}

	/**
	 * 文本内容改变监听
	 */
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
			if (BaseUtils.IsNotEmpty(cash)) {
				double change = Double.parseDouble(cash) - totalMoney;
				//保留一位小数
				BigDecimal bd = new BigDecimal(change);
				change = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				gathering_change.setText(change + "");
			} else {
				gathering_change.setText("0");
			}
		}
	};

}
