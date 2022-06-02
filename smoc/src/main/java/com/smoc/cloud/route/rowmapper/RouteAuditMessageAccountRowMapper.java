package com.smoc.cloud.route.rowmapper;
import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import com.smoc.cloud.common.smoc.route.qo.RouteAuditMessageAccountQo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 短信上行
 */
public class RouteAuditMessageAccountRowMapper implements RowMapper<RouteAuditMessageAccountQo> {
    @Override
    public RouteAuditMessageAccountQo mapRow(ResultSet resultSet, int i) throws SQLException {

        RouteAuditMessageAccountQo qo = new RouteAuditMessageAccountQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setTotalNum(resultSet.getInt("TOTAL_NUM"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));

        return qo;
    }
}
