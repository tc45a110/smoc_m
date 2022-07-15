package com.smoc.cloud.iot.system.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 充值记录
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface FinanceAccountRechargeFeignClient {

    /**
     * 分查询列表
     * @param pageParams
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    @RequestMapping(value = "finance/recharge/page/{flag}", method = RequestMethod.POST)
    ResponseData<PageList<FinanceAccountRechargeValidator>> page(@RequestBody PageParams<FinanceAccountRechargeValidator> pageParams, @PathVariable String flag) throws Exception;

    /**
     * 统计充值金额
     *
     * @param financeAccountRechargeValidator
     * @return
     */
    @RequestMapping(value = "finance/recharge/countRechargeSum", method = RequestMethod.POST)
    ResponseData<Map<String, Object>> countRechargeSum(@RequestBody FinanceAccountRechargeValidator financeAccountRechargeValidator) throws Exception;

}
