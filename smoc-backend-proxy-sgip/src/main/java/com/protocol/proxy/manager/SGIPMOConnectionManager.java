/**
 * @desc
 * 
 */
package com.protocol.proxy.manager;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.base.common.manager.ResourceManager;
import com.base.common.worker.SuperMapWorker;
import com.huawei.insa2.comm.sgip.SGIPConnection;
import com.huawei.smproxy.SGIPMOProxy;

public class SGIPMOConnectionManager extends SuperMapWorker<String, SGIPMOProxy>{
	
	private int port = ResourceManager.getInstance().getIntValue("mo.port");
	private ServerSocket serversocket;
	
	private static SGIPMOConnectionManager manager = new SGIPMOConnectionManager();
	
	private SGIPMOConnectionManager(){
		beginListen();
	}
	
	public static SGIPMOConnectionManager getInstance(){
		return manager;
	}

    private void beginListen() {
	 	try {
			serversocket = new ServerSocket();
			serversocket.bind(new InetSocketAddress("127.0.0.1", port));
			logger.info("启动上行监听端口{}",port);
		} catch (Exception ex) {
			logger.error("启动上行监听端口失败",ex);
		}
	}

	@Override
	protected void doRun() throws Exception {
        Socket socket = serversocket.accept();  
        logger.info("上行链接{}",socket);
        SGIPMOProxy SGIPMOProxy = new SGIPMOProxy();
        SGIPConnection conn = new 
        conn.addEventListener(new SGIPEventAdapter(this, conn));
        conn.attach(args, socket);
        serconns.put(new String(String.valueOf(peerIP) +":"+ String.valueOf(port)), conn);
	}
    
}


