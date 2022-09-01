package com.inse.manager;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;

import java.util.HashSet;
import java.util.Set;

public class ReportWorkerManager extends SuperQueueWorker<BusinessRouteValue> {
    private static ReportWorkerManager manager = new ReportWorkerManager();

    private Set<ReportWorkerManager.ReportWorker> reportWorkerSet = new HashSet<ReportWorkerManager.ReportWorker>();

    private ReportWorkerManager() {
        for(int i = 0; i< FixedConstant.CPU_NUMBER; i++){
            ReportWorkerManager.ReportWorker reportWorker = new ReportWorkerManager.ReportWorker();
            reportWorkerSet.add(reportWorker);
            reportWorker.setName(new StringBuilder("ReportWorker-").append(i+1).toString());
            reportWorker.start();
        }
        this.setName("ReportWorkerManager");
        this.start();
    }

    public static ReportWorkerManager getInstance() {
        return manager;
    }

    public void process(BusinessRouteValue businessRouteValue){
        add(businessRouteValue);
    }

    public void doRun() throws Exception {
        sleep(INTERVAL);
    }

    /**
     * 退出线程
     */
    public void exit() {
        super.exit();
        for (ReportWorkerManager.ReportWorker reportWorker : reportWorkerSet) {
            reportWorker.exit();
        }
    }

    class ReportWorker extends SuperCacheWorker {

        @Override
        protected void doRun() throws Exception {
            //取出状态报告
            BusinessRouteValue businessRouteValue = poll();
            if (businessRouteValue == null) {
                return;
            }

            if (DynamicConstant.REPORT_SUCCESS_CODE.equals(businessRouteValue.getStatusCode())) {
                businessRouteValue.setSuccessCode(InsideStatusCodeConstant.SUCCESS_CODE);
            } else {
                businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
            }
            businessRouteValue.setChannelReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
            CacheBaseService.saveReportToMiddlewareCache(businessRouteValue);
            logger.info(new StringBuilder().append("状态报告信息")
                            .append("{}phoneNumber={}")
                            .append("{}channelMessageID={}")
                            .append("{}statusCode={}")
                            .append("{}channelID={}")
                            .append("{}channelReportTime={}")
                            .toString(),
                    FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
                    FixedConstant.SPLICER, businessRouteValue.getChannelMessageID(),
                    FixedConstant.SPLICER, businessRouteValue.getStatusCode(),
                    FixedConstant.SPLICER, businessRouteValue.getChannelID(),
                    FixedConstant.SPLICER, businessRouteValue.getChannelReportTime());

        }

    }

}
