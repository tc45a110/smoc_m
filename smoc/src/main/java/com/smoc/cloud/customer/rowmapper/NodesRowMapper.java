package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NodesRowMapper implements RowMapper<Nodes> {
    @Override
    public Nodes mapRow(ResultSet resultSet, int i) throws SQLException {

        Nodes node = new Nodes();
        node.setId(resultSet.getString("ENTERPRISE_ID"));
        node.setText(resultSet.getString("ENTERPRISE_NAME"));
        node.setLazyLoad(true);
        node.setSvcType("root");
        node.setOrgCode("ENTERPRISE");
        return node;
    }
}
