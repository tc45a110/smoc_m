package com.smoc.cloud.reconciliation.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 业务账号对账
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface ReconciliationPeriodRemoteClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/reconciliation/account/page", method = RequestMethod.POST)
    ResponseData<PageList<ReconciliationPeriodValidator>> page(@RequestBody PageParams<ReconciliationPeriodValidator> pageParams) throws Exception;

    /**
     * 创建业务账号对账
     */
    @RequestMapping(value = "/reconciliation/account/buildAccountPeriod", method = RequestMethod.POST)
    ResponseData buildAccountPeriod(@RequestBody ReconciliationPeriodValidator validator) throws Exception;

    /**
     * 查询近5个月账期
     */
    @RequestMapping(value = "/reconciliation/account/findAccountPeriod", method = RequestMethod.GET)
    ResponseData<Map<String, String>> findAccountPeriod() throws Exception;

    /**
     * 删除账期
     */
    @RequestMapping(value = "/reconciliation/account/deleteAccountPeriod/{id}", method = RequestMethod.GET)
    ResponseData deleteAccountPeriod(@PathVariable String id) throws Exception;
}
