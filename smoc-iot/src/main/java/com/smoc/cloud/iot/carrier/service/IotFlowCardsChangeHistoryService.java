package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsChangeHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IotFlowCardsChangeHistoryService {

    @Resource
    private IotFlowCardsChangeHistoryRepository iotFlowCardsChangeHistoryRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotFlowCardsChangeHistoryValidator>> page(PageParams<IotFlowCardsChangeHistoryValidator> pageParams) {
        PageList<IotFlowCardsChangeHistoryValidator> page = iotFlowCardsChangeHistoryRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 批量保存物联网卡的历史记录
     *
     * @param histories
     * @return
     */
    @Transactional
    public ResponseData save(String msisdn, String imsi, String iccid, List<IotFlowCardsChangeHistoryValidator> histories) {
        return ResponseDataUtil.buildSuccess();
    }
}
