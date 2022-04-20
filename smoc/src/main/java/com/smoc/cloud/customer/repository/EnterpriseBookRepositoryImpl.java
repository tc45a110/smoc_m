package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.BookExcelModel;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.rowmapper.EnterpriseBookInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 通讯录管理 数据库操作
 */
@Slf4j
public class EnterpriseBookRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public PageList<EnterpriseBookInfoValidator> page(PageParams<EnterpriseBookInfoValidator> pageParams) {
        //组织查询条件
        EnterpriseBookInfoValidator enterpriseBookInfoValidator = pageParams.getParams();

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.GROUP_ID,t.NAME,t.MOBILE,t.STATUS,str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME,t.CREATED_BY " +
                " from enterprise_book_info t where t.GROUP_ID=? ");

        List<Object> paramsList = new ArrayList<Object>();
        if (null != enterpriseBookInfoValidator) {
            paramsList.add( enterpriseBookInfoValidator.getGroupId().trim());

            if (!StringUtils.isEmpty(enterpriseBookInfoValidator.getName())) {
                sqlBuffer.append(" and t.NAME like ? ");
                paramsList.add(  "%" + enterpriseBookInfoValidator.getName().trim() + "%");
            }
            if (!StringUtils.isEmpty(enterpriseBookInfoValidator.getMobile())) {
                sqlBuffer.append(" and t.MOBILE like ? ");
                paramsList.add( "%" + enterpriseBookInfoValidator.getMobile().trim()+ "%");
            }
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<EnterpriseBookInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new EnterpriseBookInfoRowMapper());
        return pageList;
    }


    public void bathSave(EnterpriseBookInfoValidator enterpriseBookInfoValidator) {

        //每batchSize 分批执行一次
        int batchSize = 60000;
        Connection connection = null;
        PreparedStatement statement = null;
        List<BookExcelModel> list= enterpriseBookInfoValidator.getExcelModelList();
        final String sql = "insert into enterprise_book_info(ID,ENTERPRISE_ID,GROUP_ID,NAME,MOBILE,EMAIL,IS_SYNC,STATUS,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,?,?,now()) ";
        log.info(sql);
        log.info("[通讯录导入开始]数据：{}- 共{}条", System.currentTimeMillis(),list.size());
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            int i=0;
            for (BookExcelModel entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, enterpriseBookInfoValidator.getEnterpriseId());
                statement.setString(3, enterpriseBookInfoValidator.getGroupId());
                statement.setString(4, entry.getName());
                statement.setString(5, entry.getMobile());
                statement.setString(6, entry.getEmail());
                statement.setString(7, enterpriseBookInfoValidator.getIsSync());
                statement.setString(8, enterpriseBookInfoValidator.getStatus());
                statement.setString(9, enterpriseBookInfoValidator.getCreatedBy());
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

        log.info("[通讯录导入结束]数据：{}", System.currentTimeMillis());

    }

    public List<String> findByGroupIdAndStatus(String groupId, String status) {

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.MOBILE " +
                " from enterprise_book_info t where t.GROUP_ID='"+groupId+"' and t.status='"+status+"'" );

        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        List<String> list = this.queryForObjectList(sqlBuffer.toString(), null, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        return list;
    }

}
