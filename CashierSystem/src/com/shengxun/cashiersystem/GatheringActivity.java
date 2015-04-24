package com.shengxun.cashiersystem;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/**
 * 收款界面
 * @author sw
 * @date 2015-4-24
 */
public class GatheringActivity extends BaseActivity{
	/**
	 * 返回按钮
	 */
	TextView gathering_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_gathering_view);
		
		initWidget();
		
	}
	/**
	 * 初始化view
	 * sw
	 */
	private void initWidget(){
		gathering_back = (TextView) findViewById(R.id.cashier_gathering_back);
		
		gathering_back.setOnClickListener(myclick);
		
	}
	/**
	 * 点击事件
	 */
	OnClickListener myclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.cashier_gathering_back:
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	

}
