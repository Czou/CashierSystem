package com.shengxun.cashiersystem;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.constant.C;
import com.shengxun.entity.OrderDetailInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

/**
 * 退货功能界面
 * 
 * @author sw
 * @date 2015-5-4
 */
public class GoodsReturnActivity extends BaseActivity {

	// 订单号输入框
	EditText order_no;
	// 确认退款，返回和查询订单按钮
	Button ok, back, search_order;
	// 支付方式，1、现金，2、信用卡，3、储蓄卡，4、储值卡，目前只支持现金支付
	String pay_way = "1";
	// 收银员卡号
	String cashier_card_no, refund_order_no, order_money;
	// 退款金额
	TextView return_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cashier_goods_return_view);

		initWidget();
	}

	/**
	 * 初始化控件
	 * 
	 * @auth shouwei
	 */
	private void initWidget() {
		order_no = (EditText) findViewById(R.id.cashier_goods_return_order_no);
		ok = (Button) findViewById(R.id.cashier_goods_return_ok);
		back = (Button) findViewById(R.id.cashier_goods_return_exit);
		search_order = (Button) findViewById(R.id.cashier_goods_return_search_order);
		return_money = (TextView) findViewById(R.id.cashier_goods_return_money);

		ok.setOnClickListener(myclick);
		back.setOnClickListener(myclick);
		search_order.setOnClickListener(myclick);
	}

	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 确认退款
			case R.id.cashier_goods_return_ok:
				ConnectManager.getInstance().getReturnOrderFormResult(
						refund_order_no, ajaxcallback);
				break;
			// 返回
			case R.id.cashier_goods_return_exit:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			// 查询订单
			case R.id.cashier_goods_return_search_order:
				checkIfNull();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 检查输入是否为空
	 * 
	 * @auth shouwei
	 */
	private void checkIfNull() {
		String order = order_no.getText().toString().trim();
		if (BaseUtils.IsNotEmpty(order)) {
			// 查询该订单是否存在
			ConnectManager.getInstance().getOrderFormDetailResult(order,
					searchorder);
		} else {
			C.showShort(resources
					.getString(R.string.cashier_system_alert_no_order_number),
					mActivity);
		}
	}

	/**
	 * 退货订单退款回调
	 */
	AjaxCallBack<String> ajaxcallback = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("return", data).equals(
						"ok")) {
					C.showShort("退款成功", mActivity);
				} else {
					C.showShort("退款失败", mActivity);
				}
			} else {
				C.showShort("退款失败", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("退款失败", mActivity);
		};
	};
	/**
	 * 查询订单回调
	 */
	AjaxCallBack<String> searchorder = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				LG.i(getClass(), "t============>" + t);
				String data = JSONParser.getStringFromJsonString("data", t);
				OrderDetailInfo order = (OrderDetailInfo) JSONParser
						.JSON2Array(data, OrderDetailInfo.class);
				if (BaseUtils.IsNotEmpty(applicationCS.cashier_card_no)) {
					cashier_card_no = applicationCS.cashier_card_no;
				}
				// 创建退货订单
				ConnectManager.getInstance().getOrderFormRefundResult(
						order.getOrder_info().co_id, order.getProduct_info(),
						cashier_card_no, pay_way, orderreturnCallBack);
			} else {
				C.showShort("该订单并不存在", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("该订单并不存在", mActivity);
		};
	};
	/**
	 * 创建退货订单回调
	 */
	AjaxCallBack<String> orderreturnCallBack = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			if (JSONParser.getStringFromJsonString("status", t).equals("q")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				refund_order_no = JSONParser.getStringFromJsonString(
						"refund_order_id", data);
				order_money = JSONParser.getStringFromJsonString("order_money",
						data);
				// 创建退货订单成功
				return_money.setText(return_money.getText() + order_money);
				ok.setEnabled(true);
			} else {
				C.showShort("创建订单失败", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("创建订单失败", mActivity);
		};
	};
}
