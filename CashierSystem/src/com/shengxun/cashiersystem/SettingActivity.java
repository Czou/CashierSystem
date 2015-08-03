package com.shengxun.cashiersystem;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.shengxun.externalhardware.cashbox.JBCashBoxInterface;
import com.shengxun.externalhardware.led.JBLEDInterface;
import com.shengxun.externalhardware.print.util.JBPrintInterface;
import com.shengxun.externalhardware.print.util.PrintTools_58mm;
import com.shengxun.util.CheckVersionManager;
import com.shengxun.util.ConnectManager;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.JSONParser;
import com.zvezda.android.utils.LG;
import com.zvezda.data.utils.DataSP;

/**
 * 设置界面
 * 
 * @author sw
 * @date 2015-5-15
 */
public class SettingActivity extends MyTimeLockBaseActivity {

	Button btn_back, btn_open, btn_close;

	Switch sw_open;

	private TextView check_new_app;

	private TextView click_to_update_all_product;

	private TextView click_to_update_lock_psd;

	private TextView version;

	private TextView btn_test_print;

	String update_lock_psd_first_time = null;

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
		btn_test_print = (TextView) findViewById(R.id.setting_test_print);

		check_new_app = (TextView) findViewById(R.id.check_new_app);
		click_to_update_all_product = (TextView) findViewById(R.id.click_to_update_all_product);
		click_to_update_lock_psd = (TextView) findViewById(R.id.click_to_update_lock_psd);
		version = (TextView) findViewById(R.id.cashier_system_version);
		version.setText(getResources().getString(
				R.string.cashier_system_current_version)
				+ getPackInfo());
		btn_open.setOnClickListener(myclick);
		btn_close.setOnClickListener(myclick);
		btn_test_print.setOnClickListener(myclick);
		btn_back.setOnClickListener(myclick);
		check_new_app.setOnClickListener(myclick);
		click_to_update_all_product.setOnClickListener(myclick);
		click_to_update_lock_psd.setOnClickListener(myclick);

	}

	/**
	 * 获得当前版本号
	 * 
	 * @return
	 * @auth shouwei
	 */
	private String getPackInfo() {
		String version = "";
		try {
			PackageManager mPackManager = this.getPackageManager();
			PackageInfo packInfo = mPackManager.getPackageInfo(
					mActivity.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	OnClickListener myclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cashier_setting_back:
				AppManager.getAppManager().finishActivity(mActivity);
				break;
			// 打开客显
			case R.id.cashier_setting_btn_open:
				JBLEDInterface.openLed();
				break;
			// 关闭客显
			case R.id.cashier_setting_btn_close:
				JBLEDInterface.closeLed();
				break;
			// 检测软件更新
			case R.id.check_new_app: {
				CheckVersionManager.checkVersion(mActivity, true);
			}
				break;
			// 更新数据
			case R.id.click_to_update_all_product: {
				// 更新所有数据
				C.openProgressDialog(mActivity, null, "正在同步所有产品数据信息，请耐心等待...");
				ConnectManager.getInstance().getProductList("",
						productAjaxCallBack);
			}
				break;
			// 更改锁屏密码
			case R.id.click_to_update_lock_psd: {
				// updateLockPsd(1);
				C.showDialogAlert("暂时不可用", mActivity);
			}
				break;
			case R.id.setting_test_print:
				testPrint();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 测试打印收银信息
	 */
	private void testPrint(){
			// 开始打印
			Log.i("savion", "开始打印--------------");
			JBPrintInterface.convertPrinterControl();
			PrintTools_58mm.print(PrintTools_58mm.ESC_ALIGN_CENTER);
			PrintTools_58mm.writeEnterLine(5);
			PrintTools_58mm.print_gbk(">>>>>>>>>>>");
			PrintTools_58mm.writeEnterLine(1);
			PrintTools_58mm.print_gbk("测试专用、他用无效");
			PrintTools_58mm.writeEnterLine(1);
			PrintTools_58mm.print_gbk(">>>>>>>>>>>");
			PrintTools_58mm.writeEnterLine(5);
			PrintTools_58mm.resetPrint();
	}
	

	/**
	 * 修改锁屏密码 index 代表执行的次数1：输入原密码，2：新密码，3：新密码确认
	 * 
	 * @auth shouwei
	 */
	private void updateLockPsd(int index) {
		final int fIndex = index;
		String hint = null;
		switch (index) {
		case 1:
			hint = "输入原密码";
			break;
		case 2:
			hint = "输入新密码";
			break;
		case 3:
			hint = "确认密码";
			break;
		default:
			break;
		}
		C.showDialogAlert("修改锁屏密码", true, hint, index != 3 ? "下一步" : "完成",
				mActivity, new UpdatePsdListener() {
					@Override
					public void callBack(String psd, AlertDialog dialog) {
						switch (fIndex) {
						case 1:
							if (psd.trim().equals(C.CURRENT_LOCK_PSD)) {
								updateLockPsd(2);
								dialog.dismiss();
							} else {
								C.showDialogAlert("密码不匹配", mActivity);
							}
							break;
						case 2:
							if (BaseUtils.IsNotEmpty(psd.trim())) {
								update_lock_psd_first_time = psd;
								dialog.dismiss();
								updateLockPsd(3);
							} else {
								C.showDialogAlert("密码不能为空", mActivity);
							}
							break;
						case 3:
							if (psd.equals(update_lock_psd_first_time)) {
								C.showLong("修改成功", getApplicationContext());
								C.CURRENT_LOCK_PSD = psd;
								DataSP sp = new DataSP(mActivity,
										C.SHARED_PREFENCE_NAME);
								sp.setValue(C.SHARED_LOCK_PSD, psd);
								dialog.dismiss();
							} else {
								C.showDialogAlert("两次输入不一致", mActivity);
							}
							break;
						default:
							break;
						}
					}
				});
	}

	private AjaxCallBack<String> productAjaxCallBack = new AjaxCallBack<String>() {

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.i(getClass(), "所有数据----->" + t);
			if (BaseUtils.IsNotEmpty(t)
					&& JSONParser.getStringFromJsonString("status", t).equals(
							"1")) {
				String data = JSONParser.getStringFromJsonString("data", t);
				String product_list = JSONParser.getStringFromJsonString(
						"product_list", data);
				String last_syn_time = JSONParser.getStringFromJsonString(
						"last_syn_time", data);
				ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) JSONParser
						.JSON2Array(product_list, ProductInfo.class);
				updateAllData(products, last_syn_time);
			}
			
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			C.closeProgressDialog();
			LDialog.openMessageDialog("产品信息数据全部更新失败,请稍后再试!", false, mActivity);
		}

	};

	private void updateAllData(final ArrayList<ProductInfo> products,
			final String last_syn_time) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (products != null && products.size() > 0) {
//						ormOpearationDao.deleteThisTable(ProductInfo.class);// 删除当前表
						Dao<ProductInfo, Integer> productsDao = ormOpearationDao.getDao(ProductInfo.class);
						LG.i(ApplicationCS.class, "收银系统手动产品信息数据全部更新");
						if(productsDao==null){
							productsDao = ormOpearationDao.getDao(ProductInfo.class);
						}
						productsDao.executeRawNoArgs("DELETE FROM productInfosTable");//删除所有数据
						for (ProductInfo entity : products) {
							productsDao.create(entity);
						}
						applicationCS.sp.setValue(ApplicationCS.LAST_SYN_TIME,
								last_syn_time);
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				C.closeProgressDialog();
				LDialog.openMessageDialog("产品信息数据全部更新成功!", false,
				mActivity);
			}else if(msg.what==0){
				C.closeProgressDialog();
				LDialog.openMessageDialog("产品信息数据全部更新失败!", false,
				mActivity);
			}
		};
	};
	/**
	 * 修改密码回调监听
	 * 
	 * @author sw
	 * @date 2015-6-16
	 */
	public interface UpdatePsdListener {
		public void callBack(String psd, AlertDialog dialog);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ormOpearationDao.closeDataHelper();
	}
}
