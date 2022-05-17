package com.smoc.examples.intellect.callback;

import com.google.gson.Gson;
import com.smoc.examples.utils.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * 模版状态回执 示例
 */
public class IntellectTemplateStatus {

   public  static void main(String[] args) throws Exception {

       //请求路径（具体参见技术文档）
       String url = "http://localhost:18088/smoc-gateway/intellect/smoc-identity/AimService/v1/TemplateManage/aimTplAuditRpt";

       //自定义header协议
       Map<String, String> header = new HashMap<>();
       header.put("account","AP0681");
       String timestamp = DateTimeUtils.getDateFormat(new Date(), "MMddHHmmss");
       header.put("timestamp", timestamp);
       header.put("pwd", DigestUtils.md5Hex("AP0681".toUpperCase() + "00000000" + "679870213340024832lwoSvz" + timestamp));

       //
       Map<String,Object> data = new HashMap<>();
       data.put("tplId","600059887");
       data.put("bizId","4de0759554a44da4a25d691feee8fb9d");
       data.put("bizFlag","INTEL100001558");
       data.put("tplState",0);
       data.put("auditState",3);
       data.put("auditDesc","审核不通过");
       List<Map<String,Object>> factoryInfoList = new ArrayList<>();
       Map<String,Object> HuaWei = new HashMap<>();
       HuaWei.put("factoryType","HuaWei");
       HuaWei.put("state",0);
       factoryInfoList.add(HuaWei);
       data.put("factoryInfoList",factoryInfoList);

       //请求的数据
       Map<String,Object> requestDataMap = new HashMap<>();
       //订单号，成功后的订单不能重复
       requestDataMap.put("custId",DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
       //认证账号；参见给的账号EXCEL文件
       requestDataMap.put("msgBody",data);
       //转JSON请求数据
       String requestJsonData = new Gson().toJson(requestDataMap);

       String result = Okhttp3Utils.postJson(url,requestJsonData,header);
       System.out.println("[请求响应]数据:" +result);
   }
}
