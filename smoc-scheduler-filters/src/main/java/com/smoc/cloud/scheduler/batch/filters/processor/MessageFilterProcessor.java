package com.smoc.cloud.scheduler.batch.filters.processor;

import com.google.gson.Gson;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.service.FullFilterParamsFilterService;
import com.smoc.cloud.scheduler.service.filters.model.RequestFullParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component
public class MessageFilterProcessor implements ItemProcessor<BusinessRouteValue, BusinessRouteValue> {

    @Autowired
    private FullFilterParamsFilterService fullFilterParamsFilterService;

    @Override
    public BusinessRouteValue process(BusinessRouteValue businessRouteValue) throws Exception {
        RequestFullParams requestFullParams = new RequestFullParams();
        requestFullParams.setAccount(businessRouteValue.getAccountId());
        requestFullParams.setPhone(businessRouteValue.getPhoneNumber());
        requestFullParams.setMessage(businessRouteValue.getMessageContent());
        requestFullParams.setTemplateId(businessRouteValue.getAccountTemplateId());
        //requestFullParams.setCarrier(businessRouteValue.getBusinessCarrier());
        Map<String,String> result = fullFilterParamsFilterService.filter(requestFullParams);
        log.info("[filter result]:{}",new Gson().toJson(result));
        return businessRouteValue;
    }
}
