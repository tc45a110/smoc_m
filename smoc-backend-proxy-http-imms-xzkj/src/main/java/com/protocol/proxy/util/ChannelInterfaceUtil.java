package com.protocol.proxy.util;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ChannelInfoManager;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ChannelInterfaceUtil {

    /**
     * 根据协议规范转化成协议对接的字段参数
     * @param channelID
     * @return
     */
    static public Map<String, String> getArgMap(String channelID){
        Map<String, String> interfaceInfoMap = ChannelInfoManager.getInstance().getChannelInterfaceInfo(channelID);
        Map<String, String> resultMap = new HashMap<String, String>();

        resultMap.put("url", interfaceInfoMap.get("CHANNEL_SERVICE_URL"));
        resultMap.put("login-name", interfaceInfoMap.get("CHANNEL_ACCESS_ACCOUNT"));
        resultMap.put("login-pass", interfaceInfoMap.get("CHANNEL_ACCESS_PASSWORD"));
        resultMap.put("version", interfaceInfoMap.get("VERSION"));
        resultMap.put("srcId", interfaceInfoMap.get("SRC_ID"));
        resultMap.put("corpId", interfaceInfoMap.get("SP_ID"));
        resultMap.put("serviceType", interfaceInfoMap.get("BUSINESS_CODE"));
        resultMap.put("heartbeat-interval", interfaceInfoMap.get("HEARTBEAT_INTERVAL"));

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
