package com.shengxun.constant;
/**
 * 模块描述：接口访问地址
 * 2014-9-18 上午10:06:40
 * Write by LILIN
 */
public class U
{
	/**********************************域名地址********************************************/
	/**
	 * 测试接口地址http://tcadmin360.051jk.com/gateway
	 * 
	 */
	private static final String CASHIER_SYSTEM_DOMAIN_TSET="http://tcadmin360.051jk.com/gateway/";
	/**
	 * 正式发布接口地址 http://tcadmin.051jk.com/gateway
	 */
	private static final String CASHIER_SYSTEM_DOMAIN="http://tcadmin.051jk.com/gateway/";
	
	/**
	 * 根据测试和发布换地址
	 */
	private static final String CASHIER_SYSTEM_URL = CASHIER_SYSTEM_DOMAIN_TSET;
	/*********************************收银系统_V1.0接口地址--start************************************/
	/**
	 * 1.收银登录接口
	 */
	public static final String CASHIER_SYSTEM_LOGIN=CASHIER_SYSTEM_URL+"sy_user/login";
	/**
	 * 2.产品列表接口
	 */
	public static final String CASHIER_SYSTEM_PRODUCT_LIST=CASHIER_SYSTEM_URL+"sy_product/product_list";
	
}
