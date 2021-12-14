package com.smoc.cloud.parameter.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.parameter.remote.ParameterExtendFiltersValueFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务扩展参数值服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParameterExtendFiltersValueService {

    @Autowired
    private ParameterExtendFiltersValueFeignClient parameterExtendFiltersValueFeignClient;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 查询列表
     *
     * @param businessId
     * @return
     */
    public ResponseData<List<ParameterExtendFiltersValueValidator>> findParameterValue(String businessId) {
        try {
            ResponseData<List<ParameterExtendFiltersValueValidator>> data = this.parameterExtendFiltersValueFeignClient.findParameterValue(businessId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param list       要保存的数据列表
     * @param businessId 业务id
     * @return
     */
    public ResponseData save(List<ParameterExtendFiltersValueValidator> list, String businessId,String createdBy,String businessType) {
        try {
            ResponseData data = this.parameterExtendFiltersValueFeignClient.save(list, businessId);

            //记录操作日志
            if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                systemUserLogService.logsAsync(businessType, businessId, createdBy, "paramExtend", "通道过滤参数配置", JSON.toJSONString(list));
            }
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
