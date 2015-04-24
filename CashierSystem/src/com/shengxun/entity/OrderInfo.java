package com.shengxun.entity;

/**
 * 订单信息实体类
 * @author sw
 * @date 2015-4-23
 */
public class OrderInfo {

	/**
	 * 订单号
	 */
	public String co_id;
	/**
	 * 交易号	
	 */
	public String co_trade_no;
	/**
	 * 消费会员id
	 */
	public String me_id;
	/**
	 * 营业员会员id
	 */
	public String cashier_me_id;
	/**
	 * 付款方式 
	 */
	public String co_pay_way;
	/**
	 * 订单金额
	 */
	public double co_money;
	/**
	 * 订单创建时间
	 */
	public String co_ctime;
	/**
	 * 收银机编号
	 */
	public String mc_id;
	/**
	 * 订单状态 1：正常 2：已付 3：取消
	 */
	public int co_status;
	public String cro_id;
	public String cro_trade_no;
	public String sell_cashier_me_id;
	public String cro_pay_way;
	public double cro_money;
	public String cro_ctime;
	public int cro_status;
	
	
	
	
}
