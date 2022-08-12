package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseSignCertifyFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sign/certify/page", method = RequestMethod.POST)
    ResponseData<PageList<EnterpriseSignCertifyValidator>> page(@RequestBody PageParams<EnterpriseSignCertifyValidator> pageParams)  throws Exception;

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/sign/certify/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseSignCertifyValidator enterpriseSignCertifyValidator, @PathVariable String op)  throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sign/certify/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseSignCertifyValidator> findById(@PathVariable String id)  throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sign/certify/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id)   throws Exception;
}
