package com.smoc.cloud.configure.filters;


import com.smoc.cloud.common.filters.utils.InitializeFiltersData;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface InitializeFiltersFeign {

    /**
     * @return
     */
    @RequestMapping(value = "/initialize/filters/initialize", method = RequestMethod.POST)
    ResponseData initialize(@RequestBody InitializeFiltersData initializeFiltersData) throws Exception;
}
