package com.shengxun.externalhardware.print.util;
import android.content.Context;
import android.graphics.Bitmap;

public class JBPrintInterface {

	public static boolean openPrinter() {
		int result = GpioControl.activate(GpioControl.printer_o, true);
		if (result == 0)
			return true;
		else
			return false;
	}

	public static boolean closePrinter() {
		int result = GpioControl.activate(GpioControl.printer_o, false);
		if (result == 0)
			return true;
		else
			return false;
	}

	public static boolean convertPrinterControl() {
		int result = GpioControl.convertPrinter();
		PC.printSerialPortTools = new SerialPortTools(PC.printPort_58mm,
				PC.printBaudrate_58mm);
		if (result == 0)
			return true;
		else
			return false;
	}

	/** 自检 */
	public static void testPrinter() {
		PrintTools_58mm.printTest();
	}

	/** 打印文字GB2312 */
	public static void printText_GB2312(String text) {
		PrintTools_58mm.printText_GB2312(text);
	}
	
	/** 打印文字Unicode */
	public static void printText_Unicode(String text) {
		PrintTools_58mm.printText_Unicode(text);
	}
	
	/**
	 * @param qrcodeImagePath 路径为assets根目录
	 */
	public static void printQRCodeWithPath(String qrcodeImagePath) {
		PrintTools_58mm.printPhotoWithPath(qrcodeImagePath);
	}
	/**
	 * @param qrcodeImagePath 路径为assets根目录
	 */
	public static void printImageWithPath(String iamgePath) {
		PrintTools_58mm.printPhotoWithPath(iamgePath);
	}

	public static void printQRCode(Bitmap bitmap) {
		byte[] command = PrintTools_58mm.decodeBitmap(bitmap);
		PrintTools_58mm.printPhoto(command);
	}

	public static void printImage(Bitmap bitmap) {
		byte[] command = PrintTools_58mm.decodeBitmap(bitmap);
		PrintTools_58mm.printPhoto(command);
	}
	
	public static void printQRCodeImageInAssets(Context context,String fileName){
		PrintTools_58mm.printPhotoInAssets(context, fileName);
	}
	
	public static void printImageInAssets(Context context,String fileName){
		PrintTools_58mm.printPhotoInAssets(context, fileName);
	}
}
