package com.inse.server.handler;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import com.inse.util.ChannelInterfaceUtil;
import com.sun.net.httpserver.HttpServer;

public class CallbackHTTPServer {
	private static Logger logger = Logger.getLogger(CallbackHTTPServer.class);
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
			logger.info("HTTP Server is listening on port " + port);

		} catch (Exception e) {
			logger.error("HTTP Server 启动失败:", e);
			e.printStackTrace();
			System.exit(0);
		}
	}
}
