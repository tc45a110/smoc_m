 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.access.worker;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.log.AccessBusinessLogManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
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
			
			String statusCode = BusinessDataManager.getInstance().getAccountStatusCodeConversion(businessRouteValue.getAccountID(), businessRouteValue.getStatusCode());
			//当账号存在状态码转换时，需设置转换后的值
			if(StringUtils.isNotEmpty(statusCode)){
				businessRouteValue.setStatusCode(statusCode);
			}
			FinanceWorkerManager.getInstance().process(businessRouteValue);
			doLog(businessRouteValue);
			if(BusinessDataManager.getInstance().getReportStoreToRedisProtocol().contains(businessRouteValue.getProtocol())){
				CacheBaseService.saveReportToMiddlewareCache(businessRouteValue.getAccountID(), businessRouteValue);
				logger.info("保存回执{}{}",FixedConstant.SPLICER,businessRouteValue.toString());
			}
			if(BusinessDataManager.getInstance().getReportStoreToDatabaseProtocol().contains(businessRouteValue.getProtocol())){
				reportStoreWorker.add(businessRouteValue);
			}
			businessRouteValue = null;
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
				.append(DateUtil.getIntervalTime(businessRouteValue.getChannelReportTime(), nowTime))//29-间隔时间
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


