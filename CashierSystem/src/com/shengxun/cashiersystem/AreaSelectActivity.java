package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.adapter.AreaAdapte;
import com.shengxun.adapter.OpcenterAdapte;
import com.shengxun.constant.C;
import com.shengxun.entity.AreaInfo;
import com.shengxun.entity.OpcenterInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

/**
 * 选择地区运营中心
 * 
 * @author sw
 * @date 2015-5-8
 */
public class AreaSelectActivity extends BaseActivity {

	Spinner sp_province, sp_city, sp_town, sp_opcenter;
	/**
	 * spinner标记，用来区分当前改变的spinner，1:province,2:city,3:town,4:opcenter
	 */
	int areaFlag = 1;
	/**
	 * 省，市，县的数据list
	 */
	ArrayList<AreaInfo> provinceList, cityList, townList;
	/**
	 * 招商中心数据list
	 */
	ArrayList<OpcenterInfo> opcenterList;
	/**
	 * 当前省，市，县，招商中心以及父id
	 */
	String province, city, town, opcenter, parent_id = "";
	/**
	 * 返回，确定按钮
	 */
	Button btn_back,btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.area_select_view);

		initWidget();
		initAreaData();
	}

	/**
	 * 初始化控件
	 * 
	 * @auth shouwei
	 */
	private void initWidget() {
		sp_province = (Spinner) findViewById(R.id.area_select_province);
		sp_city = (Spinner) findViewById(R.id.area_select_city);
		sp_town = (Spinner) findViewById(R.id.area_select_town);
		sp_opcenter = (Spinner) findViewById(R.id.area_select_opcenter);
		btn_back = (Button) findViewById(R.id.cashier_area_select_back);
		btn_ok = (Button) findViewById(R.id.cashier_area_select_ok);

		sp_province.setOnItemSelectedListener(myitemclick);
		sp_city.setOnItemSelectedListener(myitemclick);
		sp_town.setOnItemSelectedListener(myitemclick);
		sp_opcenter.setOnItemSelectedListener(myitemclick);
		btn_back.setOnClickListener(myclick);
		btn_ok.setOnClickListener(myclick);
	}

	/**
	 * 初始化地区信息
	 * 
	 * @auth shouwei
	 */
	private void initAreaData() {
		provinceList = new ArrayList<AreaInfo>();
		areaFlag = 1;
		ConnectManager.getInstance()
				.getAreaResult("2", parent_id, areacallback);
	}

	/**
	 * 刷新spinner信息
	 * 
	 * @param list
	 * @param flag
	 * @auth shouwei
	 */
	private void refreshAreaData(ArrayList<?> list, int flag) {
		if (areaFlag == 1) {
			@SuppressWarnings("unchecked")
			AreaAdapte areaAdapter = new AreaAdapte(mActivity,
					(ArrayList<AreaInfo>) list);
			sp_province.setAdapter(areaAdapter);
		} else if (areaFlag == 2) {
			@SuppressWarnings("unchecked")
			AreaAdapte areaAdapter = new AreaAdapte(mActivity,
					(ArrayList<AreaInfo>) list);
			sp_city.setAdapter(areaAdapter);
		} else if (areaFlag == 3) {
			@SuppressWarnings("unchecked")
			AreaAdapte areaAdapter = new AreaAdapte(mActivity,
					(ArrayList<AreaInfo>) list);
			sp_town.setAdapter(areaAdapter);
		} else if (areaFlag == 4) {
			@SuppressWarnings("unchecked")
			OpcenterAdapte opcenterAdapter = new OpcenterAdapte(mActivity,
					(ArrayList<OpcenterInfo>) list);
			sp_opcenter.setAdapter(opcenterAdapter);
		}
	}

	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_area_select_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_area_select_ok:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			default:
				break;
			}
		}
	};

	OnItemSelectedListener myitemclick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> av, View view, int position,
				long id) {
			LG.i(getClass(), "arg0= =>" + av.getId());
			LG.i(getClass(), "arg1= =>" + view.getId());
			LG.i(getClass(), "arg2= =>" + position);
			LG.i(getClass(), "arg3= =>" + id);
			switch (av.getId()) {
			case R.id.area_select_province:
				areaFlag = 2;
				ConnectManager.getInstance().getAreaResult("3",
						provinceList.get(position).aid, areacallback);
				province = provinceList.get(position).name;
				break;
			case R.id.area_select_city:
				areaFlag = 3;
				ConnectManager.getInstance().getAreaResult("4",
						cityList.get(position).aid, areacallback);
				city = cityList.get(position).name;
				break;
			case R.id.area_select_town:
				areaFlag = 4;
				town = townList.get(position).name;
				ConnectManager.getInstance().getOpcenterResult("", "",
						"fw_center", province, city, town, "", "",
						new AjaxCallBack<String>() {
							@SuppressWarnings("unchecked")
							public void onSuccess(String t) {
								super.onSuccess(t);
								LG.i(getClass(), "opcenter =---->" + t);
								if (JSONParser.getStringFromJsonString(
										"status", t).equals("1")) {
									String data = JSONParser
											.getStringFromJsonString("data", t);
									String opcenter = JSONParser
											.getStringFromJsonString(
													"opcenter_list", data);
									opcenterList = (ArrayList<OpcenterInfo>) JSONParser
											.JSON2Array(opcenter,
													OpcenterInfo.class);
									if (BaseUtils.IsNotEmpty(opcenterList)
											&& opcenterList.size() > 0) {
										refreshAreaData(opcenterList, areaFlag);
									} else {
										C.showShort("当前地区无取货店", mActivity);
									}
								} else {
									C.showShort(JSONParser
											.getStringFromJsonString(
													"error_desc", t), mActivity);
								}
							};
						});
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	AjaxCallBack<String> areacallback = new AjaxCallBack<String>() {
		@SuppressWarnings("unchecked")
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "area -- >" + t);
			if (JSONParser.getStringFromJsonString("status", t).equals("1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				String area = JSONParser.getStringFromJsonString("area_list",
						data);
				switch (areaFlag) {
				case 1:
					provinceList = (ArrayList<AreaInfo>) JSONParser.JSON2Array(
							area, AreaInfo.class);
					refreshAreaData(provinceList, areaFlag);
					break;
				case 2:
					cityList = (ArrayList<AreaInfo>) JSONParser.JSON2Array(
							area, AreaInfo.class);
					refreshAreaData(cityList, areaFlag);
					break;
				case 3:
					townList = (ArrayList<AreaInfo>) JSONParser.JSON2Array(
							area, AreaInfo.class);
					refreshAreaData(townList, areaFlag);
					break;
				default:
					break;
				}

			} else {
				C.showShort(JSONParser.getStringFromJsonString("error_dec", t),
						mActivity);
			}
		};

		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.showShort("获取地区失败", mActivity);
		};
	};

}
