package com.protocol.access.manager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.vo.MessageInfo;


public class LongSubmitManagerFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(LongSubmitManagerFactory.class);
	
	private Map<String,LongSubmitManager> longSubmitManagerMap = new HashMap<String,LongSubmitManager>();
	private static LongSubmitManagerFactory manager = new LongSubmitManagerFactory();
	
	public static LongSubmitManagerFactory getInstance(){
		return manager;
	}
	
	private LongSubmitManagerFactory(){

	}

	//添加一次，执行一次match,match到的要删除
	public void persistence(MessageInfo messageInfo){
		LongSubmitManager longSubmitManager = register(messageInfo.getAccountId());
		longSubmitManager.process(messageInfo);
	}
	
	private LongSubmitManager register(String accountId){
		LongSubmitManager longSubmitManager = longSubmitManagerMap.get(accountId);
		if(longSubmitManager== null){
			longSubmitManager = new LongSubmitManager(accountId);
			longSubmitManagerMap.put(accountId, longSubmitManager);
			logger.info("{}注册长短信处理类",accountId);
		}
		return longSubmitManager;
	}
}