package com.smoc.cloud.filter.rowmapper;

import com.smoc.cloud.filter.entity.KeyWordsMaskKeyWords;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyWordRowMapper implements RowMapper<KeyWordsMaskKeyWords> {
    @Override
    public KeyWordsMaskKeyWords mapRow(ResultSet resultSet, int i) throws SQLException {
        KeyWordsMaskKeyWords qo = new KeyWordsMaskKeyWords();
        qo.setMaskKeyWord(resultSet.getString("WASK_KEY_WORDS"));
        qo.setKeyWord(resultSet.getString("KEY_WORDS"));
        qo.setBusinessId(resultSet.getString("BUSINESS_ID"));
        return qo;
    }
}
