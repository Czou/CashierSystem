package com.shengxun.cashiersystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.j256.ormlite.dao.Dao;
import com.shengxun.adapter.CashierGoodsListAdapter;
import com.shengxun.entity.ProductInfo;
import com.shengxun.externalhardware.cashbox.JBCashBoxInterface;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;
import com.zvezda.android.utils.TimeConversion;

/**
 * 模块描述：收银系统主页
 * 2015-4-20 下午3:29:28
 * Write by LILIN
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends BaseActivity {

	
	//收款
	private TextView cashier_system_receive_payments=null;
	//打开钱箱
	private TextView cashier_system_open_cashbox=null;
	//机器是否联网
	private TextView cashier_system_machine_status=null;
	//当前时间
	private TextView cashier_system_machine_time=null;
	//商品条码
	private EditText cashier_system_business=null;
	//当前用户购物商品清单
	private ArrayList<ProductInfo> dataList=new ArrayList<ProductInfo>();
	private CashierGoodsListAdapter cashierGoodsListAdapter=null;
	private ListView cashier_listview=null;
	private Timer timerUpdate=null;
	/**
	 * 产品数据库
	 */
	private Dao<ProductInfo,Integer> productsDao=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		productsDao=ormOpearationDao.getDao(ProductInfo.class);
		initWidget();
	}
	private void initWidget() {
		cashier_system_receive_payments=(TextView) this.findViewById(R.id.cashier_system_receive_payments);
		cashier_system_open_cashbox=(TextView) this.findViewById(R.id.cashier_system_open_cashbox);
		cashier_system_machine_status=(TextView) this.findViewById(R.id.cashier_system_machine_status);
		cashier_system_machine_time=(TextView) this.findViewById(R.id.cashier_system_machine_time);
		
		cashier_system_business=(EditText)this.findViewById(R.id.cashier_system_business);
		cashier_system_business.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_DONE){
					String op_bar_code=cashier_system_business.getText().toString();
					if(BaseUtils.IsNotEmpty(op_bar_code)){
						try {
							ArrayList<ProductInfo> productInfos=(ArrayList<ProductInfo>) productsDao.queryBuilder().where().eq("op_bar_code", op_bar_code).query();
							//查询到数据且唯一
							if(productInfos!=null&&productInfos.size()==1){
								LG.e(getClass(),"productInfos.get(0).qp_name"+productInfos.get(0).qp_name);
								refreshData(productInfos.get(0));
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				return false;
			}
		});
		
		cashier_listview=(ListView) this.findViewById(R.id.cashier_listview);
		cashier_system_receive_payments.setOnClickListener(onClickListener);
		cashier_system_open_cashbox.setOnClickListener(onClickListener);
		initWidgetData();
	}
	private void initWidgetData() {
		if(BaseUtils.isNetworkAvailable(mActivity)){
			cashier_system_machine_status.setText(Html.fromHtml(resources.getString(R.string.cashier_system_machine_status)+BaseUtils.getColorHtmlText("已联网", "#FF0000")));
		}else{
			cashier_system_machine_status.setText(Html.fromHtml(resources.getString(R.string.cashier_system_machine_status)+BaseUtils.getColorHtmlText("无网络", "#5E5E5E")));
		}
		cashier_system_machine_time.setText(""+TimeConversion.getSystemAppTimeCN());
		timerUpdate=new Timer();
		timerUpdate.schedule(myMachineTime, 1000, 1000);
		cashierGoodsListAdapter=new CashierGoodsListAdapter(mActivity,dataList);
		cashier_listview.setAdapter(cashierGoodsListAdapter);
		
	}
	
	private void refreshData(ProductInfo entity){
		entity.buy_number+=1;
		boolean isHaved=false;
		for(int i=0;i<dataList.size();i++){
			if(dataList.get(i).op_bar_code.equals(entity.op_bar_code)){
				dataList.get(i).buy_number+=1;
				isHaved=true;
			}
		}
		if(!isHaved){
			dataList.add(entity);
		}
		cashierGoodsListAdapter.notifyDataSetChanged();
		//购买商品总价
		double totalPayment=0;
		for(int i=0;i<dataList.size();i++){
			//每个商品的总价相加
			totalPayment+=dataList.get(i).buy_number*dataList.get(i).op_market_price;
			
		}
		//刷新付款总金额
		cashier_system_receive_payments.setText(resources.getString(R.string.cashier_system_receive_payments)+"￥"+totalPayment+"");
	}
	Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			cashier_system_machine_time.setText(""+TimeConversion.getSystemAppTimeCN());
		}
		
	};
	TimerTask myMachineTime=new TimerTask() {
		@Override
		public void run() {
			myHandler.sendEmptyMessage(0);
		}
	};
	
	 OnClickListener onClickListener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
					//收款
				case R.id.cashier_system_receive_payments:
				{
					
				}
					break;
					//打开钱箱
				case R.id.cashier_system_open_cashbox:
				{
					JBCashBoxInterface.openCashBox();
				}
					break;
					//关闭钱箱
				case R.id.cashier_system_close_cashbox:
				{
					JBCashBoxInterface.closeCashBox();
				}
					break;
			}
		}
		
	};
}
