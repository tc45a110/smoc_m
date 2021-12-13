package com.smoc.cloud.configure.codenumber.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.CodeNumberInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class CodeNumberInfoRowMapper implements RowMapper<CodeNumberInfoValidator> {

    @Override
    public CodeNumberInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        CodeNumberInfoValidator qo = new CodeNumberInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setSrcId(resultSet.getString("SRC_ID"));
        qo.setMaxComplaintRate(resultSet.getBigDecimal("MAX_COMPLAINT_RATE"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setSrcIdPrice(resultSet.getBigDecimal("SRC_ID_PRICE"));
        qo.setUseType(resultSet.getString("USE_TYPE"));
        qo.setSrcIdSource(resultSet.getString("SRC_ID_SOURCE"));
        qo.setAccessPoint(resultSet.getString("ACCESS_POINT"));
        qo.setMinConsumeDemand(resultSet.getInt("MIN_CONSUME_DEMAND"));
        qo.setCaSrcId(resultSet.getString("CA_SRC_ID"));
        qo.setProvince(resultSet.getString("PROVINCE"));
        qo.setCity(resultSet.getString("CITY"));
        qo.setSrcIdStatus(resultSet.getString("SRC_ID_STATUS"));
        qo.setSrcIdRemark(resultSet.getString("SRC_ID_REMARK"));
        qo.setPriceEffectiveDate(resultSet.getString("PRICE_EFFECTIVE_DATE"));
        qo.setAccessTime(resultSet.getString("ACCESS_TIME"));
        qo.setMinConsumeEffectiveDate(resultSet.getString("MIN_CONSUME_EFFECTIVE_DATE"));
        qo.setSrcIdEffectiveDate(resultSet.getString("SRC_ID_EFFECTIVE_DATE"));
        return qo;
    }
}
