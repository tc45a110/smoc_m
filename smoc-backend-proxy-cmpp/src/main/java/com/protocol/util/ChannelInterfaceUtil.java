/**
 * @desc
 * 连接参数缓存
 */
package com.protocol.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ChannelInfoManager;

public class ChannelInterfaceUtil {
	
	/**
	 * 根据协议规范转化成协议对接的字段参数
	 * @param channelID
	 * @return
	 */
	static public Map<String, String> getArgMap(String channelID){
		Map<String, String> interfaceInfoMap = ChannelInfoManager.getInstance().getChannelInterfaceInfo(channelID);	
		Map<String, String> resultMap = new HashMap<String, String>(interfaceInfoMap);
		
		String channelServiceUrl = interfaceInfoMap.get("CHANNEL_SERVICE_URL");
		if(StringUtils.isNotEmpty(channelServiceUrl) ){
			String[] array = channelServiceUrl.split(":");
			if(array.length > 0){
				resultMap.put("host", array[0]);
			}
			if(array.length > 1){
				resultMap.put("port", array[1]);
			}else{
				resultMap.put("port", "7890");
			}
		}else{
			resultMap.put("host","127.0.0.1");
			resultMap.put("port", "7890");
		}
		
		resultMap.put("login-name", interfaceInfoMap.get("CHANNEL_ACCESS_ACCOUNT"));
		resultMap.put("login-pass", interfaceInfoMap.get("CHANNEL_ACCESS_PASSWORD"));
		resultMap.put("version", interfaceInfoMap.get("VERSION"));
		resultMap.put("srcId", interfaceInfoMap.get("SRC_ID"));
		resultMap.put("corpId", interfaceInfoMap.get("SP_ID"));
		resultMap.put("serviceType", interfaceInfoMap.get("BUSINESS_CODE"));
		resultMap.put("heartbeat-interval", interfaceInfoMap.get("HEARTBEAT_INTERVAL"));
		resultMap.put("priority", "5");
		resultMap.put("reportFlag","1");
		resultMap.put("feeType","01");
		
		//个性化扩展参数可以覆盖默认参数
		String extendInterfaceParam = BusinessDataManager.getInstance().getExtendInterfaceParam(channelID);
		if(StringUtils.isNotEmpty(extendInterfaceParam) ){
			String[] array = extendInterfaceParam.split(FixedConstant.DATABASE_SEPARATOR);
			for(String param:array){
				String[] paramArray = param.split("=");
				if(paramArray.length == 2){
					resultMap.put(paramArray[0], paramArray[1]);
				}
			}
		}
		
		return resultMap;
	}
}


