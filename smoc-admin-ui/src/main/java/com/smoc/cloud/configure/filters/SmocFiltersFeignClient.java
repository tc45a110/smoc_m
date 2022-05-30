package com.smoc.cloud.configure.filters;

import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc-filters", path = "/smoc-filters")
public interface SmocFiltersFeignClient {

    /**
     * @return
     */
    @RequestMapping(value = "/full-filter/filters", method = RequestMethod.POST)
    ResponseData filter(@RequestBody RequestFullParams requestFullParams) throws Exception;
}
