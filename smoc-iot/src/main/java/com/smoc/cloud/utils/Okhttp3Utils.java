package com.smoc.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类
 */
@Slf4j
public class Okhttp3Utils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * json格式post请求接口调用
     *
     * @param url             接口地址
     * @param requestJsonData json格式请求参数体
     * @return
     */
    public static String postJson(String url, String requestJsonData, Map<String, String> header) {

        log.info("[url]:{}", url);
        log.info("[requestJsonData]:{}", requestJsonData);
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
            } else {
                return "{\"status\":3001,\"message\":\"获取运营商数据异常\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":3001,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * get请求
     *
     * @param url 请求url
     * @return 结果字符串
     */
    public static String get(String url) throws Exception {

        Response response;
        try {
            Request.Builder requestBuilder = new Request.Builder();
            Request request = requestBuilder.url(url)
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":3001,\"message\":\"" + e.getMessage() + "\"}";
        }
    }


}
