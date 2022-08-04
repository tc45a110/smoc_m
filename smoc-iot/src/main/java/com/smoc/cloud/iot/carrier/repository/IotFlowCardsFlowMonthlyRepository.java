package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.api.response.flow.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.entity.IotFlowCardsFlowMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotFlowCardsFlowMonthlyRepository extends JpaRepository<IotFlowCardsFlowMonthly, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<SimFlowUsedMonthlyResponse> page(String account, String iccid, String queryMonth, PageParams pageParams);

}