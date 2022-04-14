package com.smoc.cloud.http.message.template.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.SendMessageByTemplateRequestParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SendMessageService {

    public ResponseData<Map<String, String>> sendMessageByTemplate(SendMessageByTemplateRequestParams params){

        Map<String,String> result = new HashMap<>();
        result.put("orderNo",params.getOrderNo());
        return ResponseDataUtil.buildSuccess("0000","订单提交成功",result);
    }
}
