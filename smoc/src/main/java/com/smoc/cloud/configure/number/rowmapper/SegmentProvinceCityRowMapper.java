package com.smoc.cloud.configure.number.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class SegmentProvinceCityRowMapper implements RowMapper<SystemSegmentProvinceCityValidator> {

    @Override
    public SystemSegmentProvinceCityValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemSegmentProvinceCityValidator qo = new SystemSegmentProvinceCityValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setSegment(resultSet.getString("SEGMENT"));
        qo.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
        qo.setProvinceName(resultSet.getString("PROVINCE_NAME"));
        qo.setCityName(resultSet.getString("CITY_NAME"));

        return qo;
    }
}
