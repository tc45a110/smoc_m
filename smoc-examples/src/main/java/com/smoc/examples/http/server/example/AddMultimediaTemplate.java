package com.smoc.examples.http.server.example;

import com.google.gson.Gson;
import com.smoc.examples.utils.*;

import java.util.*;

/**
 * 添加多媒体短信模板
 */
public class AddMultimediaTemplate {

    public static void main(String[] args) throws Exception {

        String url = "http://localhost:18088/smoc-gateway/http-server/template/addMultimediaTemplate";

        //自定义header协议
        Map<String, String> header = new HashMap<>();
        //signature-nonce 为17位数字，并且每次请求signature-nonce不能重复
        header.put("signature-nonce", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
        header.put("account", "YQT113");

        //请求的数据
        Map<String, Object> requestDataMap = new HashMap<>();
        //订单号，成功后的订单不能重复
        requestDataMap.put("orderNo", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(10));
        //业务账号；参见给的账号EXCEL文件
        requestDataMap.put("account", "YQT113");
        //模板类型 1 表示普通模板 2 表示变量模板
        requestDataMap.put("templateType", "2");
        //模板内容
        requestDataMap.put("content", "【我是一只小小鸟】你的小小鸟验证码为${1}，请勿告知他人");
        //时间戳
        requestDataMap.put("timestamp", DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS"));

        requestDataMap.put("templateTitle", "多媒体变量模板");

        List<Map<String,Object>> mediaItems = new ArrayList<>();
        Map<String,Object> param = new HashMap<>();
        param.put("subject","图片");
        param.put("mediaType","PIC");
        param.put("fileType","jpg");
        param.put("stayTimes",2);
        param.put("frameTxt","多媒体文件API测试,${1}");
        param.put("mediaFile", FileBASE64Utils.getFileToBase64("F:\\pic.jpg"));
        mediaItems.add(param);

        Map<String,Object> param1 = new HashMap<>();
        param1.put("subject","图片");
        param1.put("mediaType","PIC");
        param1.put("fileType","jpg");
        param1.put("stayTimes",2);
        param1.put("frameTxt","多媒体文件API测试,${1}");
        param1.put("mediaFile", FileBASE64Utils.getFileToBase64("F:\\pic.jpg"));
        mediaItems.add(param1);

        requestDataMap.put("items", mediaItems);
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
        String sign = HMACUtil.md5_HMAC_sign(signData.toString(), "LJ9u!X7di");
        System.out.println("[接口请求][签名数据]数据:" + signData);
        System.out.println("[接口请求][签名]数据:" + sign);

        header.put("signature", sign);

        String result = Okhttp3Utils.postJson(url, requestJsonData, header);
        System.out.println("[请求响应]数据:" + result);
    }
}
