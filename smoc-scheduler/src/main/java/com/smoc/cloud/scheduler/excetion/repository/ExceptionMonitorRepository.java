package com.smoc.cloud.scheduler.excetion.repository;

import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 异常记录
 */
@Slf4j
@Component
public class ExceptionMonitorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //处理数据
    @Transactional
    public void exceptionLog(String module,String exception){

        StringBuffer sql = new StringBuffer("insert into smoc.system_exception_monitor(ID,MODULE,EXCEPTION,STATUS,CREATED_TIME) ");
        sql.append(" VALUES(?,?,?,'0',now())");
        log.info("[保存异常]:{}",sql.toString());
        Object[] params = new Object[3];
        params[0] = UUID.uuid32();
        params[1] = module;
        params[2] = exception;
        jdbcTemplate.update(sql.toString(),params);
    }
}
