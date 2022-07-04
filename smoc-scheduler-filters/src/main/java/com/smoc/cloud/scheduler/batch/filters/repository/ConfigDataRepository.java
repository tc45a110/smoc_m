package com.smoc.cloud.scheduler.batch.filters.repository;


import com.smoc.cloud.common.http.server.message.request.ReportStatusRequestParams;
import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import com.smoc.cloud.common.page.PageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConfigDataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询 关键词
     *
     * @param businessType
     * @param businessId
     * @param keyWordType
     * @return
     */
//    public List<String> loadKeyWordsAndMaskKeyWord(String businessType, String businessId, String keyWordType) {
//        //查询sql
//        StringBuilder sqlBuffer = new StringBuilder("select ");
//        sqlBuffer.append("  t.KEY_WORDS,");
//        sqlBuffer.append("  t.BUSINESS_ID,");
//        sqlBuffer.append("  t.WASK_KEY_WORDS");
//        sqlBuffer.append("  from filter_key_words_info t  where (1=1) ");
//        List<String> paramsList = new ArrayList<>();
//        if (!StringUtils.isEmpty(businessType)) {
//            sqlBuffer.append(" and t.KEY_WORDS_BUSINESS_TYPE=? ");
//            paramsList.add(businessType);
//        }
//        if (!StringUtils.isEmpty(businessId)) {
//            sqlBuffer.append(" and  t.BUSINESS_ID=? ");
//            paramsList.add(businessId);
//        }
//        if (!StringUtils.isEmpty(keyWordType)) {
//            sqlBuffer.append(" and t.KEY_WORDS_TYPE =? ");
//            paramsList.add(keyWordType);
//        }
//        Object[] params = new Object[paramsList.size()];
//        paramsList.toArray(params);
//        List<String> result = this.jdbcTemplate.query(sqlBuffer.toString(), params, new KeyWordRowMapper());
//        return result;
//    }

}
