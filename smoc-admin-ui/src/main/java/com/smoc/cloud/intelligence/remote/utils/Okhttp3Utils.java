package com.smoc.cloud.intelligence.remote.utils;

import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类
 */
public class Okhttp3Utils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * json格式post请求接口调用
     *
     * @param url  接口地址
     * @param requestJsonData json格式请求参数体
     * @return
     */
    public static String postJson(String url, String requestJsonData, Map<String, String> header) {

        RequestBody body = RequestBody.create(JSON, requestJsonData);

        //循环添加header
        Request.Builder requestBuilder = new Request.Builder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.url(url)
                .post(body)
                .build();

        Response response;
        String jsonString = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            response = client.newCall(request).execute();
            jsonString = response.body().string();
            //System.out.println("[请求响应]原数据:" + jsonString);
            if (response.isSuccessful()) {
                return jsonString;
            }else{
                return "{\"subCode\":500,\"message\":\"系统无响应\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"subCode\":500,\"message\":\""+e.getMessage()+"\"}";
        }
    }




}
