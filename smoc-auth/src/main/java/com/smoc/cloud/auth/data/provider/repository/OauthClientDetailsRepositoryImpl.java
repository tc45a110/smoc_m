package com.smoc.cloud.auth.data.provider.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * client repository 实现类
 * 2019/5/5 12:39
 **/
public class OauthClientDetailsRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public void resetClientSecret(String id, String secret) {

        String sql = "update oauth_client_details set client_secret=? where client_id=?";

        Object[] params = new Object[2];
        params[0] = secret;
        params[1] = id;
        jdbcTemplate.update(sql, params);

    }
}
