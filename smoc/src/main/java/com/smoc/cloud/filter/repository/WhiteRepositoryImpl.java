package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.filter.rowmapper.ExcelModelRowMapper;
import com.smoc.cloud.filter.rowmapper.WhiteRowMapper;
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
 * 白名单管理 数据库操作
 */
@Slf4j
public class WhiteRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public PageList<FilterWhiteListValidator> page(PageParams<FilterWhiteListValidator> pageParams) {
        //组织查询条件
        FilterWhiteListValidator filterWhiteListValidator = pageParams.getParams();

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.GROUP_ID,t.NAME,t.MOBILE,t.STATUS,str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME,t.CREATED_BY " +
                " from filter_white_list t where t.GROUP_ID=? ");

        List<Object> paramsList = new ArrayList<Object>();
        if (null != filterWhiteListValidator) {
            paramsList.add( filterWhiteListValidator.getGroupId().trim());

            if (!StringUtils.isEmpty(filterWhiteListValidator.getName())) {
                sqlBuffer.append(" and t.NAME like ? ");
                paramsList.add(  "%" + filterWhiteListValidator.getName().trim() + "%");
            }
            if (!StringUtils.isEmpty(filterWhiteListValidator.getMobile())) {
                sqlBuffer.append(" and t.MOBILE like ? ");
                paramsList.add( "%" + filterWhiteListValidator.getMobile().trim()+ "%");
            }
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FilterWhiteListValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new WhiteRowMapper());
        return pageList;
    }

    public void bathSave(FilterWhiteListValidator filterWhiteListValidator) {

        //每batchSize 分批执行一次
        int batchSize = 60000;
        Connection connection = null;
        PreparedStatement statement = null;
        List<ExcelModel> list= filterWhiteListValidator.getExcelModelList();
        final String sql = "insert into filter_white_list(ID,ENTERPRISE_ID,GROUP_ID,NAME,MOBILE,IS_SYNC,STATUS,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,?,now()) ";
        log.info(sql);
        log.info("[系统白名单导入开始]数据：{}- 共{}条", System.currentTimeMillis(),list.size());
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            int i=0;
            for (ExcelModel entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, "SMOC");
                statement.setString(3, filterWhiteListValidator.getGroupId());
                statement.setString(4, entry.getColumn2());
                statement.setString(5, entry.getColumn1());
                statement.setString(6, filterWhiteListValidator.getIsSync());
                statement.setString(7, filterWhiteListValidator.getStatus());
                statement.setString(8, filterWhiteListValidator.getCreatedBy());
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

        log.info("[系统白名单导入结束]数据：{}", System.currentTimeMillis());

    }

    public  List<ExcelModel> excelModel(PageParams<FilterWhiteListValidator> pageParams) {
        //组织查询条件
        FilterWhiteListValidator filterWhiteListValidator = pageParams.getParams();

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.NAME,t.MOBILE from filter_white_list t where t.GROUP_ID=? ");

        List<Object> paramsList = new ArrayList<Object>();
        if (null != filterWhiteListValidator) {
            paramsList.add( filterWhiteListValidator.getGroupId().trim());
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ExcelModel> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ExcelModelRowMapper());
        return pageList.getList();
    }
}
