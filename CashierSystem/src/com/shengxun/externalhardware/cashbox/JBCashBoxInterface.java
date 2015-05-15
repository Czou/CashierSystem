package com.shengxun.externalhardware.cashbox;

import com.shengxun.externalhardware.GpioControl;



public class JBCashBoxInterface {

	/**
	 * Open CashBox 打开钱箱
	 * */
	public static void openCashBox() {
		new Thread(openBox).start();
	}

	/**
	 * Close CashBox 关闭钱箱
	 * */
	public static boolean closeCashBox() {
		int result = GpioControl.activate(GpioControl.qx_o, false);
		if (result == 0)
			return true;
		else
			return false;
	}
	
	private static Runnable openBox = new Runnable() {
		@SuppressWarnings("static-access")
		public void run() {
			System.err.println("open box");
			GpioControl.activate(GpioControl.qx_o, true);
			try {
				Thread.currentThread().sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GpioControl.activate(GpioControl.qx_o, false);
			System.err.println("close box");
		}
	};
}
