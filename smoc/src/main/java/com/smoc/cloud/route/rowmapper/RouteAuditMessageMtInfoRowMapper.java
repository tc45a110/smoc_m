package com.smoc.cloud.route.rowmapper;
import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 短信上行
 */
public class RouteAuditMessageMtInfoRowMapper implements RowMapper<RouteAuditMessageMtInfoValidator> {
    @Override
    public RouteAuditMessageMtInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        RouteAuditMessageMtInfoValidator qo = new RouteAuditMessageMtInfoValidator();
        qo.setId(resultSet.getLong("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setAccountSubmitTime(resultSet.getString("ACCOUNT_SUBMIT_TIME"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setReason(resultSet.getString("REASON"));
        qo.setAuditFlag(resultSet.getInt("AUDIT_FLAG"));
        qo.setMessageMd5(resultSet.getString("MESSAGE_MD5"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));

        //解析json拿到账号名称
        String messageJson = resultSet.getString("MESSAGE_JSON");
        if(!StringUtils.isEmpty(messageJson)){
            Map maps = (Map) JSON.parse(messageJson);
            if(!StringUtils.isEmpty(maps) && !StringUtils.isEmpty(maps.get("accountName"))){
                qo.setAccountName(""+maps.get("accountName"));
            }
        }
        return qo;
    }
}
