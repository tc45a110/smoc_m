package com.smoc.cloud.scheduler.filters.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import com.smoc.cloud.scheduler.filters.service.service.FiltersRedisDataService;
import com.smoc.cloud.scheduler.tools.utils.FilterResponseCodeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageBusinessHandler {

    @Autowired
    private FiltersRedisDataService filtersRedisDataService;

    @Autowired
    private RouteMessageRepository routeMessageRepository;

    @Autowired
    private FullFilterParamsFilterService fullFilterParamsFilterService;

    /**
     * 短信业务处理
     *
     * @param list
     */
    @Async("threadPoolTaskExecutor")
    public void handleMessageBusiness(List<? extends BusinessRouteValue> list) {
        if (null == list || list.size() < 1) {
            return;
        }

        /**
         * 过滤操作
         */
        List<BusinessRouteValue> successList = new ArrayList<>();
        List<BusinessRouteValue> auditList = new ArrayList<>();
        List<BusinessRouteValue> errorList = new ArrayList<>();
        Integer count = list.size();
        for (BusinessRouteValue businessRouteValue : list) {

            /**
             * 号段运营商，根据手机号辨别运营商
             */
            Object segmentCarrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 3));
            if (null == segmentCarrier) {
                segmentCarrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 4));
            }
            if (null != segmentCarrier) {
                businessRouteValue.setSegmentCarrier(segmentCarrier.toString().split("-")[0]);
            }

            /**
             * 完善省份信息，根据号段辨别省份
             */
            Object province = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_PROVINCE_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 7));
            if (null != province) {
                businessRouteValue.setAreaCode(province.toString().split("-")[0]);
                businessRouteValue.setAreaName(province.toString().split("-")[1]);
            }

            /**
             * 过滤操作
             */
            Map<String, String> filterResult = fullFilterParamsFilterService.filter(businessRouteValue.getPhoneNumber(), businessRouteValue.getAccountId(), businessRouteValue.getMessageContent(), businessRouteValue.getAccountTemplateId(), businessRouteValue.getBusinessCarrier(), businessRouteValue.getAreaCode(), businessRouteValue.getChannelId());
            if ("false".equals(filterResult.get("result"))) {//false 标识通过了过滤
                successList.add(businessRouteValue);
            } else if ("1208".equals(filterResult.get("code")) || "1211".equals(filterResult.get("code"))) { //1208、1211表示包含审核词
                businessRouteValue.setFilterCode(filterResult.get("code"));
                businessRouteValue.setFilterMessage(filterResult.get("message"));
                auditList.add(businessRouteValue);
            } else {//没有通过过滤
                businessRouteValue.setFilterCode(filterResult.get("code"));
                businessRouteValue.setFilterMessage(filterResult.get("message"));
                businessRouteValue.setStatusCode(FilterResponseCodeConstant.mapping(filterResult.get("code")));
                errorList.add(businessRouteValue);
            }
        }
        log.info("[完成过滤操作]条数：{}:{}", count, System.currentTimeMillis());
        Boolean limiter = filtersRedisDataService.limiterMessageFilter("filter:speed:test", 8000, 8000, 1, count);
        if (!limiter) {
            log.info("[------------触发限流-----------------------]");
        }
        /**
         * 根据过滤结果，进行分业务处理
         */
        //成功通过过滤
        if (successList.size() > 0) {
            //log.info("[通过过滤]条数：{}", successList.size());
        }

        //要经过审核
        if (auditList.size() > 0) {
            //log.info("[审核过滤]条数：{}", auditList.size());
            routeMessageRepository.generateMessageAudit(auditList);
        }

        //没有通过过滤,直接生成报告
        if (errorList.size() > 0) {
            //log.info("[没通过过滤]条数：{}", errorList.size());
            routeMessageRepository.generateErrorMessageResponse(errorList);
        }

    }
}
