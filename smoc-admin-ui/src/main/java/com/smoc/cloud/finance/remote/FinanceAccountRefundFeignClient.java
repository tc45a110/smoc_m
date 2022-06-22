package com.smoc.cloud.finance.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 退款记录
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface FinanceAccountRefundFeignClient {

    /**
     * 分查询列表
     * @param pageParams
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    @RequestMapping(value = "finance/refund/page/{flag}", method = RequestMethod.POST)
    ResponseData<PageList<FinanceAccountRefundValidator>> page(@RequestBody PageParams<FinanceAccountRefundValidator> pageParams, @PathVariable String flag) throws Exception;

}
