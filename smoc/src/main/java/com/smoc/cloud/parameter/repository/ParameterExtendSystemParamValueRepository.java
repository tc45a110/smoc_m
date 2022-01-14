package com.smoc.cloud.parameter.repository;

import com.smoc.cloud.common.smoc.parameter.ParameterExtendSystemParamValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendSystemParamValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParameterExtendSystemParamValueRepository extends JpaRepository<ParameterExtendSystemParamValue, String> {

    /**
     * 批量保存业务扩展参数值
     *
     * @param list
     */
    void batchSave(List<ParameterExtendSystemParamValueValidator> list);

    /**
     * 根据业务id，删除业务扩展参数值
     *
     * @param businessId
     */
    void deleteByBusinessId(String businessId);

    /**
     * 根据业务id 查询业务扩展字段值
     *
     * @param businessId
     * @return
     */
    List<ParameterExtendSystemParamValue> findParameterExtendSystemParamValueByBusinessId(String businessId);

    ParameterExtendSystemParamValue findByBusinessTypeAndBusinessIdAndParamKey(String businessType, String businessId, String paramKey);
}