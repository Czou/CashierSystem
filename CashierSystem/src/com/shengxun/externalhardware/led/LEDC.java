package com.shengxun.externalhardware.led;

import com.shengxun.externalhardware.SerialPortTools;

public class LEDC {

	public static SerialPortTools ledSerialPortTools;
	
	public static String ledPort = "/dev/ttyS6";// 打印串口
	public static int ledBaudrate = 9600;// 打印波特率
	
}
