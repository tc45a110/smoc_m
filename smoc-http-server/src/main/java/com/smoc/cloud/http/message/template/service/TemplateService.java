package com.smoc.cloud.http.message.template.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.TemplateAddRequestParams;
import com.smoc.cloud.common.http.server.message.request.TemplateStatusRequestParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class TemplateService {

    public ResponseData<Map<String, String>> saveTemplate(TemplateAddRequestParams params) {

        //可以处理前提逻辑
        //获取模板ID
        this.save(params);
        Map<String,String> result = new HashMap<>();
        result.put("orderNo",params.getOrderNo());
        result.put("templateId","TEMP100000369");
        return ResponseDataUtil.buildSuccess("0000","订单提交成功",result);

    }

    public ResponseData<Map<String, String>> getTemplateStatus(TemplateStatusRequestParams params) {

        //可以处理前提逻辑
        //获取模板ID
        log.info("[获取普通状态]：{}", new Gson().toJson(params));
        Map<String,String> result = new HashMap<>();
        result.put("orderNo",params.getOrderNo());
        result.put("templateId","TEMP100000369");
        result.put("templateStatus","0");
        result.put("statusDesc","等待审核");
        return ResponseDataUtil.buildSuccess(result);

    }

    @Async
    public void save(TemplateAddRequestParams params) {


    }
}
