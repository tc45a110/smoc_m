package com.smoc.cloud.configure.number.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.SystemNumberCarrierValidator;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class SystemNumberCarrierRowMapper implements RowMapper<SystemNumberCarrierValidator> {

    @Override
    public SystemNumberCarrierValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemNumberCarrierValidator qo = new SystemNumberCarrierValidator();
        String code = resultSet.getString("DICT_NAME");
        qo.setCarrier(code);
        qo.setNumberCode(resultSet.getString("DICT_CODE"));

        if("CMCC".equals(code)){
            qo.setCarrierName("移动");
        }
        if("UNIC".equals(code)){
            qo.setCarrierName("联通");
        }
        if("TELC".equals(code)){
            qo.setCarrierName("电信");
        }
        if("INTL".equals(code)){
            qo.setCarrierName("国际");
        }

        return qo;
    }
}
