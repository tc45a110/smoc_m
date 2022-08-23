/**
 * @desc
 * 
 */
package com.protocol.access.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.util.DateUtil;
import com.base.common.vo.ProtocolRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperConcurrentMapWorker;
import com.protocol.access.util.DAO;
import com.protocol.access.vo.AuthClient;
import com.protocol.access.vo.MessageInfo;
import com.protocol.access.vo.Report;


/**
 * 回执信息分发线程管理：每个账号开启一个分发线程
 */
public class ReportManager {
	
	private static ReportManager manager = new ReportManager();
	
	//存储线程集合
	Map<Integer,ReportStoreWorker> reportStoreWorkerMap = new HashMap<Integer, ReportManager.ReportStoreWorker>();
	//数据库状态报告加载线程
	ReportLoadOfflineWorker reportLoadOfflineWorker;
	
	
	private  ReportManager(){
		for(int i = 0;i<=FixedConstant.CPU_NUMBER * 2;i++){
			ReportStoreWorker reportStoreWorker = new ReportStoreWorker();
			reportStoreWorker.setName("report-store-worker-"+(i+1));
			reportStoreWorker.start();
			reportStoreWorkerMap.put(i, reportStoreWorker);
		}
		
		reportLoadOfflineWorker = new ReportLoadOfflineWorker();
		reportLoadOfflineWorker.setName("report-load-Offline-worker");
		reportLoadOfflineWorker.start();
	}
	
	public static ReportManager getIntance(){
		return manager;
	}
	
	
	/**
	 * 回执持久化线程：处理回执推送失败
	 */
	class ReportStoreWorker extends SuperConcurrentMapWorker<String,Report> {

		public void put(String messageID, Report report) {
			add(messageID,report);
		}
		
		@Override
		protected void doRun() throws Exception {
			if(superMap.size() > 0){
				long startTime = System.currentTimeMillis();
				//临时数据
				List<Report> reportList = new ArrayList<Report>(superMap.values());
				for(Report report : reportList) {
					remove(report.getMessageId());
				}
				
				DAO.saveRouteMessageMrInfoList(reportList);
				long interval = System.currentTimeMillis() - startTime;
				logger.info("保存状态报告数据条数{},耗时{}毫秒",reportList.size(),interval);
			}else{
				//当没有数据时，需要暂停一会
				long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
				Thread.sleep(interval);
			}
		}

	}
	
	class ReportLoadOfflineWorker extends SuperCacheWorker {

		@Override
		protected void doRun() throws Exception {
			
			Map<String, AuthClient> map = AuthCheckerManager.getInstance().getMap();
			for (Map.Entry<String, AuthClient> entry : map.entrySet()) {
				String accountID = entry.getKey();
				//存在有效队列
				if (SessionManager.getInstance().getSessionValid(accountID) != null) {
					//队列阀值
					int queueThresholdNum = BusinessDataManager.getInstance().getAccountReportQueueThreshold(accountID);
					//当前队列数量
					int reportQueueSize = CacheBaseService.getAccountReportQueueSizeFromMiddlewareCache(accountID);
					
					//控制redis队列中状态报告数量
					if (reportQueueSize < queueThresholdNum) {
						String lockName = CacheNameGeneratorUtil.generateReportRedisLockCacheName(accountID);
						boolean result = CacheBaseService.lock(lockName, LogPathConstant.LOCALHOST_IP,
								BusinessDataManager.getInstance().getRedisLockExpirationTime());
						// 当获取资源锁失败时，直接获取下一个用户的离线状态报告
						if (!result) {
							logger.info("检索离线状态报告资源锁{}获取失败", lockName);
							continue;
						}
						List<Report> reportList = DAO.loadRouteMessageMrInfoList(accountID);

						boolean result2 = CacheBaseService.unlock(lockName, LogPathConstant.LOCALHOST_IP);
						if (!result2) {
							logger.info("检索离线状态报告资源锁{}释放失败", lockName);
						}

						if (reportList != null && reportList.size() > 0) {
							logger.info("{}检索离线状态报告{}条,redisReportQueueSize={}", accountID, reportList.size(),reportQueueSize);
							for (Report report : reportList) {
								CacheBaseService.saveReportToMiddlewareCache(accountID, getProtocolRouteValue(report));
							}
						}
					}
				}
			}

			Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
		}
	}
	
	public ProtocolRouteValue getProtocolRouteValue(Report report) {
		ProtocolRouteValue protocolRouteValue = new ProtocolRouteValue();
		protocolRouteValue.setAccountID(report.getAccountId());
		protocolRouteValue.setPhoneNumber(report.getPhoneNumber());
		protocolRouteValue.setChannelReportTime(report.getReportTime());
		protocolRouteValue.setAccountSubmitTime(report.getSubmitTime());
		protocolRouteValue.setStatusCode(report.getStatusCode());

		protocolRouteValue.setAccountTemplateID(report.getTemplateId());
		protocolRouteValue.setAccountSubmitSRCID(report.getAccountSrcId());
		protocolRouteValue.setAccountBusinessCode(report.getAccountBusinessCode());
		protocolRouteValue.setMessageTotal(report.getMessageTotal());
		protocolRouteValue.setMessageIndex(report.getMessageIndex());

		protocolRouteValue.setOptionParam(report.getOptionParam());
		protocolRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
		protocolRouteValue.setAccountMessageIDs(report.getMessageId());
		protocolRouteValue.setAccountReportFlag(report.getAccountReportFlag());
		//protocolRouteValue.setSubStatusCode(report.getStatusCode());
		protocolRouteValue.setReportPushTimes(report.getReportPushTimes());
		return protocolRouteValue;
	}
	
	/**
	 * 推送失败 存入持久化线程中
	 * @param vo
	 */
	public void addPushFailReport(Report vo){
		int index = (int)(Math.random() * reportStoreWorkerMap.size());
		reportStoreWorkerMap.get(index).put(vo.getMessageId(), vo);
	}
	
	/**
	 * 发送失败模拟状态报告  存入推送线程中
	 * @param vo
	 * @param status
	 */
	public void addSendFailReport(MessageInfo vo,String status){
		int i = 1;
		Report report = new Report();
		report.setAccountId(vo.getAccountId());
		report.setPhoneNumber(vo.getPhoneNumber());
		report.setReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		report.setSubmitTime(vo.getSubmitTime());
		report.setStatusCode(status);
		
		report.setTemplateId(vo.getTemplateId());
		report.setAccountSrcId(vo.getAccountSrcId());
		report.setAccountBusinessCode(vo.getAccountBusinessCode());
		report.setMessageTotal(vo.getTotal());
		report.setOptionParam("");
		for(String msgId : vo.getMessageId().split(FixedConstant.SPLICER)) {
			report.setMessageIndex(i++);
			report.setMessageId(msgId);
			CacheBaseService.saveReportToMiddlewareCache(report.getAccountId(), getProtocolRouteValue(report));
			//reportPushQueue.add(report);
		}
	}
}


