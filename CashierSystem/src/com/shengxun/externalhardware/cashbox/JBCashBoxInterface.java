package com.shengxun.externalhardware.cashbox;



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
	
	public static Runnable openBox = new Runnable() {
		public void run() {
			try {
				System.err.println("open box");
				GpioControl.activate(GpioControl.qx_o, true);
				Thread.currentThread().sleep(500);
				GpioControl.activate(GpioControl.qx_o, false);
				System.err.println("close box");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
}
