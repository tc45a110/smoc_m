package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import com.smoc.cloud.customer.remote.AccountPriceHistoryFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountPriceHistoryService {

    @Autowired
    private AccountPriceHistoryFeignClient accountPriceHistoryFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountPriceHistoryValidator>> page( PageParams<AccountPriceHistoryValidator> pageParams) {
        try {
            ResponseData<PageList<AccountPriceHistoryValidator>> data = this.accountPriceHistoryFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
