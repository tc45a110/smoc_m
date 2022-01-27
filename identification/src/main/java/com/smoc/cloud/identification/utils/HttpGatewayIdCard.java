package com.smoc.cloud.identification.utils;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.AESUtil;
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
       String url = "http://localhost:18088/gateway/identity/idNumberName";

       log.info("[请求url]：{}",url);
       //自定义协议header
       Map<String, String> header = new HashMap<>();
       header.put("signature-nonce", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));

       //身份证号 AES 加密
       String cardNo = AESUtil.encrypt("372523198002165313","fNT44A1FM57Wu8HF5D7C6bHX372699MP","E96nx1xL929I86Te");

       //请求数据
       Map<String,String> requestDataMap = new HashMap<>();
       requestDataMap.put("orderNo",DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
       requestDataMap.put("identifyAccount","XYIA100070");
       requestDataMap.put("name","武基慧");
       requestDataMap.put("cardNo",cardNo);
       String requestJsonData = new Gson().toJson(requestDataMap);

       //签名
       StringBuffer signData = new StringBuffer();
       signData.append(header.get("signature-nonce"));
       signData.append(requestDataMap.get("orderNo"));
       signData.append(requestDataMap.get("identifyAccount"));
       signData.append(requestDataMap.get("cardNo"));
       String sign = HMACUtil.md5_HMAC_sign(signData.toString(),"lv8T06232vQ409609Bb1pg2z45bVHeg6");
       log.info("[接口请求][签名数据]数据:{}" ,signData);
       log.info("[接口请求][签名]数据:{}" ,sign);

       header.put("signature", sign);

       String result = Okhttp3Utils.postJson(url,requestJsonData,header);
       log.info("[请求响应]数据:{}" ,result);
   }
}
