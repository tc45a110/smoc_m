package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.filter.rowmapper.GroupNodesRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 群组管理 数据库操作
 */
@Slf4j
public class GroupRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<Nodes> getGroupByParentId(String enterprise, String parentId) {

        if (StringUtils.isEmpty(parentId)) {
            return new ArrayList<>();
        }

        String sql = "select ID,GROUP_NAME,PARENT_ID,IS_LEAF,ENTERPRISE_ID from filter_group_list where ENTERPRISE_ID=? and PARENT_ID=? order by CREATED_TIME DESC";

        Object[] params = new Object[2];
        params[0] = enterprise;
        params[1] = parentId;
        List<Nodes> nodes = jdbcTemplate.query(sql,params, new GroupNodesRowMapper());
        return nodes;

    }

}
