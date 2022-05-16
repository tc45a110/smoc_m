package com.smoc.cloud.saler.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 销售客户管理
 */
@Slf4j
@RestController
@RequestMapping("saler/customer")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    /**
     * 客户业务账号列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<CustomerAccountInfoQo>> page(@RequestBody PageParams<CustomerAccountInfoQo> pageParams) {

        return customerService.page(pageParams);
    }

    /**
     * 单个账号发送量统计按月
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/statisticSendNumberMonthByAccount", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> statisticSendNumberMonthByAccount(@RequestBody AccountStatisticSendData statisticSendData) {

        return customerService.statisticSendNumberMonthByAccount(statisticSendData);
    }

    /**
     * 根据企业查询账号发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/statisticSendNumberByEnterpriseName", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> statisticSendNumberByEnterpriseName(@RequestBody AccountStatisticSendData statisticSendData) {

        return customerService.statisticSendNumberByEnterpriseName(statisticSendData);
    }
}
