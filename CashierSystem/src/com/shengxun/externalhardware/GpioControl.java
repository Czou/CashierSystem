package com.shengxun.externalhardware;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GpioControl {

	public static final int 
			finger = 0 // 指纹
			,led = 1 // led
			,printer = 2 // printer
			;
	
	public static final String 
			finger_o = "FINGBR_PWR_EN"
			,led_o = "LED_CTL"
			,printer_o = "PRINTER_CTL"
			,sys_pwr = "SYS_PWR_EN"
			,qx_o = "QX_CTL"
			;
	
	public static final String 
//			finger_s = "FINGBR_PWR_EN"
//			,led_s = "LED_CTL"
			printer_s = "PRINT_CTS"
			;
	
	private static final String ctrl_gpio_path = "/dev/ctrl_gpio";

	
	/**
	 * 获取设备状态
	 * */
	public static final int get_status(String type) {
		FileInputStream mCalfdIn = null;
		String str = "10" + type;
		byte[] buff = str.getBytes();
		try {
			mCalfdIn = new FileInputStream(new File(ctrl_gpio_path));
			mCalfdIn.read(buff);
			mCalfdIn.close();
			if ('A' == buff[0])
				return 0;
			else if (buff[0] > 'A')
				return 1;
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static final int convertLed()
	{
		int a = convertGPIO(led);
		return a;
	}
	
	public static final int activateLed()
	{
		int a = activate(led_o, true);
		return a;
	}
	
	public static final int activatePrinter()
	{
		int a = activate(printer_o, true);
		return a;
	}
	
	public static final int convertPrinter()
	{
		int a = convertGPIO(printer);
		return a;
	}
	
	/**
	 * 激活（上、下电）
	 **/
	public static final int activate(String type, boolean open) {
		FileInputStream mCalfdIn = null;
		String str = gB(open) + type;
		byte[] buff = str.getBytes();
		try {
			mCalfdIn = new FileInputStream(new File(ctrl_gpio_path));
			mCalfdIn.read(buff);
			mCalfdIn.close();
			return 0;
		} catch (IOException e) {
			System.err.println("【" + (open?"上":"下") + "电: " + type + " 异常!】");
			e.printStackTrace();
			return -1;
		}
	}
	
	private static final String gB(boolean open)
	{
		return open?"01":"00";
	}

	/**
	 * 切换控制组
	 * */
	public static final int convertGPIO(int sel) {
		FileInputStream mCalfdIn = null;
		String str_en = "00UART7_EN";
		String str_sel0 = "00UART7_SEL00";
		String str_sel1 = "00UART7_SEL10";
		byte[] buff_en = str_en.getBytes();
		byte[] buff_sel0 = str_sel0.getBytes();
		byte[] buff_sel1 = str_sel1.getBytes();
		if (0 == sel) {
			// 指纹
			buff_sel0[1] = '0';
			buff_sel1[1] = '0';
		} else if (1 == sel) {
			// led
			buff_sel0[1] = '1';
			buff_sel1[1] = '0';
		} else if (2 == sel) {
			// printer
			buff_sel0[1] = '0';
			buff_sel1[1] = '1';
		} else if (3 == sel) {
			// 预留
			buff_sel0[1] = '1';
			buff_sel1[1] = '1';
		}
		buff_sel0[str_sel0.length() - 1] = 0;
		buff_sel1[str_sel1.length() - 1] = 0;
		try {
			mCalfdIn = new FileInputStream(new File(ctrl_gpio_path));
			mCalfdIn.read(buff_en);
			mCalfdIn.read(buff_sel0);
			mCalfdIn.read(buff_sel1);
			mCalfdIn.read(buff_en);
			mCalfdIn.close();
			return 0;
		} catch (IOException e) {
			System.err.println("【切换控制组异常】");
			e.printStackTrace();
			return -1;
		}
	}

}
