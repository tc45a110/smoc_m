package com.smoc.cloud.identification.utils;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.HMACUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpGatewayIdCard {

   public  static void main(String[] args) throws Exception {
       String url = "http://localhost:18088/gateway/identification/idCard/name";

       //自定义协议header
       Map<String, String> header = new HashMap<>();
       header.put("signature-nonce", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
       header.put("signature-account", "ddddddddddd");


       //请求数据
       Map<String,String> requestDataMap = new HashMap<>();
       requestDataMap.put("orderNo",DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
       requestDataMap.put("name","基慧");
       requestDataMap.put("cardNo","372523198002165313");
       String requestJsonData = new Gson().toJson(requestDataMap);

       //签名
       StringBuffer signData = new StringBuffer();
       signData.append(header.get("signature-nonce"));
       signData.append(header.get("signature-account"));
       signData.append(requestDataMap.get("orderNo"));
       signData.append(requestDataMap.get("name"));
       signData.append(requestDataMap.get("cardNo"));
       String sign = HMACUtil.md5_HMAC_sign(signData.toString(),"Ww7hv8XwKVv904Iauu0026trOle");
       log.info("[接口请求][签名数据]数据:{}" ,signData);
       log.info("[接口请求][签名]数据:{}" ,sign);

       header.put("signature", sign);

       String result = Okhttp3Utils.postJson(url,requestJsonData,header);
       log.info("[请求响应]数据:{}" ,result);
   }
}
