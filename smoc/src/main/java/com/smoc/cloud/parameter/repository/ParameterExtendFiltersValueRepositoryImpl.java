package com.smoc.cloud.parameter.repository;

import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.parameter.entity.ParameterExtendFiltersValue;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ParameterExtendFiltersValueRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 批量保存业务扩展参数值
     *
     * @param list
     */
    public void batchSave(List<ParameterExtendFiltersValueValidator> list) {
        final String sql = "insert into parameter_extend_filters_value(ID,BUSINESS_TYPE,BUSINESS_ID,PARAM_NAME,PARAM_KEY,PARAM_VALUE,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ParameterExtendFiltersValueValidator parameterExtendFiltersValueValidator = list.get(i);
                ps.setString(1, parameterExtendFiltersValueValidator.getId());
                ps.setString(2, parameterExtendFiltersValueValidator.getBusinessType());
                ps.setString(3, parameterExtendFiltersValueValidator.getBusinessId());
                ps.setString(4, parameterExtendFiltersValueValidator.getParamName());
                ps.setString(5, parameterExtendFiltersValueValidator.getParamKey());
                ps.setString(6, parameterExtendFiltersValueValidator.getParamValue());
                ps.setString(7, parameterExtendFiltersValueValidator.getCreatedBy());
            }

        });
    }

    /**
     * 根据业务id，删除业务扩展参数值
     *
     * @param businessId
     */
    public void deleteByBusinessId(String businessId) {
        String sql = "delete from parameter_extend_filters_value where BUSINESS_ID = ?";

        Object[] params = new Object[1];
        params[0] = businessId;
        jdbcTemplate.update(sql, params);

    }

}
