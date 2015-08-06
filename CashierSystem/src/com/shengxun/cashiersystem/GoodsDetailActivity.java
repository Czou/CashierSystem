package com.shengxun.cashiersystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.BaseInputConnection;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class GoodsDetailActivity extends MyTimeLockBaseActivity {
	/**
	 * 返回按钮
	 */
	private TextView goods_detail_back, old_price, total_price, title;
	/**
	 * 商品单价
	 */
	private EditText new_price;
	/**
	 * 删除,确定,增加数量，减少数量按钮
	 */
	private Button goods_detail_del, goods_detail_ok, goods_add, goods_reduce;
	/**
	 * 显示数量
	 */
	private EditText show_count;

	/**
	 * 商品图片
	 */
	private ImageView cashier_goods_detail_iv;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cahsier_goods_detail_view);

		initWidget();
	}

	/**
	 * 初始化控件 sw
	 */
	private void initWidget() {
		cashier_goods_detail_iv = (ImageView) findViewById(R.id.cashier_goods_detail_iv);
		goods_detail_back = (TextView) findViewById(R.id.cashier_goods_detail_back);
		goods_detail_del = (Button) findViewById(R.id.cashier_goods_detail_del);
		goods_detail_ok = (Button) findViewById(R.id.cashier_goods_detail_ok);
		goods_add = (Button) findViewById(R.id.cashier_goods_detail_add);
		goods_reduce = (Button) findViewById(R.id.cashier_goods_detail_reduce);
		show_count = (EditText) findViewById(R.id.cashier_goods_detail_showcount);
		old_price = (TextView) findViewById(R.id.cashier_goods_detail_old_price);
		new_price = (EditText) findViewById(R.id.cashier_goods_detail_new_price);
		total_price = (TextView) findViewById(R.id.cashier_goods_detail_total_price);
		title = (TextView) findViewById(R.id.cashier_goods_detail_title);
		goods_detail_back.setOnClickListener(myclick);
		goods_detail_del.setOnClickListener(myclick);
		goods_detail_ok.setOnClickListener(myclick);
		goods_add.setOnClickListener(myclick);
		goods_reduce.setOnClickListener(myclick);

		show_count.addTextChangedListener(mytextchange);
		product = (ProductInfo) getIntent().getSerializableExtra("DATA");
		if (!product.isProductInSystem) {
			new_price.addTextChangedListener(mWatcher);
		}
		refreshData();
	}

	/**
	 * 更新显示数据
	 */
	private void refreshData() {
		Log.i("savion", "buy number11==" + product.buy_number);
		goods_count = product.buy_number;
		// 此商品不是在系统中
		if (!product.isProductInSystem) {
			new_price.setEnabled(true);
			new_price.setFocusable(true);
			new_price.setHint("请输入非合作商品价格");
			if (product.op_market_price > 0) {
				new_price.setText(product.op_market_price+"");
				old_price.setText(product.op_market_price+"");
			} else {
				new_price.setText("");
				old_price.setText("");
			}
		} else {
			new_price.setEnabled(false);
			old_price.setText(product.op_market_price + "");
			new_price.setText(product.op_market_price + "");
		}
		Log.i("savion", "buy number22==" + product.buy_number);
		title.setText(product.qp_name);
		new_price_d = product.op_market_price;
		show_count.setText(product.buy_number + "");
		// 设置edittext的光标位置
		show_count
				.setSelection(show_count.getText().toString().trim().length());
		if (BaseUtils.IsNotEmpty(product.img_url)) {
			FinalBitmap.create(mActivity).display(cashier_goods_detail_iv,
					"" + product.img_url);
		}
		calTotalPrice();
	}

	/**
	 * 计算总额
	 * 
	 * @auth sw
	 */
	@SuppressLint("NewApi")
	private void calTotalPrice() {
		total_price_d = 0;
		total_price_d = new_price_d * goods_count;

		// 格式化数值，保留两位小数并四舍五入
		DecimalFormat format = new DecimalFormat("#,##0.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		total_price.setText(BaseUtils.formatDouble(total_price_d));

		// 更改实体的数据
		product.buy_number = goods_count;
		product.op_market_price = new_price_d;
	}

	TextWatcher mWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			Log.i("savion","beforeTextChanged === "+s+",count:"+count+",after:"+after+"");
		}

		@Override
		public void afterTextChanged(Editable s) {
			LG.i(getClass(), "TEXTWATCH === " + s);
			if(BaseUtils.IsNotEmpty(s)&&BaseUtils.isNumber(s.toString())){
				old_price.setText(s);
				new_price_d = Double.parseDouble(s.toString().trim());
			}else{
				old_price.setText("");
				new_price_d=0d;
			}
			calTotalPrice();
		}
	};

	/**
	 * 点击事件
	 */
	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 点击了返回按钮
			case R.id.cashier_goods_detail_back:
				if (product != null) {
					// 若是非系统商品则需要输入金额
					if (!product.isProductInSystem) {
						if (total_price_d == 0) {
							C.showDialogAlert("请输入商品单价再按确定键修改商品返回", mActivity);
						} else {
							C.showDialogAlert("请按确定键修改商品或删除商品", mActivity);
						}
					} else {
						AppManager.getAppManager().finishActivity(mActivity);
					}
				}
				break;
			// 点击删除按钮,取消订单
			case R.id.cashier_goods_detail_del:
				if (MainActivity.instance != null) {
					MainActivity.instance.deleteGoods(product);
					AppManager.getAppManager().finishActivity(mActivity);
					C.showShort(
							resources
									.getString(R.string.cashier_system_alert_order_detail_modify_success),
							mActivity);
				}
				break;
			// 点击确定按钮
			case R.id.cashier_goods_detail_ok:
				if (total_price_d == 0) {
					C.showDialogAlert("请输入商品单价或数量", mActivity);
					return;
				}
				if (MainActivity.instance != null) {
					MainActivity.instance.updateGoods(product);
					AppManager.getAppManager().finishActivity(mActivity);
					C.showShort(
							resources
									.getString(R.string.cashier_system_alert_order_detail_modify_success),
							mActivity);
				}
				break;
			// 点击增加数量
			case R.id.cashier_goods_detail_add:
				if (goods_count < C.MAX_PRODUCES) {
					++goods_count;
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
			Log.i("savion","ss==>"+s);
			// 最多允许输入 C.MAX_PRODUCES
			if (s.toString().length() > 5) {
				show_count.setText("" + C.MAX_PRODUCES);
				show_count.setSelection(show_count.length());
				goods_count = C.MAX_PRODUCES;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (product != null) {
				if (!product.isProductInSystem) {
					if (total_price_d == 0) {
						C.showDialogAlert("请输入商品单价再按确定键修改商品返回", mActivity);
					} else {
						C.showDialogAlert("请按确定键修改商品或删除商品返回", mActivity);
					}
					return false;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
