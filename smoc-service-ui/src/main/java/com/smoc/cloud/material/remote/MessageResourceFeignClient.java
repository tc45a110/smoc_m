package com.smoc.cloud.material.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 资源远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageResourceFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/account/resource/page", method = RequestMethod.POST)
    ResponseData<PageList<AccountResourceInfoValidator>> page(@RequestBody PageParams<AccountResourceInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/resource/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountResourceInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/account/resource/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountResourceInfoValidator accountResourceInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/account/resource/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
