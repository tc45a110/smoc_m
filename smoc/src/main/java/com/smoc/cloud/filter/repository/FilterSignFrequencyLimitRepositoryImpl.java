package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import com.smoc.cloud.filter.rowmapper.FilterSignFrequencyLimitRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FilterSignFrequencyLimitRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<FilterSignFrequencyLimitValidator> page(PageParams<FilterSignFrequencyLimitValidator> pageParams) {


        //组织查询条件
        FilterSignFrequencyLimitValidator filterBlackListValidator = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.LIMIT_TYPE");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.SIGNS");
        sqlBuffer.append(", t.ACCOUNTS");
        sqlBuffer.append(", t.FREQUENCY");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from filter_sign_frequency_limit t  where t.STATUS='1' ");

        List<Object> paramsList = new ArrayList<Object>();
        if (null != filterBlackListValidator) {

            if (!StringUtils.isEmpty(filterBlackListValidator.getLimitType())) {
                sqlBuffer.append(" and t.LIMIT_TYPE = ? ");
                paramsList.add(filterBlackListValidator.getLimitType().trim());
            }

            if (!StringUtils.isEmpty(filterBlackListValidator.getInfoType())) {
                sqlBuffer.append(" and t.INFO_TYPE like ? ");
                paramsList.add("%" + filterBlackListValidator.getInfoType().trim() + "%");
            }

            if (!StringUtils.isEmpty(filterBlackListValidator.getSigns())) {
                sqlBuffer.append(" and t.SIGNS like ? ");
                paramsList.add("%" + filterBlackListValidator.getSigns().trim() + "%");
            }

            if (!StringUtils.isEmpty(filterBlackListValidator.getAccounts())) {
                sqlBuffer.append(" and t.ACCOUNTS like ? ");
                paramsList.add("%" + filterBlackListValidator.getAccounts().trim() + "%");
            }

        }
        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FilterSignFrequencyLimitValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FilterSignFrequencyLimitRowMapper());
        return pageList;
    }
}
