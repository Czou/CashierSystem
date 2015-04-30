package com.shengxun.externalhardware.print.util;

import com.shengxun.externalhardware.SerialPortTools;


public class PC {

	public static SerialPortTools printSerialPortTools;
	
	
	public static String printPort_58mm = "/dev/ttyS6"; // 打印串口
	public static int printBaudrate_58mm = 115200; // 打印波特率

	public static String printPort_80mm = "/dev/ttyS6"; // 打印串口
	public static int printBaudrate_80mm = 9600; // 打印波特率
	
}
