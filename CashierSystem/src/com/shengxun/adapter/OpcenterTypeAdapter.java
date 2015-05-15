package com.shengxun.adapter;

import java.util.ArrayList;

import com.shengxun.cashiersystem.R;
import com.shengxun.entity.OpcenterTypeInfo;
import com.shengxun.util.ViewHolder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OpcenterTypeAdapter extends ABaseAdapter<OpcenterTypeInfo> {

	public OpcenterTypeAdapter(Activity mActivity, ArrayList<OpcenterTypeInfo> dataList) {
		super(mActivity, dataList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.cashier_area_item_view,null);
		}
		TextView tv = ViewHolder.get(convertView, R.id.area_item_tv);
		tv.setText(dataList.get(position).getName());
		return convertView;
	}

}
