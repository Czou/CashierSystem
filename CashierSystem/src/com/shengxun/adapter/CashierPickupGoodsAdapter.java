package com.shengxun.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shengxun.cashiersystem.R;
import com.shengxun.entity.OrderInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ViewHolder;

/**
 * 订单商品信息工厂(通用于提货界面GoodsPickupActivity与查询订单界面SearchOrderActivity)
 * @author sw
 * @date 2015-5-15
 */
public class CashierPickupGoodsAdapter extends ABaseAdapter<ProductInfo> {

	private OrderInfo status;

	public CashierPickupGoodsAdapter(Activity mActivity,
			ArrayList<ProductInfo> dataList) {
		super(mActivity, dataList);
		// TODO Auto-generated constructor stub
	}

	public void setStatus(OrderInfo status) {
		this.status = status;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.cashier_pickup_goods_list_item, null);
		}
		TextView cashier_goods_name = ViewHolder.get(convertView,
				R.id.cashier_pickup_item_goods_name);
		TextView cashier_goods_count = ViewHolder.get(convertView,
				R.id.cashier_pickup_item_goods_count);
		TextView cashier_goods_status = ViewHolder.get(convertView,
				R.id.cashier_pickup_item_goods_status);
		TextView cashier_goods_price = ViewHolder.get(convertView,
				R.id.cashier_pickup_item_goods_single_price);
		cashier_goods_name.setText(dataList.get(position).qp_name + "");
		cashier_goods_count.setText(dataList.get(position).cop_number + "");
		cashier_goods_price.setText(dataList.get(position).cop_price+"");
		if (status != null) {
			switch (status.co_status) {
			case 1:
				cashier_goods_status.setText("正常");
				break;
			case 2:
				if(dataList.get(position).refund_number>0){
					cashier_goods_status.setText("已付款");
				}else{
					cashier_goods_status.setText("已取消");
				}
				break;
			case 3:
				cashier_goods_status.setText("已取消");
				break;
			default:
				break;
			}
		}
		return convertView;
	}

}
