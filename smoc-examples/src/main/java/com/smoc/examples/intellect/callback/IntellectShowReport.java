package com.smoc.examples.intellect.callback;

import com.google.gson.Gson;
import com.smoc.examples.utils.DateTimeUtils;
import com.smoc.examples.utils.Okhttp3Utils;
import com.smoc.examples.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * 模版状态回执 示例
 */
public class IntellectShowReport {

   public  static void main(String[] args) throws Exception {

       //请求路径（具体参见技术文档）
       String url = "http://localhost:18088/smoc-gateway/intellect/smoc-identity/AimService/v1/AimManage/aimMsgShowRpt";

       //自定义header协议
       Map<String, String> header = new HashMap<>();
       header.put("account","AP0681");
       String timestamp = DateTimeUtils.getDateFormat(new Date(), "MMddHHmmss");
       header.put("timestamp", timestamp);
       header.put("pwd", DigestUtils.md5Hex("AP0681".toUpperCase() + "00000000" + "679870213340024832lwoSvz" + timestamp));

       //
       Map<String,Object> data = new HashMap<>();
       data.put("tplId","600060181");
       data.put("custFlag","4de0759554a44da4a25d691feee8fb9d");
       data.put("custId","INTEL100001568");
       data.put("aimUrl","6mnm.cn/LY5BFy");
       data.put("aimCode","LY5BFy");
       data.put("extData","");
       data.put("status",0);
       data.put("describe","");

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
