package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class NodesRowMapper implements RowMapper<Nodes> {

    @Override
    public Nodes mapRow(ResultSet resultSet, int i) throws SQLException {

        String id = resultSet.getString("ID");
        String text = resultSet.getString("MODULE_NAME");
        String url = resultSet.getString("MODULE_PATH");
        String icon = resultSet.getString("MODULE_ICON");
        String system = resultSet.getString("SYSTEM_ID");
        Integer isOperating = resultSet.getInt("IS_OPERATING");
        Nodes node = new Nodes();
        node.setId(id);
        node.setText(text);
        node.setHref(url);
        node.setIcon(icon);
        node.setSystem(system);
        if (1 == isOperating) {
            node.setLazyLoad(false);
        } else {
            node.setLazyLoad(true);
        }

        return node;
    }
}
