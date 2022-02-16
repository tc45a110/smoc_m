package com.smoc.cloud.finance.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import com.smoc.cloud.finance.repository.FinanceAccountShareDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FinanceAccountShareDetailService {

    @Resource
    private FinanceAccountShareDetailRepository financeAccountShareDetailRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FinanceAccountShareDetailValidator>> page(PageParams<FinanceAccountShareDetailValidator> pageParams) {
        PageList<FinanceAccountShareDetailValidator> data = financeAccountShareDetailRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }
}
