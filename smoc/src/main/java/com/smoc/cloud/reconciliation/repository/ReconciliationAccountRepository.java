package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationAccountModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationAccountRowMapper;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationEnterpriseModelRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业客户账单
 */
@Service
public class ReconciliationAccountRepository extends BasePageRepository {

    /**
     * 查询企业客户账单
     *
     * @param pageParams
     * @return
     */
    public PageList<ReconciliationEnterpriseModel> page(PageParams<ReconciliationEnterpriseModel> pageParams) {
        ReconciliationEnterpriseModel qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select distinct ");
        sqlBuffer.append(" e.ENTERPRISE_NAME,");
        sqlBuffer.append(" rai.ENTERPRISE_ID,");
        sqlBuffer.append(" rai.ACCOUNT_PERIOD,");
        sqlBuffer.append(" rai.ACCOUNT_PERIOD_STATUS ");
        sqlBuffer.append(" from reconciliation_account_items rai, enterprise_basic_info e ");
        sqlBuffer.append(" where rai.ENTERPRISE_ID = e.ENTERPRISE_ID and  rai.STATUS='1' ");

        List<Object> paramsList = new ArrayList<Object>();

        //账期
        if (!StringUtils.isEmpty(qo.getAccountingPeriod())) {
            sqlBuffer.append(" and rai.ACCOUNT_PERIOD =?");
            paramsList.add(qo.getAccountingPeriod().trim());
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //账期
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and rai.ENTERPRISE_ID =?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //账单状态
        if (!StringUtils.isEmpty(qo.getAccountingStatus())) {
            sqlBuffer.append(" and rai.ACCOUNT_PERIOD_STATUS =?");
            paramsList.add(qo.getAccountingStatus().trim());
        }

        sqlBuffer.append(" ORDER BY rai.ACCOUNT_PERIOD desc,rai.ENTERPRISE_ID desc ");

        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<ReconciliationEnterpriseModel> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ReconciliationEnterpriseModelRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    public Map<String,Object> getEnterpriseBills(String enterpriseId, String accountPeriod) {

        Map<String,Object> result = new HashMap<>();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ACCOUNT_PERIOD,");
        sqlBuffer.append(" t.ACCOUNT_ID,");
        sqlBuffer.append(" t.CARRIER,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" t.TOTAL_SEND_QUANTITY,");
        sqlBuffer.append(" t.PRICE, ");
        sqlBuffer.append(" t.CHARGE_TYPE ");
        sqlBuffer.append(" from reconciliation_account_items t ");
        sqlBuffer.append(" where  t.STATUS='1' and t.ENTERPRISE_ID=? and t.ACCOUNT_PERIOD=?");

        Object[] params = new Object[2];
        params[0] = enterpriseId;
        params[1] = accountPeriod;
        List<ReconciliationAccountModel> list = jdbcTemplate.query(sqlBuffer.toString(), params, new ReconciliationAccountRowMapper());

        result.put("list",list);

        //查询sql
        String sumSql = "select sum(TOTAL_SEND_QUANTITY*PRICE) am,sum(TOTAL_SEND_QUANTITY) quantity from reconciliation_account_items t where  t.STATUS='1' and t.ENTERPRISE_ID=? and t.ACCOUNT_PERIOD=? ";
        Map<String, Object> sum = jdbcTemplate.queryForMap(sumSql,params);
        if(null != sum){
            result.put("am",sum.get("am"));
            result.put("quantity",sum.get("quantity"));
        }
        return result;

    }


}
