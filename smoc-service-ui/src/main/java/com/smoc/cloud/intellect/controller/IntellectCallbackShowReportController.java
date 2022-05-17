package com.smoc.cloud.intellect.controller;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import com.smoc.cloud.intellect.service.IntellectCallbackShowReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 智能短信成功解析回执
 */
@Slf4j
@RestController
@RequestMapping("intellect/callback/show/report")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectCallbackShowReportController {

    @Autowired
    private IntellectCallbackShowReportService intellectCallbackShowReportService;

    /**
     * 查询智能短信成功解析报告
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intellect/callback/callback_show_report_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<IntellectCallbackShowReportValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IntellectCallbackShowReportValidator intellectCallbackShowReportValidator = new IntellectCallbackShowReportValidator();
        intellectCallbackShowReportValidator.setCustFlag(user.getOrganization());
        params.setParams(intellectCallbackShowReportValidator);

        //查询
        ResponseData<PageList<IntellectCallbackShowReportValidator>> data = intellectCallbackShowReportService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("intellectCallbackShowReportValidator", intellectCallbackShowReportValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", params);
        return view;

    }

    /**
     * 查询智能短信成功解析报告
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IntellectCallbackShowReportValidator intellectCallbackShowReportValidator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intellect/callback/callback_show_report_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        intellectCallbackShowReportValidator.setCustFlag(user.getOrganization());
        pageParams.setParams(intellectCallbackShowReportValidator);

        //查询
        ResponseData<PageList<IntellectCallbackShowReportValidator>> data = intellectCallbackShowReportService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("intellectCallbackShowReportValidator", intellectCallbackShowReportValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", pageParams);
        return view;

    }

}
