package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class FlowApproveRowMapper implements RowMapper<FlowApproveValidator> {

    @Override
    public FlowApproveValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FlowApproveValidator data = new FlowApproveValidator();
        data.setUserName(resultSet.getString("REAL_NAME"));
        data.setCheckName(resultSet.getString("CHECK_NAME"));
        data.setApproveStatus(resultSet.getInt("APPROVE_STATUS"));
        data.setSubmitTimeStr(resultSet.getString("SUBMIT_TIME"));
        String appTime = resultSet.getString("APPROVE_TIME");
        String remark = resultSet.getString("APPROVE_ADVICE");
        if(!StringUtils.isEmpty(appTime)){
            data.setApproveTimeStr(appTime);
        }else{
            data.setApproveTimeStr("");
        }
        if(!StringUtils.isEmpty(remark)){
            data.setApproveAdvice(remark);
        }else{
            data.setApproveAdvice("");
        }

        return data;
    }
}
