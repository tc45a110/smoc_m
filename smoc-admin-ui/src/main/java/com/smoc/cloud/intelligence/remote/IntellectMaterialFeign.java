package com.smoc.cloud.intelligence.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectMaterialFeign {

    /**
     * 查询素材
     *
     * @return
     */
    @RequestMapping(value = "/intel/material/getMaterial/{materialTypeId}", method = RequestMethod.GET)
    ResponseData<List<IntellectMaterialValidator>> getMaterial(@PathVariable String materialTypeId) throws Exception;
}
