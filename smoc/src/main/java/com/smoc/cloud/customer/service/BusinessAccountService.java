package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.repository.AccountFinanceRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.finance.entity.FinanceAccount;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;

/**
 * 业务账号管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BusinessAccountService {

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private AccountFinanceRepository accountFinanceRepository;

    @Resource
    private FinanceAccountRepository financeAccountRepository;


    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams) {
        return businessAccountRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<AccountBasicInfo> data = businessAccountRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountBasicInfo entity = data.get();
        AccountBasicInfoValidator accountBaseInfoValidator = new AccountBasicInfoValidator();
        BeanUtils.copyProperties(entity, accountBaseInfoValidator);

        //转换日期
        accountBaseInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountBaseInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountBasicInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<AccountBasicInfo> save(AccountBasicInfoValidator accountBasicInfoValidator, String op) {

        Iterable<AccountBasicInfo> data = businessAccountRepository.findByAccountId(accountBasicInfoValidator.getAccountId());

        AccountBasicInfo entity = new AccountBasicInfo();
        BeanUtils.copyProperties(accountBasicInfoValidator, entity);

        String accountType = "";

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                AccountBasicInfo info = (AccountBasicInfo) iter.next();
                accountType = info.getBusinessType();
                if (!entity.getAccountId().equals(info.getAccountId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountBasicInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //添加账户信息
        saveFinanceAccount(entity, op, accountType);

        if ("edit".equals(op)) {
            //根据运营商先删除库里多余的运营商的价格(比如：添加的时候选择了3个运营商，修改的时候选择了2个运营商，那么就得把多余的1个运营商删除)
            accountFinanceRepository.deleteByAccountIdAndCarrier(accountBasicInfoValidator.getAccountId(), accountBasicInfoValidator.getCarrier());
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号基本信息][{}]数据:{}", op, JSON.toJSONString(entity));
        businessAccountRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    //添加账户信息
    private void saveFinanceAccount(AccountBasicInfo accountBasicInfo, String op, String accountType) {

        if ("add".equals(op)) {
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setAccountId(accountBasicInfo.getAccountId());
            financeAccount.setAccountType(accountBasicInfo.getBusinessType());
            financeAccount.setAccountTotalSum(new BigDecimal("0.00000"));
            financeAccount.setAccountUsableSum(new BigDecimal("0.00000"));
            financeAccount.setAccountFrozenSum(new BigDecimal("0.00000"));
            financeAccount.setAccountConsumeSum(new BigDecimal("0.00000"));
            financeAccount.setAccountRechargeSum(new BigDecimal("0.00000"));
            financeAccount.setAccountCreditSum(new BigDecimal("0.00000"));
            financeAccount.setAccountStatus("1");
            financeAccount.setCreatedTime(DateTimeUtils.getNowDateTime());
            financeAccount.setCreatedBy(accountBasicInfo.getCreatedBy());
            financeAccountRepository.saveAndFlush(financeAccount);
        }

        //r如果是修改了业务类型，需要修改账户的业务类型
        if ("edit".equals(op) && !accountType.equals(accountBasicInfo.getBusinessType())) {
            financeAccountRepository.updateAccountTypeByAccountId(accountBasicInfo.getBusinessType(), accountBasicInfo.getAccountId());
        }
    }

}
