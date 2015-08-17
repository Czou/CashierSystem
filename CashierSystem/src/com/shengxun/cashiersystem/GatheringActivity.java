package com.shengxun.cashiersystem;

import java.math.BigDecimal;
import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.shengxun.constant.C;
import com.shengxun.customview.LDialog.DialogCallBack;
import com.shengxun.entity.OpcenterInfo;
import com.shengxun.entity.OrderInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.externalhardware.cashbox.JBCashBoxInterface;
import com.shengxun.externalhardware.led.JBLEDInterface;
import com.shengxun.externalhardware.print.util.JBPrintInterface;
import com.shengxun.externalhardware.print.util.PrintTools_58mm;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
import com.zvezda.android.utils.TimeConversion;

/**
 * 收款界面
 * 
 * @author sw
 * @date 2015-4-24
 */
public class GatheringActivity extends MyTimeLockBaseActivity {

	LinearLayout opcenter_layout, swing_layout;
	/**
	 * 总额，现金，找零输入框
	 */
	EditText gathering_total_money;
	EditText gathering_cash, gathering_change, gathering_card_no;

	private static EditText gathering_opcenter;
	/**
	 * 保存现金数据
	 */
	private String cash = "", card_no = "", order_id;
	private static String delivery_rs_code = "", delivery_rs_code_id = "";
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
	 * 保存自有商品列表，用于打印(创建订单不会返回自有订单的信息)
	 */
	private ArrayList<ProductInfo> seller_productinfo;
	/**
	 * 付款方式,默认1(现金支付),2、信用卡，3、储蓄卡，4储值卡，目前只支持现金, 焦点位置，1为卡号输入框，2为现金输入框,默认1;
	 */
	private int pay_way = 1, FocusPosition = 2;
	/**
	 * 记录是否已经有了小数点
	 */
	private boolean hasSpot = false;
	private static OpcenterInfo opcenter;

	// 标志位0代表正常进入,1代表从订单付款页面而来
	/**
	 * 设置运营中心信息read
	 * 
	 * @param op
	 * @auth shouwei
	 */
	public static void setOpcenter(OpcenterInfo op, String type) {
		if (BaseUtils.IsNotEmpty(op)) {
			opcenter = op;
			delivery_rs_code = type;
			delivery_rs_code_id = op.id;
			gathering_opcenter.setText(opcenter.name + "");
			Log.i("savion", "op===>" + op.toString());
		} else {
			delivery_rs_code = "";
			delivery_rs_code_id = "";
			gathering_opcenter.setText("");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_gathering_view);

		initWidget();
		initWidgetData();
	}

	/**
	 * 初始化view sw
	 */
	@SuppressLint("NewApi")
	private void initWidget() {
		opcenter_layout = (LinearLayout) findViewById(R.id.cashier_gathering_opcenter_layout);
		swing_layout = (LinearLayout) findViewById(R.id.cashier_gathering_swing_layout);

		gathering_back = (TextView) findViewById(R.id.cashier_gathering_back);
		gathering_total_money = (EditText) findViewById(R.id.cashier_gathering_total_money);
		gathering_cash = (EditText) findViewById(R.id.cashier_gathering_cash);
		gathering_opcenter = (EditText) findViewById(R.id.cashier_gathering_opcenter);
		gathering_card_no = (EditText) findViewById(R.id.cashier_gathering_card_no);

		// 设置刷卡输入框的回车事件
		gathering_card_no
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// createOrder();
						Log.i("savion", "cash_no_enter");
						return true;
					}
				});
		gathering_cash.setText("");
		// 设置不显示输入法
		gathering_cash.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		gathering_cash.setTextIsSelectable(true);
		// 设置光标位置0
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
		btn_select_opcenter = (TextView) findViewById(R.id.cashier_gathering_btn_select_area);

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
		// seller_productinfo = new ArrayList<ProductInfo>();
		// for (int i = 0; i < goodsList.size(); i++) {
		// if (!goodsList.get(i).isProductInSystem) {
		// seller_productinfo.add(goodsList.get(i));
		// }
		// }
		Log.i("savion", "goods list =======>" + goodsList.size());
		// 计算总额
		for (int i = 0; i < goodsList.size(); i++) {
			Log.i("savion", "goolist +" + i + " " + goodsList.get(i));
			if (goodsList.get(i).op_is_promote == 1) {
				totalMoney += (goodsList.get(i).buy_number)
						* (goodsList.get(i).op_promote_market_price);
			} else {
				totalMoney += (goodsList.get(i).buy_number)
						* (goodsList.get(i).op_market_price);
			}
		}
		String formatMoney = BaseUtils.formatDouble(totalMoney);
		totalMoney = Double.parseDouble(formatMoney);
		LG.e(getClass(), "total money == >" + totalMoney);
		gathering_total_money.setText(formatMoney + "");
		// 显示收费金额
		JBLEDInterface.convertLedControl();
		JBLEDInterface.ledDisplay(totalMoney + "");

	}

	/**
	 * 点击事件
	 */
	private OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_gathering_back:
				finishGathering();
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
				createOrder();

				break;
			// 刷卡创建订单
			// case R.id.cashier_gathering_btn_swing_card:
			// createOrder();
			// break;
			// 取消订单
			case R.id.cashier_gathering_btn_order_cancel:
				if (BaseUtils.IsNotEmpty(order_id)
						&& BaseUtils.IsNotEmpty(applicationCS.cashier_card_no)) {
					if (BaseUtils.isNetworkAvailable(mActivity)) {
						C.openProgressDialog(mActivity, null, "正在取消订单...");
						ConnectManager.getInstance().getOrderFormCanaelResult(
								order_id, applicationCS.cashier_card_no,
								card_no, ajaxcancelorder);
					} else {
						C.showDialogAlert("当前网络不可用", mActivity);
					}
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_error),
							mActivity);
				}
				break;
			case R.id.cashier_gathering_btn_select_area:
				// 检索是否有非系统商品
				for (int i = 0; i < goodsList.size(); i++) {
					if (!goodsList.get(i).isProductInSystem) {
						C.showDialogAlert("存在非系统商品，不可异地取货", mActivity);
						return;
					}
				}
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
		if (change < 0.0) {
			C.showDialogAlert("请先付款", mActivity);
			return;
		}
		card_no = gathering_card_no.getText().toString().trim();
		LG.i(getClass(), "before create order====>card_no:" + card_no
				+ ",delivery_rs_code:" + delivery_rs_code
				+ ",delivery_rs_code_id:" + delivery_rs_code_id);
		if (BaseUtils.IsNotEmpty(card_no)) {
			if (applicationCS != null) {
				if (BaseUtils.isNetworkAvailable(mActivity)) {
					C.openProgressDialog(mActivity, null, "正在创建订单...");
					ConnectManager.getInstance().getCreateOrderFormResult(
							card_no, applicationCS.cashier_card_no, goodsList,
							delivery_rs_code, delivery_rs_code_id,
							pay_way + "", totalMoney + "", ajaxcreateorder);
				} else {
					C.showDialogAlert("当前网络不可用", mActivity);
				}
			}
		} else {
			C.showDialogAlert(
					resources
							.getString(R.string.cashier_system_alert_gathering_card_null),
					mActivity);
		}
	}

	/**
	 * 确认是否支付
	 * 
	 * @auth shouwei
	 */
	private void payOrder() {
		C.showDialogAlert(resources.getString(R.string.warm_prompt), "是否立即支付?",
				new DialogCallBack() {
					@Override
					public void okCallBack() {
						if (BaseUtils.IsNotEmpty(order_id)) {
							// 如未付款则不予执行订单付款接口
							if (change < 0.0) {
								C.showDialogAlert("还未付款", mActivity);
							}
							if (BaseUtils.isNetworkAvailable(mActivity)) {
								C.openProgressDialog(mActivity, null, "正在支付...");
								ConnectManager.getInstance()
										.getPayOrderFormResult(order_id,
												applicationCS.cashier_card_no,
												ajaxPayorder);
							} else {
								C.showDialogAlert("当前网络不可用", mActivity);
							}
						} else {
							C.showDialogAlert("订单尚未创建", mActivity);
						}
					}
				}, mActivity);

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
	 * @auth LL
	 */
	private void printBillInfo() {

		PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_CENTER);
		PrintTools_58mm.writeEnterLine(1);
		PrintTools_58mm.print_gbk("健康安全网");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print(PrintTools_58mm.FS_FONT_ALIGN_BIG);
		PrintTools_58mm.print_gbk("名优特产运营中心");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print(PrintTools_58mm.FS_FONT_ALIGN);
		if (BaseUtils.IsNotEmpty(delivery_rs_code_id) && opcenter != null) {
			PrintTools_58mm.print_gbk("【名优特产●"
					+ applicationCS.loginInfo.cashier_info.rs_code_name
					+ "】提货小票");
			PrintTools_58mm.print(PrintTools_58mm.LF);
			PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_LEFT);
			PrintTools_58mm.print_gbk("提货地址：" + opcenter.name + " "
					+ opcenter.address);
		} else {
			PrintTools_58mm.print_gbk("【名优特产●"
					+ applicationCS.loginInfo.cashier_info.rs_code_name
					+ "】销售小票");
		}
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_LEFT);
		PrintTools_58mm.print_gbk("机号:" + applicationCS.mc_id + "    收银员:"
				+ applicationCS.cashier_card_no);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("单号:" + order_id);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("商品名称" + "      " + "单价*数量" + "    " + "金额");
		int count = 0;
		// 是否是自有商品
		String str = "";
		for (int i = 0; i < productInfo.size(); i++) {
			PrintTools_58mm.print(PrintTools_58mm.LF);
			ProductInfo entity = productInfo.get(i);
			if (productInfo.get(i).cop_is_seller == 1) {
				str = "#";
				entity.qp_name = str + entity.qp_name;
			}
			// 如果是促销产品那么就打印促销产品前缀
			if (entity.op_is_promote == 1) {
				entity.qp_name = resources
						.getString(R.string.cashier_system_promote)
						+ entity.qp_name;
			} else {
				entity.qp_name = entity.qp_name;
			}
			if (BaseUtils.IsNotEmpty(entity.qp_name)
					&& entity.qp_name.length() > 7) {
				String name_prefix = entity.qp_name.substring(0, 7);
				String name_suffix = entity.qp_name.substring(7,
						entity.qp_name.length())
						+ "     "
						+ entity.op_market_price
						+ "*"
						+ entity.buy_number
						+ "     "
						+ entity.buy_number
						* entity.op_market_price + "";
				// 如果是促销产品那么就打印促销价
				if (entity.op_is_promote == 1) {
					name_suffix = entity.qp_name.substring(7,
							entity.qp_name.length())
							+ "     "
							+ entity.op_promote_market_price
							+ "*"
							+ entity.buy_number
							+ "     "
							+ entity.buy_number
							* entity.op_promote_market_price + "";
				}
				PrintTools_58mm.print_gbk("" + name_prefix);
				PrintTools_58mm.print(PrintTools_58mm.LF);
				PrintTools_58mm.print_gbk("" + name_suffix);

			} else {
				String s = entity.qp_name + "  " + entity.op_market_price + "*"
						+ entity.buy_number + "  " + entity.buy_number
						* entity.op_market_price + "";
				// 如果是促销产品那么就打印促销价
				if (entity.op_is_promote == 1) {
					s = entity.qp_name + "  " + entity.op_promote_market_price
							+ "*" + entity.buy_number + "  "
							+ entity.buy_number
							* entity.op_promote_market_price + "";

				} else {

				}
				PrintTools_58mm.print_gbk("" + s);

			}
			count += entity.buy_number;
		}
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("件数:" + count);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("实收RMB:" + cash);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("找零RMB:" + change);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_CENTER);
		PrintTools_58mm.print_gbk("======"
				+ TimeConversion.getSystemAppTimeCN() + "======");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("谢谢惠顾欢迎下次光临");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("客服电话:400-011-5808");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("请当面清点所购商品和找零");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("保留收银小票以作退换货凭证");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("带#为非本系统商品，不支持退换货");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("更多服务请登陆tc.051jk.com查询");
		PrintTools_58mm.writeEnterLine(2);
		PrintTools_58mm.resetPrint();
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
					LG.e(getClass(), "找零======>" + change);
					gathering_change.setText(BaseUtils.formatDouble(change));
					change = Double.parseDouble(BaseUtils.formatDouble(change));
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
			C.closeProgressDialog();
			LG.i(getClass(), "create order ====>" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				order_id = JSONParser.getStringFromJsonString("order_id", data);
				String product_detail = JSONParser.getStringFromJsonString(
						"product_list", data);
				productInfo = (ArrayList<ProductInfo>) JSONParser.JSON2Array(
						product_detail, ProductInfo.class);
				payOrder();
				// 创建订单成功，取消订单按钮可见
				order_cancel.setVisibility(View.VISIBLE);
				BaseUtils.closeSoftKeyBoard(mActivity);
			} else if (JSONParser.getStringFromJsonString("status", t).equals(
					"0")) {
				String msg = JSONParser
						.getStringFromJsonString("error_desc", t);
				if (BaseUtils.IsNotEmpty(msg)) {
					C.showDialogAlert(msg, mActivity);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_create_order_fail),
							mActivity);
				}
			} else {
				C.showDialogAlert("创建订单返回信息错误", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			C.showDialogAlert(
					resources
							.getString(R.string.cashier_system_alert_gathering_create_order_fail),
					mActivity);
		};
	};
	/**
	 * 取消订单接口
	 */
	AjaxCallBack<String> ajaxcancelorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			C.closeProgressDialog();
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("result", data).equals(
						"ok")) {
					C.showDialogAlert("取消订单成功,3秒后将自动关闭此窗口", mActivity);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (MainActivity.instance != null) {
								MainActivity.instance.clearGoods();
							}
							finishGathering();
						}
					}, 3000);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_cancel_fail),
							mActivity);
				}
			} else if (JSONParser.getStringFromJsonString("status", t).equals(
					"0")) {
				String msg = JSONParser
						.getStringFromJsonString("error_desc", t);
				if (BaseUtils.IsNotEmpty(msg)) {
					C.showDialogAlert(msg, mActivity);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_cancel_fail),
							mActivity);
				}
			} else {
				C.showDialogAlert("取消订单返回信息错误", mActivity);
			}

		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			C.showDialogAlert(
					resources
							.getString(R.string.cashier_system_alert_gathering_order_cancel_fail),
					mActivity);
		};
	};

	/**
	 * 订单付款接口
	 */
	AjaxCallBack<String> ajaxPayorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			C.closeProgressDialog();
			LG.i(getClass(), "t===>" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("result", data).equals(
						"ok")) {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_pay_success),
							mActivity);
					// 开始打印
					printPaymentInfo();
					if (MainActivity.instance != null) {
						MainActivity.instance.clearGoods();
					}
					finishGathering();
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_pay_fail),
							mActivity);
				}
			} else if (JSONParser.getStringFromJsonString("status", t).equals(
					"0")) {
				String error = JSONParser.getStringFromJsonString("error_desc",
						t);
				if (BaseUtils.IsNotEmpty(error)) {
					C.showDialogAlert(error, mActivity);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_pay_fail),
							mActivity);
				}
			} else {
				C.showDialogAlert("付款返回信息错误", mActivity);
			}
		}

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showDialogAlert(
					resources
							.getString(R.string.cashier_system_alert_gathering_order_pay_fail),
					mActivity);
			C.closeProgressDialog();
		};
	};

	/**
	 * 打印收银信息
	 */
	private void printPaymentInfo() {
		// 开始打印
		Log.i("savion", "开始打印--------------");
		JBPrintInterface.convertPrinterControl();
		JBCashBoxInterface.openCashBox();
		printBillInfo();
	};

	private void finishGathering() {
		opcenter = null;
		delivery_rs_code = "";
		delivery_rs_code_id = "";
		C.closeProgressDialog();
		AppManager.getAppManager().finishActivity(GatheringActivity.this);
	}

}
