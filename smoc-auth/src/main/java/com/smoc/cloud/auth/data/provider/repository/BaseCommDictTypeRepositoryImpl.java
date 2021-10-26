package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.rowmapper.NodesRowMapper;
import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
public class BaseCommDictTypeRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<Nodes> getDictTree() {

        String  sql = "select ID, DICT_TYPE_NAME MODULE_NAME, DICT_TYPE_CODE MODULE_PATH, ICON MODULE_ICON,'' SYSTEM_ID,1 IS_OPERATING from base_comm_dict_type where ACTIVE =1 order by sort ASC ";
        List<Nodes> nodes = jdbcTemplate.query(sql, new NodesRowMapper());

        return nodes;

    }

    public List<Nodes> getDictTree(String projectName) {

        Object[] params = new Object[1];
        params[0] = projectName;
        String  sql = "select ID, DICT_TYPE_NAME MODULE_NAME, DICT_TYPE_CODE MODULE_PATH, ICON MODULE_ICON,'' SYSTEM_ID,1 IS_OPERATING from base_comm_dict_type where ACTIVE =1 and DICT_TYPE_SYSTEM like '%"+projectName+"%' order by sort ASC ";
        List<Nodes> nodes = jdbcTemplate.query(sql, new NodesRowMapper());

        return nodes;

    }
}
