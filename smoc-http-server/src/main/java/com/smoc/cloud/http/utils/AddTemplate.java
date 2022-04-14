package com.smoc.cloud.http.utils;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.AESUtil;
import com.smoc.cloud.common.gateway.utils.HMACUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证号、姓名认证 示例
 */
@Slf4j
public class AddTemplate {

   public  static void main(String[] args) throws Exception {

       String url = "http://localhost:18088/smoc-gateway/http/message/template/add";

       //自定义header协议
       Map<String, String> header = new HashMap<>();
       //signature-nonce 为17位数字，并且每次请求signature-nonce不能重复
       header.put("signature-nonce", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));

       //请求的数据
       Map<String,String> requestDataMap = new HashMap<>();
       //订单号，成功后的订单不能重复
       requestDataMap.put("orderNo",DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
       //认证账号；参见给的账号EXCEL文件
       requestDataMap.put("account","SWL102");
       //认证的姓名
       requestDataMap.put("content","何大胜");
       //AES加密后的 身份证号
       requestDataMap.put("timestamp",DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS"));
       //转JSON请求数据
       String requestJsonData = new Gson().toJson(requestDataMap);

       //组织签名字符串
       StringBuffer signData = new StringBuffer();
       //header中的signature-nonce
       signData.append(header.get("signature-nonce"));
       //订单号
       signData.append(requestDataMap.get("orderNo"));
       //认证账号；
       signData.append(requestDataMap.get("account"));
       //加密后的身份证号
       signData.append(requestDataMap.get("timestamp"));
       //签名 MD5_HMAC 签名KEY,参见给的账号EXCEL文件
       String sign = HMACUtil.md5_HMAC_sign(signData.toString(),"16B6DF81DF15FDFE75441AF3FBF9E12E");
       log.info("[接口请求][签名数据]数据:{}" ,signData);
       log.info("[接口请求][签名]数据:{}" ,sign);

       header.put("signature", sign);

       String result = Okhttp3Utils.postJson(url,requestJsonData,header);
       log.info("[请求响应]数据:{}" ,result);
   }
}
