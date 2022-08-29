/**
 * @desc 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.inse.worker;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;


public class ReportWorker extends SuperQueueWorker<BusinessRouteValue> {
    private static ReportWorker manager = new ReportWorker();


    public static ReportWorker getInstance() {
        return manager;
    }

    @Override
    protected void doRun() throws Exception {
        //取出状态报告
        BusinessRouteValue businessRouteValue=poll();
        if (businessRouteValue == null) {
            logger.info("状态报告为空");
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
                FixedConstant.SPLICER, businessRouteValue.getAccountID(),
                FixedConstant.SPLICER, businessRouteValue.getChannelReportTime());


    }

    public void exit() {
        //停止线程
        super.exit();
    }
}


