package com.smoc.cloud.parameter.controller;


import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendSystemParamValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendSystemParamValue;
import com.smoc.cloud.parameter.service.ParameterExtendSystemParamValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 系统扩展字段值 接口
 */
@Slf4j
@RestController
@RequestMapping("parameter/system")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ParameterExtendSystemParamValueController {

    @Autowired
    private ParameterExtendSystemParamValueService parameterExtendSystemParamValueService;

    @RequestMapping(value = "/findParameterValue/{businessId}", method = RequestMethod.GET)
    public ResponseData<List<ParameterExtendSystemParamValue>> findParameterValue(@PathVariable String businessId) {

        ResponseData<List<ParameterExtendSystemParamValue>> data = parameterExtendSystemParamValueService.findParameterExtendSystemParamValueByBusinessId(businessId);
        return data;
    }

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param businessId 业务id
     * @return
     */
    @RequestMapping(value = "/save/{businessId}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody List<ParameterExtendSystemParamValueValidator> list, @PathVariable String businessId) {

        //保存操作
        ResponseData data = parameterExtendSystemParamValueService.save(list, businessId);
        return data;
    }


    /**
     * 系统配置参数扩展 查看单个参数
     *
     * @param businessType 参数类型
     * @param businessId   业务ID
     * @param paramKey     参数Key
     * @return
     */
    @RequestMapping(value = "/findByBusinessTypeAndBusinessIdAndParamKey/{businessType}/{businessId}/{paramKey}", method = RequestMethod.GET)
    public ResponseData findByBusinessTypeAndBusinessIdAndParamKey(@PathVariable String businessType, @PathVariable String businessId, @PathVariable String paramKey) {

        //完成参数规则验证
        if (StringUtils.isEmpty(businessType) || StringUtils.isEmpty(businessId) || StringUtils.isEmpty(paramKey)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return parameterExtendSystemParamValueService.findByBusinessTypeAndBusinessIdAndParamKey(businessType,businessId,paramKey);
    }
}
