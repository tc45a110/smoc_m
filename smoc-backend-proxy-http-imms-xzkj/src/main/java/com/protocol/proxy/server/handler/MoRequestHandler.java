package com.protocol.proxy.server.handler;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelMOManager;
import com.base.common.util.ChannelMOUtil;
import com.base.common.vo.ChannelMO;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上行回调通知接口
 */
public class MoRequestHandler implements HttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(MoRequestHandler.class);
	private String channelID;

	public MoRequestHandler(String channelID) {
		this.channelID = channelID;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String msgId = UUID.randomUUID().toString().replaceAll("-", "");
		String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
		logger.info("{},{},msgId={},ip={}", exchange.getRequestMethod(), exchange.getRequestURI(),msgId, ip);
		OutputStream responseBody = exchange.getResponseBody();
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "application/json;charset=utf-8");
		exchange.sendResponseHeaders(200, 0);
		Map<String, Object> response = new HashMap<>();
		try {

			InputStream is = exchange.getRequestBody();
			List<String> lines = IOUtils.readLines(is);
			StringBuffer sb = new StringBuffer();
			for (String line : lines) {
				sb.append(line);
			}
			String queryString = sb.toString();
			logger.info("{},{},msgId={},ip={},body={}", exchange.getRequestMethod(), exchange.getRequestURI(), msgId,
					ip, queryString);
			//请求数据转json对象
			process(queryString);
			response.put("code", "0000");
			response.put("message","接受成功");
			responseBody.write(new Gson().toJson(response).getBytes());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.put("code", "0000");
			response.put("message","接受成功");
			responseBody.write(new Gson().toJson(response).getBytes());
		} finally {
			if (responseBody != null) {
				responseBody.close();
			}
		}
	}

	private void process(String queryString) {
		if (StringUtils.isNotEmpty(queryString)) {
			JSONObject request = JSONObject.parseObject(queryString);
			JSONArray array = JSONObject.parseArray(request.getString("data"));
			for (int i = 0; i < array.size(); i++) {
				JSONObject object = array.getJSONObject(i);
				String phoneNumber = object.getString("mobile");
				String content = object.getString("content");
				String extNumber = object.getString("extNumber");
				String channelSRCID = ChannelInfoManager.getInstance().getChannelSRCID(channelID);
				ChannelMO channelMO = ChannelMOUtil.getMO(phoneNumber,extNumber,content,channelID,channelSRCID);

				ChannelMOManager.getInstance().put(channelMO.getBusinessMessageID(), channelMO);
			}

		}
	}
}
