package com.shengxun.externalhardware.led;
public class LedTools {

	public static final byte CR = 0x0D;
	public static final byte ESC = 0x1B;

	public static final byte[] ESC_INIT = new byte[] { ESC, '@' };
	public static final byte[] ESC_Q_A = new byte[] { ESC, 'Q', 'A' };

	public static void printLedNumBegin() {
		print(ESC_Q_A);
	}

	public static void printLedNumEnd() {
		print(0x0D); // CR
	}

	public static void printNum(char num) {
		print((byte) num);
	}

	public static void printNums(String nums) {
		printLedNumBegin();
		char[] cs = nums.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			printNum(cs[i]);
		}
		printLedNumEnd();
	}

	public static void print(byte[] b) {
		if (allowToS())
			LEDC.ledSerialPortTools.write(b);
	}

	public static void print(int oneByte) {
		if (allowToS())
			LEDC.ledSerialPortTools.write(oneByte);
	}

	public static boolean allowToS() {
		return LEDC.ledSerialPortTools != null;
	}
}
