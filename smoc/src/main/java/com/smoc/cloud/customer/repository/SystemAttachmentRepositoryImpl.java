package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.filter.rowmapper.KeywordsRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 关键词管理 数据库操作
 */
@Slf4j
public class SystemAttachmentRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;


    public void batchSave(List<SystemAttachmentValidator> list) {

        final String sql = "insert into system_attachment_info(ID,MOUDLE_ID,MOUDLE_INENTIFICATION,ATTACHMENT_NAME,ATTACHMENT_URI,DOC_TYPE,DOC_SIZE,ATTACHMENT_STATUS,CREATED_BY,CREATED_TIME) " +
                "values(?,?,?,?,?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SystemAttachmentValidator info = list.get(i);
                ps.setString(1, info.getId());
                ps.setString(2, info.getMoudleId());
                ps.setString(3, info.getMoudleInentification());
                ps.setString(4, info.getAttachmentName());
                ps.setString(5, info.getAttachmentUri());
                ps.setString(6, info.getDocType());
                ps.setBigDecimal(7, info.getDocSize());
                ps.setString(8, info.getAttachmentStatus());
                ps.setString(9, info.getCreatedBy());
            }

        });

    }

}
