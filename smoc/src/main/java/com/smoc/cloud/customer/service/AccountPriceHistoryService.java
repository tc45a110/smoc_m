package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import com.smoc.cloud.customer.repository.AccountPriceHistoryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountPriceHistoryService {

    @Resource
    private AccountPriceHistoryRepository accountPriceHistoryRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountPriceHistoryValidator>> page(PageParams<AccountPriceHistoryValidator> pageParams) {

        PageList<AccountPriceHistoryValidator> data = accountPriceHistoryRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }
}
