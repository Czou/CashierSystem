package com.shengxun.cashiersystem;

import com.zvezda.android.utils.BaseUtils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsDetailActivity extends BaseActivity {
	/**
	 * 返回按钮
	 */
	private TextView goods_detail_back;
	/**
	 * 商品图片
	 */
	private ImageView goods_detail_iv;
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
		goods_detail_iv = (ImageView) findViewById(R.id.cashier_goods_detail_iv);
		goods_detail_ok = (Button) findViewById(R.id.cashier_goods_detail_ok);
		goods_add = (Button) findViewById(R.id.cashier_goods_detail_add);
		goods_reduce = (Button) findViewById(R.id.cashier_goods_detail_reduce);
		show_count = (EditText) findViewById(R.id.cashier_goods_detail_showcount);

		goods_detail_back.setOnClickListener(myclick);
		goods_detail_del.setOnClickListener(myclick);
		goods_detail_ok.setOnClickListener(myclick);
		goods_add.setOnClickListener(myclick);
		goods_reduce.setOnClickListener(myclick);
		show_count.addTextChangedListener(mytextchange);
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
			case R.id.cashier_goods_detail_add:
				if (goods_count < 99) {
					goods_count++;
				}
				show_count.setText(goods_count + "");
				break;
			case R.id.cashier_goods_detail_reduce:
				if (goods_count > 0) {
					goods_count--;
				}
				show_count.setText(goods_count + "");
				break;
			default:
				break;
			}
		}
	};
	
	TextWatcher mytextchange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			Log.i("savion","afterTextChanged"+s+"");
			if(s.toString().length()>2){
				show_count.setText("99");
				goods_count=99;
			}else{
				if(BaseUtils.IsNotEmpty(s)){
					goods_count=Integer.parseInt(s.toString().trim());
				}
			}
		}
	};
}
