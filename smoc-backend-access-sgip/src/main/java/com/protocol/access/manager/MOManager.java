/**
 * @desc
 * @author ma
 * @date 2017年10月10日
 * 
 */
package com.protocol.access.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.protocol.access.client.ClientManager;
import com.protocol.access.sgip.sms.ShortMessage;
import com.protocol.access.util.DAO;
import com.protocol.access.util.SequenceUtil;
import com.protocol.access.vo.AuthClient;
import com.protocol.access.vo.Report;


import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.util.LongSMSEncode6;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperConcurrentMapWorker;
import com.base.common.worker.SuperQueueWorker;

public class MOManager {
	
	final static String DEFAULT_MO_ENCODING = "UTF-16BE";
	
	MOStoreWorker moStoreWorker;

	private static MOManager manager = new MOManager();
	
	MOPushWorker moPushWorker;
	
	MOLoadWorker moLoadWorker;

	public static MOManager getInstance() {
		return manager;
	}

	private MOManager() {
		moPushWorker = new MOPushWorker();
		moPushWorker.setName("mo-push-worker");
		moPushWorker.start();
		
		moStoreWorker = new MOStoreWorker();
		moStoreWorker.setName("mo-store");
		moStoreWorker.start();
		
		moLoadWorker = new MOLoadWorker();
		moLoadWorker.setName("mo-load-worker");
		moLoadWorker.start();
	}
	
	public void process(Report vo) {
		moPushWorker.add(vo);
	}
	
	class MOLoadWorker extends SuperCacheWorker {
		
		@Override
		protected void doRun() throws Exception {
			try {
				Map<String, AuthClient> map = AuthCheckerManager.getInstance().getMap();
				for(Map.Entry<String, AuthClient> entry : map.entrySet()){
					String accountID = entry.getKey();
					String lockName = CacheNameGeneratorUtil.generateMORedisLockCacheName(accountID);
					boolean result = CacheBaseService.lock(lockName, LogPathConstant.LOCALHOST_IP ,BusinessDataManager.getInstance().getRedisLockExpirationTime());
					//当获取资源锁失败时，直接获取下一个用户的离线状态报告
					if(!result){
						logger.info("load离线状态报告数据资源锁{}获取失败",lockName);
						continue;
					}
					List<Report> queue = DAO.loadRouteMessageMOInfoList(accountID);
					
					boolean result2 = CacheBaseService.unlock(lockName,LogPathConstant.LOCALHOST_IP);
					if(!result2){
						logger.info("load离线状态报告数据资源锁{}释放失败",lockName);
					}
					
					if(queue!= null && queue.size() > 0 ){
						logger.info("{}检索离线状态消息{}条",accountID,queue.size());
						moPushWorker.addAll(queue);
					}else{
						logger.info("{}无离线状态消息",accountID);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			
			try {
				Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME * 15);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

	class MOPushWorker extends SuperQueueWorker<Report> {

		
		public void addAll(List<Report> reports) {
			superQueue.addAll(reports);
		}

		@Override
		protected void doRun() throws Exception {
			Report report = this.take();
			boolean result = false;
			com.protocol.access.sgip.pdu.Deliver delive;
			if(report.getMOMessageContent().length() > 70){
				byte[][] datas = LongSMSEncode6.enCodeBytes(report.getMOMessageContent(),DEFAULT_MO_ENCODING);
				for (int i = 0; i < datas.length; i++) {
					delive = new com.protocol.access.sgip.pdu.Deliver();									

					delive.setSequenceId(SequenceUtil.getSequence());
					delive.setUserNumber(new StringBuffer("86").append(report.getPhoneNumber()).toString());
					delive.setSpNumber(report.getAccountSrcId());
					delive.setTpPid((byte) 0);
					delive.setTpUdhi((byte) 1);
					delive.setMessageCoding((byte) 8);
					delive.assignSequenceNumber();

					ShortMessage sm = new ShortMessage();
					sm.setMessage(datas[i], (byte) 8);
					delive.setSm(sm);
					
					byte[] body = new byte[datas[i].length - 6];
					System.arraycopy(datas[i], 6, body, 0, body.length);
					
					delive.setReserve("");
					
					ClientManager.getInstance().pushRequest(delive,report.getAccountId());
					CategoryLog.messageLogger.info("SMPP_MO{}{}",FixedConstant.LOG_SEPARATOR,delive.toString());
				}
			}else{
				delive = new com.protocol.access.sgip.pdu.Deliver();
				delive.setSequenceId(SequenceUtil.getSequence());

				delive.setUserNumber(new StringBuffer("86").append(report.getPhoneNumber()).toString());
				delive.setSpNumber(report.getAccountSrcId());
				delive.setTpPid((byte) 0);
				delive.setTpUdhi((byte) 0);
				delive.setMessageCoding((byte) 8);
				delive.assignSequenceNumber();

				ShortMessage sm = new ShortMessage();
				String msg = report.getMOMessageContent();
				byte[] bytes = null;
				try {
					bytes = msg.getBytes("UTF-16BE");
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				sm.setMessage(bytes, (byte) 8);
				delive.setSm(sm);
				delive.setReserve("");
				
				ClientManager.getInstance().pushRequest(delive,report.getAccountId());
				CategoryLog.messageLogger.info("SMPP_MO{}{}",FixedConstant.LOG_SEPARATOR,delive.toString());
			}
			if(!result){
				moStoreWorker.put(report.getBusinessMessageID(), report);
			}
			Thread.sleep(BusinessDataManager.getInstance().getMessageSaveIntervalTime());
		}
	}

	class MOStoreWorker extends SuperConcurrentMapWorker<String, Report> {
		
		public void put(String businessMessageID, Report report) {
			add(businessMessageID, report);
		}

		@Override
		public void doRun() throws Exception {
			if(superMap.size() > 0) {
				long start = System.currentTimeMillis();
				//临时数据
				List<Report> reportList = new ArrayList<Report>(superMap.values());
				for(Report report : reportList) {
					remove(report.getBusinessMessageID());
				}
				DAO.saveRouteMessageMoInfoList(reportList);
				long interval = System.currentTimeMillis() - start;
				logger.info("本次保存数据条数{},耗时{}毫秒",reportList.size(),interval);
			}else {
				//无数据时，线程暂停时间
				long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
				Thread.sleep(interval);
			}
		}
	}
}
