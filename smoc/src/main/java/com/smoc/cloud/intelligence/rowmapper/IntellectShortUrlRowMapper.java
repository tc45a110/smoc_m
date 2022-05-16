package com.smoc.cloud.intelligence.rowmapper;

import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class IntellectShortUrlRowMapper implements RowMapper<IntellectShortUrlValidator> {
    @Override
    public IntellectShortUrlValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        IntellectShortUrlValidator qo = new IntellectShortUrlValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setTplId(resultSet.getString("TPL_ID"));
        qo.setCustFlag(resultSet.getString("CUST_FLAG"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setCustId(resultSet.getString("CUST_ID"));
        qo.setAimUrl(resultSet.getString("AIM_URL"));
        qo.setAimCode(resultSet.getString("AIM_CODE"));
        qo.setExpireTimes(resultSet.getInt("EXPIRE_TIMES"));
        qo.setFactories(resultSet.getString("FACTORIES"));
        qo.setShowTimes(resultSet.getInt("SHOW_TIMES"));
        qo.setTplName(resultSet.getString("TPL_NAME"));
        qo.setDyncParams(resultSet.getString("DYNC_PARAMS"));
        qo.setCustomUrl(resultSet.getString("CUSTOM_URL"));
        qo.setExtData(resultSet.getString("EXT_DATA"));
        qo.setResultCode(resultSet.getString("RESULT_CODE"));
        qo.setErrorMessage(resultSet.getString("ERROR_MESSAGE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        Date currentDate = DateTimeUtils.dateAddDays(DateTimeUtils.getNowDateTime(),-(qo.getExpireTimes()));
        String date = DateTimeUtils.getDateTimeFormat(currentDate);
        Boolean status = qo.getCreatedTime().compareTo(date)>=0;
        if(status){
            qo.setStatus("1");
        }else {
            qo.setStatus("0");
        }
        return qo;
    }
}
