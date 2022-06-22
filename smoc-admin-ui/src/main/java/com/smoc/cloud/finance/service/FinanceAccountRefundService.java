package com.smoc.cloud.finance.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import com.smoc.cloud.finance.remote.FinanceAccountRefundFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinanceAccountRefundService {

    @Autowired
    private FinanceAccountRefundFeignClient financeAccountRefundFeignClient;

    /**
     * 分查询列表
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public ResponseData<PageList<FinanceAccountRefundValidator>> page(PageParams<FinanceAccountRefundValidator> pageParams, String flag) {

        try {
            return this.financeAccountRefundFeignClient.page(pageParams, flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}


