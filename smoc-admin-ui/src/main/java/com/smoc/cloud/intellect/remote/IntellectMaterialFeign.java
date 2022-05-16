package com.smoc.cloud.intellect.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/intel/material/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IntellectMaterialValidator intellectMaterialValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/intel/material/findById/{id}", method = RequestMethod.GET)
    ResponseData<IntellectMaterialValidator> findById(@PathVariable String id) throws Exception;
}
