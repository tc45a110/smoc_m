package com.smoc.cloud.material.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 模板远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageTemplateFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/account/template/page", method = RequestMethod.POST)
    ResponseData<PageList<AccountTemplateInfoValidator>> page(@RequestBody PageParams<AccountTemplateInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/template/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountTemplateInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/account/template/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountTemplateInfoValidator accountTemplateInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/account/template/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
