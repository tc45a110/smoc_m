/**
 * @desc
 * 
 */
package com.protocol.access.manager;

import java.util.Map;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.vo.ProtocolRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperConcurrentMapWorker;
import com.protocol.access.util.DeliverUtil;
import com.protocol.access.vo.AuthClient;
import com.protocol.access.vo.Report;


/**
 * 回执信息分发线程管理：每个账号开启一个分发线程
 */
public class ReportPullManager extends SuperConcurrentMapWorker<String, ReportPullManager.Worker>{
	
	private static ReportPullManager manager = new ReportPullManager();
	
	private  ReportPullManager(){
		super.setName("report-pull-manager-worker");
		super.start();
	}
	
	public static ReportPullManager getIntance(){
		return manager;
	}
	
	
	class Worker extends SuperCacheWorker {
		private String accountID;

		private long timestamp;

		public Worker(String accountID) {
			this.accountID = accountID;
		}
		
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		
		public long getTimestamp() {
			return timestamp;
		}

		@Override
		protected void doRun() throws Exception {
			ProtocolRouteValue value = CacheBaseService.getReportFromMiddlewareCache(accountID);
			if (value != null) {
				if (FixedConstant.RouteLable.MR.toString().equals(value.getRouteLabel())) {
					if (value.getAccountReportFlag() == 1) {
						String accountMessageIDs = value.getAccountMessageIDs();
						Report report = new Report();
						report.setAccountId(value.getAccountID());
						report.setPhoneNumber(value.getPhoneNumber());
						report.setReportTime(value.getChannelReportTime());
						report.setSubmitTime(value.getAccountSubmitTime());
						report.setStatusCode(value.getStatusCode());
						report.setTemplateId(value.getAccountTemplateID());
						report.setAccountSrcId(value.getAccountSubmitSRCID());
						report.setAccountBusinessCode(value.getAccountBusinessCode());
						report.setMessageTotal(value.getMessageTotal());
						report.setMessageIndex(value.getMessageIndex());
						report.setOptionParam(value.getOptionParam());
						report.setReportPushTimes(value.getReportPushTimes());
						for (String msgId : accountMessageIDs.split(FixedConstant.SPLICER)) {
							report.setMessageId(msgId);
							DeliverUtil.sendReport(report);
						}
						setTimestamp(System.currentTimeMillis());
					} else {
						logger.info("{}发送{}不需要返回状态报告", value.getAccountID(), value.getPhoneNumber());
					}
				}
			} else {
				//判断线程是否超时
				if((System.currentTimeMillis() - getTimestamp()) > 1000 * 90) {
					logger.info("拉去状态报告线程超时,释放资源,accountID={}",accountID);
					//释放线程
					superMap.remove(accountID);
					this.exit();
				}
				sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
			}
		}
	}


	@Override
	protected void doRun() throws Exception {
		Map<String, AuthClient> map = AuthCheckerManager.getInstance().getMap();
		for (String accountID : map.keySet()) {
			//当前队列数量
			int reportQueueSize = CacheBaseService.getAccountReportQueueSizeFromMiddlewareCache(accountID);
			if(reportQueueSize > 0 && !superMap.containsKey(accountID)) {
				Worker worker = new Worker(accountID);
				worker.setName("report-pull-" + accountID);
				worker.start();
				superMap.put(accountID, worker);
				logger.info("启动拉去状态报告线程,accountID={}",accountID);
			}
		}
		Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
	}
}
