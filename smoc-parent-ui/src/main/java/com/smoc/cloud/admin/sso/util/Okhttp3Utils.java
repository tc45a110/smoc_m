package com.smoc.cloud.admin.sso.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

/**
 * http请求工具类
 */
@Slf4j
public class Okhttp3Utils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 定义请求客户端
     */
    private static OkHttpClient client = new OkHttpClient();

    /**
     * get 请求
     *
     * @param url 请求URL
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        return doGet(url, Maps.newHashMap());
    }


    /**
     * get 请求
     *
     * @param url   请求URL
     * @param query 携带参数参数
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> query) throws Exception {

        return doGet(url, Maps.newHashMap(), query);
    }

    /**
     * get 请求
     *
     * @param url    url
     * @param header 请求头参数
     * @param query  参数
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

        try {
            Response execute = client.newCall(builder.build()).execute();
            String result = execute.body().string();
            log.info("[SSO][TOKEN验证]验证结果数据:{}", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sendGet(String url) {
        log.info("[SSO][url]验证结果数据1:");

        HttpsURLConnection connection = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL getUrl = new URL(url);
            connection = (HttpsURLConnection) getUrl.openConnection();
            connection.setConnectTimeout(1000 * 10);
            connection.setReadTimeout(20000);

            connection.setHostnameVerifier(new Okhttp3Utils().new TrustAnyHostnameVerifier());

            connection.connect();
            String line = "";
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            log.info("[SSO][TOKEN验证]验证结果数据:{}", result.toString());
            return result.toString();
        } catch (Exception e) {
            log.warn("[SSO][url]验证结果数据3:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                in.close();
                log.warn("[SSO][url]关闭流");
            } catch (Exception ex) {
                log.warn("[SSO][url]验证结果数据4:{}", ex.getMessage());
                ex.printStackTrace();
            }
        }
        log.info("[SSO][url]验证结果数据5:");
        return null;
    }

    // 定制Verifier
    public class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * 跳过Https的证书
     */
    public static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            log.info("[SSO][url]验证结果数据:10:{}", e.getMessage());
            e.printStackTrace();
        }
    }


}
