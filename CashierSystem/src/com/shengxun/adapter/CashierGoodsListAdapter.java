package com.shengxun.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.cashiersystem.R;
import com.shengxun.entity.GoodsInfo;
import com.shengxun.util.ViewHolder;
import com.zvezda.android.utils.BaseUtils;

/**
 * 模块描述：商品列表适配器
 * 2015-4-17 下午4:01:57
 * Write by LILIN
 */
public class CashierGoodsListAdapter extends ABaseAdapter<GoodsInfo>{


	public CashierGoodsListAdapter(Activity mActivity,ArrayList<GoodsInfo> dataList) {
		super(mActivity, dataList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = inflater.inflate(R.layout.cashier_goods_info_view, null);
		}
		GoodsInfo entity=(GoodsInfo) getItem(position);
		TextView cashier_system_column_sort = ViewHolder.get(convertView, R.id.cashier_system_column_sort);
		TextView cashier_system_column_cargo_number = ViewHolder.get(convertView, R.id.cashier_system_column_cargo_number);
		TextView cashier_system_column_code = ViewHolder.get(convertView, R.id.cashier_system_column_code);
		TextView cashier_system_column_name = ViewHolder.get(convertView, R.id.cashier_system_column_name);
		EditText cashier_system_column_count = ViewHolder.get(convertView, R.id.cashier_system_column_count);
		TextView cashier_system_column_old_price = ViewHolder.get(convertView, R.id.cashier_system_column_old_price);
		TextView cashier_system_column_now_price = ViewHolder.get(convertView, R.id.cashier_system_column_now_price);
		TextView cashier_system_column_money = ViewHolder.get(convertView, R.id.cashier_system_column_money);
		TextView cashier_system_column_clerk = ViewHolder.get(convertView, R.id.cashier_system_column_clerk);
		cashier_system_column_sort.setText(""+position);
		cashier_system_column_code.setText(""+entity.code);
		cashier_system_column_cargo_number.setText(""+entity.cargo_number);
		cashier_system_column_name.setText(""+entity.name);
		cashier_system_column_count.setText(""+entity.count);
		cashier_system_column_old_price.setText(""+entity.old_price);
		cashier_system_column_now_price.setText(""+entity.now_price);
		cashier_system_column_money.setText(""+entity.money);
		cashier_system_column_clerk.setText(""+entity.clerk);
		cashier_system_column_count.addTextChangedListener(new MyTextWatcher(position,cashier_system_column_money));
		return convertView;
	}
	private class MyTextWatcher implements TextWatcher{
		
		public int postion;
		public TextView cashier_system_column_money;
		public MyTextWatcher(int postion,TextView cashier_system_column_money) {
			this.postion = postion;
			this.cashier_system_column_money = cashier_system_column_money;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			//数量发生变化，总价改变
			if(BaseUtils.IsNotEmpty(s.toString())){
				int now=Integer.parseInt(s.toString());
				dataList.get(postion).count=now;
				dataList.get(postion).money=now*dataList.get(postion).now_price;
				cashier_system_column_money.setText(""+dataList.get(postion).money);
			}
		}
	};
}
