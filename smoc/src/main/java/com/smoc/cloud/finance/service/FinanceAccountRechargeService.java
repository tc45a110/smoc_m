package com.smoc.cloud.finance.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.repository.FinanceAccountRechargeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FinanceAccountRechargeService {

    @Resource
    private FinanceAccountRechargeRepository financeAccountRechargeRepository;

    /**
     * 分页查询
     * @param pageParams
     * @param flag  1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public ResponseData<PageList<FinanceAccountRechargeValidator>> page(PageParams<FinanceAccountRechargeValidator> pageParams, String flag) {


        if ("1".equals(flag)) {

        }
        if ("2".equals(flag)) {
            PageList<FinanceAccountRechargeValidator> data = financeAccountRechargeRepository.pageIdentification(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        }

        return ResponseDataUtil.buildError();
    }
}
