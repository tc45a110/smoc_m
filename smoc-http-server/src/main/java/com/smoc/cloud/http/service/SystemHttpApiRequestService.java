package com.smoc.cloud.http.service;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.http.entity.SystemHttpApiRequest;
import com.smoc.cloud.http.repository.SystemHttpApiRequestRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 保存请求记录
 */
@Service
public class SystemHttpApiRequestService {

    @Resource
    private SystemHttpApiRequestRepository systemHttpApiRequestRepository;

    @Async
    @Transactional
    public void save(String orderNo,String account,String orderType,String request){

        SystemHttpApiRequest apiRequest = new SystemHttpApiRequest();
        apiRequest.setId(UUID.uuid32());
        apiRequest.setAccount(account);
        apiRequest.setOrderNo(orderNo);
        apiRequest.setOrderType(orderType);
        apiRequest.setRequestData(request);
        apiRequest.setCreatedTime(DateTimeUtils.getNowDateTime());
        systemHttpApiRequestRepository.saveAndFlush(apiRequest);

    }
}
