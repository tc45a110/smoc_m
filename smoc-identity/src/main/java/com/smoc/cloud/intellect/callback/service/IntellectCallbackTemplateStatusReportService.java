package com.smoc.cloud.intellect.callback.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.intellect.callback.remote.IntellectCallbackTemplateStatusReportFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IntellectCallbackTemplateStatusReportService {

    @Autowired
    private IntellectCallbackTemplateStatusReportFeign intellectCallbackTemplateStatusReportFeign;

    /**
     * 查询智能短信成功解析报告
     *
     * @return
     */
    public ResponseData save(IntellectCallbackTemplateStatusReportValidator intellectCallbackTemplateStatusReportValidator) {
        try {
            ResponseData data = intellectCallbackTemplateStatusReportFeign.save(intellectCallbackTemplateStatusReportValidator);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}