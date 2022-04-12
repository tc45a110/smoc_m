package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.remote.BusinessAccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 业务账号管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BusinessAccountService {

    @Autowired
    private BusinessAccountFeignClient businessAccountFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountBasicInfoValidator>> page(PageParams<AccountBasicInfoValidator> pageParams) {
        try {
            PageList<AccountBasicInfoValidator> pageList = this.businessAccountFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<AccountBasicInfoValidator> findById(String id) {
        try {
            ResponseData<AccountBasicInfoValidator> data = this.businessAccountFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(AccountBasicInfoValidator accountBasicInfoValidator, String op) {

        try {
            ResponseData data = this.businessAccountFeignClient.save(accountBasicInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销、启用账号
     * @param id
     * @param status
     * @return
     */
    public ResponseData forbiddenAccountById(String id, String status) {
        try {
            ResponseData data = this.businessAccountFeignClient.forbiddenAccountById(id,status);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询企业所有的业务账号
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccountByEnterpriseId(String enterpriseId) {
        try {
            ResponseData<List<AccountBasicInfoValidator>> data = this.businessAccountFeignClient.findBusinessAccountByEnterpriseId(enterpriseId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 生成业务账号
     * @param enterpriseFlag
     * @return
     */
    public ResponseData<String> createAccountId(String enterpriseFlag) {
        try {
            ResponseData<String> accountId = this.businessAccountFeignClient.createAccountId(enterpriseFlag);
            return accountId;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 账号按维度统计发送量
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData statisticAccountSendNumber(AccountStatisticSendData statisticSendData) {
        ResponseData<List<AccountStatisticSendData>> responseData = this.businessAccountFeignClient.statisticAccountSendNumber(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        Map<String, String> keyValueMap = new TreeMap<>();
        if (null != list && list.size() > 0) {
            keyValueMap = list.stream().collect(Collectors.toMap(AccountStatisticSendData::getData1, accountStatisticSendData -> accountStatisticSendData.getData2()));
        }


        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getData1).toArray(String[]::new);
        //发送量
        String[] sendNumber = list.stream().map(AccountStatisticSendData::getData2).toArray(String[]::new); ;

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonth(month);
        accountStatisticSendData.setSendNumber(sendNumber);

        return accountStatisticSendData;
    }

}
