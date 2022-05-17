package com.smoc.cloud.intellect.callback.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectCallbackTemplateStatusReportFeign {

    /**
     * 保存模版回调，并变更模版状态
     *
     * @param intellectCallbackTemplateStatusReportValidator
     * @return
     */
    @RequestMapping(value = "/intel/callback/template/report/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody IntellectCallbackTemplateStatusReportValidator intellectCallbackTemplateStatusReportValidator) throws Exception;
}
