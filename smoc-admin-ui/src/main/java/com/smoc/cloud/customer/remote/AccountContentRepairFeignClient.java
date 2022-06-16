package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigContentRepairRuleValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 业务账号内容失败补发远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface AccountContentRepairFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/content/repair/page", method = RequestMethod.POST)
    PageList<ConfigContentRepairRuleValidator> page(@RequestBody PageParams<ConfigContentRepairRuleValidator> pageParams)  throws Exception;

    /**
     * 业务账号列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/configure/content/repair/accountList", method = RequestMethod.POST)
    PageList<AccountContentRepairQo> accountList(@RequestBody PageParams<AccountContentRepairQo> params);

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/content/repair/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ConfigContentRepairRuleValidator configContentRepairRuleValidator, @PathVariable String op);

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/content/repair/findById/{id}", method = RequestMethod.GET)
    ResponseData<ConfigContentRepairRuleValidator> findById(@PathVariable String id);

    /**
     * 根据ID 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/content/repair/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id);
}
