/**
 * @desc
 * 
 */
package com.base.common.util;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.MNPCacheBaseService;
import com.base.common.constant.FixedConstant;

public class MNPUtil {
	
	/**
	 * 获取国内号码业务运营商标识
	 * @param accountCarrier:账号发送运营商
	 * @param phoneNumber:手机号
	 * @param segmentCarrier:运营号段
	 * @param transferType:支持携号转网标识
	 * @return
	 */
	public static String getInternalBusinessCarrier(String accountCarrier,String phoneNumber,String segmentCarrier,String transferType){
		
		if(accountCarrier.split(FixedConstant.DATABASE_SEPARATOR).length == 1){
			//当账号发送运营商为单运营商，并且支持携号转网时，以发送运营商为主
			if(String.valueOf(FixedConstant.TransferSupport.SUPPORT.ordinal()).equals(transferType)){
				return accountCarrier;
			}else if(!accountCarrier.equals(segmentCarrier)){
				//不支持携号转网时，当发送运营商和运营商号段不一致时，以运营商号段为准
				return segmentCarrier;
			}
		}
		
		String businessCarrier = MNPCacheBaseService.getMNPFromMiddlewareCache(phoneNumber);
		if(StringUtils.isNotEmpty(businessCarrier)){
			return businessCarrier;
		}
		return segmentCarrier;
	}
}


