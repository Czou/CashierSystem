package com.shengxun.cashiersystem;

import java.sql.SQLException;
import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.j256.ormlite.dao.Dao;
import com.shengxun.adapter.AreaAdapte;
import com.shengxun.adapter.OpcenterAdapte;
import com.shengxun.adapter.OpcenterTypeAdapter;
import com.shengxun.constant.C;
import com.shengxun.entity.AreaInfo;
import com.shengxun.entity.OpcenterInfo;
import com.shengxun.entity.OpcenterTypeInfo;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

/**
 * 选择地区运营中心
 * 
 * @author sw
 * @date 2015-5-8
 */
public class AreaSelectActivity extends MyTimeLockBaseActivity {

	Spinner sp_province, sp_city, sp_town, sp_opcenter, sp_type;
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
	 * 数据操作Dao
	 */
	Dao<AreaInfo, Integer> areaDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.area_select_view);
		areaDao = ormOpearationDao.getDao(AreaInfo.class);
		opcenterList = new ArrayList<OpcenterInfo>();
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
		try {
			provinceList = (ArrayList<AreaInfo>) areaDao.queryBuilder().where()
					.eq("level", "2").query();
			if (provinceList != null && provinceList.size() > 0) {
				refreshAreaData(provinceList, 1);
			} else {
				C.showShort("获取地理信息失败", mActivity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 刷新spinner信息
	 * 
	 * @param list
	 * @param flag
	 * @auth shouwei
	 */
	private void refreshAreaData(ArrayList<?> list, int flag) {
		if (flag == 1) {
			@SuppressWarnings("unchecked")
			AreaAdapte areaAdapter = new AreaAdapte(mActivity,
					(ArrayList<AreaInfo>) list);
			sp_province.setAdapter(areaAdapter);
		} else if (flag == 2) {
			@SuppressWarnings("unchecked")
			AreaAdapte areaAdapter = new AreaAdapte(mActivity,
					(ArrayList<AreaInfo>) list);
			sp_city.setAdapter(areaAdapter);
		} else if (flag == 3) {
			@SuppressWarnings("unchecked")
			AreaAdapte areaAdapter = new AreaAdapte(mActivity,
					(ArrayList<AreaInfo>) list);
			sp_town.setAdapter(areaAdapter);
		} else if (flag == 4) {
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
		if(!BaseUtils.isNetworkAvailable(mActivity)){
			C.showDialogAlert("当前网络不可用", mActivity);
			return;
		}
		C.openProgressDialog(mActivity, null, "正在加载...");
		ConnectManager.getInstance().getOpcenterResult("", "", type, province,
				city, town, "", "", new AjaxCallBack<String>() {
					@SuppressWarnings("unchecked")
					public void onSuccess(String t) {
						super.onSuccess(t);
						C.closeProgressDialog();
						LG.i(getClass(), "opcenter =---->" + t);
						opcenterList.clear();
						if (JSONParser.getStringFromJsonString("status", t)
								.equals("1")) {
							String data = JSONParser.getStringFromJsonString(
									"data", t);
							String opcenter = JSONParser
									.getStringFromJsonString("opcenter_list",
											data);
							String opcenter_count = JSONParser
									.getStringFromJsonString("opcenter_total",
											data);
							if (opcenter_count.equals("0")) {
								C.showDialogAlert("当前地区无运营中心", mActivity);
							}
							opcenterList = (ArrayList<OpcenterInfo>) JSONParser
									.JSON2Array(opcenter, OpcenterInfo.class);
						} else if(JSONParser.getStringFromJsonString("status",t).equals("0")){
							String msg = JSONParser.getStringFromJsonString("error_desc", t);
							if(BaseUtils.IsNotEmpty(msg)){
								C.showDialogAlert(msg, mActivity);
							}else{
								C.showDialogAlert("获取运营中失败", mActivity);
							}
							
						}else {
							C.showDialogAlert("获取运营返回信息错误", mActivity);
						}
						refreshAreaData(opcenterList, 4);
					};

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						C.closeProgressDialog();
						C.showDialogAlert("获取运营中失败", mActivity);
						opcenterList.clear();
						refreshAreaData(opcenterList, 4);
					}
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
				try {
					cityList = (ArrayList<AreaInfo>) areaDao.queryBuilder()
							.where().eq("level", "3").and()
							.eq("pid", provinceList.get(position).aid).query();
					if (cityList != null && cityList.size() > 0) {
						// 初始化sp_city的列表信息
						refreshAreaData(cityList, 2);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				province = provinceList.get(position).aid;
				break;
			case R.id.area_select_city:
				try {
					townList = (ArrayList<AreaInfo>) areaDao.queryBuilder()
							.where().eq("level", "4").and()
							.eq("pid", cityList.get(position).aid).query();
					if (townList != null && townList.size() > 0) {
						// 初始化sp_town的列表信息
						refreshAreaData(townList, 3);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				city = cityList.get(position).aid;
				break;
			case R.id.area_select_opcenter_type:
				type = typeList.get(position).getType();

				// 因为sp_type是在其他spinner之前进行赋值的，
				// 所以一走入AreaSelectActivity时会出现运营中心一闪而过的画面
				// 所以第一次进入时不应该让其搜索运营中心

				// 获取 运营中心数据

				getOpcenter();
				break;
			case R.id.area_select_town:
				town = townList.get(position).aid;
				// 每次更换地区都重新获取一次运营中心列表
				initOpTypeData();
				// getOpcenter();
				initOpTypeData();
				break;
			case R.id.area_select_opcenter:
				if (BaseUtils.IsNotEmpty(opcenterList)
						&& opcenterList.size() > 0) {
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
}
