package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 账号财务管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface AccountFinanceFeignClient {

    /**
     * 根据运营商和账号ID查询运营商单价
     * @param accountFinanceInfoValidator
     * @return
     */
    @RequestMapping(value = "/account/finance/editCarrierPrice", method = RequestMethod.POST)
    ResponseData<Map<String, BigDecimal>> editCarrierPrice(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator) throws Exception;

    /**
     * 查询账号配置的运营商价格
     * @param accountFinanceInfoValidator
     * @return
     */
    @RequestMapping(value = "/account/finance/findByAccountId", method = RequestMethod.POST)
    ResponseData<List<AccountFinanceInfoValidator>> findByAccountId(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator) throws Exception;

    /**
     * 保存、修改数据
     *  op 是类型 表示了保存或修改
     * @param accountFinanceInfoValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/account/finance/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator, @PathVariable String op) throws Exception;
}
