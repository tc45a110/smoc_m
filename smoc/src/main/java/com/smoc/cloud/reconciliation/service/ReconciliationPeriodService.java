package com.smoc.cloud.reconciliation.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.reconciliation.entity.ReconciliationPeriod;
import com.smoc.cloud.reconciliation.repository.ReconciliationPeriodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReconciliationPeriodService {

    @Resource
    private ReconciliationPeriodRepository reconciliationPeriodRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationPeriodValidator>> page(PageParams<ReconciliationPeriodValidator> pageParams) {
        return ResponseDataUtil.buildSuccess(reconciliationPeriodRepository.page(pageParams));
    }

    /**
     * 查询近5个月账期
     */
    public ResponseData<Map<String, String>> findAccountPeriod() {

        List<String> list = reconciliationPeriodRepository.findAccountPeriod();
        Map<String, String> resultMap = new HashMap();
        if (null != list && list.size() > 0) {
            for (String obj : list) {
                resultMap.put(obj, obj);
            }
        }
        return ResponseDataUtil.buildSuccess(resultMap);
    }

    /**
     * 创建业务账号对账
     */
    @Transactional
    public ResponseData buildAccountPeriod(ReconciliationPeriodValidator validator) {

        String uuid = UUID.uuid32();
        validator.setId(uuid);
        ReconciliationPeriod entity = new ReconciliationPeriod();
        BeanUtils.copyProperties(validator, entity);

        entity.setAccountPeriodStartDate(DateTimeUtils.getDateTimeFormat(validator.getAccountPeriodStartDate() + " 00:00:00"));
        entity.setAccountPeriodEndDate(DateTimeUtils.getDateTimeFormat(validator.getAccountPeriodEndDate() + " 00:00:00"));
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(validator.getCreatedTime()));
        reconciliationPeriodRepository.saveAndFlush(entity);
        log.info("[ReconciliationPeriodValidator]:{}", new Gson().toJson(entity));
        reconciliationPeriodRepository.buildAccountPeriod(validator, uuid);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 删除账期
     */
    public ResponseData deleteAccountPeriod(String id) {
        reconciliationPeriodRepository.deleteAccountPeriod(id);
        return ResponseDataUtil.buildSuccess();
    }
}
