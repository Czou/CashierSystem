package com.shengxun.cashiersystem;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.constant.C;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 订单提货界面
 * 
 * @author sw
 * @date 2015-5-4
 */
public class GoodsPickupActivity extends BaseActivity {

	EditText order_no, card_no;
	Button ok, exit, swing_card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cashier_goods_pickup_view);

		initWidget();
	}

	private void initWidget() {
		order_no = (EditText) findViewById(R.id.cashier_goods_pickup_order_no);
		ok = (Button) findViewById(R.id.cashier_goods_pickup_ok);
		exit = (Button) findViewById(R.id.cashier_goods_pickup_exit);
		card_no = (EditText) findViewById(R.id.cashier_goods_pickup_card_no);
		swing_card = (Button) findViewById(R.id.cashier_goods_pickup_swing_card);

		ok.setOnClickListener(myclick);
		exit.setOnClickListener(myclick);
		swing_card.setOnClickListener(myclick);
	}

	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_goods_pickup_ok:
				//验证订单号
				if (checkIfNull(order_no)) {
					ConnectManager.getInstance().getOrderFormPickUpResult(
							order_no.getText().toString().trim(), ajaxCallBack);
				}
				break;
			case R.id.cashier_goods_pickup_exit:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_goods_pickup_swing_card:
				//验证卡号
				if (checkIfNull(card_no)) {
					
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 检查输入框是否为空
	 * 
	 * @auth shouwei
	 */
	private boolean checkIfNull(EditText et) {
		String et_str = et.getText().toString().trim();
		if (BaseUtils.IsNotEmpty(et_str)) {
			return true;
		} else {
			C.showShort(resources
					.getString(R.string.cashier_system_alert_no_order_number),
					mActivity);
			return false;
		}
	}

	AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {

		public void onSuccess(String t) {
			super.onSuccess(t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				if (JSONParser.getStringFromJsonString("result", data).equals(
						"ok")) {
					C.showShort("订单提货成功", mActivity);
					AppManager.getAppManager().finishActivity(mActivity);
				} else {
					C.showShort("订单提货失败", mActivity);
				}
			} else {
				C.showShort("订单提货失败", mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("订单提货失败", mActivity);
		};
	};
}
