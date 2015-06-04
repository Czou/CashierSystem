package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.shengxun.adapter.CashierPickupGoodsAdapter;
import com.shengxun.constant.C;
import com.shengxun.entity.OrderInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;

/**
 * 查询订单详细信息界面
 * @author sw
 * @date 2015-5-15
 */
public class SearchOrderActivity extends BaseActivity {

	EditText et_card_no;
	Button btn_search,btn_back;
	ListView lv;
	String str_order_no;
	ArrayList<ProductInfo> product_list;
	OrderInfo orderInfo;
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
		lv = (ListView) findViewById(R.id.cashier_search_order_lv);

		btn_back.setOnClickListener(myclick);
		btn_search.setOnClickListener(myclick);
		product_list = new ArrayList<ProductInfo>();
	}

	/**
	 * 刷新商品列表信息
	 * 
	 * @auth shouwei
	 */
	private void refreshData(){
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
			}
			refreshData();
		};
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("查询订单失败", mActivity);
			refreshData();
		};
	};

}
