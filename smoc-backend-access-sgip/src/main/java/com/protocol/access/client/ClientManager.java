/**
 * @author ma
 * @date 2017�?10�?10�?
 * 
 */
package com.protocol.access.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.sgip.pdu.Request;

public class ClientManager {
	private static final Logger logger = LoggerFactory
			.getLogger(ClientManager.class);
	
	Map<String,ClientPool> clientPoolMap = new HashMap<String, ClientPool>();
	
	private static ClientManager manager = new ClientManager();
	
	public static ClientManager getInstance() {
		return  manager;
	}


	private ClientManager() {
		
	}
	
	public void pushRequest(Request submit,String username){
		ClientPool clientPool = getClientPool(username);
		clientPool.process(submit);
	}
	
	/**
	 * 获取连接池，保证只创建一�?
	 * @param username
	 * @return
	 */
	private  ClientPool getClientPool(String username){
		ClientPool clientPool;
		synchronized (this) {
			clientPool = clientPoolMap.get(username);
			if (clientPool == null) {
				clientPool = new ClientPool(username);
				clientPoolMap.put(username, clientPool);
				logger.info("创建{}客户端推送连接池",username);
			}
		}
		return clientPool;
	}
	
}
