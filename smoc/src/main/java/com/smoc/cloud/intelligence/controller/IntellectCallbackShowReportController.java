package com.smoc.cloud.intelligence.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.intelligence.service.IntellectCallbackShowReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 智能短信成功解析回执
 */
@Slf4j
@RestController
@RequestMapping("intel/callback/show/report")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectCallbackShowReportController {

    @Autowired
    private IntellectCallbackShowReportService intellectCallbackShowReportService;

    /**
     * 查询智能短信成功解析报告
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<IntellectCallbackShowReportValidator> page(@RequestBody PageParams<IntellectCallbackShowReportValidator> pageParams) {

        return intellectCallbackShowReportService.page(pageParams);

    }

    /**
     * 保存智能显示回执报告，并根据显示报告状态进行计费
     *
     * @param intellectCallbackShowReportValidator
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IntellectCallbackShowReportValidator intellectCallbackShowReportValidator) {

        //启动异步
        intellectCallbackShowReportService.save(intellectCallbackShowReportValidator);

        //保存操作
        return ResponseDataUtil.buildSuccess();

    }
}
