package com.smoc.cloud.filter.rowmapper;

import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class GroupNodesRowMapper implements RowMapper<Nodes> {

    @Override
    public Nodes mapRow(ResultSet resultSet, int i) throws SQLException {

        String id = resultSet.getString("ID");
        String text = resultSet.getString("GROUP_NAME");
        String parentId = resultSet.getString("PARENT_ID");
        String svcType = resultSet.getString("ENTERPRISE_ID");
        Integer isLeaf = resultSet.getInt("IS_LEAF");
        Nodes node = new Nodes();
        node.setId(id);
        node.setText(text);
        node.setSvcType(svcType);
        node.setSystem(parentId);
        if (1 == isLeaf) {
            node.setLazyLoad(false);
        } else {
            node.setLazyLoad(true);
        }
        //node.setIcon("mdi mdi-book-open-page-variant");
        return node;
    }
}
