/**
 * @desc
 * 
 */
package com.business.access.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.dao.AccountFinanceDAO;

public class FinanceWorkerManager{
	
	private static final Logger logger = LoggerFactory.getLogger(FinanceWorkerManager.class);
	
	private static FinanceWorkerManager manager = new FinanceWorkerManager();
	//处理下发时扣费
	SubmitDeductWorker submitReductWorker;
	//处理回执成功是扣费
	ReportReductWorker reportReductWorker;
	
	private FinanceWorkerManager(){
		submitReductWorker = new SubmitDeductWorker();
		reportReductWorker = new ReportReductWorker();
		submitReductWorker.start();
		reportReductWorker.start();
	}
	
	public static FinanceWorkerManager getInstance(){
		return manager;
	}
	
	public void process(BusinessRouteValue businessRouteValue){
		//下发数据，处理下发扣费:
		if(FixedConstant.RouteLable.MT.name().equals(businessRouteValue.getRouteLabel()) 
				&& 
				String.valueOf(FixedConstant.AccountConsumeType.SUBMIT.ordinal()).equals(businessRouteValue.getConsumeType())
				){
			logger.info(
					new StringBuilder().append("计费信息")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent()
					);
			submitReductWorker.add(businessRouteValue);
		}
		//回执数据，处理回执扣费：只有回执成功才进行扣费
		else if(
				FixedConstant.RouteLable.MR.name().equals(businessRouteValue.getRouteLabel()) 
				&& 
				String.valueOf(FixedConstant.AccountConsumeType.REPORT.ordinal()).equals(businessRouteValue.getConsumeType())
				&& 
				InsideStatusCodeConstant.SUCCESS_CODE.equals(businessRouteValue.getSuccessCode())
				){
			logger.info(
					new StringBuilder().append("计费信息")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent()
					);
			reportReductWorker.add(businessRouteValue);
		}
		
	}
	
	public void process(List<BusinessRouteValue> businessRouteValueList){
		for(BusinessRouteValue businessRouteValue : businessRouteValueList){
			process(businessRouteValue);
		}
	}
	
	/**
	 * 按照回执成功扣费，不产生冻结
	 * @param businessRouteValueList
	 */
	private void doFinance(List<BusinessRouteValue> businessRouteValueList,String consumeType){
		//账号消费流水
		Map<String,Map<String,Object>> accountConsumeFlowMap = new HashMap<String, Map<String,Object>>();
		//财务账号扣费金额
		Map<String,Double> accountFinanceMap = new HashMap<String,Double>();
		
		for(BusinessRouteValue businessRouteValue:businessRouteValueList){
			//以计费账号的维度统计数据
			String accountID = businessRouteValue.getAccountID();
			String financeAccountID = businessRouteValue.getFinanceAccountID();
			//获取下发时间，归属到天yyyyMMdd
			String sendTime = businessRouteValue.getAccountSubmitTime().replace("-", "").substring(0, 8);
			
			//本条消息计费条数
			String messageNumber = businessRouteValue.getMessageNumber();
			//本条消息计费金额
			String messageAmount = businessRouteValue.getMessageAmount();
			
			//计算财务账号扣费金额
			Double consumeSum = accountFinanceMap.get(financeAccountID);
			if(consumeSum == null){
				accountFinanceMap.put(financeAccountID, Double.parseDouble(messageAmount));
			}else{
				accountFinanceMap.put(financeAccountID, consumeSum + Double.parseDouble(messageAmount));
			}
			
			//计费账号的消费流水
			String key = new StringBuilder().append(accountID)
			.append(FixedConstant.SPLICER)
			.append(financeAccountID)
			.append(FixedConstant.SPLICER)
			.append(sendTime).toString();
			
			Map<String,Object> consumeFlowMap = accountConsumeFlowMap.get(key);
			if(consumeFlowMap == null){
				consumeFlowMap = new HashMap<String, Object>();
				consumeFlowMap.put("CONSUME_NUM", Long.parseLong(messageNumber));
				consumeFlowMap.put("CONSUME_SUM", Double.parseDouble(messageAmount));
				accountConsumeFlowMap.put(key, consumeFlowMap);
			}else{
				consumeFlowMap.put("CONSUME_NUM", (Long)consumeFlowMap.get("CONSUME_NUM") + Long.parseLong(messageNumber));
				consumeFlowMap.put("CONSUME_SUM", (Double)consumeFlowMap.get("CONSUME_SUM") +Double.parseDouble(messageAmount));
			}
			
			AccountFinanceDAO.deductBalance(accountConsumeFlowMap, accountFinanceMap, consumeType, DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY));
			
		}
		
	}
	
	/**
	 * 下发扣费处理线程
	 */
	class SubmitDeductWorker extends SuperQueueWorker<BusinessRouteValue>{

		public SubmitDeductWorker() {
			
		}

		@Override
		public void doRun() throws Exception {

			if(superQueue.size() > 0){
				long start = System.currentTimeMillis();
				List<BusinessRouteValue> businessRouteValueList;
				synchronized (lock) {
					businessRouteValueList = new ArrayList<BusinessRouteValue>(superQueue);
					superQueue.clear();
				}
				doFinance(businessRouteValueList,String.valueOf(FixedConstant.AccountConsumeType.SUBMIT.ordinal()));
				long interval = System.currentTimeMillis() - start;
				logger.info("计费数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			}else{
				long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
				Thread.sleep(interval);
			}
			
		}
		
	}
	
	/**
	 * 回执成功扣费处理线程
	 */
	class ReportReductWorker extends SuperQueueWorker<BusinessRouteValue>{

		public ReportReductWorker() {
			
		}

		@Override
		public void doRun() throws Exception {

			if(superQueue.size() > 0){
				long start = System.currentTimeMillis();
				List<BusinessRouteValue> businessRouteValueList;
				synchronized (lock) {
					businessRouteValueList = new ArrayList<BusinessRouteValue>(superQueue);
					superQueue.clear();
				}
				doFinance(businessRouteValueList,String.valueOf(FixedConstant.AccountConsumeType.REPORT.ordinal()));
				long interval = System.currentTimeMillis() - start;
				logger.info("计费数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			}else{
				long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
				Thread.sleep(interval);
			}
			
		}
		
		
	}
	
}


