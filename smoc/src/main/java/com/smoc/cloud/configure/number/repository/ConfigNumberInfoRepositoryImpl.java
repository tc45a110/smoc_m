package com.smoc.cloud.configure.number.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.parameter.model.ErrorCodeExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.number.rowmapper.ConfigNumberInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
@Slf4j
public class ConfigNumberInfoRepositoryImpl extends BasePageRepository {


    public PageList<ConfigNumberInfoValidator> page(PageParams<ConfigNumberInfoValidator> pageParams) {

        //查询条件
        ConfigNumberInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.NUMBER_CODE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.PROVINCE");
        sqlBuffer.append(", t.NUMBER_CODE_TYPE");
        sqlBuffer.append(", t.STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from config_number_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getNumberCode())) {
            sqlBuffer.append(" and t.NUMBER_CODE like ?");
            paramsList.add( "%"+qo.getNumberCode().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER = ?");
            paramsList.add(qo.getCarrier().trim());
        }

        if (!StringUtils.isEmpty(qo.getProvince())) {
            sqlBuffer.append(" and t.PROVINCE = ?");
            paramsList.add(qo.getProvince().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ConfigNumberInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ConfigNumberInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    public void batchSave(ConfigNumberInfoValidator configNumberInfoValidator) {

        //每batchSize 分批执行一次
        int batchSize = 60000;
        Connection connection = null;
        PreparedStatement statement = null;
        List<ExcelModel> list= configNumberInfoValidator.getExcelModelList();
        final String sql = "insert into config_number_info(ID,NUMBER_CODE,CARRIER,PROVINCE,NUMBER_CODE_TYPE,STATUS,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,now()) ";
        log.info("[号段导入开始]数据：{}- 共{}条", System.currentTimeMillis(),list.size());
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            int i=0;
            for (ExcelModel entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, entry.getColumn1());
                statement.setString(3, configNumberInfoValidator.getCarrier());
                statement.setString(4, configNumberInfoValidator.getProvince());
                statement.setString(5, configNumberInfoValidator.getNumberCodeType());
                statement.setString(6, configNumberInfoValidator.getStatus());
                statement.setString(7, configNumberInfoValidator.getCreatedBy());
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

        log.info("[号段导入结束]数据：{}", System.currentTimeMillis());

    }
}
