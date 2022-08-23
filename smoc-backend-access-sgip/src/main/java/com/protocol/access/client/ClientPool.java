/**
 * @desc
 * 
 */
package com.protocol.access.client;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.manager.ResourceManager;
import com.protocol.access.sgip.pdu.Request;


public class ClientPool{
	private static final Logger logger = LoggerFactory
			.getLogger(ClientPool.class);
	
	private String username;
	private Set<Client> set = new HashSet<Client>();
	private JobList jobList;
	
	public ClientPool(String username){
		this.username = username;
		jobList = new JobList(10000);
		Worker worker = new Worker();
		worker.setName("client-push-pool-"+username);
		worker.start();
	}
	
	public void getClient(){
		
	}
	
	public void createClient(){
		set.add(new Client(username, jobList, this));
	}
	
	public void process(Request submit){
		boolean result = false;
		logger.info("添加数据,队列={},data={}",username,Hex.encodeHexString(submit.getData().getBuffer()));
		while(!result){
			result = jobList.addJob(submit);
			if(!result){
				logger.warn("{}缓存状态报告队列满",username);
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					
				}
			}
		}
	}

	public synchronized void putClient(Client client){
		logger.info("{}添加连接{}",username,client);
		set.add(client);
	}
	
	public synchronized void freeClient(Client client){
		logger.info("{}移除连接{}",username,client);
		set.remove(client);
	}
	
	//计算连接数和joblist的size，生成最优连接数
	class Worker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					//获取可以建立的最大连接数
					int maxSize = ResourceManager.getInstance().getIntValue("sgip.client.max.conn");
					int size = set.size();
					if(maxSize > size){
						//当前待处理量
						int jobCount = jobList.getSize();
						//建立连接的单位数量
						int unitCount = ResourceManager.getInstance().getIntValue("sgip.client.conn.unit");
						int needSize = (int)(Math.ceil(Float.valueOf(jobCount)/unitCount));
						if(needSize - size > 0){
							createClient();
						}
					}else{
						logger.info("{}超过最大连接{}",username,maxSize);
					}
					Thread.sleep(ResourceManager.getInstance().getIntValue("sgip.client.conn.check.interval"));
				} catch (InterruptedException e) {
					logger.error(e.getMessage(),e);
				}
			}

		}

	}
}


