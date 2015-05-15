package com.shengxun.externalhardware.cashbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.util.Log;

import com.shengxun.externalhardware.GpioControl;


public class JBCashBoxInterface {
	/**
	 * Open CashBox 打开钱箱
	 * */
	public static void openCashBox() {

		//打开钱箱
		QX_CTL(true);
		try {
			Thread.sleep(100);
			QX_CTL(false);//关闭钱箱
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * Close CashBox 关闭钱箱
	 * */
	public static void closeCashBox() {
		int i = GpioControl.activate(GpioControl.qx_o, false);
		Log.i("info", "关闭钱箱："+i);
		if( i != 0){
			Log.i("info", "关闭失败");
			try {
				Thread.sleep(100);
				closeCashBox();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
	private static int QX_CTL(boolean bPowerOnOff ) {
		  String ctrl_gpio_path = "/dev/ctrl_gpio";
		  FileInputStream mCalfdIn = null;
		  String str="00QX_CTL ";  
		  byte[] buff = str.getBytes();
		  buff[8]=0;
		  if(bPowerOnOff) buff[1]='1';
		  else  buff[1]='0'; 
		  try {
		   mCalfdIn = new FileInputStream(new File(ctrl_gpio_path));   
		   mCalfdIn.read(buff);
		   mCalfdIn.close();
		   return 0;
		  } catch (IOException e) {
		   //e.printStackTrace();
		   return -1;
		  }  
		 }
}
