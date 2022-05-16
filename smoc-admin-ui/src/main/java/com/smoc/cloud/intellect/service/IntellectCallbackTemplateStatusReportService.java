package com.smoc.cloud.intellect.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.intellect.remote.IntellectCallbackTemplateStatusReportFeign;
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
    public ResponseData<PageList<IntellectCallbackTemplateStatusReportValidator>> page(PageParams<IntellectCallbackTemplateStatusReportValidator> pageParams) {
        try {
            PageList<IntellectCallbackTemplateStatusReportValidator> data = intellectCallbackTemplateStatusReportFeign.page(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
