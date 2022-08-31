 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.access.worker;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.log.AccessBusinessLogManager;
import com.base.common.manager.AccountStatusCodeConversionManager;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.vo.ProtocolRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.access.manager.FinanceWorkerManager;

public class ReportPullWorker extends SuperCacheWorker{
	
	private ReportStoreWorker reportStoreWorker;

	public ReportPullWorker(ReportStoreWorker reportStoreWorker) {
		super();
		this.reportStoreWorker = reportStoreWorker;
	}

	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValue = CacheBaseService.getBusinessReportFromMiddlewareCache();
		if(businessRouteValue != null){
			logger.info(
					new StringBuilder().append("拉取回执").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}businessMessageID={}").append("{}statusCode={}").append("{}statusCodeSource={}")
							.toString(),
					FixedConstant.SPLICER, businessRouteValue.getAccountID(), 
					FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER, businessRouteValue.getBusinessMessageID(),
					FixedConstant.SPLICER, businessRouteValue.getStatusCode(),
					FixedConstant.SPLICER, businessRouteValue.getStatusCodeSource());
			
			String statusCode = AccountStatusCodeConversionManager.getInstance().getAccountStatusCodeConversion(businessRouteValue.getAccountID(), businessRouteValue.getStatusCode());
			//当账号存在状态码转换时，需设置转换后的值
			if(StringUtils.isNotEmpty(statusCode)){
				businessRouteValue.setStatusCode(statusCode);
			}
			FinanceWorkerManager.getInstance().process(businessRouteValue);
			doLog(businessRouteValue);
			if(BusinessDataManager.getInstance().getReportStoreToRedisProtocol().contains(businessRouteValue.getProtocol())){
				int size = CacheBaseService.getAccountReportQueueSizeFromMiddlewareCache(businessRouteValue.getAccountID());
				if( size > BusinessDataManager.getInstance().getAccountReportQueueThreshold(businessRouteValue.getAccountID())){
					logger.info(
							new StringBuilder().append("状态报告队列超出阈值存数据库").append("{}accountID={}").append("{}phoneNumber={}")
									.append("{}businessMessageID={}").append("{}statusCode={}").append("{}statusCodeSource={}")
									.append("{}size={}")
									.toString(),
							FixedConstant.SPLICER, businessRouteValue.getAccountID(), 
							FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
							FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID(),
							FixedConstant.SPLICER, businessRouteValue.getStatusCode(),
							FixedConstant.SPLICER, businessRouteValue.getStatusCodeSource(),
							FixedConstant.SPLICER, size
							);
					String key = new StringBuilder(businessRouteValue.getAccountMessageIDs())
							.append(",").append(businessRouteValue.getBusinessMessageID())
							.append(",").append(businessRouteValue.getMessageIndex()).toString();
					reportStoreWorker.put(key, businessRouteValue);
					AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.AccountReportQueue, 
							new StringBuilder().append(businessRouteValue.getAccountID()).append(",").append(size).toString()));
				}else{
					CacheBaseService.saveReportToMiddlewareCache(businessRouteValue.getAccountID(), new ProtocolRouteValue(businessRouteValue));
				}
				
			}else if(BusinessDataManager.getInstance().getReportStoreToDatabaseProtocol().contains(businessRouteValue.getProtocol())){
				String key = new StringBuilder(businessRouteValue.getAccountMessageIDs())
						.append(",").append(businessRouteValue.getBusinessMessageID())
						.append(",").append(businessRouteValue.getMessageIndex()).toString();
				reportStoreWorker.put(key, businessRouteValue);
			}
			logger.info(
					new StringBuilder().append("保存回执").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}businessMessageID={}").append("{}statusCode={}").append("{}statusCodeSource={}")
							.toString(),
					FixedConstant.SPLICER, businessRouteValue.getAccountID(), 
					FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID(),
					FixedConstant.SPLICER, businessRouteValue.getStatusCode(),
					FixedConstant.SPLICER, businessRouteValue.getStatusCodeSource());
		}else{
			Thread.sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
		}
	}
	
	private void doLog(BusinessRouteValue businessRouteValue){
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
		String nowTime = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI);
		StringBuilder sb = new StringBuilder()
				.append(nowTime)
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getEnterpriseFlag())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getProtocol())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountSubmitTime())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getBusinessMessageID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getPhoneNumber())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getSegmentCarrier())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getBusinessCarrier())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAreaCode())//10-区域编码
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAreaName())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getCityName())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountSubmitSRCID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountExtendCode())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountBusinessCode())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getMessageFormat())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(MessageContentUtil.handlingLineBreakCommas(businessRouteValue.getMessageContent()))
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getMessageSignature())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getMessageNumber())//19-计费条数
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getMessageIndex())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getInfoType())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getIndustryTypes())//22-行业分类 
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountPriority())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountMessageIDs())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getSuccessCode())//25 计费标识
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getStatusCode())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getSubStatusCode())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getStatusCodeSource())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(DateUtil.getIntervalTime(businessRouteValue.getAccountSubmitTime(), nowTime))//29-间隔时间
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getChannelID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getChannelSRCID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getFinanceAccountID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getMessagePrice())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getMessageAmount())//34-计费总额 
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getBusinessType())//35
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getAccountTemplateID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getRepeatSendTimes())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getConsumeType())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getOptionParam())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getPriceAreaCode())//40
				.append(System.getProperty("line.separator"));
		AccessBusinessLogManager.getInstance().process(sb.toString(),businessRouteValue.getEnterpriseFlag(),businessRouteValue.getRouteLabel());
	}
}


