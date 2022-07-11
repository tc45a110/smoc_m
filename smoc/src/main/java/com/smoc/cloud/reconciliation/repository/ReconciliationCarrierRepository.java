package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.entity.ReconciliationCarrierItems;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ReconciliationCarrierRepository extends JpaRepository<ReconciliationCarrierItems, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<ReconciliationChannelCarrierModel> page(PageParams<ReconciliationChannelCarrierModel> pageParams);

    /**
     * 根据运营商和账单周期查询账单
     * @param startDate
     * @param channelProvder
     * @return
     */
    List<ReconciliationCarrierItemsValidator> findReconciliationCarrier(String startDate, String channelProvder);

    /**
     * 保存对账
     * @param reconciliationChannelCarrierModel
     */
    void batchSave(ReconciliationChannelCarrierModel reconciliationChannelCarrierModel);

    @Modifying
    @Query(value = "update reconciliation_carrier_items set status =0 where CHANNEL_PERIOD = :channelPeriod and CHANNEL_PROVDER = :channelProvder ",nativeQuery = true)
    void deleteByChannelPeriodAndChannelProvder(@Param("channelPeriod")String channelPeriod,@Param("channelProvder")String channelProvder);
}
