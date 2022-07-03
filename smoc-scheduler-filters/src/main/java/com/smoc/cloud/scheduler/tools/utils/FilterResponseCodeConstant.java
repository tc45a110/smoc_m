/**
 * @desc
 * 
 */
package com.smoc.cloud.scheduler.tools.utils;

import java.util.HashMap;
import java.util.Map;

public class FilterResponseCodeConstant {
	//调用过滤服务接口响应码-状态码映射关系
	static Map<String,String>  FilterResponseCodeMap = new HashMap<String, String>();
	
	static {
		//操作通过
		FilterResponseCodeMap.put("0000", InsideStatusCodeConstant.SUCCESS_CODE);
		
		//该号码在第三方黑名单中
		FilterResponseCodeMap.put("1", InsideStatusCodeConstant.StatusCode.TBLACK.name());
		
		//该号码在本地黑名单中
		FilterResponseCodeMap.put("1101", InsideStatusCodeConstant.StatusCode.BLACK.name());
		//该号码在本地黑名单中
		FilterResponseCodeMap.put("1102", InsideStatusCodeConstant.StatusCode.BLACK.name());
		//该号码在第三方黑名单中
		FilterResponseCodeMap.put("1103", InsideStatusCodeConstant.StatusCode.TBLACK.name());

		//没有设置单手机号发送频率限制参数，或配置不规范!
		FilterResponseCodeMap.put("1200", InsideStatusCodeConstant.StatusCode.NOLIMIT.name());
		//单手机号发送频率参数设置不规范
		FilterResponseCodeMap.put("1201", InsideStatusCodeConstant.StatusCode.NOLIMIT.name());
		//达到手机号发送频率设置的上限
		FilterResponseCodeMap.put("1202", InsideStatusCodeConstant.StatusCode.FLIMIT.name());
		//手机号码被号码扩展字段拦截
		FilterResponseCodeMap.put("1203", InsideStatusCodeConstant.StatusCode.PNLIMIT.name());
		//手机号码被号码正则拦截
		FilterResponseCodeMap.put("1204", InsideStatusCodeConstant.StatusCode.PNLIMIT.name());
		
		//被扩展字段敏感词拦截！
		FilterResponseCodeMap.put("1205", InsideStatusCodeConstant.StatusCode.KEYWORD.name());
		//被系统敏感词正则拦截！
		FilterResponseCodeMap.put("1206", InsideStatusCodeConstant.StatusCode.KEYWORD.name());
		//被系统敏感词拦截[拦截到的词、拦截到的词]！
		FilterResponseCodeMap.put("1207", InsideStatusCodeConstant.StatusCode.KEYWORD.name());
		//被系统审核词拦截[拦截到的词、拦截到的词]！
		FilterResponseCodeMap.put("1208", InsideStatusCodeConstant.StatusCode.AUDIT.name());
		//被行业敏感词拦截[拦截到的词、拦截到的词]！
		FilterResponseCodeMap.put("1209", InsideStatusCodeConstant.StatusCode.KEYWORD.name());
		//被业务账号敏感词拦截[拦截到的词、拦截到的词]！
		FilterResponseCodeMap.put("1210", InsideStatusCodeConstant.StatusCode.KEYWORD.name());
		//被业务账号审核词拦截[拦截到的词、拦截到的词]！
		FilterResponseCodeMap.put("1211", InsideStatusCodeConstant.StatusCode.AUDIT.name());
		//手机号码被行业黑名单拦截！
		FilterResponseCodeMap.put("1212", InsideStatusCodeConstant.StatusCode.BBLACK.name());
		//被通道敏感词拦截[拦截到的词、拦截到的词]
		FilterResponseCodeMap.put("1213", InsideStatusCodeConstant.StatusCode.KEYWORD.name());
		
		//达到运营商日限量限制！
		FilterResponseCodeMap.put("1301", InsideStatusCodeConstant.StatusCode.DLIMIT.name());
		//发送时间段参数配置错误！
		FilterResponseCodeMap.put("1302", InsideStatusCodeConstant.StatusCode.ILLEGAL.name());
		//该时间不能发送短信！
		FilterResponseCodeMap.put("1303", InsideStatusCodeConstant.StatusCode.ILLEGAL.name());	
		//该发送省份已被屏蔽！
		FilterResponseCodeMap.put("1304", InsideStatusCodeConstant.StatusCode.SHIELD.name());
		//签名不能为空！
		FilterResponseCodeMap.put("1400", InsideStatusCodeConstant.StatusCode.UNSIGNA.name());
		//签名没有报备！
		FilterResponseCodeMap.put("1401", InsideStatusCodeConstant.StatusCode.ERRSIGN.name());
		//没匹配到模版ID对应模版！
		FilterResponseCodeMap.put("1402", InsideStatusCodeConstant.StatusCode.UNMATCH.name());
		//请求达到限流上限
		FilterResponseCodeMap.put("2000", InsideStatusCodeConstant.StatusCode.RLIMIT.name());
		//触发熔断机制
		FilterResponseCodeMap.put("2001", InsideStatusCodeConstant.StatusCode.FUSING.name());
		
		//非法请求
		FilterResponseCodeMap.put("3004", InsideStatusCodeConstant.StatusCode.INVAREQ.name());
		//IP访问受限
		FilterResponseCodeMap.put("3005", InsideStatusCodeConstant.StatusCode.IPLIMIT.name());

	}
	
	/**
	 * 
	 * @param filterResponseCode
	 * @return
	 */
	public static String mapping(String filterResponseCode){
		return FilterResponseCodeMap.get(filterResponseCode);
	}
	
}


