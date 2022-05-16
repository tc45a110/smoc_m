package com.smoc.cloud.intellect.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialTypeValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectMaterialTypeFeign {

    /**
     * 查询素材分类
     * @return
     */
    @RequestMapping(value = "/intel/resource/type/getMaterialType/{parentId}", method = RequestMethod.GET)
    ResponseData<List<IntellectMaterialTypeValidator>> findIntellectMaterialTypeByParentIdAndStatus(@PathVariable String parentId) throws Exception;

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/intel/resource/type/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IntellectMaterialTypeValidator intellectMaterialTypeValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/intel/resource/type/findById/{id}", method = RequestMethod.GET)
    ResponseData<IntellectMaterialTypeValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 注销或启用
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/intel/resource/type/cancel/{id}/{status}", method = RequestMethod.GET)
    ResponseData cancel(@PathVariable String id, @PathVariable String status) throws Exception;
}
