package com.smoc.cloud.intellect.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.customer.service.BusinessAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 检索企业业务账号
 */
@Slf4j
@Controller
@RequestMapping("intel/template")
public class SearchAccountController {

    @Autowired
    private BusinessAccountService businessAccountService;


    /**
     * 检索企业业务账号
     *
     * @return
     */
    @RequestMapping(value = "/account/search", method = RequestMethod.GET)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("intellect/template/search_business_account");

        //初始化数据
        PageParams<AccountBasicInfoValidator> params = new PageParams<AccountBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType("INTELLECT_SMS");
        params.setParams(accountBasicInfoValidator);

        //查询
        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("businessType", "INTELLECT_SMS");
        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * EC检索
     *
     * @return
     */
    @RequestMapping(value = "/account/search/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountBasicInfoValidator accountBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("intellect/template/search_business_account");
        accountBasicInfoValidator.setBusinessType("INTELLECT_SMS");
        //分页查询
        pageParams.setParams(accountBasicInfoValidator);

        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("businessType", "INTELLECT_SMS");
        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }


}
