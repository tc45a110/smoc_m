package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class OrgNodesRowMapper implements RowMapper<Nodes> {

    @Override
    public Nodes mapRow(ResultSet resultSet, int i) throws SQLException {

        String id = resultSet.getString("ID");
        String orgCode = resultSet.getString("ORG_CODE");
        String text = resultSet.getString("ORG_NAME");
        String parentId = resultSet.getString("PARENT_ID");
        Integer isLeaf = resultSet.getInt("IS_LEAF");
        Integer orgType = resultSet.getInt("ORG_TYPE");
        Nodes node = new Nodes();
        node.setId(id);
        node.setOrgCode(orgCode);
        node.setText(text);
        node.setSystem(parentId);
        node.setHref(orgType+"");
        if (1 == isLeaf) {
            node.setLazyLoad(false);
        } else {
            node.setLazyLoad(true);
        }

        return node;
    }
}
