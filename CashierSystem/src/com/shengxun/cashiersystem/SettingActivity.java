package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.shengxun.cashiersystem.app.ApplicationCS;
import com.shengxun.constant.C;
import com.shengxun.customview.LDialog;
import com.shengxun.entity.ProductInfo;
import com.shengxun.externalhardware.led.JBLEDInterface;
import com.shengxun.util.CheckVersionManager;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;

/**
 * 设置界面
 * @author sw
 * @date 2015-5-15
 */
public class SettingActivity extends BaseActivity {

	Button btn_back, btn_open, btn_close;
	
	Switch sw_open;

	private TextView check_new_app;
	
	private TextView click_to_update_all_product;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier_setting_view);
		initWidget();
	}

	private void initWidget() {

		btn_back = (Button) findViewById(R.id.cashier_setting_back);
		btn_open = (Button) findViewById(R.id.cashier_setting_btn_open);
		btn_close = (Button) findViewById(R.id.cashier_setting_btn_close);

		check_new_app = (TextView) findViewById(R.id.check_new_app);
		click_to_update_all_product = (TextView) findViewById(R.id.click_to_update_all_product);
		btn_open.setOnClickListener(myclick);
		btn_close.setOnClickListener(myclick);
		btn_back.setOnClickListener(myclick);
		check_new_app.setOnClickListener(myclick);
		click_to_update_all_product.setOnClickListener(myclick);
	}

	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_setting_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			 //打开客显
			case R.id.cashier_setting_btn_open:
				JBLEDInterface.openLed();
				break;
			// 关闭客显
			case R.id.cashier_setting_btn_close:
				JBLEDInterface.closeLed();
				break;
			//检测软件更新
			case R.id.check_new_app:
			{
				CheckVersionManager.checkVersion(mActivity, true);
			}
				break;
				//检测软件更新
			case R.id.click_to_update_all_product:
			{
				//启动服务更新
				C.openProgressDialog(mActivity, null, "正在同步所有产品数据信息，请耐心等待...");
				ConnectManager.getInstance().getProductList("",productAjaxCallBack);
			}
				break;
			default:
				break;
			}
		}
	};
	private AjaxCallBack<String> productAjaxCallBack = new AjaxCallBack<String>() {

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			try {
				if (BaseUtils.IsNotEmpty(t)&& JSONParser.getStringFromJsonString("status", t).equals("1")) {
					String data = JSONParser.getStringFromJsonString("data", t);
					String product_list = JSONParser.getStringFromJsonString("product_list", data);
					String last_syn_time = JSONParser.getStringFromJsonString("last_syn_time", data);
					ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) JSONParser.JSON2Array(product_list, ProductInfo.class);
					if (products != null && products.size() > 0) {
					    ormOpearationDao.deleteThisTable(ProductInfo.class);//删除当前表
						Dao<ProductInfo, Integer> productsDao = ormOpearationDao.getDao(ProductInfo.class);
							LG.i(ApplicationCS.class, "收银系统手动产品信息数据全部更新");
							//productsDao.executeRawNoArgs("DELETE FROM productInfosTable");//删除所有数据
							for (ProductInfo entity : products) {
								productsDao.create(entity);
							}
						applicationCS.sp.setValue(ApplicationCS.LAST_SYN_TIME, last_syn_time);
						LDialog.openMessageDialog("产品信息数据全部更新成功!", false, mActivity);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			C.closeProgressDialog();
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			LDialog.openMessageDialog("产品信息数据全部更新失败,请稍后再试!", false, mActivity);
		}

	};
}
