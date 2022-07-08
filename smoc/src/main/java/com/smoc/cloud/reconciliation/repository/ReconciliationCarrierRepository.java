package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.entity.ReconciliationCarrierItems;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ReconciliationCarrierRepository extends JpaRepository<ReconciliationCarrierItems, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<ReconciliationChannelCarrierModel> page(PageParams<ReconciliationChannelCarrierModel> pageParams);


}
