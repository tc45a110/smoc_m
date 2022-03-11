package com.smoc.cloud.configure.advance.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.configure.advance.repository.SystemHistoryPriceChangeRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class SystemHistoryPriceChangeRecordService {

    @Resource
    private SystemHistoryPriceChangeRecordRepository systemHistoryPriceChangeRecordRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> page(PageParams<SystemHistoryPriceChangeRecordValidator> pageParams) {
        PageList<SystemHistoryPriceChangeRecordValidator> data = systemHistoryPriceChangeRecordRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }
}
