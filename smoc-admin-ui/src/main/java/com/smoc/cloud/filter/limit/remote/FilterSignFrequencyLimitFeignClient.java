package com.smoc.cloud.filter.limit.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface FilterSignFrequencyLimitFeignClient {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/filter/limit/page", method = RequestMethod.POST)
    ResponseData<PageList<FilterSignFrequencyLimitValidator>> page(@RequestBody PageParams<FilterSignFrequencyLimitValidator> pageParams) throws Exception;

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/limit/findById/{id}", method = RequestMethod.GET)
    ResponseData<FilterSignFrequencyLimitValidator> findById(@PathVariable String id) throws  Exception;

    /**
     * 添加、修改
     *
     * @param filterSignFrequencyLimitValidator
     * @return
     */
    @RequestMapping(value = "/filter/limit/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/limit/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
