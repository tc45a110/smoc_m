package com.smoc.cloud.parameter.errorcode.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.common.smoc.parameter.model.ErrorCodeExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.filter.rowmapper.BlackRowMapper;
import com.smoc.cloud.filter.rowmapper.ExcelModelRowMapper;
import com.smoc.cloud.parameter.errorcode.rowmapper.SystemErrorCodeRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 错误码管理 数据库操作
 */
@Slf4j
public class SystemErrorCodeRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public PageList<SystemErrorCodeValidator> page(PageParams<SystemErrorCodeValidator> pageParams){
        //组织查询条件
        SystemErrorCodeValidator qo = pageParams.getParams();

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.CODE_TYPE,t.ERROR_CODE,t.ERROR_CONTENT,t.HANDLE_REMARK,t.STATUS" +
                " from system_error_code t where t.CODE_TYPE=? ");

        List<Object> paramsList = new ArrayList<Object>();
        if (null != qo) {
            paramsList.add( qo.getCodeType().trim());

            if (!StringUtils.isEmpty(qo.getErrorCode())) {
                sqlBuffer.append(" and t.ERROR_CODE like ? ");
                paramsList.add( "%" + qo.getErrorCode().trim()+ "%");
            }
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<SystemErrorCodeValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SystemErrorCodeRowMapper());
        return pageList;
    }

    public void bathSave(SystemErrorCodeValidator systemErrorCodeValidator) {

        //每batchSize 分批执行一次
        int batchSize = 60000;
        Connection connection = null;
        PreparedStatement statement = null;
        List<ErrorCodeExcelModel> list= systemErrorCodeValidator.getExcelModelList();
        final String sql = "insert into system_error_code(ID,CODE_TYPE,ERROR_CODE,ERROR_CONTENT,HANDLE_REMARK,STATUS,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,now()) ";
        log.info(sql);
        log.info("[错误码导入开始]数据：{}- 共{}条", System.currentTimeMillis(),list.size());
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            int i=0;
            for (ErrorCodeExcelModel entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, systemErrorCodeValidator.getCodeType());
                statement.setString(3, entry.getErrorCode());
                statement.setString(4, entry.getErrorContent());
                statement.setString(5, entry.getHandleRemark());
                statement.setString(6, systemErrorCodeValidator.getStatus());
                statement.setString(7, systemErrorCodeValidator.getCreatedBy());
                statement.addBatch();
                i++;
                if (i % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (null != statement) {
                    statement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        log.info("[错误码导入结束]数据：{}", System.currentTimeMillis());

    }

}
