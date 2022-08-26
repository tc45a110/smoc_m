package com.inse.server.handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.inse.message.StatusMessage;
import com.inse.worker.StatusMessageWorker;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 模板审核回调通知接口
 */
public class StatusRequestHandler implements HttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(StatusRequestHandler.class);

	StatusMessageWorker statusMessageWorker;

	public StatusRequestHandler(String channel) {
		statusMessageWorker = new StatusMessageWorker(channel);
		
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String msgid = UUID.randomUUID().toString().replaceAll("-", "");
		String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
		Map<String, String> resultMap = new HashMap<String, String>();

		logger.info("{},{},msgid={},ip={}", exchange.getRequestMethod(), exchange.getRequestURI(), msgid, ip);
		OutputStream responseBody = exchange.getResponseBody();
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain;charset=utf-8");
		exchange.sendResponseHeaders(200, 0);
		try {

			InputStream is = exchange.getRequestBody();
			List<String> lines = IOUtils.readLines(is);
			StringBuffer sb = new StringBuffer();
			for (String line : lines) {
				sb.append(line);
			}
			String queryString = sb.toString();
			logger.info("{},{},msgid={},ip={},body={}", exchange.getRequestMethod(), exchange.getRequestURI(), msgid,
					ip, queryString);

			// 请求数据转json对象
			JSONObject request = null;
			try {
				request = JSONObject.parseObject(queryString);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			process(request);
			resultMap.put("ResCode:", "1000");
			resultMap.put("ResMsg", "成功");
			// 将响应结果map转成json数据返回
			responseBody.write(resultMap.toString().getBytes());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("ResCode:", "1000");
			resultMap.put("ResMsg", "成功");
			responseBody.write(resultMap.toString().getBytes());
		} finally {
			if (responseBody != null) {
				responseBody.close();
			}
		}
	}

	private void process(JSONObject jsonObject) {
		if (jsonObject != null) {
			String msgID = jsonObject.getString("MsgID");
			String checkState = jsonObject.getString("CheckState");
			String reason = jsonObject.getString("Reason");
			String date = jsonObject.getString("Date");
			StatusMessage message = new StatusMessage();

			message.setTemplateId(msgID);
			message.setCheckState(checkState);
			message.setReason(reason);
			message.setDate(date);

			statusMessageWorker.add(message);
		}

	}

}
