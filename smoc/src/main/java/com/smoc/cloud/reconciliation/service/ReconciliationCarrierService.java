package com.smoc.cloud.reconciliation.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.repository.ReconciliationCarrierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
public class ReconciliationCarrierService {

    @Autowired
    private ReconciliationCarrierRepository reconciliationCarrierRepository;

    /**
     * 查询运营商账单
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationChannelCarrierModel>> page(PageParams<ReconciliationChannelCarrierModel> pageParams){

        PageList<ReconciliationChannelCarrierModel> pageList = reconciliationCarrierRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(pageList);
    }

    /**
     * 根据运营商和账单周期查询账单
     * @param startDate
     * @param channelProvder
     * @return
     */
    public ResponseData<List<ReconciliationCarrierItemsValidator>> findReconciliationCarrier(String startDate, String channelProvder) {
        List<ReconciliationCarrierItemsValidator> pageList = reconciliationCarrierRepository.findReconciliationCarrier(startDate,channelProvder);

        return ResponseDataUtil.buildSuccess(pageList);
    }

    /**
     * 保存对账
     * @param reconciliationChannelCarrierModel
     * @return
     */
    @Transactional
    public ResponseData save(ReconciliationChannelCarrierModel reconciliationChannelCarrierModel) {

        //先删除
        reconciliationCarrierRepository.deleteByChannelPeriodAndChannelProvder(reconciliationChannelCarrierModel.getChannelPeriod(),reconciliationChannelCarrierModel.getChannelProvder());

        reconciliationCarrierRepository.batchSave(reconciliationChannelCarrierModel);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 运营商对账记录
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationChannelCarrierModel>> reconciliationCarrierRecord(PageParams<ReconciliationChannelCarrierModel> pageParams) {
        PageList<ReconciliationChannelCarrierModel> pageList = reconciliationCarrierRepository.reconciliationCarrierRecord(pageParams);

        return ResponseDataUtil.buildSuccess(pageList);
    }
}
