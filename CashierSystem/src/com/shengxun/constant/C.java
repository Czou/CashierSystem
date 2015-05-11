package com.shengxun.constant;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.app.Activity;
import android.widget.Toast;

import com.shengxun.util.MD5Util;
import com.zvezda.algorithm.utils.AlgorithmUtils;
/**
 * 模块描述：常量数据
 * 2015-2-10 上午11:31:37
 * Write by LILIN
 */
public class C
{
	/**
	 * 当前是否是测试版
	 */
	public static final boolean isDebug=false;
	/**
	 * 每次请求必须的参数
	 */
	public final static String SOB_CODE="jk_mytc";
	public final static String SOB_PASSWORD="shengxun";
	public final static String MACHINE_CODE="00:E2:F0:13:43:53";
	public final static String VERIFY_CODE=MD5Util.GetMD5Code(""+SOB_CODE+"#"+SOB_PASSWORD+"#"+MACHINE_CODE+"");
	
	
	
	/**
	 * 加密密钥
	 */
	public final static String DES_KEY="051jks~~";
	/**
	 * 获得DES加密
	 * @param data
	 * @param DESKey
	 * @return
	 */
	public static String getDesStr(String data, String DESKey)
	{
		try
		{
			byte[] iv1 = { (byte) 0x35, (byte) 0x41, (byte) 0x43, (byte) 0x38, (byte) 0x35, (byte) 0x30, (byte) 0x35, (byte) 0x32 };
			IvParameterSpec iv = new IvParameterSpec(iv1);
			DESKeySpec dks = new DESKeySpec(DESKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte encryptedData[] = cipher.doFinal(data.getBytes());
			return AlgorithmUtils.byte2hex(encryptedData);
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return "";
	}
	
	/**  短提示
	 * @param msg
	 * @param mActivity
	 */
	public static void showShort(String msg,Activity mActivity){
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	}
	/**长提示
	 * @param msg
	 * @param mActivity
	 */
	public static void showLong(String msg,Activity mActivity){
		Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
	}
	
	
}
