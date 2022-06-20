/**
 * @desc
 * 动态常量，每间隔一定时间，从数据库中获取数据并更新
 */
package com.base.common.constant;

public class RedisHashKeyConstant {
	
	/**
	 * 账号运营商日限量hash的key值前缀
	 */
	public static final String ACCOUNT_CARRIER_DAILY_LIMIT_PREFIX = "filters:temporary:limit:flow:carrier:";
	
	/**
	 *  账号流控hash的key值前缀
	 */
	public static final String ACCOUNT_SPEED_PREFIX = "account:temporary:speed:";
	
	/**
	 * 账号日价格hash的key值前缀
	 */
	public static final String ACCOUNT_PRICE_PREFIX = "account:temporary:price:";
	
	/**
	 * 通道日价格hash的key值前缀
	 */
	public static final String CHANNEL_PRICE_PREFIX = "channel:temporary:price:";
	
	/**
	 * 通道限量hash的key值前缀
	 */
	public static final String CHANNEL_LIMIT_PREFIX = "channel:temporary:limit:";
	
}


