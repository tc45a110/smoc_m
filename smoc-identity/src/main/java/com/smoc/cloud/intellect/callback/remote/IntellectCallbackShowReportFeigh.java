package com.smoc.cloud.intellect.callback.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectCallbackShowReportFeigh {

    /**
     * 保存智能显示回执报告，并根据显示报告状态进行计费
     *
     * @param intellectCallbackShowReportValidator
     * @return
     */
    @RequestMapping(value = "/intel/callback/show/report/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody IntellectCallbackShowReportValidator intellectCallbackShowReportValidator) throws Exception;
}
