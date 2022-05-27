package com.smoc.cloud.intellect.controller;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.intellect.service.SystemAccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 账号管理
 */
@Slf4j
@Controller
@RequestMapping("/intellect/account")
public class IntellectAccountController {

    @Autowired
    private SystemAccountInfoService systemAccountInfoService;

    //通过这个字段，区分 变更账号类型;此处为智能短信账号
    private String businessType ="INTELLECT_ACCOUNT";

    /**
     * 账号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{base}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String base, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intellect/search/account_list");

        if("base".equals(base)){
            view = new ModelAndView("intellect/finance/account_list");
        }

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<SystemAccountInfoValidator> params = new PageParams<SystemAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SystemAccountInfoValidator systemAccountInfoValidator = new SystemAccountInfoValidator();
        systemAccountInfoValidator.setBusinessType(this.businessType);
        systemAccountInfoValidator.setEnterpriseId(user.getOrganization());
        systemAccountInfoValidator.setAccountStatus("1");
        params.setParams(systemAccountInfoValidator);

        //查询
        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("base", base);

        return view;

    }

    /**
     * 账号管理列表分页
     *
     * @return
     */
    @RequestMapping(value = "/page/{base}", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemAccountInfoValidator systemAccountInfoValidator,@PathVariable String base, PageParams pageParams, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("intellect/search/account_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if("base".equals(base)){
            view = new ModelAndView("intellect/finance/account_list");
        }

        //分页查询
        systemAccountInfoValidator.setBusinessType(this.businessType);
        systemAccountInfoValidator.setEnterpriseId(user.getOrganization());
        systemAccountInfoValidator.setAccountStatus("1");
        pageParams.setParams(systemAccountInfoValidator);

        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("base", base);

        return view;
    }

}
