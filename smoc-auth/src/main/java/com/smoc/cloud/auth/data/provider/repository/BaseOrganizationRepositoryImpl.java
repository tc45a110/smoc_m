package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.rowmapper.OrgNodesRowMapper;
import com.smoc.cloud.common.auth.qo.Nodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织机构 数据库操作
 */
@Slf4j
public class BaseOrganizationRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;


    public List<Nodes> getOrgByParentId(String parentId) {

        if (StringUtils.isEmpty(parentId)) {
            return new ArrayList<>();
        }

        String sql = "select ID,ORG_NAME,PARENT_ID,IS_LEAF,ORG_TYPE,ORG_CODE from base_organization where ACTIVE = 1 and ORG_TYPE = 0 and  PARENT_ID=? order by SORT ASC";

        Object[] params = new Object[1];
        params[0] = parentId;
        List<Nodes> nodes = jdbcTemplate.query(sql,params, new OrgNodesRowMapper());

        return nodes;

    }


}
