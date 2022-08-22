package com.protocol.access.smgp;



import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.smgp.pdu.ActiveTest;



public class ActiveThread extends Thread {
	private IoSession session = null;
	private static final Logger logger = LoggerFactory
			.getLogger(ActiveThread.class);
	private long heartbeatInterval = 60000;
	private long heartbeatRetry = 3;
	private long reconnectInterval = 10000;
	public static long lastActiveTime = 0;
	private long lastCheckTime = 0;
	private String client;

	public ActiveThread(IoSession s,String client) {
		super("active-thread-"+client+"-"+s.getId());
		setDaemon(true);
		this.session = s;
		lastCheckTime = System.currentTimeMillis();
		lastActiveTime = System.currentTimeMillis();
		this.client = client;
	}

	public void run() {
		try {
			while (true) {
				if(session.isConnected()){
					long currentTime = System.currentTimeMillis();
					if ((currentTime - lastCheckTime) > heartbeatInterval) {
						if ((currentTime - lastActiveTime) < (heartbeatInterval * heartbeatRetry)) {
							logger.info("send ActiveTest to {}",client);
							lastCheckTime = currentTime;
							ActiveTest activeTest = new ActiveTest();
							activeTest.assignSequenceNumber();
							activeTest.timeStamp = currentTime;
							session.write(activeTest);
						} else {
							logger.info("{} connection lost!session={}",client,session);
							session.closeNow();
							break;
						}
					}
					try {
						Thread.sleep(reconnectInterval);
					} catch (Exception e) {
						logger.info(e.getMessage(),e);
					}
				}else{
					logger.warn("session={}失效，退出线程！",session);
					break;
				}
		
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public String toString() {
		return "ActiveThread [session=" + session + ", client=" + client + "]";
	}
	
	
}
