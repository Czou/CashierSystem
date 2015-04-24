package com.shengxun.cashiersystem;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsDetailActivity extends BaseActivity{
	/**
	 * 返回按钮
	 */
	private TextView goods_detail_back;
	/**
	 * 商品图片
	 */
	private ImageView goods_detail_iv;
	/**
	 * 删除与确定按钮
	 */
	private Button goods_detail_del,goods_detail_ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cahsier_goods_detail_view);
		
		initWidget();
	}

	/**
	 *初始化控件 
	 * sw
	 */
	private void initWidget(){
		goods_detail_back = (TextView) findViewById(R.id.cashier_goods_detail_back);
		goods_detail_del = (Button) findViewById(R.id.cashier_goods_detail_del);
		goods_detail_iv = (ImageView) findViewById(R.id.cashier_goods_detail_iv);
		goods_detail_ok = (Button) findViewById(R.id.cashier_goods_detail_ok);
		
		goods_detail_back.setOnClickListener(myclick);
		goods_detail_del.setOnClickListener(myclick);
		goods_detail_ok.setOnClickListener(myclick);
	}
	
	/**
	 * 点击事件
	 */
	OnClickListener myclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_goods_detail_back:
				finish();
				break;
			case R.id.cashier_goods_detail_del:
				
				break;
			case R.id.cashier_goods_detail_ok:
				
				break;
			default:
				break;
			}
		}
	};
}

