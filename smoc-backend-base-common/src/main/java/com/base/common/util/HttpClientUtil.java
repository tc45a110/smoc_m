/**
 * @desc
 * 
 */
package com.base.common.util;

import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * 
	 * @param url:请求路径
	 * @param body:请求体
	 * @param connectTimeout:连接超时时间，单位毫秒
	 * @param socketTimeout:请求获取数据的超时时间，单位毫秒
	 * @return
	 */
	public static String doRequest(String url,String body,int connectTimeout,int socketTimeout){
		return doRequest(url, "POST", null, body, connectTimeout, socketTimeout);
	}
	
	/**
	 * 
	 * @param url:请求路径
	 * @param header:请求头 json格式
	 * @param body:请求体
	 * @param connectTimeout:连接超时时间，单位毫秒
	 * @param socketTimeout:请求获取数据的超时时间，单位毫秒
	 * @return
	 */
	public static String doRequest(String url,Map<String,String> headerMap,String body,int connectTimeout,int socketTimeout){
		return doRequest(url, "POST", headerMap, body, connectTimeout, socketTimeout);
	}
	
	/**
	 * 
	 * @param url:请求路径
	 * @param method:请求方法GET/POST
	 * @param header:请求头 json格式
	 * @param body:请求体
	 * @param connectTimeout:连接超时时间，单位毫秒
	 * @param socketTimeout:请求获取数据的超时时间，单位毫秒
	 * @return
	 */
	public static String doRequest(String url,String method,Map<String,String> headerMap,String body,int connectTimeout,int socketTimeout){
		
		RequestConfig requestConfig = RequestConfig.custom()  
		        .setConnectTimeout(connectTimeout)
		        .setSocketTimeout(socketTimeout).build();

		if("POST".equalsIgnoreCase(method)){
			return doPostRequest(url, headerMap, body, requestConfig);
		}
		
		return doGetRequest(url, headerMap, requestConfig);
	}
	
	/**
	 * 
	 * @param url
	 * @param headerMap
	 * @param body
	 * @param requestConfig
	 * @return
	 */
	private static String doPostRequest(String url,Map<String,String> headerMap,String body,RequestConfig requestConfig){
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();  
			HttpPost httpPost = new HttpPost(url);
			if(headerMap != null){
				for(Map.Entry<String, String> entry:headerMap.entrySet()){
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			httpPost.setConfig(requestConfig);
			StringEntity entity = new StringEntity(body, "utf-8");
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);  
			return EntityUtils.toString(response.getEntity(), "utf-8");
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
		    try {
                if (null != response) {
                    response.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(),e);
            }
		 }
		return null;
	}
	
	/**
	 * @param url
	 * @param headerMap
	 * @param requestConfig
	 * @return
	 */
	private static String doGetRequest(String url,Map<String,String> headerMap,RequestConfig requestConfig){
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();  
			HttpGet httpGet = new HttpGet(url);
			if(headerMap != null){
				for(Map.Entry<String, String> entry:headerMap.entrySet()){
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);  
			return EntityUtils.toString(response.getEntity(), "utf-8");
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
		    try {
                if (null != response) {
                    response.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(),e);
            }
		 }
		return null;
	}
	
}


