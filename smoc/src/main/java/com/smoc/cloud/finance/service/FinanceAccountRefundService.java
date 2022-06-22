package com.smoc.cloud.finance.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import com.smoc.cloud.finance.repository.FinanceAccountRefundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FinanceAccountRefundService {

    @Resource
    private FinanceAccountRefundRepository financeAccountRefundRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户  4表示共用账号
     * @return
     */
    public ResponseData<PageList<FinanceAccountRefundValidator>> page(PageParams<FinanceAccountRefundValidator> pageParams, String flag) {

        if ("1".equals(flag)) {
            PageList<FinanceAccountRefundValidator> data = financeAccountRefundRepository.pageBusiness(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        }


        return ResponseDataUtil.buildError();
    }


}
