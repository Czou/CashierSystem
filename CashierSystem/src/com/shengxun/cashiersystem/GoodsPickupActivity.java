package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.os.Handler;
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
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

/**
 * 订单提货界面
 * 
 * @author sw
 * @date 2015-5-4
 */
public class GoodsPickupActivity extends MyTimeLockBaseActivity {

	private EditText et_order_no, et_card_no;
	private Button ok, exit, swing_card, check_order;
	private String card_no, order_no;
	private ArrayList<ProductInfo> product_list;
	// 适配器
	private CashierPickupGoodsAdapter cpga;
	private ListView lv;
	// 总额
	private double total_money = 0;
	private TextView show_money;
	private OrderInfo status;
	// 是否查询订单，如未查询则不允许提货
	private boolean hasSearchOrder = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cashier_goods_pickup_view);

		initWidget();
	}

	private void initWidget() {
		product_list = new ArrayList<ProductInfo>();
		et_order_no = (EditText) findViewById(R.id.cashier_goods_pickup_order_no);
		ok = (Button) findViewById(R.id.cashier_goods_pickup_ok);
		exit = (Button) findViewById(R.id.cashier_goods_pickup_exit);
		et_card_no = (EditText) findViewById(R.id.cashier_goods_pickup_card_no);
		swing_card = (Button) findViewById(R.id.cashier_goods_pickup_swing_card);
		check_order = (Button) findViewById(R.id.cashier_goods_pickup_checkorder);
		lv = (ListView) findViewById(R.id.cashier_goods_pickup_lv);
		show_money = (TextView) findViewById(R.id.cashier_goods_pickup_showmoney);

		ok.setOnClickListener(myclick);
		check_order.setOnClickListener(myclick);
		exit.setOnClickListener(myclick);
		swing_card.setOnClickListener(myclick);
	}
	
	/**
	 * 刷新商品列表信息
	 * 
	 * @param list
	 * @auth shouwei
	 */
	private void refreshGoodsData(ArrayList<ProductInfo> list) {
		cpga = new CashierPickupGoodsAdapter(mActivity, list);
		cpga.setStatus(status);
		lv.setAdapter(cpga);
		total_money = 0;
		for (int i = 0; i < list.size(); i++) {
			total_money += list.get(i).cop_number * list.get(i).cop_price;
		}
		show_money.setText(total_money+"");
	}

	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 开始订单提货
			case R.id.cashier_goods_pickup_ok:
				if (BaseUtils.IsNotEmpty(applicationCS.cashier_card_no)) {
					if (product_list != null && product_list.size() > 0) {
						ConnectManager.getInstance().getOrderFormPickUpResult(
								order_no, card_no,
								applicationCS.cashier_card_no, ajaxCallBack);
					}else{
						C.showDialogAlert("请先查单", mActivity);
					}
				}
				break;
			// 退出
			case R.id.cashier_goods_pickup_exit:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_goods_pickup_swing_card:
				// 验证卡号
				break;
			case R.id.cashier_goods_pickup_checkorder:
				order_no = et_order_no.getText().toString().trim();
				card_no = et_card_no.getText().toString().trim();
				// 验证消费者卡号非空
				if (BaseUtils.IsNotEmpty(card_no)) {
					// 验证订单号非空
					if (BaseUtils.IsNotEmpty(order_no)) {
						product_list.clear();
						show_money.setText("");
						// 查询该取货店订单是否存在
						ConnectManager.getInstance()
								.getOrderFormDeliveryDetailResult(order_no,
										ordercheck);
					} else {
						C.showDialogAlert("请输入订单号", mActivity);
					}
					BaseUtils.closeSoftKeyBoard(mActivity);
				} else {
					C.showDialogAlert("请刷卡", mActivity);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 订单提货接口回调
	 */
	AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {

		public void onSuccess(String t) {
			super.onSuccess(t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("result", data).equals(
						"ok")) {
					C.showDialogAlert("订单提货成功,3秒后将自动关闭些窗口", mActivity);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							AppManager.getAppManager().finishActivity(GoodsPickupActivity.this);
						}
					}, 3000);
				} else {
					C.showDialogAlert("订单提货失败", mActivity);
				}
			} else {
				C.showDialogAlert(
						JSONParser.getStringFromJsonString("error_desc", t),
						mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showDialogAlert("订单提货失败", mActivity);
		};
	};

	

	/**
	 * 订单信息回调
	 */
	AjaxCallBack<String> ordercheck = new AjaxCallBack<String>() {
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showDialogAlert("订单错误", mActivity);
			refreshGoodsData(product_list);
		};

		@SuppressWarnings("unchecked")
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "pick up t ====>" + t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				String order_detail = JSONParser.getStringFromJsonString(
						"order_detail", data);
				status = (OrderInfo) JSONParser.JSON2Object(order_detail,
						OrderInfo.class);
				String product_detail = JSONParser.getStringFromJsonString(
						"product_list", data);
				OrderInfo od = (OrderInfo) JSONParser.JSON2Object(order_detail,
						OrderInfo.class);
				product_list = (ArrayList<ProductInfo>) JSONParser.JSON2Array(
						product_detail, ProductInfo.class);
			} else {
				C.showDialogAlert(
						JSONParser.getStringFromJsonString("error_desc", t),
						mActivity);
			}
			refreshGoodsData(product_list);
		};
	};
}
