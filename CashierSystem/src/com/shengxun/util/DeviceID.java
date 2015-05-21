package com.shengxun.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.Context;
import android.provider.Settings.Secure;

/**
 * 模块描述：获取设备的唯一编号
 * 2015-5-18 下午4:44:24
 * Write by LILIN
 */
public class DeviceID {
	
	public static String getDeviceID(Context mContext){
		String android_id=Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID);
		return android_id+getMac();
	}
	
	private static String getMac() {
		String macSerial = null;
		String str = "";
		try {
		Process pp = Runtime.getRuntime().exec(
		"cat /sys/class/net/wlan0/address ");
		InputStreamReader ir = new InputStreamReader(pp.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);


		for (; null != str;) {
		str = input.readLine();
		if (str != null) {
		macSerial = str.trim();// 去空格
		break;
		}
		}
		} catch (IOException ex) {
		// 赋予默认值
		ex.printStackTrace();
		}
		return macSerial;
		}
}
