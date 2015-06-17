package com.shengxun.service;

import java.util.Timer;

import com.shengxun.cashiersystem.LockActivity;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * 锁屏监听服务
 * @author sw
 * @date 2015-6-17
 */
public class MyScreenService extends Service {

	long screenShutMill=0;
	boolean isGoingToSleep = false;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() {
		// 屏蔽系统的屏保
		KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock lock = manager
				.newKeyguardLock("KeyguardLock");
		lock.disableKeyguard();

		// 注册一个监听屏幕开启和关闭的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenReceiver, filter);
		
//		new Thread(){
//			@Override
//			public void run() {
//				super.run();
//				while(!isGoingToSleep){
//					try {
//						Thread.sleep(1000);
//						screenShutMill++;
//						if(screenShutMill>=300){
//							isGoingToSleep = !isGoingToSleep;
//							Log.i("savion","going to sleep ------ >"+screenShutMill);
//						}
//						Log.i("savion","screenShutMill ------ >"+screenShutMill);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}.start();
	}

	BroadcastReceiver screenReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_ON)) {
				
			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {//如果接受到关闭屏幕的广播
				PowerManagerWakeLock.acquire(MyScreenService.this);  
                Intent intent2 = new Intent(MyScreenService.this,  
                        LockActivity.class);  
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                startActivity(intent2);  
                PowerManagerWakeLock.release(); 
			}
		}
	};

	public void onDestroy() {
		PowerManagerWakeLock.release();
		unregisterReceiver(screenReceiver);
	};

}

