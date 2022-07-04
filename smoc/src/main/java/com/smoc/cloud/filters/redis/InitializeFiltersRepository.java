package com.smoc.cloud.filters.redis;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.smoc.index.CheckRemindModel;
import com.smoc.cloud.filter.entity.KeyWordsMaskKeyWords;
import com.smoc.cloud.filter.rowmapper.BlackRowMapper;
import com.smoc.cloud.filter.rowmapper.KeyWordRowMapper;
import com.smoc.cloud.filter.rowmapper.WhiteRowMapper;
import com.smoc.cloud.statistics.rowmapper.StatisticCheckRemindRowMapper;
import com.smoc.cloud.statistics.rowmapper.StatisticProfitRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 首页统计数据查询
 */
@Service
public class InitializeFiltersRepository extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 查询系统黑名单
     *
     * @return
     */
    public List<String> findSystemBlackList() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select t.MOBILE from filter_black_list t  where t.ENTERPRISE_ID='SYSTEM' ");
        List<String> result = this.jdbcTemplate.queryForList(sqlBuffer.toString(), String.class);

        return result;

    }

    /**
     * 查询系统白名单
     *
     * @return
     */
    public List<String> findSystemWhiteList() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select t.MOBILE from filter_white_list t  where t.ENTERPRISE_ID='SYSTEM' ");
        List<String> result = this.jdbcTemplate.queryForList(sqlBuffer.toString(), String.class);

        return result;
    }

    /**
     * 查询行业黑名单
     *
     * @return
     */
    public List<FilterBlackListValidator> findIndustryBlackList(){
        //查询sql
        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.GROUP_ID,t.NAME,t.MOBILE,t.STATUS,str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME,t.CREATED_BY " +
                " from filter_black_list t where t.ENTERPRISE_ID='INDUSTRY' ");
        List<FilterBlackListValidator> result = this.jdbcTemplate.query(sqlBuffer.toString(), new BlackRowMapper());

        return result;
    }

    /**
     * 查询行业白名单
     *
     * @return
     */
    public List<FilterWhiteListValidator> findIndustryWhiteList() {
        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.GROUP_ID,t.NAME,t.MOBILE,t.STATUS,str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME,t.CREATED_BY " +
                " from filter_white_list t where t.ENTERPRISE_ID='INDUSTRY' ");
        List<FilterWhiteListValidator> result = this.jdbcTemplate.query(sqlBuffer.toString(), new WhiteRowMapper());
        return result;
    }

    /**
     * 查询 关键词
     *
     * @param businessType
     * @param businessId
     * @param keyWordType
     * @return
     */
    public List<String> loadKeyWords(String businessType, String businessId, String keyWordType) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.KEY_WORDS");
        sqlBuffer.append("  from filter_key_words_info t  where t.KEY_WORDS_BUSINESS_TYPE=? and  t.BUSINESS_ID=? and t.KEY_WORDS_TYPE =?  ");

        Object[] params = new Object[3];
        params[0] = businessType;
        params[1] = businessId;
        params[2] = keyWordType;
        List<String> result = this.jdbcTemplate.queryForList(sqlBuffer.toString(), params, String.class);
        return result;
    }

    /**
     * 查询 关键词
     *
     * @param businessType
     * @param businessId
     * @param keyWordType
     * @return
     */
    public List<KeyWordsMaskKeyWords> loadKeyWordsAndMaskKeyWord(String businessType, String businessId, String keyWordType) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.KEY_WORDS,");
        sqlBuffer.append("  t.BUSINESS_ID,");
        sqlBuffer.append("  t.WASK_KEY_WORDS");
        sqlBuffer.append("  from filter_key_words_info t  where (1=1) ");
        List<String> paramsList = new ArrayList<>();
        if (!StringUtils.isEmpty(businessType)) {
            sqlBuffer.append(" and t.KEY_WORDS_BUSINESS_TYPE=? ");
            paramsList.add(businessType);
        }
        if (!StringUtils.isEmpty(businessId)) {
            sqlBuffer.append(" and  t.BUSINESS_ID=? ");
            paramsList.add(businessId);
        }
        if (!StringUtils.isEmpty(keyWordType)) {
            sqlBuffer.append(" and t.KEY_WORDS_TYPE =? ");
            paramsList.add(keyWordType);
        }
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        List<KeyWordsMaskKeyWords> result = this.jdbcTemplate.query(sqlBuffer.toString(), params, new KeyWordRowMapper());
        return result;
    }
}
