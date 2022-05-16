package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerService {

    @Resource
    private CustomerRepository customerRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<CustomerAccountInfoQo>> page(PageParams<CustomerAccountInfoQo> pageParams) {

        PageList<CustomerAccountInfoQo> list = customerRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 单个账号发送量统计按月
     * @param statisticSendData
     * @return
     */
    public ResponseData<List<AccountStatisticSendData>> statisticSendNumberMonthByAccount(AccountStatisticSendData statisticSendData) {
        List<AccountStatisticSendData> list = customerRepository.statisticSendNumberMonthByAccount(statisticSendData);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据企业查询账号发送量统计按月
     * @param statisticSendData
     * @return
     */
    public ResponseData<List<AccountStatisticSendData>> statisticSendNumberByEnterpriseName(AccountStatisticSendData statisticSendData) {
        List<AccountStatisticSendData> list = customerRepository.statisticSendNumberByEnterpriseName(statisticSendData);
        return ResponseDataUtil.buildSuccess(list);
    }
}
