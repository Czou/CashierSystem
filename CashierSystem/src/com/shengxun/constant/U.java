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
	private static final String CASHIER_SYSTEM_URL = CASHIER_SYSTEM_DOMAIN;
	/*********************************收银系统_V1.0接口地址--start************************************/
	/**
	 * 1.收银登录接口
	 */
	public static final String CASHIER_SYSTEM_LOGIN=CASHIER_SYSTEM_URL+"sy_user/login";
	/**
	 * 2.产品列表接口
	 */
	public static final String CASHIER_SYSTEM_PRODUCT_LIST=CASHIER_SYSTEM_URL+"sy_product/product_list";
	/**
	 * 2.  收银注册接口
	 */
	public static final String CASH_STRING_REGIST=CASHIER_SYSTEM_URL+"sy_user/register";
	/**
	 * 3.  产品列表接口
	 */
	public static final String CASH_STRING_PRODUCT_LIST = CASHIER_SYSTEM_URL+"sy_product/product_list";
	/**
	 * 4.  产品分类列表
	 */
	public static final String CASH_STRING_PRODUCT_CATEFORY_LIST = CASHIER_SYSTEM_URL+"sy_product/product_category_list";
	/**
	 * 5.  创建订单接口
	 */
	public static final String CASH_STRING_CREATE_ORDERFORM = CASHIER_SYSTEM_URL+"sy_order/order_create"; 
	/**
	 * 6.  订单付款接口
	 */
	public static final String CASH_STRING_PAY_ORDERFORM = CASHIER_SYSTEM_URL+"sy_order/order_pay";
	/**
	 * 7.  订单(收银店)详细信息接口
	 */
	public static final String CASH_STRING_ORDERFORM_DETAIL = CASHIER_SYSTEM_URL+"sy_order/order_detail";
	/**
	 * 8.  订单(提货单)详细信息接口
	 */
	public static final String CASH_STRING_ORDERFORM_DELIVERY_DETAIL = CASHIER_SYSTEM_URL+"sy_order/delivery_order_detail";
	/**
	 * 9. 订单（收银店）列表接口
	 */
	public static final String CASH_STRING_ORDERFORM_LIST = CASHIER_SYSTEM_URL+"sy_order/order_list";
	/**
	 * 10.  订单（提货店）列表接口
	 */
	public static final String CASH_STRING_ORDERFORM_DELIVERY_LIST = CASHIER_SYSTEM_URL+"sy_order/delivery_order_list";
	/**
	 * 11.  订单取消接口
	 */
	public static final String CASH_STRING_ORDERFORM_CANAEL = CASHIER_SYSTEM_URL+"sy_order/order_cancel";
	/**
	 * 12.  订单提货接口
	 */
	public static final String CASH_STRING_ORDERFORM_PICKUP = CASHIER_SYSTEM_URL+"sy_order/order_pickup";
	/**
	 * 13.  创建退货订单接口
	 */
	public static final String CASH_STRING_ORDERFORM_REFUND=CASHIER_SYSTEM_URL+"sy_refund_order/order_create";
	/**
	 * 14.  退货订单退款接口
	 */
	public static final String CASH_STRING_ORDERFORM_REFUND_MONEY = CASHIER_SYSTEM_URL+"sy_refund_order/refund_pay";
	/**
	 * 15.  退货订单(收银店)详情接口
	 */
	public static final String CASH_STRING_ORDERFORM_REFUND_DELIVERY_DETAIL = CASHIER_SYSTEM_URL+"sy_refund_order/order_detail";
	/**
	 * 16.  退货订单(提货店)详情接口
	 */
	public static final String CASH_STRING_ORDERFORM_REFUND_DETAIL = CASHIER_SYSTEM_URL+"sy_refund_order/delivery_order_detail";
	/**
	 * 17.  退货订单(收银店)列表
	 */
	public  static final String CASH_STRING_ORDERFORM_REFUND_LIST =CASHIER_SYSTEM_URL+"sy_refund_order/order_list";
	/**
	 * 18.  退货订单（提货店）列表
	 */
	public static final String CASH_STRING_ORDERFORM_REFUND_DELIVERY_LIST = CASHIER_SYSTEM_URL+"sy_refund_order/delivery_order_list";
	/**
	 * 19.  取消退货订单接口
	 */
	public static final String CASH_STRING_ORDERFORM_REFUND_CANCEL = CASHIER_SYSTEM_URL+"sy_refund_order/order_cancel";
	/**
	 * 20.  运营中心接口
	 */
	public static final String CASH_STRING_OPCENTER = CASHIER_SYSTEM_URL+"sy_opcenter/opcenter_list";
	/**
	 * 21.  地区列表接口
	 */
	public static final String CASH_STRING_AREA = CASHIER_SYSTEM_URL+"sy_area/area_list";
	
	/**
	 * 22.软件更新http://tcadmind.051jk.com/gateway/app/get_app_version
	 */
	public static final String CASH_STRING_APP_UPDATE=CASHIER_SYSTEM_URL+"app/get_app_version";
	
}
