package com.smoc.cloud.intellect.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectCallbackShowReportFeigh {

    /**
     * 查询智能短信成功解析报告
     *
     * @return
     */
    @RequestMapping(value = "/intel/callback/show/report/page", method = RequestMethod.POST)
    PageList<IntellectCallbackShowReportValidator> page(@RequestBody PageParams<IntellectCallbackShowReportValidator> pageParams) throws Exception;
}
