package com.smoc.cloud.finance.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 财务账号
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface FinanceAccountFeignClient {

    /**
     * 分查询列表
     * @param pageParams
     * @param flag 1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号
     * @return
     */
    @RequestMapping(value = "/finance/account/page/{flag}", method = RequestMethod.POST)
    ResponseData<PageList<FinanceAccountValidator>> page(@RequestBody PageParams<FinanceAccountValidator> pageParams, @PathVariable String flag) throws Exception;

    /**
     * 分查询列表
     * @param pageParams
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    @RequestMapping(value = "finance/recharge/page/{flag}", method = RequestMethod.POST)
    ResponseData<PageList<FinanceAccountRechargeValidator>> rechargePage(@RequestBody PageParams<FinanceAccountRechargeValidator> pageParams, @PathVariable String flag) throws Exception;

}
