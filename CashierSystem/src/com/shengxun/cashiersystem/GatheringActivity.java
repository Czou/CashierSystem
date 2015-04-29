package com.shengxun.cashiersystem;

import java.math.BigDecimal;
import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.constant.C;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
	 * 总额，现金，找零输入框
	 */
	EditText gathering_total_money, gathering_cash, gathering_change,
			gathering_card_no;
	/**
	 * 保存现金数据
	 */
	private static String cash = "", card_no = "";
	/**
	 * 保存产品总额
	 */
	private double totalMoney = 0;
	/**
	 * 所有按钮
	 */
	private TextView btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
			btn_8, btn_9, btn_50, btn_100, btn_200, btn_300, btn_00, btn_spot,
			btn_back_up, btn_ok, swing_card, gathering_back;
	private Button order_cancel;
	/**
	 * 接收传递过来的商品列表
	 */
	private ArrayList<ProductInfo> goodsList;
	/**
	 * 付款方式,默认1(现金支付),2、信用卡，3、储蓄卡，4储值卡，目前只支持现金
	 */
	private int pay_way = 1;
	/**
	 * 焦点位置，1为卡号输入框，2为现金输入框,默认1;
	 */
	private int FocusPosition = 1;
	/**
	 * 记录是否已经有了小数点
	 */
	private String order_id;
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
		gathering_card_no = (EditText) findViewById(R.id.cashier_gathering_card_no);
		gathering_cash.setText("");
		// 设置不显示输入法
		gathering_cash.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		gathering_cash.setTextIsSelectable(true);
		// 设置光标位置
		gathering_cash.setSelection(gathering_cash.length());
		gathering_change = (EditText) findViewById(R.id.cashier_gathering_change);
		btn_0 = (TextView) findViewById(R.id.gathering_btn_0);
		btn_1 = (TextView) findViewById(R.id.gathering_btn_1);
		btn_2 = (TextView) findViewById(R.id.gathering_btn_2);
		btn_3 = (TextView) findViewById(R.id.gathering_btn_3);
		btn_4 = (TextView) findViewById(R.id.gathering_btn_4);
		btn_5 = (TextView) findViewById(R.id.gathering_btn_5);
		btn_6 = (TextView) findViewById(R.id.gathering_btn_6);
		btn_7 = (TextView) findViewById(R.id.gathering_btn_7);
		btn_8 = (TextView) findViewById(R.id.gathering_btn_8);
		btn_9 = (TextView) findViewById(R.id.gathering_btn_9);
		btn_50 = (TextView) findViewById(R.id.gathering_btn_50);
		btn_100 = (TextView) findViewById(R.id.gathering_btn_100);
		btn_200 = (TextView) findViewById(R.id.gathering_btn_200);
		btn_300 = (TextView) findViewById(R.id.gathering_btn_300);
		btn_00 = (TextView) findViewById(R.id.gathering_btn_00);
		btn_spot = (TextView) findViewById(R.id.gathering_btn_spot);
		btn_back_up = (TextView) findViewById(R.id.gathering_btn_backup);
		btn_ok = (TextView) findViewById(R.id.gathering_btn_ok);
		swing_card = (TextView) findViewById(R.id.cashier_gathering_btn_swing_card);
		order_cancel = (Button) findViewById(R.id.cashier_gathering_btn_order_cancel);

		gathering_cash.setOnFocusChangeListener(myfocuschange);
		gathering_card_no.setOnFocusChangeListener(myfocuschange);
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
		order_cancel.setOnClickListener(myclick);
		swing_card.setOnClickListener(myclick);

	}

	/**
	 * 初始化控件显示数据
	 * 
	 * @auth sw
	 */
	@SuppressWarnings("unchecked")
	private void initWidgetData() {
		// 获得传递商品信息列表
		goodsList = (ArrayList<ProductInfo>) getIntent().getSerializableExtra(
				"DATA");
		if (goodsList == null||goodsList.size()==0) {
			return;
		}
		// 计算总额
		for (int i = 0; i < goodsList.size(); i++) {
			totalMoney += (goodsList.get(i).buy_number)
					* (goodsList.get(i).op_market_price);
			LG.i(getClass(), "goodslist ======>"+goodsList.get(i));
		}
		gathering_total_money.setText(totalMoney + "");
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
				addStringToEditText(btn_0.getText().toString().trim());
				break;
			case R.id.gathering_btn_1:
				addStringToEditText(btn_1.getText().toString().trim());
				break;
			case R.id.gathering_btn_2:
				addStringToEditText(btn_2.getText().toString().trim());
				break;
			case R.id.gathering_btn_3:
				addStringToEditText(btn_3.getText().toString().trim());
				break;
			case R.id.gathering_btn_4:
				addStringToEditText(btn_4.getText().toString().trim());
				break;
			case R.id.gathering_btn_5:
				addStringToEditText(btn_5.getText().toString().trim());
				break;
			case R.id.gathering_btn_6:
				addStringToEditText(btn_6.getText().toString().trim());
				break;
			case R.id.gathering_btn_7:
				addStringToEditText(btn_7.getText().toString().trim());
				break;
			case R.id.gathering_btn_8:
				addStringToEditText(btn_8.getText().toString().trim());
				break;
			case R.id.gathering_btn_9:
				addStringToEditText(btn_9.getText().toString().trim());
				break;
			case R.id.gathering_btn_50:
				addStringToEditText(btn_50.getText().toString().trim());
				break;
			case R.id.gathering_btn_100:
				addStringToEditText(btn_100.getText().toString().trim());
				break;
			case R.id.gathering_btn_200:
				addStringToEditText(btn_200.getText().toString().trim());
				break;
			case R.id.gathering_btn_300:
				addStringToEditText(btn_300.getText().toString().trim());
				break;
			case R.id.gathering_btn_00:
				addStringToEditText(btn_00.getText().toString().trim());
				break;
			case R.id.gathering_btn_spot:
				if (!hasSpot) {
					addStringToEditText(btn_spot.getText().toString().trim());
					hasSpot = true;
				}
				break;
			case R.id.gathering_btn_backup:
				delStringFromEditText();
				break;
			// 支付订单
			case R.id.gathering_btn_ok:
				if (BaseUtils.IsNotEmpty(order_id)) {
					ConnectManager.getInstance().getPayOrderFormResult(
							order_id, ajaxPayorder);
				}
				break;
			// 刷卡创建订单
			case R.id.cashier_gathering_btn_swing_card:
				card_no = gathering_card_no.getText().toString().trim();
				if (BaseUtils.IsNotEmpty(card_no)) {
					if (applicationCS != null) {
						ConnectManager.getInstance().getCreateOrderFormResult(
								card_no, applicationCS.cashier_card_no,
								montageString(), "", "", pay_way + "",
								totalMoney + "", ajaxcreateorder);
					}
				} else {
					C.showShort("卡号不能为空", mActivity);
				}
				break;
			// 取消订单
			case R.id.cashier_gathering_btn_order_cancel:
				if (BaseUtils.IsNotEmpty(order_id)) {
					ConnectManager.getInstance().getOrderFormCanaelResult(
							order_id, ajaxcancelorder);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 拼接产品购买信息字符串
	 * 
	 * @auth sw
	 */
	private String montageString() {
		String product_info = "";
		for (int i = 0; i < goodsList.size(); i++) {
			if (i == 0) {
				product_info = "product_info[" + goodsList.get(i).op_id + "]"
						+ goodsList.get(i).buy_number;
			} else {
				product_info += "&product_info[" + goodsList.get(i).op_id + "]"
						+ goodsList.get(i).buy_number;
			}
		}
		LG.e(getClass(), "product_info-------------->" + product_info);
		return product_info;
	}

	/**
	 * 在指定位置插入字符
	 * 
	 * @param str
	 * @param index
	 * @auth sw
	 */
	private void addStringToEditText(String str) {
		int index;
		// 向card_no输入框插入
		if (FocusPosition == 1) {
			card_no = gathering_card_no.getText().toString().trim();
			index = gathering_card_no.getSelectionStart();
			if (index < 0 || index >= card_no.length()) {
				gathering_card_no.append(str);
			} else {
				gathering_card_no.getText().insert(index, str);
			}
			// 向cash输入框插入
		} else if (FocusPosition == 2) {
			index = gathering_cash.getSelectionStart();
			if (index < 0 || index >= cash.length()) {
				gathering_cash.append(str);
			} else {
				gathering_cash.getText().insert(index, str);
			}
			gathering_cash.getText().toString().trim();
		}
	}

	/**
	 * 删除当前光标所处位置字符
	 * 
	 * @auth sw
	 */
	private void delStringFromEditText() {
		int index;
		// 删除card_no中的数据
		if (FocusPosition == 1) {
			card_no = gathering_card_no.getText().toString().trim();
			index = gathering_card_no.getSelectionStart();
			if (index > 0 && BaseUtils.IsNotEmpty(card_no)) {
				gathering_card_no.getText().delete(index - 1, index)
						.toString();
			}
			// 删除cash中的数据
		} else if (FocusPosition == 2) {
			index = gathering_cash.getSelectionStart();
			// 光标当前位置不在第一位并且金额不为空
			if (index > 0 && BaseUtils.IsNotEmpty(cash)) {
				// 判断删除的是否是小数点
				if (cash.substring(index - 1, index).equals(".")) {
					hasSpot = false;
				}
				gathering_cash.getText().delete(index - 1, index)
						.toString();
			}
		}
	}

	/**
	 * 焦点改变监听
	 */
	OnFocusChangeListener myfocuschange = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
			case R.id.cashier_gathering_card_no:
				if (hasFocus) {
					FocusPosition = 1;
				}
				break;
			case R.id.cashier_gathering_cash:
				if (hasFocus) {
					FocusPosition = 2;
				}
				break;
			default:
				break;
			}
		}
	};

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
			cash = gathering_cash.getText().toString().trim();
			if (BaseUtils.IsNotEmpty(cash)) {
				double change = Double.parseDouble(cash) - totalMoney;
				LG.i(getClass(), "cash===>"+cash+",totalmoney====>"+totalMoney);
				// 保留一位小数
				BigDecimal bd = new BigDecimal(change);
				change = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				gathering_change.setText(change + "");
			} else {
				gathering_change.setText("0");
			}
		}
	};
	/**
	 * 创建订单信息回调
	 */
	AjaxCallBack<String> ajaxcreateorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "订单详细信息----->" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				order_id = JSONParser.getStringFromJsonString("order_id", t);
				C.showShort("创建订单成功", mActivity);
				// 创建订单成功，取消订单按钮可见
				order_cancel.setVisibility(View.VISIBLE);
				finish();
			} else {
				C.showShort("订单创建失败", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("创建订单失败", mActivity);
		};
	};
	/**
	 * 取消订单接口
	 */
	AjaxCallBack<String> ajaxcancelorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "取消订单信息------->" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("result", t).equals(
							"ok")) {
				C.showShort("取消订单成功", mActivity);
				finish();
			} else {
				C.showShort("取消订单失败", mActivity);
			}

		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("取消订单失败", mActivity);
		};
	};
	/**
	 * 订单付款接口
	 */
	AjaxCallBack<String> ajaxPayorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("result", t).equals(
							"ok")) {
				C.showShort("订单付款成功", mActivity);
			} else {
				C.showShort("订单付款失败", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("订单付款失败", mActivity);
		};
	};
}
