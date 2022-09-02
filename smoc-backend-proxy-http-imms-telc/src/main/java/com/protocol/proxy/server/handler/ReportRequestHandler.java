package com.protocol.proxy.server.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.common.vo.BusinessRouteValue;
import com.protocol.proxy.manager.ReportWorkerManager;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 状态报告回调通知接口
 */
public class ReportRequestHandler implements HttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(ReportRequestHandler.class);
	private String channelID;
	

	public ReportRequestHandler(String channelID) {
		this.channelID=channelID;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
		JSONObject resultject = new JSONObject();
		logger.info("{},{},ip={}", exchange.getRequestMethod(), exchange.getRequestURI(), ip);
		OutputStream responseBody = exchange.getResponseBody();
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain;charset=utf-8");
		exchange.sendResponseHeaders(200, 0);
		try {

			InputStream is = exchange.getRequestBody();
			List<String> lines = IOUtils.readLines(is,"UTF-8");

			StringBuilder sb = new StringBuilder();

			for (String line : lines) {
				sb.append(line);
			}
			String queryString = sb.toString();
			logger.info("{},{},ip={},body={}", exchange.getRequestMethod(), exchange.getRequestURI(),
					ip, queryString);

			// 请求数据转json对象
			JSONObject request = null;
			try {
				request = JSONObject.parseObject(queryString);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if (request != null) {			
				process(request.getJSONArray("ReportList"));
			}
			
			resultject.put("ResCode", "1000");
			resultject.put("ResMsg", "成功");
			// 将响应结果map转成json数据返回
			responseBody.write(resultject.toString().getBytes());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultject.put("ResCode", "1000");
			resultject.put("ResMsg", "成功");
			responseBody.write(resultject.toString().getBytes());
		} finally {
			if (responseBody != null) {
				responseBody.close();
			}
		}
	}

	private void process(JSONArray array) {
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject object = array.getJSONObject(i);
				
				String transId = object.getString("TransID");
				String phone = object.getString("Phone");
				String state = object.getString("State");
				
				BusinessRouteValue newBusinessRouteValue = new BusinessRouteValue();
				newBusinessRouteValue.setChannelMessageID(transId);
				newBusinessRouteValue.setPhoneNumber(phone);
				newBusinessRouteValue.setStatusCode(state);
				newBusinessRouteValue.setChannelID(channelID);
				ReportWorkerManager.getInstance().process(newBusinessRouteValue);
			}

		}

	}
}
