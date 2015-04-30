package com.shengxun.externalhardware.led;

import com.shengxun.externalhardware.GpioControl;
import com.shengxun.externalhardware.SerialPortTools;

public class JBLEDInterface {

	public static boolean openLed() {
		int result = GpioControl.activate(GpioControl.led_o, true);
		if (result == 0)
			return true;
		else
			return false;
	}

	public static boolean closeLed() {
		int result = GpioControl.activate(GpioControl.led_o, false);
		if (result == 0)
			return true;
		else
			return false;
	}

	public static boolean convertLedControl() {
		int result = GpioControl.convertLed();
		LEDC.ledSerialPortTools = new SerialPortTools(LEDC.ledPort, LEDC.ledBaudrate);
		if (result == 0)
			return true;
		else
			return false;

	}

	public static void ledDisplay(String number) {
		LedTools.printNums(number);
	}

}
