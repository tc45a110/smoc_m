/**
 * @desc
 * 动态常量，每间隔一定时间，从数据库中获取数据并更新
 */
package com.base.common.constant;

import com.base.common.manager.ResourceManager;

public class DynamicConstant {
	/**
	 * 平台标识
	 */
	public static final String PLATFORM_IDENTIFIER = ResourceManager.getInstance().getValue("platform.identifier");
	
	/**
	 * 程序停止的端口
	 */
	public static final int SHUTDOWN_PORT = ResourceManager.getInstance().getIntValue("shutdown.port");
	
	/**
	 * 响应成功码，当未配置时，默认使用平台InsideStatusCodeConstant.SUCCESS_CODE
	 */
	public static String  RESPONSE_SUCCESS_CODE = ResourceManager.getInstance().getValue("response.success.code");
	static{
		if(RESPONSE_SUCCESS_CODE == null){
			RESPONSE_SUCCESS_CODE=InsideStatusCodeConstant.SUCCESS_CODE;
		}
	}
	
	/**
	 * 回执成功码，当未配置时，默认使用平台InsideStatusCodeConstant.StatusCode.DELIVRD.name()
	 */
	public static String  REPORT_SUCCESS_CODE = ResourceManager.getInstance().getValue("report.success.code");
	static{
		if(REPORT_SUCCESS_CODE == null){
			REPORT_SUCCESS_CODE=InsideStatusCodeConstant.StatusCode.DELIVRD.name();
		}
	}
	
}


