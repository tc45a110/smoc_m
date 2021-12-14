package com.smoc.cloud.parameter.repository;

import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendFiltersValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *业务扩展字段值
 */
public interface ParameterExtendFiltersValueRepository extends JpaRepository<ParameterExtendFiltersValue, String> {

    /**
     * 批量保存业务扩展参数值
     * @param list
     */
    void batchSave(List<ParameterExtendFiltersValueValidator> list);

    /**
     * 根据业务id，删除业务扩展参数值
     * @param businessId
     */
    void deleteByBusinessId(String businessId);

    /**
     * 根据业务id 查询业务扩展字段值
     * @param businessId
     * @return
     */
    List<ParameterExtendFiltersValue> findParameterExtendFiltersValueByBusinessId(String businessId);
}