package com.smoc.cloud.saler.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface CustomerFeignClient {

    /**
     * 客户业务账号列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/saler/customer/page", method = RequestMethod.POST)
    ResponseData<PageList<CustomerAccountInfoQo>> page(@RequestBody PageParams<CustomerAccountInfoQo> params);

    /**
     * 查询账号信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountBasicInfoValidator> findAccountById(@PathVariable String id);

    /**
     * 单个账号发送量统计按月Ajax
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/saler/customer/statisticSendNumberMonthByAccount", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticSendData>> statisticSendNumberMonthByAccount(@RequestBody AccountStatisticSendData statisticSendData);

    /**
     * 查询企业基本数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseBasicInfoValidator> findEnterpriseBasicInfoById(@PathVariable String id);

    /**
     * 根据企业查询账号发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/saler/customer/statisticSendNumberByEnterpriseName", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticSendData>> statisticSendNumberByEnterpriseName(@RequestBody AccountStatisticSendData statisticSendData);
}
