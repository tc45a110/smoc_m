/**
 * @desc
 * 
 */
package com.protocol.access.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.util.DateUtil;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;
import com.protocol.access.util.DAO;
import com.protocol.access.util.DeliverUtil;
import com.protocol.access.vo.Report;

import com.protocol.access.vo.AuthClient;
import com.protocol.access.vo.MessageInfo;


/**
 * 回执信息分发线程管理：每个账号开启一个分发线程
 */
public class ReportManager {
	
	private static ReportManager manager = new ReportManager();
	
	ReportStoreWorker reportStoreWorker;
	
	ReportPushWorker reportPushWorker;
	
	ReportLoadOfflineWorker reportLoadOfflineWorker;
	
	private  ReportManager(){
		reportStoreWorker = new ReportStoreWorker();
		reportStoreWorker.setName("report-store-worker");
		reportStoreWorker.start();
		
		reportPushWorker = new ReportPushWorker();
		reportPushWorker.setName("report-push-worker");
		reportPushWorker.start();
		
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
	class ReportStoreWorker extends SuperQueueWorker<Report> {

		@Override
		protected void doRun() throws Exception {
			long interval = 0;
			if(superQueue.size() > 0){
				long startTime = System.currentTimeMillis();
				//临时数据
				List<Report> reporList;
				synchronized (lock) {
					reporList = new ArrayList<Report>(superQueue);
					superQueue.clear();
				}
				DAO.saveRouteMessageMrInfoList(reporList);
				interval = System.currentTimeMillis() - startTime;
				logger.info("保存状态报告数据条数{},耗时{}毫秒",reporList.size(),interval);
			}else{
				//当没有数据时，需要暂停一会
				interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
				Thread.sleep(interval);
			}
		}

	}
	
	/**
	 * 回执信息分发线程
	 */
	class ReportPushWorker extends SuperQueueWorker<Report>{
		
		@Override
		protected void doRun() throws Exception {
			Report vo = take();
			if(vo.getAccountId() != null){
				processReport(vo);
			}
		}
		
	}
	
	class ReportLoadOfflineWorker extends SuperCacheWorker {

		@Override
		protected void doRun() throws Exception {
			Map<String, AuthClient> map = AuthCheckerManager.getInstance().getMap();
			for(Map.Entry<String, AuthClient> entry : map.entrySet()){
				String accountID = entry.getKey();
				if(SessionManager.getInstance().getSessionValid(accountID) != null){
					String lockName = CacheNameGeneratorUtil.generateReportRedisLockCacheName(accountID);
					boolean result = CacheBaseService.lock(lockName, LogPathConstant.LOCALHOST_IP , BusinessDataManager.getInstance().getRedisLockExpirationTime());
					//当获取资源锁失败时，直接获取下一个用户的离线状态报告
					if(!result){
						logger.info("load离线状态报告数据资源锁{}获取失败",lockName);
						continue;
					}
					List<Report> reportList = DAO.loadRouteMessageMrInfoList(accountID);
					
					boolean result2 = CacheBaseService.unlock(lockName,LogPathConstant.LOCALHOST_IP);
					if(!result2){
						logger.info("load离线状态报告数据资源锁{}释放失败",lockName);
					}
					
					if(reportList!= null && reportList.size() > 0 ){
						logger.info("{}检索离线状态消息{}条",accountID,reportList.size());
						for(Report report:reportList){
							reportPushWorker.add(report);
						}
					
					}else{
						logger.info("{}无离线状态消息",accountID);
					}
				}
			}
			Thread.sleep(FixedConstant.COMMON_EFFECTIVE_TIME);
		}
	}
	
	/**
	 * 推送回执
	 * @param report
	 * @param msgid
	 */
	private void processReport(Report report) {
		IoSession session = SessionManager.getInstance().getSession(
				report.getAccountId());
		DeliverUtil.sendReport(session, report);
	}
	
	/**
	 * 推送失败 存入持久化线程中
	 * @param vo
	 */
	public void addPushFailReport(Report vo){
		reportStoreWorker.add(vo);
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
			reportPushWorker.add(report);
		}
	}
}


