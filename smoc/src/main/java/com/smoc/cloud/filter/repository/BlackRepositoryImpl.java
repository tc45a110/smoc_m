package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.filter.rowmapper.BlackRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 黑名单管理 数据库操作
 */
@Slf4j
public class BlackRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public PageList<FilterBlackListValidator> page(PageParams<FilterBlackListValidator> pageParams) {
        //组织查询条件
        FilterBlackListValidator filterBlackListValidator = pageParams.getParams();

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.ID,t.GROUP_ID,t.NAME,t.MOBILE,t.STATUS,str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME,t.CREATED_BY " +
                " from filter_black_list t where t.GROUP_ID=? ");
        int paramSize = 0;
        Object[] tempParams = new Object[3];
        if (null != filterBlackListValidator) {
            tempParams[paramSize] = filterBlackListValidator.getGroupId();
            paramSize++;
            if (!StringUtils.isEmpty(filterBlackListValidator.getName())) {
                sqlBuffer.append(" and t.NAME like ? ");
                tempParams[paramSize] = "%" + filterBlackListValidator.getName().trim() + "%";
                paramSize++;
            }
            if (!StringUtils.isEmpty(filterBlackListValidator.getMobile())) {
                sqlBuffer.append(" and t.MOBILE like ? ");
                tempParams[paramSize] = "%" + filterBlackListValidator.getMobile().trim()+ "%";
                paramSize++;
            }
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        //根据参数个数，组织参数值
        Object[] params = null;
        if (!(0 == paramSize)) {
            params = new Object[paramSize];
            int j = 0;
            for (int i = 0; i < tempParams.length; i++) {
                if (null != tempParams[i]) {
                    params[j] = tempParams[i];
                    j++;
                }
            }
        }

        PageList<FilterBlackListValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new BlackRowMapper());
        return pageList;
    }

}
