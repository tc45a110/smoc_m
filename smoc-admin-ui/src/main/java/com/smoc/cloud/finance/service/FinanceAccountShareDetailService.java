package com.smoc.cloud.finance.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import com.smoc.cloud.finance.remote.FinanceAccountShareDetailFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 共享财务账户明细记录
 */
@Slf4j
@Service
public class FinanceAccountShareDetailService {

    @Autowired
    private FinanceAccountShareDetailFeignClient financeAccountShareDetailFeignClient;

    /**
     * 分查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FinanceAccountShareDetailValidator>> page(PageParams<FinanceAccountShareDetailValidator> pageParams) {

        try {
            return this.financeAccountShareDetailFeignClient.page(pageParams);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
