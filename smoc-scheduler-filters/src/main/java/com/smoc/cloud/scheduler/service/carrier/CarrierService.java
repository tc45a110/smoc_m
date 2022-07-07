package com.smoc.cloud.scheduler.service.carrier;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.service.filters.service.FiltersRedisDataService;
import com.smoc.cloud.scheduler.tools.utils.InsideStatusCodeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CarrierService {

    @Autowired
    private FiltersRedisDataService filtersRedisDataService;

    /**
     * 运营商-业务逻辑处理
     *
     * @param businessRouteValue
     * @return
     */
    public void carrierBusiness(BusinessRouteValue businessRouteValue, AccountBaseInfo messageRouteAccountParams) {

        Object segmentCarrier;

        //如果业务账号配置的运营商为空
        if (StringUtils.isEmpty(messageRouteAccountParams.getBusinessCarrier())) {
            businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOROUTE.name());
            businessRouteValue.setStatusMessage("业务账号运营商为空！");
            return;
        }

        //如果是国际运营商
        if ("INTL".equals(messageRouteAccountParams.getBusinessCarrier())) {
            segmentCarrier = "INTL";
            businessRouteValue.setBusinessCarrier(messageRouteAccountParams.getBusinessCarrier());
            businessRouteValue.setSegmentCarrier(segmentCarrier.toString());
            return;
        }

        /**
         * 根据手机号获取运营商
         */
        segmentCarrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 3));
        if (null == segmentCarrier) {
            segmentCarrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 4));
        }

        //判断号段运营商及业务账号配置的运营商
        if (StringUtils.isEmpty(segmentCarrier)) {
            businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOROUTE.name());
            businessRouteValue.setStatusMessage("号段运营商为空！");
            return;
        }


        //账号配置运营商与号段运营商是否匹配
        if (messageRouteAccountParams.getBusinessCarrier().contains(segmentCarrier.toString())) {
            businessRouteValue.setBusinessCarrier(messageRouteAccountParams.getBusinessCarrier());
            businessRouteValue.setSegmentCarrier(segmentCarrier.toString().split("-")[0]);
            return;
        }

        //是否可以携号转网
        if ("1".equals(messageRouteAccountParams.getTransferType())) {
            businessRouteValue.setBusinessCarrier(messageRouteAccountParams.getBusinessCarrier());
            businessRouteValue.setSegmentCarrier(segmentCarrier.toString().split("-")[0]);
            return;
        }



        businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.INVAREQ.name());
        businessRouteValue.setStatusMessage("运营商-业务逻辑处理，未知错误！");
    }
}
