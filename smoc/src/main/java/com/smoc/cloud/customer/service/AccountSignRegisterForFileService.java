package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.repository.AccountSignRegisterForFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AccountSignRegisterForFileService {

    @Resource
    private AccountSignRegisterForFileRepository accountSignRegisterForFileRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterForFileValidator>> page(PageParams<AccountSignRegisterForFileValidator> pageParams){

        PageList<AccountSignRegisterForFileValidator> pageList = this.accountSignRegisterForFileRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(pageList);
    }
}
