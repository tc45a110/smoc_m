package com.smoc.cloud.query.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/query")
public class QueryInfoController {

    @Autowired
    private EnterpriseWebService enterpriseWebService;

    @Autowired
    private BusinessAccountService businessAccountService;

    /**
     *  查询web账号
     * @return
     */
    @RequestMapping(value = "/enterprise/web/list", method = RequestMethod.GET)
    public ModelAndView webAll() {
        ModelAndView view = new ModelAndView("query/enterprise_web_list");

        //初始化数据
        PageParams<EnterpriseWebAccountInfoValidator> params = new PageParams<EnterpriseWebAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator = new EnterpriseWebAccountInfoValidator();
        params.setParams(enterpriseWebAccountInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseWebAccountInfoValidator>> data = enterpriseWebService.webAll(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseWebAccountInfoValidator", enterpriseWebAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * web账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/enterprise/web/page", method = RequestMethod.POST)
    public ModelAndView webAllPage(@ModelAttribute EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("query/enterprise_web_list");

        //分页查询
        pageParams.setParams(enterpriseWebAccountInfoValidator);

        ResponseData<PageList<EnterpriseWebAccountInfoValidator>> data = enterpriseWebService.webAll(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseWebAccountInfoValidator", enterpriseWebAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     *  业务账号综合查询
     * @return
     */
    @RequestMapping(value = "/enterprise/account/list", method = RequestMethod.GET)
    public ModelAndView accountAll() {
        ModelAndView view = new ModelAndView("query/enterprise_account_list");

        //初始化数据
        PageParams<AccountInfoQo> params = new PageParams<AccountInfoQo>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountInfoQo accountInfoQo = new AccountInfoQo();
        params.setParams(accountInfoQo);

        //查询
        ResponseData<PageList<AccountInfoQo>> data = businessAccountService.accountAll(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountInfoQo", accountInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * web账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/enterprise/account/page", method = RequestMethod.POST)
    public ModelAndView accountPage(@ModelAttribute AccountInfoQo accountInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("query/enterprise_account_list");

        //分页查询
        pageParams.setParams(accountInfoQo);

        ResponseData<PageList<AccountInfoQo>> data = enterpriseWebService.webAll(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountInfoQo", accountInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }
}
