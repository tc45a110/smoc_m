package com.smoc.cloud.parameter.repository;

import com.smoc.cloud.common.smoc.parameter.ParameterExtendBusinessParamValueValidator;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ParameterExtendBusinessParamValueRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 批量保存业务扩展参数值
     *
     * @param list
     */
    public void batchSave(List<ParameterExtendBusinessParamValueValidator> list) {
        final String sql = "insert into parameter_extend_business_param_value(ID,BUSINESS_TYPE,BUSINESS_ID,PARAM_NAME,PARAM_KEY,PARAM_VALUE,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,?) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ParameterExtendBusinessParamValueValidator parameterExtendBusinessParamValueValidator = list.get(i);
                ps.setString(1, parameterExtendBusinessParamValueValidator.getId());
                ps.setString(2, parameterExtendBusinessParamValueValidator.getBusinessType());
                ps.setString(3, parameterExtendBusinessParamValueValidator.getBusinessId());
                ps.setString(4, parameterExtendBusinessParamValueValidator.getParamName());
                ps.setString(5, parameterExtendBusinessParamValueValidator.getParamKey());
                ps.setString(6, parameterExtendBusinessParamValueValidator.getParamValue());
                ps.setString(7, parameterExtendBusinessParamValueValidator.getCreatedBy());
                ps.setDate(8, new Date(parameterExtendBusinessParamValueValidator.getCreatedTime().getTime()));
            }

        });
    }

    /**
     * 根据业务id，删除业务扩展参数值
     *
     * @param businessId
     */
    public void deleteByBusinessId(String businessId) {
        String sql = "delete from parameter_extend_business_param_value where BUSINESS_ID = ?";

        Object[] params = new Object[1];
        params[0] = businessId;
        jdbcTemplate.update(sql, params);

    }
}
