package com.business.alarm.worker.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.HttpClientUtil;

public class SendMessage {	
	
	private static  Logger logger = LoggerFactory.getLogger(SendMessage.class);
	public static void  push(String text) {
		
	 String templateId=ResourceManager.getInstance().getValue("templateId");
		 
     Map<String, String> header = new HashMap<>();
     //signature-nonce 为17位数字，并且每次请求signature-nonce不能重复
     header.put("signature-nonce", DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI) + Utils.getRandom(10));
     header.put("account", ResourceManager.getInstance().getValue("username"));

     //请求的数据
     Map<String, Object> requestDataMap = new HashMap<>();
     //订单号，成功后的订单不能重复
     requestDataMap.put("orderNo",DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI) + Utils.getRandom(10));
     //业务账号
     requestDataMap.put("account", ResourceManager.getInstance().getValue("username"));

     //模板ID
     requestDataMap.put("templateId",templateId);

     //模板短信内容
     List<String> list = new ArrayList<>();
     List<String> phoneList = Arrays.asList(ResourceManager.getInstance().getValue("phone").split(","));
     for (int i = 0; i < phoneList.size(); i++) {  
    		
     list.add(new StringBuffer().append(phoneList.get(i)).append("|").append(text).toString());	 
    	  		 	 
     }
    
     requestDataMap.put("content", list);

     //扩展号码
     requestDataMap.put("extNumber", Utils.getRandom(4));
     //客户可选业务类型
     requestDataMap.put("business", "");

     //时间戳
     requestDataMap.put("timestamp", DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI));
     
      //转JSON请求数据
     String requestJsonData = JSON.toJSONString(requestDataMap);
  
     
     //组织签名字符串
     StringBuffer signData = new StringBuffer();
    
     signData.append(header.get("signature-nonce"));
     //订单号
     signData.append(requestDataMap.get("orderNo"));
     //认证账号；
     signData.append(requestDataMap.get("account"));
     //加密后的身份证号
     signData.append(requestDataMap.get("timestamp"));
     //签名 MD5_HMAC 签名KEY
     String sign = Utils.md5_HMAC_sign(signData.toString(), ResourceManager.getInstance().getValue("password"));
     
     header.put("signature", sign);
     header.put("Content-Type", "application/json;charset=utf-8");
      
     String result = HttpClientUtil.doRequest(ResourceManager.getInstance().getValue("Url"), header, requestJsonData, 1000, 1000);
     
     if(StringUtils.isNotEmpty(result)){
    	 JSONObject json = JSONObject.parseObject(result);
    	 if("0000".equals(json.getString("code"))) {
    		 logger.info("短信发送成功");    		
    	 }
     }  
	}

}
