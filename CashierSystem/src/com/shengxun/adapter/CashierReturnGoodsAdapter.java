package com.shengxun.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.shengxun.cashiersystem.CheckBoxChangeListener;
import com.shengxun.cashiersystem.R;
import com.shengxun.entity.OrderInfo;
import com.shengxun.entity.ProductInfo;
import com.shengxun.util.ViewHolder;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;

public class CashierReturnGoodsAdapter extends ABaseAdapter<ProductInfo> {

	OrderInfo order;

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
		final TextView cashier_goods_number = ViewHolder.get(convertView,
				R.id.cashier_return_item_showcount);
		Button add = ViewHolder.get(convertView, R.id.cashier_return_item_add);
		Button reduce = ViewHolder.get(convertView,
				R.id.cashier_return_item_reduce);
		CheckBox cashier_goods_cb = ViewHolder.get(convertView,
				R.id.cashier_return_item_goods_cb);
		LG.i(getClass(), convertView.getTag() + "");

		cashier_goods_name.setText(entity.qp_name + "");
		cashier_goods_number.setText(entity.cop_number + "");
		switch (order.co_status) {
		case 1:
			cashier_is_return.setText("正常");
			break;
		case 2:
			cashier_is_return.setText("已付");
			break;
		case 3:
			cashier_is_return.setText("已取消");
			break;
		default:
			break;
		}
		cashier_goods_cb.setChecked(false);
		final int mPosition = position;

		cashier_goods_cb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							dataList.get(mPosition).isChecked = true;
						} else {
							dataList.get(mPosition).isChecked = false;
						}
						listener.setCheckedPosition(dataList);
					}
				});
		return convertView;
	}

}
