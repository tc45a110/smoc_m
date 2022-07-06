 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;


import com.base.common.cache.CacheBaseService;
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
			ReportProcessWorkerManager.getInstance().process(businessRouteValueReport);
		}else{
			Thread.sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
		}
	}
	
}


