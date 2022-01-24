package com.smoc.cloud.identification.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 认账账号管理
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface IdentificationAccountInfoFeignClient {

    /**
     * 分查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/identification/account/page", method = RequestMethod.POST)
    PageList<IdentificationAccountInfoValidator> page(@RequestBody PageParams<IdentificationAccountInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/identification/account/findById/{id}", method = RequestMethod.GET)
    ResponseData<IdentificationAccountInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/identification/account/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IdentificationAccountInfoValidator identificationAccountInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/identification/account/logout/{id}", method = RequestMethod.GET)
    ResponseData logout(@PathVariable String id) throws Exception;

}
