package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.validator.DictTypeValidator;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 业务扩展参数
 */
@FeignClient(name = "smoc-auth", path = "/auth")
public interface SystemExtendBusinessParameterFeignClient {

    /**
     * 查询列表
     */
    @RequestMapping(value = "/param/list/{businessType}", method = RequestMethod.GET)
    ResponseData<List<SystemExtendBusinessParamValidator>> list(@PathVariable String businessType) throws Exception;

    /**
     * 查询列表
     */
    @RequestMapping(value = "/param/findParamByBusinessTypeAndParamKey", method = RequestMethod.GET)
    ResponseData<SystemExtendBusinessParamValidator> findParamByBusinessTypeAndParamKey(@RequestBody SystemExtendBusinessParamValidator systemExtendBusinessParamValidator);
}
