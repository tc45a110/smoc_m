package com.smoc.cloud.auth.data.provider.repository;


import com.smoc.cloud.auth.data.provider.rowmapper.FlowApproveRowMapper;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

public class BaseFlowApproveRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<FlowApproveValidator> checkRecord(FlowApproveValidator flowApproveValidator){

        Object[] params = new Object[1];
        params[0] = flowApproveValidator.getApproveId();
        String  sql = "select u.REAL_NAME,u1.REAL_NAME AS CHECK_NAME,t.APPROVE_STATUS,DATE_FORMAT(t.SUBMIT_TIME, '%Y-%m-%d %H:%i:%s')SUBMIT_TIME,DATE_FORMAT(t.APPROVE_TIME, '%Y-%m-%d %H:%i:%s')APPROVE_TIME,t.APPROVE_ADVICE " +
                " from base_flow_approve t left join base_user_extends u on t.USER_ID=u.ID LEFT JOIN base_user_extends u1 ON t.USER_APPROVE_ID= u1.ID " +
                " where t.APPROVE_ID=? order by t.SUBMIT_TIME desc ";

        List<FlowApproveValidator> list = jdbcTemplate.query(sql,params, new FlowApproveRowMapper());
        return list;
    }
}
