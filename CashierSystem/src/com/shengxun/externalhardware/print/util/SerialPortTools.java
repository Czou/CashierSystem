package com.shengxun.externalhardware.print.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class SerialPortTools {
	
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	
	public SerialPort getSerialPort(String port,int baudrate) throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			mSerialPort = new SerialPort(new File(port), baudrate, 0);
		}
		return mSerialPort;
	}
	
	/**
	 * @param port 端口
	 * @param baudrate 波特率
	 * */
	public SerialPortTools(String port,int baudrate)
	{
		try {
			mSerialPort = this.getSerialPort(port,baudrate);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
	
	void initp()
	{
		if (mOutputStream != null) {
			try {
				mOutputStream.write(new byte[]{0x1B,'@'});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ReadThread extends Thread {
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						System.out.println("接收到数据 大小: " + size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	/** 关闭串口 */
	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	protected void destroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		this.closeSerialPort();
		mSerialPort = null;
	}
	
	// [s] 输出
	public void write(String msg)
	{
		try {
			if(allowToWrite())
			{
				if(msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("unicode"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// [s] 输出
	public void write_unicode(String msg)
	{
		try {
			if(allowToWrite())
			{
				if(msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("unicode"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// [s] 输出
	public void write_gbk(String msg)
	{
		try {
			if(allowToWrite())
			{
				if(msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("GBK"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出
	 * */
	public void write(byte[] b)
	{
		try {
			if(allowToWrite())
			{
				if(b == null)
					return;
				mOutputStream.write(b);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出
	 * */
	public void write(int oneByte)
	{
		try {
			if(allowToWrite())
			{
				mOutputStream.write(oneByte);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否允许打印
	 * */
	public boolean allowToWrite()
	{
		if (mOutputStream == null) {
			System.out.println("输出流为空! 不能打印! ");
			return false;
		}
		return true;
	}
	
	// [e]
	
}
