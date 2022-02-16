package com.smoc.cloud.finance.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 共享财务账户明细记录
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface FinanceAccountShareDetailFeignClient {

    /**
     * 分查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "finance/account/share/record/page", method = RequestMethod.POST)
    ResponseData<PageList<FinanceAccountShareDetailValidator>> page(@RequestBody PageParams<FinanceAccountShareDetailValidator> pageParams) throws Exception;

}
