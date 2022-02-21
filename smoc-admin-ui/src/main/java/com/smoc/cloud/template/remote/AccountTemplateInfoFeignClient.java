package com.smoc.cloud.template.remote;


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
 * 模板管理
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface AccountTemplateInfoFeignClient {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/account/template/page", method = RequestMethod.POST)
    ResponseData<PageList<AccountTemplateInfoValidator>> page(@RequestBody PageParams<AccountTemplateInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/template/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountTemplateInfoValidator> findById(@PathVariable String id) throws Exception;


    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/account/template/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountTemplateInfoValidator accountTemplateInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 注销模板
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/template/cancelTemplate/{id}", method = RequestMethod.GET)
    ResponseData cancelTemplate(@PathVariable String id) throws Exception;
}
