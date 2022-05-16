package com.smoc.cloud.intelligence.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.intelligence.service.IntellectCallbackTemplateStatusReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 模版状态回执报告
 */
@Slf4j
@RestController
@RequestMapping("intel/callback/template/report")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectCallbackTemplateStatusReportController {

    @Autowired
    private IntellectCallbackTemplateStatusReportService intellectCallbackTemplateStatusReportService;


    /**
     * 分页查询 模版状态回执报告
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<IntellectCallbackTemplateStatusReportValidator> page(@RequestBody PageParams<IntellectCallbackTemplateStatusReportValidator> pageParams) {

        return intellectCallbackTemplateStatusReportService.page(pageParams);

    }

    /**
     * 保存模版回调，并变更模版状态
     *
     * @param intellectCallbackTemplateStatusReportValidator
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IntellectCallbackTemplateStatusReportValidator intellectCallbackTemplateStatusReportValidator) {

        //启动异步
        intellectCallbackTemplateStatusReportService.save(intellectCallbackTemplateStatusReportValidator);

        //保存操作
        return ResponseDataUtil.buildSuccess();

    }
}
