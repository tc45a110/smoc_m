package com.smoc.cloud.saler.controller;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户管理
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerAccountController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客户业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/account/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/customer_account_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<CustomerAccountInfoQo> params = new PageParams<CustomerAccountInfoQo>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        CustomerAccountInfoQo customerAccountInfoQo = new CustomerAccountInfoQo();
        customerAccountInfoQo.setSalerId(user.getId());
        params.setParams(customerAccountInfoQo);

        //查询
        ResponseData<PageList<CustomerAccountInfoQo>> data = customerService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("customerAccountInfoQo", customerAccountInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 客户业务账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/account/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute CustomerAccountInfoQo customerAccountInfoQo, PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/customer_account_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        customerAccountInfoQo.setSalerId(user.getId());
        pageParams.setParams(customerAccountInfoQo);

        ResponseData<PageList<AccountBasicInfoValidator>> data = customerService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("customerAccountInfoQo", customerAccountInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 账号发送量统计
     *
     * @return
     */
    @RequestMapping(value = "/account/statistic/{accountId}", method = RequestMethod.GET)
    public ModelAndView statistic(@PathVariable String accountId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/customer_statistic_account_send");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询账号信息
        ResponseData<AccountBasicInfoValidator> info = customerService.findAccountById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //判断是否是本人客户
        if(!user.getOrganization().equals(info.getData().getEnterpriseId())){
            view.addObject("error", "无权限查看");
            return view;
        }

        view.addObject("accountId", accountId);

        return view;

    }
}
