package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.customer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * EC业务账号失败补发管理
 */
@Slf4j
@RestController
@RequestMapping("/account/channel/repair")
public class AccountController {

    @Autowired
    private BusinessAccountService businessAccountService;

    /**
     * 业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //初始化数据
        PageParams<AccountBasicInfoValidator> params = new PageParams<AccountBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        params.setParams(accountBasicInfoValidator);

        //查询
        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountBasicInfoValidator accountBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //分页查询
        pageParams.setParams(accountBasicInfoValidator);

        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }


}
