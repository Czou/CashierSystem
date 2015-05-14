package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;

import com.shengxun.adapter.AreaAdapte;
import com.shengxun.adapter.OpcenterTypeAdapter;
import com.shengxun.adapter.OpcenterAdapte;
import com.shengxun.constant.C;
import com.shengxun.entity.AreaInfo;
import com.shengxun.entity.OpcenterInfo;
import com.shengxun.entity.OpcenterTypeInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

import android.os.Bundle;
import android.util.Log;
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

	Spinner sp_province, sp_city, sp_town, sp_opcenter, sp_type;
	/**
	 * spinner标记，用来区分当前改变的spinner，1:province,2:city,3:town,4:opcenter
	 * 
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
	 * 运营中心类型list
	 */
	ArrayList<OpcenterTypeInfo> typeList;
	/**
	 * 当前省，市，县，招商中心以及父id
	 */
	String province, city, town, opcenter, parent_id = "", type = "fw_center";
	/**
	 * 返回，确定按钮
	 */
	Button btn_back, btn_ok, btn_del;
	/**
	 * 运营中心类型adapter
	 */
	OpcenterTypeAdapter ota;

	/**
	 * 当前选择运营中心
	 */
	OpcenterInfo opcenterInfo;
	/**
	 * isFirst:是否是第一次进入,第一次进入
	 */
	boolean isFirstIn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.area_select_view);

		initWidget();
//		initOpTypeData();
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
		sp_type = (Spinner) findViewById(R.id.area_select_opcenter_type);
		btn_back = (Button) findViewById(R.id.cashier_area_select_back);
		btn_ok = (Button) findViewById(R.id.cashier_area_select_ok);
		btn_del = (Button) findViewById(R.id.cashier_area_select_del);

		sp_province.setOnItemSelectedListener(myitemclick);
		sp_city.setOnItemSelectedListener(myitemclick);
		sp_town.setOnItemSelectedListener(myitemclick);
		sp_opcenter.setOnItemSelectedListener(myitemclick);
		sp_type.setOnItemSelectedListener(myitemclick);
		btn_back.setOnClickListener(myclick);
		btn_ok.setOnClickListener(myclick);
		btn_del.setOnClickListener(myclick);
	}

	/**
	 * 初始化运营中心类型数据
	 * 
	 * @auth shouwei
	 */
	private void initOpTypeData() {
		type = "fw_center";
		typeList = new ArrayList<OpcenterTypeInfo>();
		OpcenterTypeInfo oti = new OpcenterTypeInfo("fw_center", "服务中心");
		typeList.add(oti);
		oti = new OpcenterTypeInfo("zs_center", "招商中心");
		typeList.add(oti);
		oti = new OpcenterTypeInfo("ps_center", "配送中心");
		typeList.add(oti);
		ota = new OpcenterTypeAdapter(mActivity, typeList);
		sp_type.setAdapter(ota);
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

	/**
	 * 获取当前地区运营中心
	 * 
	 * @auth shouwei
	 */
	private void getOpcenter() {
		LG.i(getClass(), "province===>" + province);
		LG.i(getClass(), "city===>" + city);
		LG.i(getClass(), "town===>" + town);
		LG.i(getClass(), "type===>" + type);
		ConnectManager.getInstance().getOpcenterResult("", "", type,
				province, city, town, "", "", new AjaxCallBack<String>() {
					@SuppressWarnings("unchecked")
					public void onSuccess(String t) {
						super.onSuccess(t);
						LG.i(getClass(), "opcenter =---->" + t);
						if (JSONParser.getStringFromJsonString("status", t)
								.equals("1")) {
							String data = JSONParser.getStringFromJsonString(
									"data", t);
							String opcenter = JSONParser
									.getStringFromJsonString("opcenter_list",
											data);
							opcenterList = (ArrayList<OpcenterInfo>) JSONParser
									.JSON2Array(opcenter, OpcenterInfo.class);
							if (BaseUtils.IsNotEmpty(opcenterList)) {
								refreshAreaData(opcenterList, areaFlag);
								if (opcenterList.size() == 0) {
									C.showShort("当前地区无运营中心", mActivity);
								}
							}
						} else {
							C.showShort(JSONParser.getStringFromJsonString(
									"error_desc", t), mActivity);
						}
					};
				});
	}

	OnClickListener myclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_area_select_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_area_select_ok:
				if (BaseUtils.IsNotEmpty(sp_opcenter.getSelectedItem())) {
					GatheringActivity.setOpcenter(opcenterInfo, type);
				}
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			case R.id.cashier_area_select_del:
				opcenterInfo = null;
				type = "";
				GatheringActivity.setOpcenter(opcenterInfo, type);
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
			switch (av.getId()) {
			case R.id.area_select_province:
				LG.i(getClass(), "province on itemselect");
				areaFlag = 2;
				ConnectManager.getInstance().getAreaResult("3",
						provinceList.get(position).aid, areacallback);
				province = provinceList.get(position).aid;
				break;
			case R.id.area_select_city:
				LG.i(getClass(), "city on itemselect");
				areaFlag = 3;
				ConnectManager.getInstance().getAreaResult("4",
						cityList.get(position).aid, areacallback);
				city = cityList.get(position).aid;
				break;
			case R.id.area_select_opcenter_type:
				LG.i(getClass(), "type on itemselect");
				type = typeList.get(position).getType();
				areaFlag = 4;
				//获取 运营中心数据
				getOpcenter();
				break;
			case R.id.area_select_town:
				LG.i(getClass(), "town on itemselect");
				town = townList.get(position).aid;
				// 每次更换地区都重新获取一次运营中心列表
				areaFlag = 4;
				initOpTypeData();
				break;
			case R.id.area_select_opcenter:
				LG.i(getClass(), "opcenter on itemselect");
				if (BaseUtils.IsNotEmpty(opcenterList)
						&& opcenterList.size() != 0) {
					opcenterInfo = opcenterList.get(position);
				}
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
