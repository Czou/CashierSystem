package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
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
import com.shengxun.constant.C;
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
 * @author sw
 * @date 2015-5-15
 */
public class SearchOrderActivity extends MyTimeLockBaseActivity {

	EditText et_card_no;
	TextView show_money;
	Button btn_search,btn_back,btn_pay;
	//查询显示listview
	ListView lv;
	//订单号与付款总额
	String str_order_no,pay_money;
	//是否需要付款
	boolean isPayable=false;
	//结果商品列表
	ArrayList<ProductInfo> product_list = new ArrayList<ProductInfo>();
	//订单信息
	OrderInfo orderInfo = new OrderInfo();
	//适配器
	CashierPickupGoodsAdapter goodsAdapter;


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
		show_money = (TextView) findViewById(R.id.cashier_search_order_money);
		lv = (ListView) findViewById(R.id.cashier_search_order_lv);

		btn_back.setOnClickListener(myclick);
		btn_search.setOnClickListener(myclick);
		btn_pay.setOnClickListener(myclick);
		product_list = new ArrayList<ProductInfo>();
	}

	/**
	 * 刷新商品列表信息
	 * 
	 * @auth shouwei
	 */
	private void refreshData(){
		//订单信息与订单金额不为空,则设置金额显示
		if(BaseUtils.IsNotEmpty(orderInfo)){
			//订单状态为未付状态,则设置付款按钮的可见与否
			if(orderInfo.co_status==1){
				btn_pay.setVisibility(View.INVISIBLE);
			}else{
				btn_pay.setVisibility(View.GONE);
			}
			show_money.setText(getResources().getString(R.string.cashier_system_column_total_money_withcolon)+orderInfo.co_money);
			// 显示收费金额
			JBLEDInterface.convertLedControl();
			JBLEDInterface.ledDisplay(orderInfo.co_money + "");
		}else{
			show_money.setText(getResources().getString(R.string.cashier_system_column_total_money_withcolon)+"0");
			btn_pay.setVisibility(View.GONE);
		}
		
		goodsAdapter = new CashierPickupGoodsAdapter(mActivity,product_list);
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
		ConnectManager.getInstance().getOrderFormDetailResult(str_order_no,
				ajaxCallBack);
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
		//printBillInfo();
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
				goActivity(GatheringActivity.class,product_list);
				GatheringActivity.setOrder(str_order_no);
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
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				String product = JSONParser.getStringFromJsonString("product_list", data);
				String order = JSONParser.getStringFromJsonString("order_detail", data);
				product_list = (ArrayList<ProductInfo>) JSONParser.JSON2Array(product, ProductInfo.class);
				orderInfo = (OrderInfo) JSONParser.JSON2Object(order, OrderInfo.class);
			} else {
				String error_msg = JSONParser.getStringFromJsonString("error_desc", t);
				C.showLong(error_msg, mActivity);
				product_list.clear();
				orderInfo = null;
			}
			refreshData();
		};
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("查询订单失败", mActivity);
			product_list.clear();
			orderInfo = null;
			refreshData();
		};
	};

}
