package com.smoc.cloud.parameter.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendFiltersValue;
import com.smoc.cloud.parameter.service.ParameterExtendFiltersValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 过滤扩展字段值 接口
 */
@Slf4j
@RestController
@RequestMapping("parameter/filter")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ParameterExtendFiltersValueController {

    @Autowired
    private ParameterExtendFiltersValueService parameterExtendFiltersValueService;

    @RequestMapping(value = "/findParameterValue/{businessId}", method = RequestMethod.GET)
    public ResponseData<List<ParameterExtendFiltersValue>> findParameterValue(@PathVariable String businessId) {

        ResponseData<List<ParameterExtendFiltersValue>> data = parameterExtendFiltersValueService.findParameterExtendFiltersValueByBusinessId(businessId);
        return data;
    }

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param businessId 业务id
     * @return
     */
    @RequestMapping(value = "/save/{businessId}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody List<ParameterExtendFiltersValueValidator> list, @PathVariable String businessId) {

        //保存操作
        ResponseData data = parameterExtendFiltersValueService.save(list, businessId);
        return data;
    }
}
