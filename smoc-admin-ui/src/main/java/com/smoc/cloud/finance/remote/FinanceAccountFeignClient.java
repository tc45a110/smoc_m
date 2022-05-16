package com.smoc.cloud.finance.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 财务账号
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface FinanceAccountFeignClient {

    /**
     * 分查询列表
     * @param pageParams
     * @param flag  1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号 4表示共用的账号财务账户
     * @return
     */
    @RequestMapping(value = "/finance/account/page/{flag}", method = RequestMethod.POST)
    ResponseData<PageList<FinanceAccountValidator>> page(@RequestBody PageParams<FinanceAccountValidator> pageParams, @PathVariable String flag) throws Exception;

    /**
     * 统计账户金额
     * @param flag  1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号 4表示共用的账号财务账户
     * @return
     */
    @RequestMapping(value = "/finance/account/count/{flag}", method = RequestMethod.POST)
    ResponseData<Map<String,Object>> count(@RequestBody FinanceAccountValidator financeAccountValidator,@PathVariable String flag) throws Exception;

    /**
     * 账户充值,保存充值记录，变更财务账户
     *
     * @return
     */
    @RequestMapping(value = "/finance/account/recharge", method = RequestMethod.POST)
    ResponseData recharge(@RequestBody FinanceAccountRechargeValidator financeAccountRechargeValidator) throws Exception;

    /**
     * 根据id查询
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/finance/account/findById/{accountId}", method = RequestMethod.GET)
    ResponseData<FinanceAccountValidator> findById(@PathVariable String accountId) throws Exception;

    /**
     * 根据enterpriseId，查询企业所有财务账户
     *
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/finance/account/findEnterpriseFinanceAccounts/{enterpriseId}", method = RequestMethod.GET)
    ResponseData<List<FinanceAccountValidator>> findEnterpriseFinanceAccounts(@PathVariable String enterpriseId) throws Exception;

    /**
     * 根据企业enterpriseId，查询企业所有财务账户(包括子企业财务账户)
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/finance/account/findEnterpriseAndSubsidiaryFinanceAccount/{enterpriseId}", method = RequestMethod.GET)
    ResponseData<List<FinanceAccountValidator>> findEnterpriseAndSubsidiaryFinanceAccount(@PathVariable String enterpriseId) throws Exception;

    /**
     * 根据enterpriseId 汇总企业金额统计
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/finance/account/countEnterpriseSum/{enterpriseId}", method = RequestMethod.GET)
    ResponseData<Map<String, Object>> countEnterpriseSum(@PathVariable String enterpriseId) throws Exception;

    /**
     * 创建共享账户
     *
     * @param financeAccountValidator
     * @param op 操作类型 为add、edit
     * @return
     */
    @RequestMapping(value = "/finance/account/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FinanceAccountValidator financeAccountValidator,@PathVariable String op) throws Exception;

}
