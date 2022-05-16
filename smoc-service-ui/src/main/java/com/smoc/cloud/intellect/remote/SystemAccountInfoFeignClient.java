package com.smoc.cloud.intellect.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "smoc", path = "/smoc")
public interface SystemAccountInfoFeignClient {


    /**
     * 分查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/system/account/page", method = RequestMethod.POST)
    PageList<SystemAccountInfoValidator> page(@RequestBody PageParams<SystemAccountInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/system/account/findById/{id}", method = RequestMethod.GET)
    ResponseData<SystemAccountInfoValidator> findById(@PathVariable String id) throws Exception;


}
