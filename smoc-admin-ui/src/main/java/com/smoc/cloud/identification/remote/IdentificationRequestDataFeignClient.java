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
     * 根据orderNo获取信息
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/identification/data/findByOrderNo/{orderNo}", method = RequestMethod.GET)
    ResponseData<IdentificationRequestDataValidator> findByOrderNo(@PathVariable String orderNo) throws Exception;
}
