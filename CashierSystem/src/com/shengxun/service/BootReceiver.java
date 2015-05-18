package com.shengxun.service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.shengxun.cashiersystem.LoginActivity;
/**
 * 模块描述：开机启动
 * 2015-5-15 下午5:53:19
 * Write by LILIN
 */
public class BootReceiver extends BroadcastReceiver {
 
	static final String action_boot ="android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent bootStartIntent = new Intent(context, LoginActivity.class);
				bootStartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(bootStartIntent);
			}
		}, 1500);
		
	}
 
}