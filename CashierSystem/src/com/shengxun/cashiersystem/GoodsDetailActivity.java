package com.shengxun.cashiersystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.constant.C;
import com.shengxun.entity.ProductInfo;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;

/**
 * 商品详细信息界面
 * 
 * @author sw
 * @date 2015-4-24
 */
public class GoodsDetailActivity extends BaseActivity {
	/**
	 * 返回按钮
	 */
	private TextView goods_detail_back, old_price, new_price, total_price,
			title;
	/**
	 * 删除,确定,增加数量，减少数量按钮
	 */
	private Button goods_detail_del, goods_detail_ok, goods_add, goods_reduce;
	/**
	 * 显示数量
	 */
	private EditText show_count;
	/**
	 * 商品数量
	 */
	private int goods_count = 0;
	/**
	 * 保存传递过来的商品数据
	 */
	private ProductInfo product;
	/**
	 * 售价与总额
	 */
	private double new_price_d, total_price_d = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cahsier_goods_detail_view);

		initWidget();
	}

	/**
	 * 初始化控件 sw
	 */
	private void initWidget() {
		goods_detail_back = (TextView) findViewById(R.id.cashier_goods_detail_back);
		goods_detail_del = (Button) findViewById(R.id.cashier_goods_detail_del);
		goods_detail_ok = (Button) findViewById(R.id.cashier_goods_detail_ok);
		goods_add = (Button) findViewById(R.id.cashier_goods_detail_add);
		goods_reduce = (Button) findViewById(R.id.cashier_goods_detail_reduce);
		show_count = (EditText) findViewById(R.id.cashier_goods_detail_showcount);
		old_price = (TextView) findViewById(R.id.cashier_goods_detail_old_price);
		new_price = (TextView) findViewById(R.id.cashier_goods_detail_new_price);
		total_price = (TextView) findViewById(R.id.cashier_goods_detail_total_price);
		title = (TextView) findViewById(R.id.cashier_goods_detail_title);
		goods_detail_back.setOnClickListener(myclick);
		goods_detail_del.setOnClickListener(myclick);
		goods_detail_ok.setOnClickListener(myclick);
		goods_add.setOnClickListener(myclick);
		goods_reduce.setOnClickListener(myclick);
		show_count.addTextChangedListener(mytextchange);

		product = (ProductInfo) getIntent().getSerializableExtra("DATA");
		LG.i(getClass(), "product--------->" + product);
		refreshData();
	}

	/**
	 * 更新显示数据
	 */
	private void refreshData() {
		title.setText(product.qp_name);
		show_count.setText(product.buy_number + "");
		// 设置edittext的光标位置
		show_count
				.setSelection(show_count.getText().toString().trim().length());
		goods_count = product.buy_number;
		old_price.setText(product.op_market_price + "");
		new_price.setText(product.op_market_price + "");
		new_price_d = product.op_market_price;
		calTotalPrice();
	}

	/**
	 * 计算总额
	 * 
	 * @auth sw
	 */
	private void calTotalPrice() {
		total_price_d = new_price_d * goods_count;
		total_price.setText(total_price_d + "");
		// 更改实体的数据
		product.buy_number = goods_count;
	}

	/**
	 * 点击事件
	 */
	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 点击了返回按钮
			case R.id.cashier_goods_detail_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			// 点击删除按钮,取消订单
			case R.id.cashier_goods_detail_del:
				if (MainActivity.instance != null) {
					MainActivity.instance.deleteGoods(product);
					AppManager.getAppManager().finishActivity(mActivity);
					C.showShort(
							resources.getString(R.string.cashier_system_alert_order_detail_modify_success),mActivity);
				}
				break;
			// 点击确定按钮
			case R.id.cashier_goods_detail_ok:
				if (MainActivity.instance != null) {
					MainActivity.instance.updateGoods(product);
					AppManager.getAppManager().finishActivity(mActivity);
					C.showShort(resources.getString(R.string.cashier_system_alert_order_detail_modify_success),mActivity);
				}
				break;
			// 点击增加数量
			case R.id.cashier_goods_detail_add:
				if (goods_count < 99) {
					goods_count++;
				}
				show_count.setText(goods_count + "");
				show_count.setSelection(show_count.getText().toString().trim()
						.length());
				break;
			// 点击减少数量
			case R.id.cashier_goods_detail_reduce:
				if (goods_count > 0) {
					goods_count--;
				}
				show_count.setText(goods_count + "");
				show_count.setSelection(show_count.getText().toString().trim()
						.length());
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 输入框文本改变监听
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
			// 最多允许输入99
			if (s.toString().length() > 2) {
				show_count.setText("99");
				show_count.setSelection(show_count.length());
				goods_count = 99;
			} else {
				if (BaseUtils.IsNotEmpty(s)) {
					goods_count = Integer.parseInt(s.toString().trim());
				} else {
					goods_count = 0;
				}
			}
			calTotalPrice();
		}
	};

}
