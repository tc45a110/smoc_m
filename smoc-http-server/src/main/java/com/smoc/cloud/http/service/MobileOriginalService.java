package com.smoc.cloud.http.service;


import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.MobileOriginalRequestParams;
import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MobileOriginalService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SystemHttpApiRequestService systemHttpApiRequestService;

    /**
     * 根据业务账号查询上行短信 做多返回1000条
     *
     * @param params
     * @return
     */
    public ResponseData<List<MobileOriginalResponseParams>> getMobileOriginalByAccount(MobileOriginalRequestParams params) {

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "getMobileOriginalByAccount", new Gson().toJson(params));

        List<MobileOriginalResponseParams> mobileOriginalResponseParams = messageRepository.getMobileOriginalByAccount(params);
        deleteMobileOriginal(mobileOriginalResponseParams);
        return ResponseDataUtil.buildSuccess(mobileOriginalResponseParams);
    }

    /**
     * 异步，批量删除上行短信
     *
     * @param params
     */
    @Async
    @Transactional
    public void deleteMobileOriginal(List<MobileOriginalResponseParams> params) {
        messageRepository.batchDelete(params);
    }
}
