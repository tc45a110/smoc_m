package com.smoc.cloud.intellect.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectCallbackTemplateStatusReportFeign {

    /**
     * 分页查询 模版状态回执报告
     *
     * @return
     */
    @RequestMapping(value = "/intel/callback/template/report/page", method = RequestMethod.POST)
    PageList<IntellectCallbackTemplateStatusReportValidator> page(@RequestBody PageParams<IntellectCallbackTemplateStatusReportValidator> pageParams) throws Exception;
}
