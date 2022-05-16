package com.smoc.cloud.intelligence.rowmapper;

import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntellectCallbackTemplateStatusReportRowMapper implements RowMapper<IntellectCallbackTemplateStatusReportValidator> {
    @Override
    public IntellectCallbackTemplateStatusReportValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        IntellectCallbackTemplateStatusReportValidator qo = new IntellectCallbackTemplateStatusReportValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setOrderNo(resultSet.getString("ORDER_NO"));
        qo.setTplId(resultSet.getString("TPL_ID"));
        qo.setBizId(resultSet.getString("BIZ_ID"));
        qo.setBizFlag(resultSet.getString("BIZ_FLAG"));
        qo.setTplState(resultSet.getInt("TPL_STATE"));
        qo.setAuditState(resultSet.getInt("AUDIT_STATE"));
        qo.setFactoryInfoList(resultSet.getString("FACTORY_INFO_LIST"));
        qo.setAuditDesc(resultSet.getString("AUDIT_DESC"));
        qo.setFactoryType(resultSet.getString("FACTORY_TYPE"));
        qo.setState(resultSet.getInt("STATE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
