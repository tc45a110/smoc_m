package com.smoc.cloud.book.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 通讯录管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface BookFeignClient {

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/book/page", method = RequestMethod.POST)
    PageList<EnterpriseBookInfoValidator> page(@RequestBody PageParams<EnterpriseBookInfoValidator> pageParams);

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/book/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseBookInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param enterpriseBookInfoValidator
     * @return
     */
    @RequestMapping(value = "/book/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseBookInfoValidator enterpriseBookInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/book/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 批量保存
     * @param enterpriseBookInfoValidator
     */
    @RequestMapping(value = "/book/bathSave", method = RequestMethod.POST)
    void bathSave(@RequestBody EnterpriseBookInfoValidator enterpriseBookInfoValidator);

}
