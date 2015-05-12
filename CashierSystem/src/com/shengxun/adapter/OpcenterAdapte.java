package com.shengxun.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shengxun.cashiersystem.R;
import com.shengxun.entity.OpcenterInfo;
import com.shengxun.util.ViewHolder;

public class OpcenterAdapte extends ABaseAdapter<OpcenterInfo> {

	public OpcenterAdapte(Activity mActivity, ArrayList<OpcenterInfo> dataList) {
		super(mActivity, dataList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.cashier_area_item_view,
					null);
		}
		TextView tv = ViewHolder.get(convertView, R.id.area_item_tv);
		tv.setText(dataList.get(position).name);
		return convertView;
	}

}
