package com.shengxun.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 产品列表信息实体类
 * 
 * @author sw
 */
@DatabaseTable(tableName = "productInfosTable")
public class ProductInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9089160653387632848L;
	/**
	 * 产品编号
	 */
	@DatabaseField(id = true)
	public int op_id;
	/**
	 * 招商中心企业产品编号
	 */
	@DatabaseField
	public String zqp_id;
	/**
	 * 零售价
	 */
	@DatabaseField
	public double op_market_price;
	/**
	 * 促销价
	 */
	@DatabaseField
	public double op_promote_market_price;
	/**
	 * 促销数量
	 */
	@DatabaseField
	public int op_promote_number;
	/**
	 * 促销开始时间
	 */
	@DatabaseField
	public String op_promote_start_date;
	/**
	 * 促销结束时间
	 */
	@DatabaseField
	public String op_promote_end_date;
	/**
	 * 是否促销
	 */
	@DatabaseField
	public int op_is_promote=0;
	/**
	 * 库存数量
	 */
	@DatabaseField
	public int op_number;
	/**
	 * 状态 1：正常 2：下架 产品状态 1上架销售 0下架，不需进入收银系统，已经进入的需要删除
	 */
	@DatabaseField
	public int op_status;
	/**
	 * 是否展示品  1不需要进入收银系统，已经进入的需要删除
	 */
	@DatabaseField
	public int op_is_for_show;
	/**
	 * 产品名称
	 */
	@DatabaseField
	public String qp_name;
	/**
	 * 生成企业id
	 */
	@DatabaseField
	public int qy_id;
	/**
	 * 条形码信息
	 */
	@DatabaseField
	public String op_bar_code;
	/**
	 * 图片地址
	 */
	@DatabaseField
	public String img_url;

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
	//是否是自有商品
	public int cop_is_seller;
	// 商品项是否被选中，在退货时使用到
	public boolean isChecked = false;
	//商品是否在系统中，非系统的商品也能被付款
	public boolean isProductInSystem=  true;
	@Override
	public String toString() {
		return "ProductInfo [op_id=" + op_id + ", zqp_id=" + zqp_id
				+ ", op_market_price=" + op_market_price
				+ ", op_promote_market_price=" + op_promote_market_price
				+ ", op_promote_number=" + op_promote_number
				+ ", op_promote_start_date=" + op_promote_start_date
				+ ", op_promote_end_date=" + op_promote_end_date
				+ ", op_is_promote=" + op_is_promote + ", op_number="
				+ op_number + ", op_status=" + op_status + ", op_is_for_show="
				+ op_is_for_show + ", qp_name=" + qp_name + ", qy_id=" + qy_id
				+ ", op_bar_code=" + op_bar_code + ", img_url=" + img_url
				+ ", cop_id=" + cop_id + ", qp_id=" + qp_id + ", buy_number="
				+ buy_number + ", co_id=" + co_id + ", cop_price=" + cop_price
				+ ", cop_number=" + cop_number + ", cop_refund_number="
				+ cop_refund_number + ", cop_plat_buy_price="
				+ cop_plat_buy_price + ", cop_plat_sell_price="
				+ cop_plat_sell_price + ", cro_id=" + cro_id + ", crop_id="
				+ crop_id + ", refund_number=" + refund_number
				+ ", crop_price=" + crop_price + ", crop_number=" + crop_number
				+ ", crop_plat_buy_price=" + crop_plat_buy_price
				+ ", crop_plat_sell_price=" + crop_plat_sell_price
				+ ", cop_is_seller=" + cop_is_seller + ", isChecked="
				+ isChecked + ", isProductInSystem=" + isProductInSystem + "]";
	}
}
