package com.shengxun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.cashiersystem.CheckBoxChangeListener;
import com.shengxun.cashiersystem.R;
import com.shengxun.constant.C;
import com.shengxun.entity.OrderInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ViewHolder;
import com.zvezda.android.utils.LG;

public class CashierReturnGoodsAdapter extends ABaseAdapter<ProductInfo> {

	OrderInfo order;
	List<Integer> product_number;

	public CashierReturnGoodsAdapter(Activity mActivity,
			ArrayList<ProductInfo> dataList) {
		super(mActivity, dataList);
		
	}

	public void setOrderInfo(OrderInfo order) {
		this.order = order;
	}

	/**
	 * 带checkbox状态改变监听的构造
	 * 
	 * @param mActivity
	 * @param dataList
	 * @param listener
	 */
	public CashierReturnGoodsAdapter(Activity mActivity,
			ArrayList<ProductInfo> dataList, CheckBoxChangeListener listener) {
		super(mActivity, dataList, listener);
		//保存初始商品列表数量,
		product_number = new ArrayList<Integer>();
		for (int i = 0; i < dataList.size(); i++) {
			product_number.add(dataList.get(i).cop_number);
		}
		LG.i(getClass(), "size ====>"+product_number.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.cashier_return_goods_list_item, null);
		}
		ProductInfo entity = (ProductInfo) getItem(position);
		TextView cashier_goods_name = ViewHolder.get(convertView,
				R.id.cashier_return_item_goods_name);
		TextView cashier_is_return = ViewHolder.get(convertView,
				R.id.cashier_return_item_is_return);
		TextView cashier_goods_number = ViewHolder.get(convertView,
				R.id.cashier_return_item_showcount);
		Button add = ViewHolder.get(convertView, R.id.cashier_return_item_add);
		Button reduce = ViewHolder.get(convertView,
				R.id.cashier_return_item_reduce);
		CheckBox cashier_goods_cb = ViewHolder.get(convertView,
				R.id.cashier_return_item_goods_cb);

		add.setOnClickListener(new MyClick(position, cashier_goods_number));
		reduce.setOnClickListener(new MyClick(position, cashier_goods_number));
		cashier_goods_cb
				.setOnCheckedChangeListener(new MyCheckChange(position));

		cashier_goods_name.setText(entity.qp_name + "");
		cashier_goods_number.setText(entity.cop_number + "");
		//订单状态
		switch (order.co_status) {
		case 1:
			cashier_is_return.setText("未退款");
			break;
		case 2:
			cashier_is_return.setText("已退款");
			break;
		case 3:
			cashier_is_return.setText("已取消");
			break;
		default:
			break;
		}

		return convertView;
	}

	// 单击事件
	class MyClick implements OnClickListener {

		int position;
		TextView et;

		public MyClick(int position, TextView et) {
			this.position = position;
			this.et = et;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_return_item_add:
				if (dataList.get(position).cop_number < product_number
						.get(position)) {
					dataList.get(position).cop_number += 1;
					et.setText(dataList.get(position).cop_number + "");
					listener.setCheckedPosition(dataList);
				}else{
					C.showShort("退货数量不能大于订单最大数量", mActivity);
				}
				break;
			case R.id.cashier_return_item_reduce:
				if (dataList.get(position).cop_number > 0) {
					dataList.get(position).cop_number -= 1;
					et.setText(dataList.get(position).cop_number + "");
					listener.setCheckedPosition(dataList);
				}else{
					C.showShort("退货数量不能小于0", mActivity);
				}
				break;
			default:
				break;
			}
		}
	}

	// checkbox状态改变事件
	class MyCheckChange implements OnCheckedChangeListener {
		int position;

		public MyCheckChange(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			dataList.get(position).isChecked = !dataList.get(position).isChecked;
			notifyDataSetChanged();
			listener.setCheckedPosition(dataList);
		}
	}

}
