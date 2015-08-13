package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shengxun.adapter.CashierPickupGoodsAdapter;
import com.shengxun.cashiersystem.SettingActivity.UpdatePsdListener;
import com.shengxun.constant.C;
import com.shengxun.entity.OrderDetailInfo;
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
 * 查询订单详细信息界面
 * 
 * @author sw
 * @date 2015-5-15
 */
public class SearchOrderActivity extends MyTimeLockBaseActivity {

	EditText et_card_no;
	TextView show_money;
	Button btn_search, btn_back, btn_pay, btn_cancel;
	// 查询显示listview
	ListView lv;
	// 订单号与付款总额
	String str_order_no, pay_money;
	// 是否需要付款
	boolean isPayable = false;
	// 结果商品列表
	ArrayList<ProductInfo> product_list = new ArrayList<ProductInfo>();
	// 订单信息
	OrderInfo orderInfo = new OrderInfo();
	// 适配器
	CashierPickupGoodsAdapter goodsAdapter;
	// 消费人卡号
	String consume_card_no;
	AlertDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_search_order_view);

		initWidget();
	}

	/**
	 * 初始化控件
	 * 
	 * @auth shouwei
	 */
	private void initWidget() {
		btn_back = (Button) findViewById(R.id.cashier_search_order_btnback);
		et_card_no = (EditText) findViewById(R.id.cashier_search_order_no);
		btn_search = (Button) findViewById(R.id.cashier_search_order_btnsearch);
		btn_pay = (Button) findViewById(R.id.cashier_search_order_ok);
		btn_cancel = (Button) findViewById(R.id.cashier_search_order_cancelorder);
		show_money = (TextView) findViewById(R.id.cashier_search_showmoney);
		lv = (ListView) findViewById(R.id.cashier_search_order_lv);

		btn_back.setOnClickListener(myclick);
		btn_search.setOnClickListener(myclick);
		btn_pay.setOnClickListener(myclick);
		btn_cancel.setOnClickListener(myclick);
		product_list = new ArrayList<ProductInfo>();
	}

	/**
	 * 刷新商品列表信息
	 * 
	 * @auth shouwei
	 */
	private void refreshData() {
		// 订单信息与订单金额不为空,则设置金额显示
		if (BaseUtils.IsNotEmpty(orderInfo)) {
			// 订单状态为未付状态,则设置付款按钮的可见与否
			if (orderInfo.co_status == 1) {
				btn_pay.setVisibility(View.VISIBLE);
				btn_cancel.setVisibility(View.VISIBLE);
			} else {
				btn_pay.setVisibility(View.GONE);
				btn_cancel.setVisibility(View.GONE);
			}
			show_money.setText("" + orderInfo.co_money);
			// 显示收费金额
			JBLEDInterface.convertLedControl();
			JBLEDInterface.ledDisplay(orderInfo.co_money + "");
		} else {
			show_money.setText("0");
			btn_pay.setVisibility(View.GONE);
			btn_cancel.setVisibility(View.GONE);
		}

		goodsAdapter = new CashierPickupGoodsAdapter(mActivity, product_list);
		goodsAdapter.setStatus(orderInfo);
		lv.setAdapter(goodsAdapter);
	}

	/**
	 * 根据订单号搜索订单
	 * 
	 * @auth shouwei
	 */
	private void searchOrder() {
		product_list.clear();
		if (BaseUtils.isNetworkAvailable(mActivity)) {
			C.openProgressDialog(mActivity, null, "正在查询订单...");
			ConnectManager.getInstance().getOrderFormDetailResult(str_order_no,
					ajaxCallBack);
		} else {
			C.showDialogAlert("当前网络不可用", mActivity);
		}
		BaseUtils.closeSoftKeyBoard(mActivity);
	}

	/**
	 * 检查输入是否为空
	 * 
	 * @auth shouwei
	 */
	private void checkIfInputNull(EditText et) {
		str_order_no = et_card_no.getText().toString().trim();
		if (BaseUtils.IsNotEmpty(str_order_no)) {
			searchOrder();
		} else {
			C.showShort("输入不能为空", mActivity);
		}
	}

	/**
	 * 打印收银信息
	 */
	private void printPaymentInfo() {
		// 开始打印
		Log.i("savion", "开始打印--------------");
		JBPrintInterface.convertPrinterControl();
		JBCashBoxInterface.openCashBox();
		// printBillInfo();
	};

	/**
	 * 单击事件
	 */
	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_search_order_btnback:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_search_order_btnsearch:
				checkIfInputNull(et_card_no);
				break;
			case R.id.cashier_search_order_ok:
				goActivity(GatheringForSearchOrderActivity.class,
						new OrderDetailInfo(orderInfo, product_list));
				orderInfo = null;
				product_list.clear();
				refreshData();
				break;
			case R.id.cashier_search_order_cancelorder:
				C.showDialogAlert("输入消费人卡号", true, "请输入消费人卡号", "完成", mActivity,
						new UpdatePsdListener() {
							@Override
							public void callBack(String psd, AlertDialog dialog) {
								consume_card_no = psd;
								dialog.dismiss();
								if (BaseUtils.isNetworkAvailable(mActivity)) {
									C.openProgressDialog(mActivity, null,
											"正在取消订单...");
									ConnectManager
											.getInstance()
											.getOrderFormCanaelResult(
													str_order_no,
													applicationCS.cashier_card_no,
													consume_card_no,
													ajaxcancelorder);
								} else {
									C.showDialogAlert("当前网络不可用", mActivity);
								}
							}
						});
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 查询订单回调
	 */
	AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {
		@SuppressWarnings("unchecked")
		public void onSuccess(String t) {
			super.onSuccess(t);
			C.closeProgressDialog();
			LG.i(getClass(), "订单查询回调--->" + t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				String product = JSONParser.getStringFromJsonString(
						"product_list", data);
				String order = JSONParser.getStringFromJsonString(
						"order_detail", data);
				product_list = (ArrayList<ProductInfo>) JSONParser.JSON2Array(
						product, ProductInfo.class);
				for (int i = 0; i < product_list.size(); i++) {
					if (product_list.get(i).cop_is_seller == 1) {
					}
				}
				orderInfo = (OrderInfo) JSONParser.JSON2Object(order,
						OrderInfo.class);
			} else {
				String error_msg = JSONParser.getStringFromJsonString(
						"error_desc", t);
				if (BaseUtils.IsNotEmpty(error_msg)) {
					C.showDialogAlert(error_msg, mActivity);
				} else {
					C.showDialogAlert("查询订单失败", mActivity);
				}
				product_list.clear();
				orderInfo = null;
			}
			refreshData();
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			C.showDialogAlert("查询订单失败", mActivity);
			product_list.clear();
			orderInfo = null;
			refreshData();
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
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_cancel_success),
							mActivity);
					checkIfInputNull(et_card_no);
				} else {
					C.showDialogAlert(
							resources
									.getString(R.string.cashier_system_alert_gathering_order_cancel_fail),
							mActivity);
				}
			} else {
				String msg = JSONParser
						.getStringFromJsonString("error_desc", t);
				if (BaseUtils.IsNotEmpty(msg)) {
					C.showDialogAlert(msg, mActivity);
				} else {
					C.showDialogAlert("取消订单失败", mActivity);
				}
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

}
