package com.smoc.cloud.scheduler.filters.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import com.smoc.cloud.scheduler.filters.service.service.FiltersRedisDataService;
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
public class MessageBusinessHandle {

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
        //log.info("[batch number start]条数：{}:{}", list.size(), System.currentTimeMillis());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("FILTER");
        for (BusinessRouteValue businessRouteValue : list) {

            /**
             * 完善businessRouteValue 的运营商、省份数据
             */
            //运营商
            Object carrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 3));
            if (null == carrier) {
                carrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 4));
            }
            //log.info("carrier:{}",carrier);
            if (null != carrier) {
                businessRouteValue.setBusinessCarrier(carrier.toString().split("-")[0]);
            }
            //省份
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
                errorList.add(businessRouteValue);
            }
        }
        stopWatch.stop();
        log.info("[stopWatch]:{}",stopWatch.prettyPrint());

        /**
         * 根据过滤结果，进行分业务处理
         */
        //成功通过过滤
        if (successList.size() > 0) {
        }

        //要经过审核
        if (auditList.size() > 0) {
            routeMessageRepository.generateMessageAudit(auditList);
        }

        //没有通过过滤,直接生成报告
        if (errorList.size() > 0) {
            routeMessageRepository.generateMessageResponse(errorList);
        }

    }
}
