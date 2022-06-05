/**
 * @desc
 * 
 */
package com.base.common.constant;

import java.util.HashMap;
import java.util.Map;

public class FilterResponseCodeConstant {
	//调用过滤服务接口响应码-状态码映射关系
	static Map<String,String>  FilterResponseCodeMap = new HashMap<String, String>();
	
	static {
		//该号码在本地黑名单中
		FilterResponseCodeMap.put("1101", InsideStatusCodeConstant.StatusCode.BLACK.name());
		//该号码在本地黑名单中
		FilterResponseCodeMap.put("1102", InsideStatusCodeConstant.StatusCode.BLACK.name());
		//该号码在第三方黑名单中
		FilterResponseCodeMap.put("1103", InsideStatusCodeConstant.StatusCode.TBLACK.name());

		FilterResponseCodeMap.put("", "");
		FilterResponseCodeMap.put("", "");
		FilterResponseCodeMap.put("", "");
	}
	
}


