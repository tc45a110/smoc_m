package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.common.smoc.filter.WhiteExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.filter.rowmapper.KeywordsRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 关键词管理 数据库操作
 */
@Slf4j
public class KeywordsRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public PageList<FilterKeyWordsInfoValidator> page(PageParams<FilterKeyWordsInfoValidator> pageParams) {
        //组织查询条件
        FilterKeyWordsInfoValidator data = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.KEY_WORDS_BUSINESS_TYPE");
        sqlBuffer.append(", t.BUSINESS_ID");
        sqlBuffer.append(", t.KEY_WORDS_TYPE");
        sqlBuffer.append(", t.WASK_KEY_WORDS");
        sqlBuffer.append(", t.KEY_WORDS");
        sqlBuffer.append(", t.KEY_DESC");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from filter_key_words_info t  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();
        if (null != data) {

            if (!StringUtils.isEmpty(data.getKeyWordsBusinessType())) {
                sqlBuffer.append(" and t.KEY_WORDS_BUSINESS_TYPE = ? ");
                paramsList.add(data.getKeyWordsBusinessType().trim());
            }

            if (!StringUtils.isEmpty(data.getBusinessId())) {
                sqlBuffer.append(" and t.BUSINESS_ID = ? ");
                paramsList.add(data.getBusinessId().trim());
            }

            if (!StringUtils.isEmpty(data.getKeyWordsType())) {
                sqlBuffer.append(" and t.KEY_WORDS_TYPE = ? ");
                paramsList.add(data.getKeyWordsType().trim());
            }

            if (!StringUtils.isEmpty(data.getKeyWords())) {
                sqlBuffer.append(" and t.KEY_WORDS like ? ");
                paramsList.add("%"+data.getKeyWords().trim()+"%");
            }

        }
        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FilterKeyWordsInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new KeywordsRowMapper());
        return pageList;
    }

    public void bathSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {

        List<FilterKeyWordsInfoValidator> list = filterKeyWordsInfoValidator.getFilterKeyWordsList();

        final String sql = "insert into filter_key_words_info(ID,KEY_WORDS_BUSINESS_TYPE,BUSINESS_ID,KEY_WORDS_TYPE,KEY_WORDS,KEY_DESC,CREATED_BY,CREATED_TIME,WASK_KEY_WORDS) " +
                "values(?,?,?,?,?,?,?,now(),?) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                FilterKeyWordsInfoValidator info = list.get(i);
                ps.setString(1, UUID.uuid32());
                ps.setString(2, filterKeyWordsInfoValidator.getKeyWordsBusinessType());
                ps.setString(3, filterKeyWordsInfoValidator.getBusinessId());
                ps.setString(4, info.getKeyWordsType());
                ps.setString(5, info.getKeyWords());
                ps.setString(6, info.getKeyDesc());
                ps.setString(7, filterKeyWordsInfoValidator.getCreatedBy());
                ps.setString(8, filterKeyWordsInfoValidator.getWaskKeyWords());
            }

        });

    }

    public void expBatchSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {



        final String sql = "insert into filter_key_words_info(ID,KEY_WORDS_BUSINESS_TYPE,BUSINESS_ID,KEY_WORDS_TYPE,KEY_WORDS,KEY_DESC,CREATED_BY,CREATED_TIME,WASK_KEY_WORDS) " +
                "values(?,?,?,?,?,?,?,now(),?) ";

        if("CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType()) || "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())){
            List<ExcelModel> list = filterKeyWordsInfoValidator.getExccelList();
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public int getBatchSize() {
                    return list.size();
                }

                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ExcelModel info = list.get(i);
                    ps.setString(1, UUID.uuid32());
                    ps.setString(2, filterKeyWordsInfoValidator.getKeyWordsBusinessType());
                    ps.setString(3, filterKeyWordsInfoValidator.getBusinessId());
                    ps.setString(4, filterKeyWordsInfoValidator.getKeyWordsType());
                    ps.setString(5, info.getColumn1());
                    ps.setString(6, info.getColumn2());
                    ps.setString(7, filterKeyWordsInfoValidator.getCreatedBy());
                    ps.setString(8, filterKeyWordsInfoValidator.getWaskKeyWords());
                }
            });

        }else{
            List<WhiteExcelModel> list = filterKeyWordsInfoValidator.getWhiteExccelList();
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                public int getBatchSize() {
                    return list.size();
                }

                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    WhiteExcelModel info = list.get(i);
                    ps.setString(1, UUID.uuid32());
                    ps.setString(2, filterKeyWordsInfoValidator.getKeyWordsBusinessType());
                    ps.setString(3, filterKeyWordsInfoValidator.getBusinessId());
                    ps.setString(4, filterKeyWordsInfoValidator.getKeyWordsType());
                    ps.setString(5, info.getColumn1());
                    ps.setString(6, info.getColumn2());
                    ps.setString(7, filterKeyWordsInfoValidator.getCreatedBy());
                    ps.setString(8, info.getColumn3());
                }

            });
        }


    }

}
