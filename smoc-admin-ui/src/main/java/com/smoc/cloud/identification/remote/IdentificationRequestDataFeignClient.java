package com.smoc.cloud.identification.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 认证请求数据
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface IdentificationRequestDataFeignClient {

    /**
     * 分查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/identification/data/page", method = RequestMethod.POST)
    PageList<IdentificationRequestDataValidator> page(@RequestBody PageParams<IdentificationRequestDataValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/identification/data/findById/{id}", method = RequestMethod.GET)
    ResponseData<IdentificationRequestDataValidator> findById(@PathVariable String id) throws Exception;
}
