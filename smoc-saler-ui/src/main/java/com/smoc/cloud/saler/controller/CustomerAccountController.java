package com.smoc.cloud.saler.controller;

import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
     * 单个账号发送量统计
     *
     * @return
     */
    @RequestMapping(value = "/account/statistic/messageSend/{accountId}", method = RequestMethod.GET)
    public ModelAndView statistic(@PathVariable String accountId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/customer_statistic_account_send");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询账号信息
        ResponseData<AccountBasicInfoValidator> info = customerService.findAccountById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //查询企业基本数据
        ResponseData<EnterpriseBasicInfoValidator> data = customerService.findEnterpriseBasicInfoById(info.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //判断是否是本人客户
        if(!user.getId().equals(data.getData().getSaler())){
            view.addObject("error", "无权限查看");
            return view;
        }

        view.addObject("accountId", accountId);

        return view;
    }

    /**
     * 单个账号发送量统计按月Ajax
     * @param accountId
     * @param request
     * @return
     */
    @RequestMapping(value = "/account/statistic/statisticAccountSendMonth/{accountId}/{type}", method = RequestMethod.GET)
    public AccountStatisticSendData statisticAccountSendMonth(@PathVariable String accountId, @PathVariable String type,HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询账号信息
        ResponseData<AccountBasicInfoValidator> info = customerService.findAccountById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            return new AccountStatisticSendData();
        }

        //查询企业基本数据
        ResponseData<EnterpriseBasicInfoValidator> data = customerService.findEnterpriseBasicInfoById(info.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return new AccountStatisticSendData();
        }

        //判断是否是本人客户
        if(!user.getId().equals(data.getData().getSaler())){
            return new AccountStatisticSendData();
        }

        AccountStatisticSendData statisticSendData = new AccountStatisticSendData();
        statisticSendData.setAccountId(accountId);
        statisticSendData.setDimension(type);

        statisticSendData = customerService.statisticSendNumberMonthByAccount(statisticSendData);

        return statisticSendData;
    }

    /**
     * 客户发送量统计显示页面：根据企业查询
     *
     * @return
     */
    @RequestMapping(value = "/statistic/statisticSendMonth", method = RequestMethod.GET)
    public ModelAndView statisticSendMonth(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/customer_statistic_send");

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();

        view.addObject("accountStatisticSendData", accountStatisticSendData);

        return view;
    }

    /**
     * 查询客户发送量统计显示页面：根据企业查询
     *
     * @return
     */
    @RequestMapping(value = "/statistic/statisticSendQuery", method = RequestMethod.POST)
    public ModelAndView statisticSendQuery(@ModelAttribute AccountStatisticSendData accountStatisticSendData,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/customer_statistic_send");

        view.addObject("accountStatisticSendData", accountStatisticSendData);

        return view;
    }

    /**
     * 企业发送量统计按月：根据企业查询
     * @param statisticSendData
     * @param request
     * @return
     */
    @RequestMapping(value = "/statistic/statisticSendMonthByAccount", method = RequestMethod.POST)
    public AccountStatisticSendData statisticSendByAccount(@RequestBody AccountStatisticSendData statisticSendData, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        statisticSendData.setSaler(user.getId());
        statisticSendData.setDimension("month");

        statisticSendData = customerService.statisticSendNumberByEnterpriseName(statisticSendData);

        return statisticSendData;
    }

    /**
     * 企业发送量统计按天：根据企业查询
     * @param statisticSendData
     * @param request
     * @return
     */
    @RequestMapping(value = "/statistic/statisticSendDayByAccount", method = RequestMethod.POST)
    public AccountStatisticSendData statisticSendDayByName(@RequestBody AccountStatisticSendData statisticSendData, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        statisticSendData.setSaler(user.getId());
        statisticSendData.setDimension("day");

        statisticSendData = customerService.statisticSendNumberByEnterpriseName(statisticSendData);

        return statisticSendData;
    }
}
