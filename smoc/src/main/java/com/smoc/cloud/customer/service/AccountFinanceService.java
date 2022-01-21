package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.repository.AccountFinanceRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 *  账号财务接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountFinanceService {

    @Resource
    private AccountFinanceRepository accountFinanceRepository;

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private FinanceAccountRepository financeAccountRepository;


    /**
     * 根据账号ID查询运营商单价
     * @param accountFinanceInfoValidator
     * @return
     */
    public ResponseData editCarrierPrice(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator) {

        //根据账号id查询已有的价格
        List<AccountFinanceInfoValidator> list = accountFinanceRepository.findByAccountId(accountFinanceInfoValidator);

        //前台选择的运营商
        Map<String, BigDecimal> map = new TreeMap<>();
        String[] carrier = accountFinanceInfoValidator.getCarrier().split(",");
        for (int i = 0; i < carrier.length; i++) {
            map.put(carrier[i], null);
        }

        //循环已设定过价格的运营商，赋值给选择的运营商
        if (!StringUtils.isEmpty(list)) {
            for (AccountFinanceInfoValidator accountFinanceInfo : list) {
                map.put(accountFinanceInfo.getCarrier(), accountFinanceInfo.getCarrierPrice());
            }
        }

        return ResponseDataUtil.buildSuccess(map);
    }

    public ResponseData<List<AccountFinanceInfoValidator>> findByAccountId(AccountFinanceInfoValidator accountFinanceInfoValidator) {
        List<AccountFinanceInfoValidator> list = accountFinanceRepository.findByAccountId(accountFinanceInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    @Transactional
    public ResponseData save(AccountFinanceInfoValidator accountFinanceInfoValidator, String op) {

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //先删除数据
        accountFinanceRepository.deleteByAccountId(accountFinanceInfoValidator.getAccountId());

        //批量保存
        accountFinanceRepository.batchSave(accountFinanceInfoValidator);

        //修改账户的授信额度
        financeAccountRepository.updateAccountCreditSumByAccountId(accountFinanceInfoValidator.getAccountId(),accountFinanceInfoValidator.getAccountCreditSum());

        //设置账号完成进度
        if("add".equals(op)){
            Optional<AccountBasicInfo> optional = businessAccountRepository.findById(accountFinanceInfoValidator.getAccountId());
            if(optional.isPresent()){
                AccountBasicInfo accountBasicInfo = optional.get();
                StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
                accountProcess = accountProcess.replace(1, 2, "1");
                accountBasicInfo.setAccountProcess(accountProcess.toString());
                businessAccountRepository.save(accountBasicInfo);
            }
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号财务信息][{}]数据:{}",op, JSON.toJSONString(accountFinanceInfoValidator));

        return ResponseDataUtil.buildSuccess();
    }
}
