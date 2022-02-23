package com.smoc.cloud.filter.rowmapper;

import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class KeywordsRowMapper implements RowMapper<FilterKeyWordsInfoValidator> {

    @Override
    public FilterKeyWordsInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FilterKeyWordsInfoValidator data = new FilterKeyWordsInfoValidator();
        data.setId(resultSet.getString("ID"));
        data.setKeyWordsBusinessType(resultSet.getString("KEY_WORDS_BUSINESS_TYPE"));
        data.setBusinessId(resultSet.getString("BUSINESS_ID"));
        data.setKeyWordsType(resultSet.getString("KEY_WORDS_TYPE"));
        data.setKeyWords(resultSet.getString("KEY_WORDS"));
        data.setKeyDesc(resultSet.getString("KEY_DESC"));
        data.setCreatedTimeStr(resultSet.getString("CREATED_TIME"));
        data.setCreatedBy(resultSet.getString("CREATED_BY"));
        return data;
    }
}
