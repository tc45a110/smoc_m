package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.remote.CustomerFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * 客户管理
 */
@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerFeignClient customerFeignClient;

    /**
     * 客户业务账号列表
     * @param params
     * @return
     */
    public ResponseData<PageList<CustomerAccountInfoQo>> page(PageParams<CustomerAccountInfoQo> params) {
        try {
            ResponseData<PageList<CustomerAccountInfoQo>> page = customerFeignClient.page(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询账号信息
     * @param accountId
     * @return
     */
    public ResponseData<AccountBasicInfoValidator> findAccountById(String accountId) {
        try {
            ResponseData<AccountBasicInfoValidator> data = this.customerFeignClient.findAccountById(accountId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询企业基本数据
     * @param enterpriseId
     * @return
     */
    public ResponseData<EnterpriseBasicInfoValidator> findEnterpriseBasicInfoById(String enterpriseId) {
        try {
            ResponseData<EnterpriseBasicInfoValidator> data = this.customerFeignClient.findEnterpriseBasicInfoById(enterpriseId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 单个账号发送量统计按月Ajax
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData statisticSendNumberMonthByAccount(AccountStatisticSendData statisticSendData) {
        ResponseData<List<AccountStatisticSendData>> responseData = this.customerFeignClient.statisticSendNumberMonthByAccount(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
        //总发送量
        BigDecimal totalNumber = list.stream().map(AccountStatisticSendData::getSendNumber).reduce(BigDecimal::add).get();

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonthArray(month);
        accountStatisticSendData.setSendNumberArray(sendNumber);
        accountStatisticSendData.setTotalNumber(totalNumber);

        return accountStatisticSendData;
    }

    /**
     * 根据企业查询账号发送量统计按月
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData statisticSendNumberByEnterpriseName(AccountStatisticSendData statisticSendData) {
        ResponseData<List<AccountStatisticSendData>> responseData = this.customerFeignClient.statisticSendNumberByEnterpriseName(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
        //总发送量
        BigDecimal totalNumber = list.stream().map(AccountStatisticSendData::getSendNumber).reduce(BigDecimal::add).get();

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonthArray(month);
        accountStatisticSendData.setSendNumberArray(sendNumber);
        accountStatisticSendData.setTotalNumber(totalNumber);

        return accountStatisticSendData;
    }
}
