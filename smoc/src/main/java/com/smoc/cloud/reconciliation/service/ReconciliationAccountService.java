package com.smoc.cloud.reconciliation.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationAccountModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.reconciliation.repository.ReconciliationAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReconciliationAccountService {

    @Autowired
    private ReconciliationAccountRepository reconciliationAccountRepository;

    /**
     * 查询企业客户账单
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationEnterpriseModel>> page(PageParams<ReconciliationEnterpriseModel> pageParams){

        PageList<ReconciliationEnterpriseModel> pageList = reconciliationAccountRepository.page(pageParams);
        if(null != pageList.getList() && pageList.getList().size()>0){

            for(ReconciliationEnterpriseModel obj:pageList.getList()){
                Map<String,Object> resutMap = reconciliationAccountRepository.getEnterpriseBills(obj.getEnterpriseId(),obj.getAccountingPeriod());
                List<ReconciliationAccountModel> list = (List<ReconciliationAccountModel>) resutMap.get("list");
                obj.setAccounts(list);
                obj.setQuantity(resutMap.get("quantity").toString());
                obj.setSum(resutMap.get("am").toString());
            }
        }

        return ResponseDataUtil.buildSuccess(pageList);
    }
}
