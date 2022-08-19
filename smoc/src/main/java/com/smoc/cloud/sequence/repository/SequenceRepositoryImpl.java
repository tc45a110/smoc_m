package com.smoc.cloud.sequence.repository;

import com.smoc.cloud.common.BasePageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 序列管理 数据库操作
 */
@Slf4j
public class SequenceRepositoryImpl extends BasePageRepository {

    @Autowired
    public DataSource dataSource;

    @Resource
    public JdbcTemplate jdbcTemplate;

    public Integer findSequence(String seqName){
        String sql = "select system_nextval('"+seqName+"')";
        Integer seq = jdbcTemplate.queryForObject(sql,Integer.class);
        return seq;
    }

    /**
     * 创建新序列
     * @param sequenceName
     */
    public void createSequence(String sequenceName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            //保存报备导出记录
            StringBuffer insertSql = new StringBuffer("insert into system_sequence(SEQ_NAME,CURRENT_VAL,INCREMENT_VAL) ");
            insertSql.append("values(?,?,?)");
            stmt = conn.prepareStatement(insertSql.toString());
            stmt.setString(1, sequenceName);
            stmt.setInt(2, 1000);
            stmt.setInt(3, 1);
            stmt.execute();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.clearBatch();
                    stmt.clearParameters();
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
