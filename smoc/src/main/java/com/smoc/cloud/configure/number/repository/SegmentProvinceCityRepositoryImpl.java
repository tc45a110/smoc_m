package com.smoc.cloud.configure.number.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.SystemNumberCarrierValidator;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.number.rowmapper.ConfigNumberInfoRowMapper;
import com.smoc.cloud.configure.number.rowmapper.SegmentProvinceCityRowMapper;
import com.smoc.cloud.configure.number.rowmapper.SystemNumberCarrierRowMapper;
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
public class SegmentProvinceCityRepositoryImpl extends BasePageRepository {


    public PageList<SystemSegmentProvinceCityValidator> page(PageParams<SystemSegmentProvinceCityValidator> pageParams) {

        //查询条件
        SystemSegmentProvinceCityValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.SEGMENT");
        sqlBuffer.append(", t.PROVINCE_CODE");
        sqlBuffer.append(", t.PROVINCE_NAME");
        sqlBuffer.append(", t.CITY_NAME");
        sqlBuffer.append("  from system_segment_province_city t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getProvinceCode())) {
            sqlBuffer.append(" and t.PROVINCE_CODE = ?");
            paramsList.add(qo.getProvinceCode().trim());
        }

        if (!StringUtils.isEmpty(qo.getSegment())) {
            sqlBuffer.append(" and t.SEGMENT like ?");
            paramsList.add("%"+qo.getSegment().trim()+"%");
        }

        sqlBuffer.append(" order by t.PROVINCE_CODE ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<SystemSegmentProvinceCityValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SegmentProvinceCityRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    public void batchSave(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator) {

        //每batchSize 分批执行一次
        int batchSize = 60000;
        Connection connection = null;
        PreparedStatement statement = null;
        List<ExcelModel> list= systemSegmentProvinceCityValidator.getExcelModelList();
        final String sql = "insert into system_segment_province_city(ID,SEGMENT,PROVINCE_CODE,PROVINCE_NAME,CITY_NAME) values(?,?,?,?,?) ";
        log.info("[省号码导入开始]数据：{}- 共{}条", System.currentTimeMillis(),list.size());
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            int i=0;
            for (ExcelModel entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, entry.getColumn1());
                statement.setString(3, systemSegmentProvinceCityValidator.getProvinceCode());
                statement.setString(4, systemSegmentProvinceCityValidator.getProvinceName());
                statement.setString(5, systemSegmentProvinceCityValidator.getCityName());
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

        log.info("[省号码入结束]数据：{}", System.currentTimeMillis());

    }

    /**
     * 查询省号码
     *
     * @return
     */
    public List<SystemSegmentProvinceCityValidator> findSegmentProvinceList(){
        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.SEGMENT,t.PROVINCE_CODE,t.PROVINCE_NAME,t.CITY_NAME " +
                " from system_segment_province_city t ");
        List<SystemSegmentProvinceCityValidator> result = this.jdbcTemplate.query(sqlBuffer.toString(), new SegmentProvinceCityRowMapper());

        return result;
    }

    /**
     * 查询号段
     *
     * @return
     */
    public List<SystemNumberCarrierValidator> findNumberCarrierList(){
        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.DICT_NAME,t.DICT_CODE " +
                " from smoc_oauth.base_comm_dict t where t.DICT_TYPE = 'carrierSegment' ");
        List<SystemNumberCarrierValidator> result = this.jdbcTemplate.query(sqlBuffer.toString(), new SystemNumberCarrierRowMapper());

        return result;
    }
}
