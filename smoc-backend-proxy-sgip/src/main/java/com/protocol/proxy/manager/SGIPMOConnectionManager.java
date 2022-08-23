/**
 * @desc
 * 
 */
package com.protocol.proxy.manager;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.base.common.log.CategoryLog;
import com.base.common.manager.ResourceManager;
import com.base.common.worker.SuperMapWorker;
import com.huawei.smproxy.SGIPMOProxy;

public class SGIPMOConnectionManager extends SuperMapWorker<String, SGIPMOProxy>{
	
	private int port = ResourceManager.getInstance().getIntValue("mo.port");
	private ServerSocket serversocket;
	
	private static SGIPMOConnectionManager manager = new SGIPMOConnectionManager();
	
	private SGIPMOConnectionManager(){
		beginListen();
		this.start();
	}
	
	public static SGIPMOConnectionManager getInstance(){
		return manager;
	}

    private void beginListen() {
	 	try {
			serversocket = new ServerSocket();
			serversocket.bind(new InetSocketAddress("0.0.0.0", port));
			System.out.println("启动上行监听端口"+port);
		} catch (Exception ex) {
			System.out.println("启动上行监听端口失败"+ex.getMessage());
		}
	}

	@Override
	protected void doRun() throws Exception {
        Socket socket = serversocket.accept();  
        CategoryLog.connectionLogger.info("接收上行链接{}",socket);
        String host = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        SGIPMOProxy proxy = new SGIPMOProxy();
        add(new StringBuilder().append(host).append(":").append(port).toString(), proxy);
        proxy.onConnect(socket);
        CategoryLog.connectionLogger.info("上行链接总数{}",superMap);
	}
	
	public SGIPMOProxy remove(String key){
		CategoryLog.connectionLogger.info("释放上行链接{}",key);
		SGIPMOProxy proxy =  super.remove(key);
		CategoryLog.connectionLogger.info("上行链接总数{}",superMap.keySet());
		return proxy;
	}
    
}


