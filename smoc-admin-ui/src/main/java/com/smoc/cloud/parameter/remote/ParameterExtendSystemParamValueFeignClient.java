package com.smoc.cloud.parameter.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendSystemParamValueValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 系统扩展字段值，接口
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface ParameterExtendSystemParamValueFeignClient {


    /**
     * 查询列表
     *
     * @param businessId
     * @return
     */
    @RequestMapping(value = "/parameter/system/findParameterValue/{businessId}", method = RequestMethod.GET)
    ResponseData<List<ParameterExtendSystemParamValueValidator>> findParameterValue(@PathVariable String businessId) throws Exception;

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param list       要保存的数据列表
     * @param businessId 业务id
     * @return
     */
    @RequestMapping(value = "/parameter/system/save/{businessId}", method = RequestMethod.POST)
    ResponseData save(@RequestBody List<ParameterExtendSystemParamValueValidator> list, @PathVariable String businessId) throws Exception;
}
