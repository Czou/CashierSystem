package com.shengxun.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shengxun.cashiersystem.R;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ViewHolder;

public class CashierPickupGoodsAdapter extends ABaseAdapter<ProductInfo>{

	public CashierPickupGoodsAdapter(Activity mActivity,
			ArrayList<ProductInfo> dataList) {
		super(mActivity, dataList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.cashier_pickup_goods_list_item, null);
		}
		TextView cashier_goods_name = ViewHolder.get(convertView, R.id.cashier_pickup_item_goods_name);
		TextView cashier_goods_count = ViewHolder.get(convertView, R.id.cashier_pickup_item_goods_count);
		cashier_goods_name.setText(dataList.get(position).qp_name+"");
		cashier_goods_count.setText(dataList.get(position).cop_number+"");
		return convertView;
	}
	

}
