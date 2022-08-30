package com.inse.server.handler;

import com.inse.util.ChannelInterfaceUtil;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

public class CallbackHTTPServer {
	private static final Logger logger = LoggerFactory.getLogger(CallbackHTTPServer.class);
	HttpServer server;
	private String port;
	private String channelID;

	public CallbackHTTPServer(String port, String channelID) {
		this.port = port;
		this.channelID = channelID;
	}

	public void start() {
		try {
			// 获取通道接口扩展参数
			Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);

			InetSocketAddress addr = new InetSocketAddress(Integer.valueOf(port));
			server = HttpServer.create(addr, Integer.valueOf(resultMap.get("maxconnect")));

			// 模板回执
			String statusServiceName = resultMap.get("statusUrl");
			// 状态回执
			String reportServiceName = resultMap.get("reportUrl");

			server.createContext(statusServiceName, new StatusRequestHandler(channelID));
			server.createContext(reportServiceName, new ReportRequestHandler(channelID));
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			logger.info("通道{}监听端口{},接收服务启动成功",channelID,port);

		} catch (Exception e) {
			logger.error("通道{}监听端口{},接收服务启动失败",channelID,port,e);
		}
	}

	public void exit(){
		server.stop(0);
	}
}
