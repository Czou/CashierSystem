package com.shengxun.adapter;

import java.util.ArrayList;

import com.shengxun.cashiersystem.CheckBoxChangeListener;
import com.shengxun.cashiersystem.app.ApplicationCS;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 模块描述：封装基础适配器 2014-12-18 下午12:16:45 Write by LILIN
 */
public abstract class ABaseAdapter<T> extends BaseAdapter {

	protected ArrayList<T> dataList = null;;
	protected LayoutInflater inflater = null;;
	protected Activity mActivity = null;;
	protected Resources resources;
	protected ApplicationCS applicationCS = null;
	protected CheckBoxChangeListener listener;

	public ABaseAdapter(Activity mActivity, ArrayList<T> dataList) {
		this.dataList = dataList;
		this.mActivity = mActivity;
		resources = mActivity.getResources();
		applicationCS = (ApplicationCS) mActivity.getApplication();
		inflater = LayoutInflater.from(mActivity);
	}

	public ABaseAdapter(Activity mActivity, ArrayList<T> dataList,
			CheckBoxChangeListener listener) {
		this.dataList = dataList;
		this.mActivity = mActivity;
		this.listener = listener;
		resources = mActivity.getResources();
		applicationCS = (ApplicationCS) mActivity.getApplication();
		inflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		if (dataList != null) {
			return dataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (dataList != null && dataList.size() > position) {
			return dataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
