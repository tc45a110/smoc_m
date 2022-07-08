package com.smoc.cloud.configure.number.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 号段管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ConfigNumberInfoFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/number/page", method = RequestMethod.POST)
    PageList<ConfigNumberInfoValidator> page(@RequestBody PageParams<ConfigNumberInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/number/findById/{id}", method = RequestMethod.GET)
    ResponseData<ConfigNumberInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/number/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ConfigNumberInfoValidator configNumberInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/number/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id);

    /**
     * 批量导出
     * @param configNumberInfoValidator
     * @return
     */
    @RequestMapping(value = "/configure/number/batchSave", method = RequestMethod.POST)
    void batchSave(@RequestBody ConfigNumberInfoValidator configNumberInfoValidator);

    /**
     * 查询携号转网数据是否在redis库
     * @param numberCode
     * @return
     */
    @RequestMapping(value = "/configure/number/findRedis/{numberCode}", method = RequestMethod.GET)
    ResponseData<ConfigNumberInfoValidator> findRedis(@PathVariable String numberCode);

    @RequestMapping(value = "/configure/number/deleteRedis/{numberCode}", method = RequestMethod.GET)
    ResponseData deleteRedis(@PathVariable String numberCode);
}
