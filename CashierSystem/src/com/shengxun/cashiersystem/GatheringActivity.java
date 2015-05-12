package com.shengxun.cashiersystem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.shengxun.constant.C;
import com.shengxun.entity.OpcenterInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.externalhardware.led.JBLEDInterface;
import com.shengxun.externalhardware.print.util.JBPrintInterface;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

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

	private static EditText gathering_opcenter;
	/**
	 * 保存现金数据
	 */
	private static String cash = "", card_no = "", order_id,
			delivery_rs_code = "", delivery_rs_code_id = "";
	/**
	 * 保存产品总额
	 */
	private double totalMoney = 0, change = -1;
	/**
	 * 所有按钮
	 */
	private TextView btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
			btn_8, btn_9, btn_50, btn_100, btn_200, btn_300, btn_00, btn_spot,
			btn_ok, swing_card, gathering_back, btn_select_opcenter;
	private ImageButton btn_back_up;
	private Button order_cancel;
	/**
	 * 接收传递过来的商品列表
	 */
	private ArrayList<ProductInfo> goodsList;
	/**
	 * 保存创建订单返回的商品列表用以打印
	 */
	private ArrayList<ProductInfo> productInfo;
	/**
	 * 付款方式,默认1(现金支付),2、信用卡，3、储蓄卡，4储值卡，目前只支持现金, 焦点位置，1为卡号输入框，2为现金输入框,默认1;
	 */
	private int pay_way = 1, FocusPosition = 1;
	/**
	 * 记录是否已经有了小数点
	 */
	private boolean hasSpot = false;
	private static OpcenterInfo opcenter;

	/**
	 * 设置运营中心信息read
	 * 
	 * @param op
	 * @auth shouwei
	 */
	public static void setOpcenter(OpcenterInfo op) {
		opcenter = op;
		gathering_opcenter.setText(opcenter.name + "");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_gathering_view);

		initWidget();
		initWidgetData();
		initPrinter();
	}

	/**
	 * 初始化view sw
	 */
	private void initWidget() {
		gathering_back = (TextView) findViewById(R.id.cashier_gathering_back);
		gathering_total_money = (EditText) findViewById(R.id.cashier_gathering_total_money);
		gathering_cash = (EditText) findViewById(R.id.cashier_gathering_cash);
		gathering_opcenter = (EditText) findViewById(R.id.cashier_gathering_opcenter);
		gathering_card_no = (EditText) findViewById(R.id.cashier_gathering_card_no);
		gathering_card_no.addTextChangedListener(new TextWatcher() {
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
				String str = s.toString();
				//刷卡字符处理方法
				//SwingStringManage(str);
			}
		});
		// 设置刷卡输入框的回车事件
		gathering_card_no
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							createOrder();
						}
						return false;
					}
				});
		// gathering_cash.setText("");
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
		btn_back_up = (ImageButton) findViewById(R.id.gathering_btn_backup);
		btn_ok = (TextView) findViewById(R.id.gathering_btn_ok);
		swing_card = (TextView) findViewById(R.id.cashier_gathering_btn_swing_card);
		order_cancel = (Button) findViewById(R.id.cashier_gathering_btn_order_cancel);
		btn_select_opcenter = (TextView) findViewById(R.id.cashier_gathering_btn_select_opcenter);

		gathering_cash.setOnFocusChangeListener(myfocuschange);
		gathering_card_no.setOnFocusChangeListener(myfocuschange);
		btn_select_opcenter.setOnClickListener(myclick);
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
		if (goodsList == null || goodsList.size() == 0) {
			return;
		}
		// 计算总额
		for (int i = 0; i < goodsList.size(); i++) {
			totalMoney += (goodsList.get(i).buy_number)
					* (goodsList.get(i).op_market_price);
		}
		gathering_total_money.setText(totalMoney + "");
		if (applicationCS.isOpenLED) {
			JBLEDInterface.ledDisplay(totalMoney + "");
		} else {
			applicationCS.isOpenLED = JBLEDInterface.openLed();
			JBLEDInterface.ledDisplay(totalMoney + "");
		}
	}

	/**
	 * 初始化打印机
	 * 
	 * @auth shouwei
	 */
	private void initPrinter() {
		JBPrintInterface.openPrinter();
		// JBPrintInterface.convertPrinterControl();
	}
	
	/**
	 * 刷卡 字符处理
	 * @auth shouwei
	 */
	private void SwingStringManage(String str){
		String newStr="";
		//过滤掉特殊字符
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((c >= '0' && c <= '9') || (c <= 'Z' && c >= 'A')
					|| (c <= 'z' && c >= 'a')) {
				newStr += c;
			}
		}
		card_no = newStr;
		//将开头部分asc码转化为asc字符
		if (newStr.length() > 10) {
			card_no = BaseUtils.StringToChar(newStr.substring(0, 3))
					+ newStr.substring(3);
			gathering_card_no.setText(card_no);
			gathering_cash.setText(card_no);
		}
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
				AppManager.getAppManager().finishActivity(mActivity);
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
					// 如未付款则不予执行订单付款接口
					if (change < 0) {
						C.showShort("还未付款", mActivity);
						break;
					}
					ConnectManager.getInstance().getPayOrderFormResult(
							order_id, ajaxPayorder);
				} else {
					C.showShort(
							resources.getString(R.string.cashier_system_alert_gathering_order_error),mActivity);
				}
				break;
			// 刷卡创建订单
			case R.id.cashier_gathering_btn_swing_card:
				createOrder();
				break;
			// 取消订单
			case R.id.cashier_gathering_btn_order_cancel:
				if (BaseUtils.IsNotEmpty(order_id)
						&& BaseUtils.IsNotEmpty(applicationCS.cashier_card_no)) {
					LG.i(getClass(), "cancel order id =====>" + order_id);
					ConnectManager.getInstance().getOrderFormCanaelResult(
							order_id, applicationCS.cashier_card_no, card_no,
							ajaxcancelorder);
				} else {
					C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_error),mActivity);
				}
				break;
			case R.id.cashier_gathering_btn_select_opcenter:
				goActivity(AreaSelectActivity.class);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 创建订单
	 * 
	 * @auth shouwei
	 */
	private void createOrder() {
		card_no = gathering_card_no.getText().toString().trim();
		if (BaseUtils.IsNotEmpty(card_no)) {
			if (applicationCS != null) {
				if (opcenter != null) {
					// delivery_rs_code =
					delivery_rs_code_id = opcenter.id;
				}
				ConnectManager.getInstance().getCreateOrderFormResult(card_no,
						applicationCS.cashier_card_no, goodsList,
						delivery_rs_code, delivery_rs_code_id, pay_way + "",
						totalMoney + "", ajaxcreateorder);
			}
		} else {
			C.showShort(resources.getString(R.string.cashier_system_alert_gathering_card_null),mActivity);
		}
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
				gathering_card_no.getText().delete(index - 1, index).toString();
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
				gathering_cash.getText().delete(index - 1, index).toString();
			}
		}
	}

	/**
	 * 拼接打印字符串
	 * 
	 * @param data
	 * @auth shouwei
	 */
	@SuppressLint("SimpleDateFormat")
	private String printBillInfo() {
		String s = "健康安全网" + "\n" + "名优特产运营中心" + "\n" + "【名优特产●重庆招商运营中心】销售小票"
				+ "\n" + "机号:" + "收银员:" + applicationCS.cashier_card_no + "\n"
				+ "单号:" + order_id + "\n" + "商品名称" + "    " + "单价*数量" + "  "
				+ "金额" + "\n";
		int count = 0;
		for (int i = 0; i < productInfo.size(); i++) {
			s = s + productInfo.get(i).qp_name + "  "
					+ productInfo.get(i).op_market_price + "*"
					+ productInfo.get(i).buy_number + "  "
					+ productInfo.get(i).buy_number
					* productInfo.get(i).op_market_price + "\n";
			count += productInfo.get(i).buy_number;
		}
		s = s + "\n" + "件数:" + count + "\n\n" + "实收RMB:" + cash + "\n"
				+ "找零RMB:" + change + "\n\n";
		Date d = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		s = s + "======" + sim.format(d) + "======" + "\n" + "谢谢惠顾欢迎下次光临"
				+ "\n" + "客服电话:400-011-5808" + "\n" + "请当面清点所购商品和找零" + "\n"
				+ "保留收银小票以作退换货凭证" + "\n" + "更多服务请登陆tc.051jk.com查询";
		System.out.println(s);
		return s;
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
				// 如果cash是一个数字,则计算change
				if (BaseUtils.isNumber(cash)) {
					change = Double.parseDouble(cash) - totalMoney;
					// 保留一位小数
					BigDecimal bd = new BigDecimal(change);
					change = bd.setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					gathering_change.setText(change + "");
				}
			} else {
				gathering_change.setText("0");
				change = -1;
			}
		}
	};

	/**
	 * 创建订单信息回调
	 */
	AjaxCallBack<String> ajaxcreateorder = new AjaxCallBack<String>() {
		@SuppressWarnings("unchecked")
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "create t====>" + t);
			if (BaseUtils.IsNotEmpty(t) && JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				order_id = JSONParser.getStringFromJsonString("order_id", data);
				String product_detail = JSONParser.getStringFromJsonString("product_list", data);
				productInfo = (ArrayList<ProductInfo>) JSONParser.JSON2Array(product_detail, ProductInfo.class);
				C.showShort(resources.getString(R.string.cashier_system_alert_gathering_create_order_success),mActivity);
				// 创建订单成功，取消订单按钮可见
				order_cancel.setVisibility(View.VISIBLE);
			} else {
				C.showShort(JSONParser.getStringFromJsonString("error_desc", t),mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort(resources.getString(R.string.cashier_system_alert_gathering_create_order_fail),mActivity);
		};
	};
	/**
	 * 取消订单接口
	 */
	AjaxCallBack<String> ajaxcancelorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "CANCEL ORDER ====>" + t);
			if (BaseUtils.IsNotEmpty(t) && JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("result", data).equals("ok")) {
					C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_cancel_success),mActivity);
					AppManager.getAppManager().finishActivity(mActivity);
				} else {
					C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_cancel_fail),mActivity);
				}
			} else {
				C.showShort(JSONParser.getStringFromJsonString("error_desc", t),mActivity);
			}

		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_cancel_fail),mActivity);
			LG.i(getClass(), "t ===>" + t);
		};
	};

	/**
	 * 订单付款接口
	 */
	AjaxCallBack<String> ajaxPayorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			if (BaseUtils.IsNotEmpty(t)&& JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("result", data).equals("ok")) {

					// 开始打印
					JBPrintInterface.printText_GB2312(printBillInfo());

					C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_pay_success),mActivity);
					AppManager.getAppManager().finishActivity(mActivity);
					JBPrintInterface.closePrinter();
				} else {
					C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_pay_fail),mActivity);
				}
			} else {
				C.showShort(JSONParser.getStringFromJsonString("error_desc", t),mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort(resources.getString(R.string.cashier_system_alert_gathering_order_pay_fail),mActivity);
		};
	};
	

}
