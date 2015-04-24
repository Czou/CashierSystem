package com.shengxun.cashiersystem;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.shengxun.adapter.CashierGoodsListAdapter;
import com.shengxun.entity.GoodsInfo;
import com.shengxun.util.ConnectManager;
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

	
	
	private TextView cashier_system_machine_number=null;
	private TextView cashier_system_machine_order_total=null;
	private TextView cashier_system_machine_order_return_balance=null;
	private TextView cashier_system_machine_status=null;
	private TextView cashier_system_machine_order_receive=null;
	private TextView cashier_system_machine_user=null;
	private TextView cashier_system_machine_time=null;
	//收款按钮
	private Button cashier_system_gathering=null;
	//当前用户购物商品清单
	private ArrayList<GoodsInfo> dataList=new ArrayList<GoodsInfo>();
	private CashierGoodsListAdapter cashierGoodsListAdapter=null;
	private ListView cashier_listview=null;
	private Timer timerUpdate=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		initWidget();
	}
	private void initWidget() {
		cashier_system_machine_number=(TextView) this.findViewById(R.id.cashier_system_machine_number);
		cashier_system_machine_order_total=(TextView) this.findViewById(R.id.cashier_system_machine_order_total);
		cashier_system_machine_order_return_balance=(TextView) this.findViewById(R.id.cashier_system_machine_order_return_balance);
		cashier_system_machine_status=(TextView) this.findViewById(R.id.cashier_system_machine_status);
		cashier_system_machine_order_receive=(TextView) this.findViewById(R.id.cashier_system_machine_order_receive);
		cashier_system_machine_user=(TextView) this.findViewById(R.id.cashier_system_machine_user);
		cashier_system_machine_time=(TextView) this.findViewById(R.id.cashier_system_machine_time);
		
		cashier_system_gathering = (Button) findViewById(R.id.gathering);
		cashier_system_gathering.setOnClickListener(myClick);
		
		cashier_listview=(ListView) this.findViewById(R.id.cashier_listview);
		cashier_listview.setOnItemClickListener(myItemClick);
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
		cashier_system_machine_number.setText(resources.getString(R.string.cashier_system_machine_number)+applicationCS.loginInfo.mc_id);
		cashierGoodsListAdapter=new CashierGoodsListAdapter(mActivity,dataList);
		cashier_listview.setAdapter(cashierGoodsListAdapter);
		ConnectManager.getInstance().getProductList(ajaxCallBack);
	}
	
	private void refreshData(){
		GoodsInfo goodsInfo=new GoodsInfo();
		goodsInfo.cargo_number="5455";
		goodsInfo.code="67854515555";
		goodsInfo.name="猕猴桃果汁";
		goodsInfo.old_price=5.0;
		goodsInfo.now_price=3.5;
		goodsInfo.money=3.5;
		goodsInfo.count=1;
		goodsInfo.clerk="admin5";
		goodsInfo.name="猕猴桃果汁";
		dataList.add(goodsInfo);
		cashierGoodsListAdapter.notifyDataSetChanged();
	}
	Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			cashier_system_machine_time.setText(""+TimeConversion.getSystemAppTimeCN());
			if(dataList!=null&&dataList.size()<10){
				refreshData();
			}
			
		}
		
	};
	TimerTask myMachineTime=new TimerTask() {
		@Override
		public void run() {
			myHandler.sendEmptyMessage(0);
		}
	};
	private AjaxCallBack<String> ajaxCallBack=new AjaxCallBack<String>() {

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			LG.e(getClass(), "产品信息----->"+t);
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
		}
		
		
	};
	/**
	 * 点击事件
	 */
	OnClickListener myClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			goActivity(GatheringActivity.class);
		}
	};
	/**
	 * item点击事件
	 */
	OnItemClickListener myItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			goActivity(GoodsDetailActivity.class);
		}
	};

}
