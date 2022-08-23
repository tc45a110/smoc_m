 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;


import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.proxy.manager.ReportProcessWorkerManager;

/**
 * 只负责拉取数据，实际由ReportProcessWorker处理
 */
public class ReportPullWorker extends SuperCacheWorker{

	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValueReport = CacheBaseService.getReportFromMiddlewareCache();
		if(businessRouteValueReport != null){
			logger.info(
					new StringBuilder().append("拉取状态报告")
					.append("{}channelID={}")
					.append("{}phoneNumber={}")
					.append("{}channelMessageID={}")
					.append("{}channelReportSRCID={}")
					.append("{}statusCode={}")
					.append("{}subStatusCode={}")
					.append("{}successCode={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValueReport.getChannelID(),
					FixedConstant.SPLICER,businessRouteValueReport.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValueReport.getChannelMessageID(),
					FixedConstant.SPLICER,businessRouteValueReport.getChannelReportSRCID(),
					FixedConstant.SPLICER,businessRouteValueReport.getStatusCode(),
					FixedConstant.SPLICER,businessRouteValueReport.getSubStatusCode(),
					FixedConstant.SPLICER,businessRouteValueReport.getSuccessCode()
					);
			ReportProcessWorkerManager.getInstance().process(businessRouteValueReport);
		}else{
			Thread.sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
		}
	}
	
}


