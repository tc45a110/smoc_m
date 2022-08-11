package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.remote.AccountSignRegisterForFileFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountSignRegisterForFileService {

    @Autowired
    private AccountSignRegisterForFileFeignClient accountSignRegisterForFileFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterForFileValidator>> page(PageParams<AccountSignRegisterForFileValidator> pageParams) {
        try {
            ResponseData<PageList<AccountSignRegisterForFileValidator>> responseData = this.accountSignRegisterForFileFeignClient.page(pageParams);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
