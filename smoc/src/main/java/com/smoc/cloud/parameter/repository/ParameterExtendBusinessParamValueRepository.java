package com.smoc.cloud.parameter.repository;

import com.smoc.cloud.common.smoc.parameter.ParameterExtendBusinessParamValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendBusinessParamValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParameterExtendBusinessParamValueRepository extends JpaRepository<ParameterExtendBusinessParamValue, String> {

    /**
     * 批量保存业务扩展参数值
     *
     * @param list
     */
    void batchSave(List<ParameterExtendBusinessParamValueValidator> list);

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
    List<ParameterExtendBusinessParamValue> findParameterExtendBusinessParamValueByBusinessId(String businessId);
}