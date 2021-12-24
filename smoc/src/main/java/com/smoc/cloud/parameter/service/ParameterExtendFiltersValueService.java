package com.smoc.cloud.parameter.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendFiltersValue;
import com.smoc.cloud.parameter.repository.ParameterExtendFiltersValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务扩展字段值
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParameterExtendFiltersValueService {

    @Resource
    private ParameterExtendFiltersValueRepository parameterExtendFiltersValueRepository;

    /**
     * 根据业务id 查询业务扩展字段值
     *
     * @param businessId
     * @return
     */
    public ResponseData<List<ParameterExtendFiltersValue>> findParameterExtendFiltersValueByBusinessId(String businessId) {

        List<ParameterExtendFiltersValue> data = parameterExtendFiltersValueRepository.findParameterExtendFiltersValueByBusinessId(businessId);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param list       要保存的数据列表
     * @param businessId 业务id
     * @return
     */
    @Transactional
    public ResponseData save(List<ParameterExtendFiltersValueValidator> list, String businessId) {

        parameterExtendFiltersValueRepository.deleteByBusinessId(businessId);
        parameterExtendFiltersValueRepository.batchSave(list);

        //记录日志
        log.info("[过滤扩展字段][批量保存]数据:{}", JSON.toJSONString(list));
        return ResponseDataUtil.buildSuccess();
    }
}
