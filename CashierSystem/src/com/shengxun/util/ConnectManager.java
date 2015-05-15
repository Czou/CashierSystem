package com.shengxun.util;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.shengxun.constant.C;
import com.shengxun.constant.U;
import com.shengxun.entity.ProductInfo;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;

/**
 * 连接服务器的连接管理
 * 
 * @author LILIN 下午4:23:35
 */
public class ConnectManager {
	private static ConnectManager instance = null;
	private FinalHttp finalHttp;

	public static ConnectManager getInstance() {
		if (instance == null) {
			instance = new ConnectManager();
		}
		return instance;
	}

	public ConnectManager() {
		finalHttp = new FinalHttp();
		finalHttp.configRequestExecutionRetryCount(2);
	}

	/***************** 登录接口 LILIN Start ********************/

	/**
	 * @param login_code
	 *            des_crypt(cardno#password) des加密秘钥051jks~~ des加密向量 array(0x35,
	 *            0x41, 0x43, 0x38, 0x35, 0x30, 0x35, 0x32)
	 * @param ajaxCallBack
	 */
	public void getLoginResult(String login_code,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("login_code", login_code);
		finalHttp.configCharset("UTF-8");
		finalHttp.get(U.CASHIER_SYSTEM_LOGIN, params, ajaxCallBack);
	}

	/***************** 登录接口 LILIN End ********************/

	/***************** 产品列表接口 LILIN Start ********************/

	/**
	 * @param login_code
	 *            des_crypt(cardno#password) des加密秘钥051jks~~ des加密向量 array(0x35,
	 *            0x41, 0x43, 0x38, 0x35, 0x30, 0x35, 0x32)
	 * @param ajaxCallBack
	 */
	public void getProductList(AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		finalHttp.configCharset("UTF-8");
		finalHttp.get(U.CASHIER_SYSTEM_PRODUCT_LIST, params, ajaxCallBack);
	}

	/***************** 产品列表接口 LILIN End ********************/

	/***************** 注册接口 sw start ********************/
	/**
	 * @param card_no
	 * @param card_verify_code
	 * @param nickname
	 * @param realname
	 * @param id_card_no
	 * @param summary
	 * @param mobile
	 * @param ajaxCallback
	 */
	public void getRegisteResult(String card_no, String card_verify_code,
			String nickname, String realname, String id_card_no,
			String summary, String mobile, AjaxCallBack<String> ajaxCallback) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("card_no", card_no);
		params.put("card_verify_code", card_verify_code);
		params.put("nickname", nickname);
		params.put("realname", realname);
		params.put("id_card_no", id_card_no);
		params.put("summary", summary);
		params.put("mobile", mobile);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_REGIST, params, ajaxCallback);
	}

	/***************** 注册接口 End ********************/

	/***************** 产品列表接口 sw start *********************/
	/**
	 * @param ajaxCallback
	 */
	public void getProductListResult(AjaxCallBack<String> ajaxCallback) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_PRODUCT_LIST, params, ajaxCallback);

	}

	/***************** 产品列表接口 end *********************/

	/***************** 产品分类列表接口 SW start *********************/
	public void getProductCategoryListResult(AjaxCallBack<String> ajaxCallBack) {

		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		finalHttp.configCharset("utf-8");
		finalHttp
				.get(U.CASH_STRING_PRODUCT_CATEFORY_LIST, params, ajaxCallBack);

	}

	/***************** 产品分类列表接口 end *********************/

	/***************** 创建订单接口 SW start ********************/
	/**
	 * @param consume_card_no
	 * @param cashier_card_no
	 * @param product_info
	 * @param pay_way
	 * @param pay_money
	 * @param ajaxCallback
	 */
	public void getCreateOrderFormResult(String consume_card_no,
			String cashier_card_no, ArrayList<ProductInfo> products,
			String delivery_rs_code, String delivery_rs_code_id,
			String pay_way, String pay_money, AjaxCallBack<String> ajaxCallback) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		
		params.put("consume_card_no", consume_card_no);
		params.put("cashier_card_no", cashier_card_no);
		if(BaseUtils.IsNotEmpty(delivery_rs_code)){
			params.put("delivery_rs_code", delivery_rs_code);
		}
		if(BaseUtils.IsNotEmpty(delivery_rs_code_id)){
			params.put("delivery_rs_code_id", delivery_rs_code_id);
		}
		if (products != null && products.size() > 0) {
			for (int i = 0; i < products.size(); i++) {
				params.put("product_info[" + products.get(i).op_id + "]",
						products.get(i).buy_number + "");
			}
		}

		params.put("pay_way", pay_way);
		params.put("pay_money", pay_money);
		finalHttp.configCharset("utf-8");
		finalHttp.post(U.CASH_STRING_CREATE_ORDERFORM, params, ajaxCallback);
	}

	/***************** 创建订单接口 End ********************/

	/***************** 订单付款接口 SW start ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getPayOrderFormResult(String order_id,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);

		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_PAY_ORDERFORM, params, ajaxCallBack);

	}

	/***************** 订单付款接口 End ********************/

	/***************** 订单(收银店)详细接口 SW START ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getOrderFormDetailResult(String order_id,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);

		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_DETAIL, params, ajaxCallBack);

	}

	/***************** 订单(收银店)详细接口 end ********************/

	/***************** 订单(提货店)详细接口 SW START ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getOrderFormDeliveryDetailResult(String order_id,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);

		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_DELIVERY_DETAIL, params,
				ajaxCallBack);

	}

	/***************** 订单(提货店)详细接口 end ********************/

	/***************** 订单（收银店）列表接口 START ********************/
	/**
	 * @param number
	 * @param offset
	 * @param ajaxCallBack
	 */
	public void getOrderListResult(String number, String offset,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("number", number);
		params.put("offset", offset);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_LIST, params, ajaxCallBack);

	}

	/***************** 订单（收银店）列表接口 end ********************/

	/***************** 订单（提货店）列表接口 SW START ********************/
	/**
	 * @param number
	 * @param offset
	 * @param ajaxCallback
	 */
	public void getOrderDeliveryListResult(String number, String offset,
			AjaxCallBack<String> ajaxCallback) {

		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("number", number);
		params.put("offset", offset);

		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_DELIVERY_LIST, params,
				ajaxCallback);
	}

	/***************** 订单（提货店）列表接口 end ********************/

	/***************** 订单取消接口 SW start ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getOrderFormCanaelResult(String order_id,
			String cashier_card_no, String consume_card_no,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("cashier_card_no", cashier_card_no);
		params.put("consume_card_no", consume_card_no);
		params.put("order_id", order_id);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_CANAEL, params, ajaxCallBack);
	}

	/***************** 订单取消接口 end ********************/

	/***************** 订单提货接口 SW START ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getOrderFormPickUpResult(String order_id, String card_no,
			String cashier_card_no, AjaxCallBack<String> ajaxCallBack) {

		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("cashier_card_no", cashier_card_no);
		params.put("consume_card_no", card_no);
		params.put("order_id", order_id);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_PICKUP, params, ajaxCallBack);
	}

	/***************** 订单提货接口 END ********************/

	/***************** 创建退货订单接口 SW START ********************/
	/**
	 * @param order_id
	 * @param product_info
	 * @param cashier_card_no
	 * @param pay_way
	 * @param ajaxCallBack
	 */
	public void getOrderFormRefundResult(String order_id,
			ArrayList<ProductInfo> product_info, String cashier_card_no,
			String pay_way, String consume_card_no,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		// params.put("card_no", card_no);
		params.put("order_id", order_id);
		if (BaseUtils.IsNotEmpty(cashier_card_no)) {
			params.put("cashier_card_no", cashier_card_no);
		}
		if (BaseUtils.IsNotEmpty(pay_way)) {
			params.put("pay_way", pay_way);
		}

		if (BaseUtils.IsNotEmpty(consume_card_no)) {
			params.put("consume_card_no", consume_card_no);
		}

		if (product_info != null && product_info.size() > 0) {
			for (int i = 0; i < product_info.size(); i++) {
				params.put("product_info[" + product_info.get(i).cop_id + "]",
						product_info.get(i).buy_number + "");
			}
		}
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_REFUND, params, ajaxCallBack);

	}

	/***************** 创建退货接口 END ********************/

	/***************** 退货订单退款接口 SW START ********************/
	/**
	 * @param order_id
	 * @param product_info
	 * @param cashier_card_no
	 * @param pay_way
	 * @param ajaxCallBack
	 */
	public void getReturnOrderFormResult(String order_id,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_REFUND_MONEY, params,
				ajaxCallBack);

	}

	/***************** 退货订单退款接口 END ********************/

	/***************** 退货订单(收银店)详情接口 SW START ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getOrderRefundDetailResult(String order_id,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_REFUND_DETAIL, params,
				ajaxCallBack);
	}

	/***************** 退货订单（收银店）详情接口 END ********************/

	/***************** 退货订单（提货店）详情接口 SW START ********************/
	/**
	 * @param order_id
	 * @param ajaxAjaxCallBack
	 */
	public void getOrderRefundDetailDeliveryResult(String order_id,
			AjaxCallBack<String> ajaxAjaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_REFUND_DELIVERY_DETAIL, params,
				ajaxAjaxCallBack);

	}

	/***************** 退货订单（提货店）详情接口 END ********************/

	/***************** 退货订单（收银店）列表接口SW START ********************/
	/**
	 * @param number
	 * @param offset
	 * @param ajaxCallBack
	 */
	public void getOrderFundlistResult(String number, String offset,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("number", number);
		params.put("offset", offset);
		finalHttp.configCharset("utf-8");
		finalHttp
				.get(U.CASH_STRING_ORDERFORM_REFUND_LIST, params, ajaxCallBack);

	}

	/***************** 退货订单（收银店）列表接口 END ********************/

	/***************** 退货订单（提货店）列表接口 SW START ********************/
	public void getOrderFundDeliveryListResult(String number, String offset,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("number", number);
		params.put("offset", offset);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_REFUND_DELIVERY_LIST, params,
				ajaxCallBack);

	}

	/***************** 退货订单（提货店）列表接口 END ********************/

	/***************** 取消退货订单接口 SW START ********************/
	/**
	 * @param order_id
	 * @param ajaxCallBack
	 */
	public void getOrderFundCancelResult(String order_id,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		params.put("order_id", order_id);
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_ORDERFORM_REFUND_CANCEL, params,
				ajaxCallBack);

	}

	/***************** 取消退货订单接口 END ********************/

	/***************** 运营中心接口 SW START ********************/
	/**
	 * @param number
	 * @param offset
	 * @param opcenter_type
	 * @param search_province
	 * @param search_city
	 * @param search_town
	 * @param search_name
	 * @param search_address
	 * @param ajaxCallBack
	 */
	public void getOpcenterResult(String number, String offset,
			String opcenter_type, String search_province, String search_city,
			String search_town, String search_name, String search_address,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		// 每次请求必须得验证码
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		if(BaseUtils.IsNotEmpty(opcenter_type)){
			params.put("opcenter_type", opcenter_type);
		}
		if(BaseUtils.IsNotEmpty(number)){
			params.put("number", number);
		}
		if(BaseUtils.IsNotEmpty(offset)){
			params.put("offset", offset);
		}
		if(BaseUtils.IsNotEmpty(search_province)){
			params.put("search_province", search_province);
		}
		if(BaseUtils.IsNotEmpty(search_city)){
			params.put("search_city", search_city);
		}
		if(BaseUtils.IsNotEmpty(search_town)){
			params.put("search_town", search_town);
		}
		if(BaseUtils.IsNotEmpty(search_name)){
			params.put("search_name", search_name);
		}
		if(BaseUtils.IsNotEmpty(search_address)){
			params.put("search_address", search_address);
		}
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_OPCENTER, params, ajaxCallBack);
	}

	/***************** 运营中心接口 END ********************/

	/***************** 地区接口 SW start ********************/
	/**
	 * @param search_level
	 * @param parent_aid
	 * @param ajaxCallBack
	 */
	public void getAreaResult(String search_level, String parent_aid,
			AjaxCallBack<String> ajaxCallBack) {
		AjaxParams params = new AjaxParams();
		params.put("sob_code", C.SOB_CODE);
		params.put("sob_password", C.SOB_PASSWORD);
		params.put("machine_code", C.MACHINE_CODE);
		params.put("verify_code", C.VERIFY_CODE);

		if(BaseUtils.IsNotEmpty(search_level)){
			params.put("search_level", search_level);
		}
		if(BaseUtils.IsNotEmpty(parent_aid)){
			params.put("parent_aid", parent_aid);
		}
		finalHttp.configCharset("utf-8");
		finalHttp.get(U.CASH_STRING_AREA, params, ajaxCallBack);

	}
	/***************** 地区接口 end ********************/

}