package com.smoc.cloud.finance.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountConsumeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.customer.repository.EnterpriseRepository;
import com.smoc.cloud.finance.entity.FinanceAccount;
import com.smoc.cloud.finance.entity.FinanceAccountRecharge;
import com.smoc.cloud.finance.repository.FinanceAccountRechargeRepository;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.smoc.cloud.common.response.ResponseCode.PARAM_QUERY_ERROR;


@Slf4j
@Service
public class FinanceAccountService {

    @Resource
    private EnterpriseRepository enterpriseRepository;

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private FinanceAccountRechargeRepository financeAccountRechargeRepository;


    /**
     * 分页查询
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号
     * @return
     */
    public ResponseData<PageList<FinanceAccountValidator>> page(PageParams<FinanceAccountValidator> pageParams, String flag) {

        if ("1".equals(flag)) {
            PageList<FinanceAccountValidator> data = financeAccountRepository.pageBusinessType(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        }
        if ("2".equals(flag)) {
            PageList<FinanceAccountValidator> data = financeAccountRepository.pageIdentification(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        }
        if ("3".equals(flag)) {
            PageList<FinanceAccountValidator> data = financeAccountRepository.pageShare(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        }

        return ResponseDataUtil.buildError();
    }

    /**
     * 统计账户金额
     *
     * @param flag 1 表示业务账号 账户  2表示认证账号 账户 3表示财务共享账户
     * @return
     */
    public ResponseData<Map<String, Object>> countSum(String flag) {
        Map<String, Object> data = financeAccountRepository.countSum(flag);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 账户充值,保存充值记录，变更财务账户
     *
     * @return
     */
    @Transactional
    public ResponseData recharge(FinanceAccountRechargeValidator financeAccountRechargeValidator) {

        financeAccountRepository.recharge(financeAccountRechargeValidator.getRechargeSum(), financeAccountRechargeValidator.getAccountId());
        FinanceAccountRecharge entity = new FinanceAccountRecharge();
        BeanUtils.copyProperties(financeAccountRechargeValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(financeAccountRechargeValidator.getCreatedTime()));
        financeAccountRechargeRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();

    }

    /**
     * 账户冻结 冻结要消费的金额，冻结时，会根据余额、授信额度来进行计算。冻结失败会提示余额不足
     *
     * @return
     */
    @Transactional
    public ResponseData freeze(FinanceAccountConsumeValidator financeAccountConsumeValidator) {

        return ResponseDataUtil.buildSuccess();

    }

    /**
     * 账户消费（从冻结中消费）
     *
     * @return
     */
    @Transactional
    public ResponseData consume(FinanceAccountConsumeValidator financeAccountConsumeValidator) {

        return ResponseDataUtil.buildSuccess();

    }

    /**
     * 账户解冻 解冻冻结金额
     *
     * @return
     */
    @Transactional
    public ResponseData unfreeze(FinanceAccountConsumeValidator financeAccountConsumeValidator) {

        return ResponseDataUtil.buildSuccess();

    }

    /**
     * 根据id查询
     *
     * @param accountId
     * @return
     */
    public ResponseData<FinanceAccountValidator> findById(String accountId) {
        Optional<FinanceAccount> optional = financeAccountRepository.findById(accountId);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        BeanUtils.copyProperties(optional.get(), financeAccountValidator);
        //转换日期
        financeAccountValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(optional.get().getCreatedTime()));

        return ResponseDataUtil.buildSuccess(financeAccountValidator);
    }

    /**
     * 根据enterpriseId，查询企业所有财务账户
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<FinanceAccountValidator>> findEnterpriseFinanceAccounts(String enterpriseId) {

        List<FinanceAccountValidator> list = financeAccountRepository.findEnterpriseFinanceAccount(enterpriseId);
        return ResponseDataUtil.buildSuccess(list);

    }

    /**
     * 根据企业enterpriseId，查询企业所有财务账户(包括子企业财务账户)
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<FinanceAccountValidator>> findEnterpriseAndSubsidiaryFinanceAccount(String enterpriseId) {

        List<String> enterpriseIds = enterpriseRepository.findEnterpriseAndSubsidiaryId(enterpriseId);
        if (null != enterpriseIds && enterpriseIds.size() > 0) {
            log.info("[enterpriseIds]:{}", new Gson().toJson(enterpriseIds));
            List<FinanceAccountValidator> data = financeAccountRepository.findEnterpriseAndSubsidiaryFinanceAccount(enterpriseIds);
            log.info("[data]:{}", new Gson().toJson(data));
            return ResponseDataUtil.buildSuccess(data);
        }

        return ResponseDataUtil.buildError();

    }

    /**
     * 根据enterpriseId 汇总企业金额统计
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<Map<String, Object>> countEnterpriseSum(String enterpriseId) {

        Map<String, Object> data = financeAccountRepository.countEnterpriseSum(enterpriseId);
        return ResponseDataUtil.buildSuccess(data);

    }
}
