package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import com.smoc.cloud.customer.rowmapper.AccountPriceHistoryRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AccountPriceHistoryRepositoryImpl extends BasePageRepository {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<AccountPriceHistoryValidator> page(PageParams<AccountPriceHistoryValidator> pageParams){
        AccountPriceHistoryValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.SOURCE_ID,");
        sqlBuffer.append(" t.ACCOUNT_ID,");
        sqlBuffer.append(" t.CARRIER_TYPE,");
        sqlBuffer.append(" t.CARRIER,");
        sqlBuffer.append(" t.CARRIER_PRICE,");
        sqlBuffer.append(" t.PRICE_DATE,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME, ");
        sqlBuffer.append(" DATE_FORMAT(t.UPDATED_TIME, '%Y-%m-%d %H:%i:%S')UPDATED_TIME ");
        sqlBuffer.append(" from account_price_history t ");
        sqlBuffer.append(" where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        //业务账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =?");
            paramsList.add(qo.getAccountId().trim());
        }

        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.PRICE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.PRICE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getStartDate().trim());
        }


        sqlBuffer.append(" order by t.PRICE_DATE desc");

        log.info("[sql]:{}",sqlBuffer.toString());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountPriceHistoryValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountPriceHistoryRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }
}
