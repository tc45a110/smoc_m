package com.smoc.examples.identity.example;

import com.google.gson.Gson;

import com.smoc.examples.identity.utils.*;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证号、姓名认证 示例
 */
public class IdentityIdNumberName {

   public  static void main(String[] args) throws Exception {

       //请求路径（具体参见技术文档）
       String url = "http://localhost:18088/gateway/identity/idNumberName";

       //自定义header协议
       Map<String, String> header = new HashMap<>();
       //signature-nonce 为17位数字，并且每次请求signature-nonce不能重复
       header.put("signature-nonce", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));

       //对要传输的敏感信息进行AES加密。AES密钥（AES_KEY）、AES偏移量（IV）参见给的账号EXCEL文件
       String cardNo = AESUtil.encrypt("510702197104034815","fNT44A1FM57Wu8HF5D7C6bHX372699MP","E96nx1xL929I86Te");

       //请求的数据
       Map<String,String> requestDataMap = new HashMap<>();
       //订单号，成功后的订单不能重复
       requestDataMap.put("orderNo",DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
       //认证账号；参见给的账号EXCEL文件
       requestDataMap.put("identifyAccount","XYIA100070");
       //认证的姓名
       requestDataMap.put("name","何大胜");
       //AES加密后的 身份证号
       requestDataMap.put("cardNo",cardNo);
       //转JSON请求数据
       String requestJsonData = new Gson().toJson(requestDataMap);

       //组织签名字符串
       StringBuffer signData = new StringBuffer();
       //header中的signature-nonce
       signData.append(header.get("signature-nonce"));
       //订单号
       signData.append(requestDataMap.get("orderNo"));
       //认证账号；
       signData.append(requestDataMap.get("identifyAccount"));
       //加密后的身份证号
       signData.append(requestDataMap.get("cardNo"));
       //签名 MD5_HMAC 签名KEY,参见给的账号EXCEL文件
       String sign = HMACUtil.md5_HMAC_sign(signData.toString(),"lv8T06232vQ409609Bb1pg2z45bVHeg6");
       System.out.println("[接口请求][签名数据]数据:" +signData);
       System.out.println("[接口请求][签名]数据:" +sign);

       header.put("signature", sign);

       String result = Okhttp3Utils.postJson(url,requestJsonData,header);
       System.out.println("[请求响应]数据:" +result);
   }
}
