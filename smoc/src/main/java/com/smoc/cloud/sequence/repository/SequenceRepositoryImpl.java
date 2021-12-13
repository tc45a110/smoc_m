package com.smoc.cloud.sequence.repository;

import com.smoc.cloud.common.BasePageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * 序列管理 数据库操作
 */
@Slf4j
public class SequenceRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public Integer findSequence(String seqName){
        String sql = "select smoc_nextval('"+seqName+"')";
        Integer seq = jdbcTemplate.queryForObject(sql,Integer.class);
        return seq;
    }

}
