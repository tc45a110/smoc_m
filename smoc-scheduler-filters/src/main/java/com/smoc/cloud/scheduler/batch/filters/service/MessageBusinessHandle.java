package com.smoc.cloud.scheduler.batch.filters.service;

import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageBusinessHandle {

    @Autowired
    private RouteMessageRepository routeMessageRepository;

    @Autowired
    private FullFilterParamsFilterService fullFilterParamsFilterService;

    /**
     * 短信业务处理
     *
     * @param list
     */
    @Async
    public void handleMessageBusiness(List<? extends BusinessRouteValue> list) {
        if (null == list || list.size() < 1) {
            return;
        }

        /**
         *  根据手机号、业务账号、进一步初始化数据
         */

        /**
         * 过滤操作
         */
        List<BusinessRouteValue> successList = new ArrayList<>();
        List<BusinessRouteValue> auditList = new ArrayList<>();
        List<BusinessRouteValue> errorList = new ArrayList<>();
        log.info("[batch number]条数：{}:{}",list.size(),System.currentTimeMillis());
        for (BusinessRouteValue businessRouteValue : list) {
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
        log.info("[batch number]条数：{}:{}",list.size(),System.currentTimeMillis());

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
