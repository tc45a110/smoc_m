package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigRepairRuleValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



/**
 * 补发规则管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ConfigRepairRuleFeignClient {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/repair/findById/{id}", method = RequestMethod.GET)
    ResponseData<ConfigRepairRuleValidator> findById(@PathVariable String id);


    /**
     * 保存补发通道
     * @param configRepairRuleValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/configure/repair/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ConfigRepairRuleValidator configRepairRuleValidator, @PathVariable String op);

}
