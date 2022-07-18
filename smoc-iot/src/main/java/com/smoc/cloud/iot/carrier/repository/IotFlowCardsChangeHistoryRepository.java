package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.entity.IotFlowCardsChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotFlowCardsChangeHistoryRepository extends JpaRepository<IotFlowCardsChangeHistory, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IotFlowCardsChangeHistoryValidator> page(PageParams<IotFlowCardsChangeHistoryValidator> pageParams);
}