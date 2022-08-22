/**
 * @desc
 * 
 */
package com.protocol.access.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.manager.ResourceManager;

import com.protocol.access.smpp.ActiveThread;

public class ActiveManager {
	private static final Logger logger = LoggerFactory
			.getLogger(ActiveManager.class);
	private Set<String> clients =new HashSet<String>();
	
	private static ActiveManager manager = new ActiveManager();
	
	public static ActiveManager getInstance(){
		return manager;
	}
	
	private Map<IoSession,ActiveThread> activeThreadMap = new ConcurrentHashMap<IoSession,ActiveThread>();
	
	private ActiveManager(){
		String activeClients = ResourceManager.getInstance().getValue("server.send.active.client");
		logger.info("需要服务端发送的心跳的client={}",activeClients);
		if(activeClients != null && activeClients.length() > 0){
			for (String activeClient : activeClients.trim().split(",")) {
				clients.add(activeClient);
			}
		}
	}
	
	public boolean exist(String client){
		if(client == null){
			return false;
		}
		return clients.contains(client);
	}
	
	public void put(IoSession session,ActiveThread thread){
		activeThreadMap.put(session, thread);
		thread.start();
	}
	
	public void remove(IoSession session){
		ActiveThread thread = activeThreadMap.remove(session);
		if(thread != null){
			logger.info("移除心跳检测线程{}",thread);
		}
		
	}
}


