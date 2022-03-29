package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationPeriodRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReconciliationPeriodRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<ReconciliationPeriodValidator> page(PageParams<ReconciliationPeriodValidator> pageParams) {
        //查询条件
        ReconciliationPeriodValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.ACCOUNT_PERIOD,");
        sqlBuffer.append(" t.ACCOUNT_PERIOD_TYPE,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" DATE_FORMAT(t.ACCOUNT_PERIOD_START_DATE, '%Y-%m-%d')ACCOUNT_PERIOD_START_DATE, ");
        sqlBuffer.append(" DATE_FORMAT(t.ACCOUNT_PERIOD_END_DATE, '%Y-%m-%d')ACCOUNT_PERIOD_END_DATE, ");
        sqlBuffer.append(" t.ACCOUNT_PERIOD_STATUS,");
        sqlBuffer.append(" t.STATUS,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from reconciliation_period t ");
        sqlBuffer.append(" where (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        //账期
        if (!StringUtils.isEmpty(qo.getAccountPeriod())) {
            sqlBuffer.append(" and t.ACCOUNT_PERIOD =?");
            paramsList.add(qo.getAccountPeriod().trim());
        }
        //账期类型
        if (!StringUtils.isEmpty(qo.getAccountPeriodType())) {
            sqlBuffer.append(" and t.ACCOUNT_PERIOD_TYPE =?");
            paramsList.add(qo.getAccountPeriodType().trim());
        }
        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }
        //有效状态
        if (!StringUtils.isEmpty(qo.getStatus())) {
            sqlBuffer.append(" and t.STATUS =?");
            paramsList.add(qo.getStatus().trim());
        }

        sqlBuffer.append(" order by t.ACCOUNT_PERIOD desc");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[SQL1]:{}",sqlBuffer);
        PageList<ReconciliationPeriodValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ReconciliationPeriodRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }

    /**
     * 查询近6个月账期
     */
    public List<String> findAccountPeriod() {

        //查询sql
        String sqlBuffer = "select distinct ACCOUNT_PERIOD from reconciliation_period t where STATUS='1'  order by t.ACCOUNT_PERIOD desc limit 0,6";
        List<String> list = jdbcTemplate.queryForList(sqlBuffer, String.class);
        return list;
    }

    /**
     * 创建业务账号对账
     */
    public void buildAccountPeriod(ReconciliationPeriodValidator validator, String uuid) {

        StringBuffer sql = new StringBuffer("insert into reconciliation_account_items(ID,ENTERPRISE_ID,ACCOUNT_PERIOD_ID,ACCOUNT_PERIOD,ACCOUNT_ID,CARRIER,TOTAL_SEND_QUANTITY,TOTAL_SUBMIT_QUANTITY,TOTAL_AMOUNT,TOTAL_NO_REPORT_QUANTITY,CHARGE_TYPE,PRICE,ACCOUNT_PERIOD_STATUS,STATUS,CREATED_BY,CREATED_TIME)");
        sql.append(" select system_nextval('reconciliation'),ENTERPRISE_ID ,'" + uuid + "','" + validator.getAccountPeriod() + "',BUSINESS_ACCOUNT,CARRIER,MESSAGE_SUCCESS_NUM,0,0.00,0,CARRIER_TYPE,ACCOUNT_PRICE,'1','1','" + validator.getCreatedBy() + "',now() from view_reconciliation_account_original where DATA_DATE = '" + validator.getAccountPeriod() + "'");

        jdbcTemplate.update(sql.toString());
    }

    /**
     * 删除账期
     */
    public void deleteAccountPeriod(String id) {
        String deleteAccountPeriod = "update reconciliation_period set STATUS='0' where id='" + id + "'";

        String deleteAccountPeriodItem = "update reconciliation_account_items set STATUS='0' where ACCOUNT_PERIOD_ID='" + id + "'";

        String[] sql = new String[2];
        sql[0] = deleteAccountPeriod;
        sql[1] = deleteAccountPeriodItem;
        jdbcTemplate.batchUpdate(sql);
    }


}
