package com.smoc.cloud.identification.utils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
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
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .build();
            response = client.newCall(request).execute();
            jsonString = response.body().string();
            if (response.isSuccessful()) {
                return jsonString;
            }else{
                return "{\"code\":500,\"message\":\"系统无响应\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"code\":500,\"message\":\""+e.getMessage()+"\"}";
        }
    }

    /**
     * 定义请求客户端
     */
    private static OkHttpClient client = new OkHttpClient();

    /**
     * get 请求
     * @param url 请求URL
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        return doGet(url, Maps.newHashMap());
    }


    /**
     * get 请求
     * @param url 请求URL
     * @param query 携带参数参数
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> query) throws Exception {

        return doGet(url, Maps.newHashMap(), query);
    }

    /**
     * get 请求
     * @param url url
     * @param header 请求头参数
     * @param query 参数
     * @return
     */
    public static String doGet(String url, Map<String, Object> header, Map<String, Object> query) throws Exception {

        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(url).build();

        // 创建一个 HttpUrl.Builder
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();

        // 装载请求头参数
        Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
        headerIterator.forEachRemaining(e -> {
            headerBuilder.add(e.getKey(), (String) e.getValue());
        });

        // 装载请求的参数
        Iterator<Map.Entry<String, Object>> queryIterator = query.entrySet().iterator();
        queryIterator.forEachRemaining(e -> {
            urlBuilder.addQueryParameter(e.getKey(), (String) e.getValue());
        });

        // 设置自定义的 builder
        // 因为 get 请求的参数，是在 URL 后面追加  http://xxxx:8080/user?name=xxxx?sex=1
        builder.url(urlBuilder.build()).headers(headerBuilder.build());

        try (Response execute = client.newCall(builder.build()).execute()) {
            System.out.println("[接口响应][响应header]数据:" + new Gson().toJson(execute.headers()));
            String mpmSignature = execute.headers().get("mpm-signature");
            System.out.println("[接口响应][响应数据签名]数据:" + mpmSignature);
            return execute.body().string();
        }
    }

    /**
     * post 请求， 请求参数 并且 携带文件上传
     * @param url
     * @param header
     * @param parameter
     * @param file 文件
     * @param fileFormName 远程接口接收 file 的参数
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> header, Map<String, Object> parameter, File file, String fileFormName) throws Exception {

        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(url).build();

        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();

        // 装载请求头参数
        Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
        headerIterator.forEachRemaining(e -> {
            headerBuilder.add(e.getKey(), (String) e.getValue());
        });

        // 或者 FormBody.create 方式，只适用于接口只接收文件流的情况
        // RequestBody requestBody = FormBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder requestBuilder = new MultipartBody.Builder();

        // 状态请求参数
        Iterator<Map.Entry<String, Object>> queryIterator = parameter.entrySet().iterator();
        queryIterator.forEachRemaining(e -> {
            requestBuilder.addFormDataPart(e.getKey(), (String) e.getValue());
        });

        if (null != file) {
            // application/octet-stream
            //requestBuilder.addFormDataPart(StringUtils.isNotBlank(fileFormName) ? fileFormName : "file", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")));
        }

        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(requestBuilder.build());

        // 然后再 build 一下
        try (Response execute = client.newCall(builder.build()).execute()) {
            return execute.body().string();
        }
    }

    /**
     * post 请求， 请求参数 并且 携带文件上传二进制流
     * @param url
     * @param header
     * @param parameter
     * @param fileName 文件名
     * @param fileByte 文件的二进制流
     * @param fileFormName 远程接口接收 file 的参数
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> header, Map<String, Object> parameter, String fileName, byte [] fileByte, String fileFormName) throws Exception {
        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(url).build();

        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();

        // 装载请求头参数
        Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
        headerIterator.forEachRemaining(e -> {
            headerBuilder.add(e.getKey(), (String) e.getValue());
        });

        MultipartBody.Builder requestBuilder = new MultipartBody.Builder();

        // 状态请求参数
        Iterator<Map.Entry<String, Object>> queryIterator = parameter.entrySet().iterator();
        queryIterator.forEachRemaining(e -> {
            requestBuilder.addFormDataPart(e.getKey(), (String) e.getValue());
        });

        if (fileByte.length > 0) {
            // application/octet-stream
            //requestBuilder.addFormDataPart(StringUtils.isNotBlank(fileFormName) ? fileFormName : "file", fileName, RequestBody.create(fileByte, MediaType.parse("application/octet-stream")));
        }

        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(requestBuilder.build());

        try (Response execute = client.newCall(builder.build()).execute()) {
            return execute.body().string();
        }
    }


    /**
     * post 请求  携带文件上传
     * @param url
     * @param file
     * @return
     * @throws Exception
     */
    public static String doPost(String url, File file, String fileFormName) throws Exception {
        return doPost(url, Maps.newHashMap(), Maps.newHashMap(), file, fileFormName);
    }

    /**
     * post 请求
     * @param url
     * @param header 请求头
     * @param parameter 参数
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> header, Map<String, Object> parameter) throws Exception {
        return doPost(url, header, parameter, null, null);
    }

    /**
     * post 请求
     * @param url
     * @param parameter 参数
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> parameter) throws Exception {
        return doPost(url, Maps.newHashMap(), parameter, null, null);
    }

    /**
     * 没有请求头的请求
     */
    public static void formBody(String url,Map<String,Object> params) throws Exception {


    }



}
