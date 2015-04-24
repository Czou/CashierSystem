package com.shengxun.cashiersystem;

import android.os.Bundle;
import android.view.Window;

public class GoodsDetailActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cahsier_goods_detail_view);
	}

}
