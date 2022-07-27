package com.smoc.examples.iot.example;

import com.google.gson.Gson;
import com.smoc.examples.utils.DateTimeUtils;
import com.smoc.examples.utils.HMACUtil;
import com.smoc.examples.utils.Okhttp3Utils;
import com.smoc.examples.utils.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuerySimFlowUsedThisMonthTotal extends Base{

    public  static void main(String[] args) throws Exception {

        //请求路径（具体参见技术文档）
        String url = baseUrl + "/iot/sim/flow/querySimFlowUsedThisMonthTotal";

        //自定义header协议
        Map<String, String> header = new HashMap<>();
        //signature-nonce 为17位数字，并且每次请求signature-nonce不能重复
        String signatureNonce =  DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10);
        System.out.println("[接口请求][signatureNonce]数据:" +signatureNonce);
        header.put("signature-nonce",signatureNonce);
        header.put("account", "IOT100001618");

        //请求的数据
        Map<String,String> requestDataMap = new HashMap<>();
        //账号
        requestDataMap.put("account","IOT100001618");
        //时间戳
        String timestamp = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS");
        requestDataMap.put("timestamp",timestamp);

        requestDataMap.put("msisdn","14765004176");

        //组织签名字符串
        StringBuffer signData = new StringBuffer();
        //header中的signature-nonce
        signData.append(header.get("signature-nonce"));
        //认证账号；
        signData.append(requestDataMap.get("account"));
        //加密后的身份证号
        signData.append(requestDataMap.get("timestamp"));
        //签名 MD5_HMAC 签名KEY,参见给的账号EXCEL文件
        String sign = HMACUtil.md5_HMAC_sign(signData.toString(),"235P990t4vGB917d009t313UEF378224");
        System.out.println("[接口请求][签名数据]数据:" +signData);
        System.out.println("[接口请求][签名]数据:" +sign);

        header.put("signature", sign);
        //转JSON请求数据
        String requestJsonData = new Gson().toJson(requestDataMap);
        System.out.println("[请求JSON]数据:" +requestJsonData);
        String result = Okhttp3Utils.postJson(url,requestJsonData,header);
        System.out.println("[请求响应]数据:" +result);
    }
}
