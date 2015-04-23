package com.shengxun.entity;

/**
 * 产品列表信息实体类
 * @author sw
 */
public class ProductInfo {

	/**
	 * 产品编号
	 */
	public String op_id;
	/**
	 * 招商中心企业产品编号
	 */
	public String zqp_id;
	/**
	 * 零售价
	 */
	public double op_market_price;
	/**
	 * 库存数量
	 */
	public int op_number;
	/**
	 * 状态 1：正常  2：下架
	 */
	public int op_status;
	/**
	 * 产品名称
	 */
	public int qp_name;
	/**
	 * 生成企业id
	 */
	public String qy_id;
	/**
	 * 条形码信息
	 */
	public String op_bar_code;
	/**
	 * 订单产品编号 
	 */
	public String cop_id;
	/**
	 * 企业产品编号
	 */
	public String qp_id;
	/**
	 * 购买数量
	 */
	public int buy_number;
	/**
	 * 原始订单号
	 */
	public String co_id;
	public double cop_price;
	public int cop_number;
	public int cop_refund_number;
	public double cop_plat_buy_price;
	public double cop_plat_sell_price;
	public String cro_id;
	public String crop_id;
	public int refund_number;
	public double crop_price;
	public int crop_number;
	public double crop_plat_buy_price;
	public double crop_plat_sell_price;
	
	
	

}
