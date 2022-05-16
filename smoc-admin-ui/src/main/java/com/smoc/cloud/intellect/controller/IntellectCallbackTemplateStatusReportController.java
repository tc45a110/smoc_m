package com.smoc.cloud.intellect.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.intellect.service.IntellectCallbackTemplateStatusReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

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
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("intellect/callback/callback_template_status_list");

        //初始化数据
        PageParams<IntellectCallbackTemplateStatusReportValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IntellectCallbackTemplateStatusReportValidator intellectCallbackTemplateStatusReportValidator = new IntellectCallbackTemplateStatusReportValidator();
        params.setParams(intellectCallbackTemplateStatusReportValidator);

        //查询
        ResponseData<PageList<IntellectCallbackTemplateStatusReportValidator>> data = intellectCallbackTemplateStatusReportService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("intellectCallbackTemplateStatusReportValidator", intellectCallbackTemplateStatusReportValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", params);
        return view;

    }

    /**
     * 分页查询 模版状态回执报告
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IntellectCallbackTemplateStatusReportValidator intellectCallbackTemplateStatusReportValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("intellect/callback/callback_template_status_list");
        pageParams.setParams(intellectCallbackTemplateStatusReportValidator);

        //查询
        ResponseData<PageList<IntellectCallbackTemplateStatusReportValidator>> data = intellectCallbackTemplateStatusReportService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("intellectCallbackTemplateStatusReportValidator", intellectCallbackTemplateStatusReportValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", pageParams);
        return view;

    }
}
