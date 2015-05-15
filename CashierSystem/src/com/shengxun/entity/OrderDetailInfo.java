package com.shengxun.entity;

import java.util.List;

/**
 * 查询订单返回结果信息
 * 
 * @author sw
 * @date 2015-5-4
 */
public class OrderDetailInfo {

	OrderInfo order_info;
	List<ProductInfo> product_info;

	public OrderDetailInfo(OrderInfo order_info, List<ProductInfo> proInfos) {
		this.order_info = order_info;
		this.product_info = proInfos;
	}

	public OrderInfo getOrder_info() {
		return order_info;
	}

	public void setOrder_info(OrderInfo order_info) {
		this.order_info = order_info;
	}

	public List<ProductInfo> getProduct_info() {
		return product_info;
	}

	public void setProduct_info(List<ProductInfo> product_info) {
		this.product_info = product_info;
	}

	@Override
	public String toString() {
		return "OrderDetailInfo [order_info=" + order_info + ", product_info="
				+ product_info + "]";
	}
	
	
}
