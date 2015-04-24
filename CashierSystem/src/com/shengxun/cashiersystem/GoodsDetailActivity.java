package com.shengxun.cashiersystem;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;

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
/**
 * 商品详细信息界面
 * @author sw
 * @date 2015-4-24
 */
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
		
		//获得产品信息
		//ConnectManager.getInstance.
	}
	/**
	 * 更新显示数据
	 */
	private void updateData(){
		
	}

	/**
	 * 点击事件
	 */
	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//点击了返回按钮
			case R.id.cashier_goods_detail_back:
				finish();
				break;
			//点击删除按钮
			case R.id.cashier_goods_detail_del:

				break;
			//点击确定按钮	
			case R.id.cashier_goods_detail_ok:

				break;
			//点击增加数量
			case R.id.cashier_goods_detail_add:
				if (goods_count < 99) {
					goods_count++;
				}
				show_count.setText(goods_count + "");
				break;
			//点击减少数量
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
	/**
	 * 输入框文本改变监听
	 */
	TextWatcher mytextchange = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
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
			Log.i("savion", "afterTextChanged" + s + "");
			//最多允许输入99
			if (s.toString().length() > 2) {
				show_count.setText("99");
				goods_count = 99;
			} else {
				if (BaseUtils.IsNotEmpty(s)) {
					goods_count = Integer.parseInt(s.toString().trim());
				}
			}
		}
	};

	/**
	 * 获得产品信息回调
	 */
	AjaxCallBack<String> ajaxcallback = new AjaxCallBack<String>() {
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "产品详细信息----->" + t);
			updateData();
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
		};
	};
}
