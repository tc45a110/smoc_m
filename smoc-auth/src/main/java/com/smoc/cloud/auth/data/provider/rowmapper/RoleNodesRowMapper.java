package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.RoleNodes;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class RoleNodesRowMapper implements RowMapper<RoleNodes> {

    @Override
    public RoleNodes mapRow(ResultSet resultSet, int i) throws SQLException {

        String id = resultSet.getString("ID");
        String text = resultSet.getString("MODULE_NAME");
        String icon = resultSet.getString("MODULE_ICON");
        String systemId = resultSet.getString("SYSTEM_ID");
        String checked = resultSet.getString("CHECKED");
        boolean lazyLoad = resultSet.getBoolean("LAZY_LOAD");
        Integer isOperating = resultSet.getInt("IS_OPERATING");

        RoleNodes node = new RoleNodes();
        node.setId(id);
        node.setText(text);
        node.setIcon(icon);
        node.setSystemId(systemId);

        if(!StringUtils.isEmpty(checked)){
            Map<String, Object> stateMap = new HashMap<String, Object>();
            stateMap.put("checked", true);
            node.setState(stateMap);

        }
        node.setLazyLoad(lazyLoad);
        node.setIsOperating(isOperating);


        return node;
    }
}
