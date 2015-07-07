package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shengxun.constant.C;
import com.shengxun.entity.OrderDetailInfo;
import com.shengxun.entity.OrderInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.externalhardware.cashbox.JBCashBoxInterface;
import com.shengxun.externalhardware.print.util.JBPrintInterface;
import com.shengxun.externalhardware.print.util.PrintTools_58mm;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
import com.zvezda.android.utils.TimeConversion;

/**
 * 收款界面2(因为查询订单返回的数据与创建订单返回的数据字段不相同，所以用这个来完成查询订单的支付)
 * 
 * @author sw
 * @date 2015-4-24
 */
public class GatheringForSearchOrderActivity extends MyTimeLockBaseActivity {

	LinearLayout opcenter_layout, swing_layout;
	/**
	 * 总额，现金，找零输入框
	 */
	EditText gathering_total_money;
	EditText gathering_cash, gathering_change;

	/**
	 * 保存现金数据
	 */
	private static String cash = "", card_no = "", order_id;
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
	/**
	 * 保存创建订单返回的商品列表用以打印
	 */
	private ArrayList<ProductInfo> productInfos;
	
	private OrderInfo mOrderInfo;
	
	private OrderDetailInfo mOrderDetail;
	/**
	 * 付款方式,默认1(现金支付),2、信用卡，3、储蓄卡，4储值卡，目前只支持现金, 焦点位置，1为卡号输入框，2为现金输入框,默认1;
	 */
	private String pay_way = "1";
	/**
	 * 记录是否已经有了小数点
	 */
	private boolean hasSpot = false;

	// 标志位0代表正常进入,1代表从订单付款页面而来

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_gathering_view);

		initWidget();
		//initWidgetData();
		initData();
	}

	/**
	 * 初始化view sw
	 */
	@SuppressLint("NewApi")
	private void initWidget() {
		opcenter_layout = (LinearLayout) findViewById(R.id.cashier_gathering_opcenter_layout);
		swing_layout = (LinearLayout) findViewById(R.id.cashier_gathering_swing_layout);
		opcenter_layout.setVisibility(View.GONE);
		swing_layout.setVisibility(View.GONE);

		gathering_back = (TextView) findViewById(R.id.cashier_gathering_back);
		gathering_total_money = (EditText) findViewById(R.id.cashier_gathering_total_money);
		gathering_cash = (EditText) findViewById(R.id.cashier_gathering_cash);

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
		btn_select_opcenter = (TextView) findViewById(R.id.cashier_gathering_btn_select_area);

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
		swing_card.setOnClickListener(myclick);

	}

//	/**
//	 * 初始化控件显示数据
//	 * 
//	 * @auth sw
//	 */
//	@SuppressWarnings("unchecked")
//	private void initWidgetData() {
//		// 获得传递商品信息列表
//		goodsList = (ArrayList<ProductInfo>) getIntent().getSerializableExtra(
//				"DATA");
//		if (goodsList == null || goodsList.size() == 0) {
//			return;
//		}
//		Log.i("savion", "goods list =======>" + goodsList.size());
//		// 计算总额
//		for (int i = 0; i < goodsList.size(); i++) {
//			Log.i("savion", "goolist +" + i + " " + goodsList.get(i));
//			if (goodsList.get(i).op_is_promote == 1) {
//				totalMoney += (goodsList.get(i).buy_number)
//						* (goodsList.get(i).op_promote_market_price);
//			} else {
//				totalMoney += (goodsList.get(i).buy_number)
//						* (goodsList.get(i).op_market_price);
//			}
//		}
//		String formatMoney = BaseUtils.formatDouble(totalMoney);
//		totalMoney = Double.parseDouble(formatMoney);
//		LG.e(getClass(), "total money == >" + totalMoney);
//		gathering_total_money.setText(formatMoney + "");
//		// 显示收费金额
//		JBLEDInterface.convertLedControl();
//		JBLEDInterface.ledDisplay(totalMoney + "");
//
//	}
	
	private void initData(){
		mOrderDetail = (OrderDetailInfo) getIntent().getSerializableExtra("DATA");
		if(mOrderDetail.getOrder_info()!=null){
			mOrderInfo = mOrderDetail.getOrder_info();
			order_id = mOrderInfo.co_id;
			pay_way = mOrderInfo.co_pay_way;
			totalMoney = mOrderInfo.co_money;
			gathering_total_money.setText(totalMoney+"");
		}
		if(mOrderDetail.getProduct_info()!=null){
			productInfos = (ArrayList<ProductInfo>) mOrderDetail.getProduct_info();
		}
	}

	/**
	 * 点击事件
	 */
	private OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
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
					if (change < 0.0) {
						C.showDialogAlert("还未付款", mActivity);
						break;
					}
					ConnectManager.getInstance().getPayOrderFormResult(
							order_id, ajaxPayorder);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_error),
							mActivity);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 在指定位置插入字符
	 * 
	 * @param str
	 * @param index
	 * @auth sw
	 */
	private void addStringToEditText(String str) {
		int index;
		index = gathering_cash.getSelectionStart();
		if (index < 0 || index >= cash.length()) {
			gathering_cash.append(str);
		} else {
			gathering_cash.getText().insert(index, str);
		}
		gathering_cash.getText().toString().trim();
	}

	/**
	 * 删除当前光标所处位置字符
	 * 
	 * @auth sw
	 */
	private void delStringFromEditText() {
		int index;
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
//		if (BaseUtils.IsNotEmpty(delivery_rs_code_id) && opcenter != null) {
//			PrintTools_58mm.print_gbk("【名优特产●"
//					+ applicationCS.loginInfo.cashier_info.rs_code_name
//					+ "】提货小票");
//			PrintTools_58mm.print(PrintTools_58mm.LF);
//			PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_LEFT);
//			PrintTools_58mm.print_gbk("提货地址：" + opcenter.name + " "
//					+ opcenter.address);
//		} else {
		PrintTools_58mm.print_gbk("【名优特产●"
					+ applicationCS.loginInfo.cashier_info.rs_code_name
					+ "】销售小票");
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_LEFT);
		PrintTools_58mm.print_gbk("机号:" + applicationCS.mc_id + "    收银员:"
				+ applicationCS.cashier_card_no);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("单号:" + order_id);
		PrintTools_58mm.print(PrintTools_58mm.LF);
		PrintTools_58mm.print_gbk("商品名称" + "      " + "单价*数量" + "    " + "金额");
		int count = 0;
		for (int i = 0; i < productInfos.size(); i++) {
			PrintTools_58mm.print(PrintTools_58mm.LF);
			ProductInfo entity = productInfos.get(i);
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
						+ entity.cop_price
						+ "*"
						+ entity.cop_number
						+ "     "
						+ entity.cop_number
						* entity.cop_price + "";
				// 如果是促销产品那么就打印促销价(暂无)
//				if (entity.op_is_promote == 1) {
//					name_suffix = entity.qp_name.substring(7,
//							entity.qp_name.length())
//							+ "     "
//							+ entity.op_promote_market_price
//							+ "*"
//							+ entity.buy_number
//							+ "     "
//							+ entity.buy_number
//							* entity.op_promote_market_price + "";
//				}
				PrintTools_58mm.print_gbk("" + name_prefix);
				PrintTools_58mm.print(PrintTools_58mm.LF);
				PrintTools_58mm.print_gbk("" + name_suffix);

			} else {
				String s = entity.qp_name + "  " + entity.cop_price + "*"
						+ entity.cop_number + "  " + entity.cop_number
						* entity.cop_price + "";
				// 如果是促销产品那么就打印促销价
//				if (entity.op_is_promote == 1) {
//					s = entity.qp_name + "  " + entity.op_promote_market_price
//							+ "*" + entity.buy_number + "  "
//							+ entity.cop_number
//							* entity.op_promote_market_price + "";
//
//				} else {
//
//				}
				PrintTools_58mm.print_gbk("" + s);

			}
			count += entity.cop_number;
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
		PrintTools_58mm.print_gbk("更多服务请登陆tc.051jk.com查询");
		PrintTools_58mm.writeEnterLine(2);
		PrintTools_58mm.resetPrint();
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
	 * 订单付款接口
	 */
	AjaxCallBack<String> ajaxPayorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
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
					AppManager.getAppManager().finishActivity(mActivity);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_pay_fail),
							mActivity);
				}
			} else {
				C.showDialogAlert(
						JSONParser.getStringFromJsonString("error_desc", t),
						mActivity);
			}
		}

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showDialogAlert(
					resources
							.getString(R.string.cashier_system_alert_gathering_order_pay_fail),
					mActivity);
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

}
