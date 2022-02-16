package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import com.smoc.cloud.finance.rowmapper.FinanceAccountShareDetailRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FinanceAccountShareDetailRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountShareDetailValidator> page(PageParams<FinanceAccountShareDetailValidator> pageParams) {

        FinanceAccountShareDetailValidator qo = pageParams.getParams();
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.SHARE_ACCOUNT_ID,");
        sqlBuffer.append(" t.ACCOUNT_ID,");
        sqlBuffer.append(" t.IS_USABLE_SUM_POOL,");
        sqlBuffer.append(" t.IS_FREEZE_SUM_POOL,");
        sqlBuffer.append(" t.USABLE_SUM_POOL,");
        sqlBuffer.append(" t.FREEZE_SUM_POOL,");
        sqlBuffer.append(" t.SHARE_STATUS,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from finance_account_share_detail t");
        sqlBuffer.append(" where (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        //共享账户ID
        if (!StringUtils.isEmpty(qo.getShareAccountId())) {
            sqlBuffer.append(" and t.SHARE_ACCOUNT_ID = ?");
            paramsList.add(qo.getShareAccountId().trim());
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FinanceAccountShareDetailValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FinanceAccountShareDetailRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;


    }
}
