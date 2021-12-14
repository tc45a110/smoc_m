package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户操作日志
 */
@FeignClient(name = "smoc-auth", path = "/auth")
public interface SystemUserLogFeignClient {


    /**
     * 分页查询日志信息
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/user/logs/page", method = RequestMethod.POST)
    ResponseData<PageList<SystemUserLogValidator>> page(@RequestBody PageParams<SystemUserLogValidator> pageParams);

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/user/logs/findById/{id}", method = RequestMethod.GET)
    ResponseData<SystemUserLogValidator> findById(@PathVariable(value = "id") String id) throws Exception;

    /**
     * 保存信息
     */
    @RequestMapping(value = "/user/logs/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody SystemUserLogValidator systemUserLogValidator) throws Exception;
}
