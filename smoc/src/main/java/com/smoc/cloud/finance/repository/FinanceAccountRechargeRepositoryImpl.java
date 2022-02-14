package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.rowmapper.FinanceAccountRechargeRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FinanceAccountRechargeRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询  认证账号充值记录
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountRechargeValidator> pageIdentification(PageParams<FinanceAccountRechargeValidator> pageParams){
        //查询条件
        FinanceAccountRechargeValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  t.BUSINESS_TYPE 'IDENTIFICATION',");
        sqlBuffer.append("  t.ACCOUNT_NAME '',");
        sqlBuffer.append("  t.RECHARGE_FLOW_NO,");
        sqlBuffer.append("  t.RECHARGE_SOURCE,");
        sqlBuffer.append("  t.RECHARGE_SUM,");
        sqlBuffer.append("  t.RECHARGE_COST,");
        sqlBuffer.append("  t.RECHARGE_ACCOUNT_USABLE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account_recharge t,identification_account_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.IDENTIFICATION_ACCOUNT and i.ENTERPRISE_ID = e.ENTERPRISE_ID and t.RECHARGE_SOURCE ='IDENTIFICATION_ACCOUNT' ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =?");
            paramsList.add(qo.getAccountId().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FinanceAccountRechargeValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FinanceAccountRechargeRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 分页查询  业务账号充值记录
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountRechargeValidator> pageBusiness(PageParams<FinanceAccountRechargeValidator> pageParams){
        //查询条件
        FinanceAccountRechargeValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  i.BUSINESS_TYPE,");
        sqlBuffer.append("  i.ACCOUNT_NAME,");
        sqlBuffer.append("  t.RECHARGE_FLOW_NO,");
        sqlBuffer.append("  t.RECHARGE_SOURCE,");
        sqlBuffer.append("  t.RECHARGE_SUM,");
        sqlBuffer.append("  t.RECHARGE_COST,");
        sqlBuffer.append("  t.RECHARGE_ACCOUNT_USABLE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account_recharge t,account_base_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.ACCOUNT_ID and i.ENTERPRISE_ID = e.ENTERPRISE_ID and t.RECHARGE_SOURCE ='BUSINESS_ACCOUNT' ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =? ");
            paramsList.add(qo.getAccountId().trim());
        }
        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and i.BUSINESS_TYPE =? ");
            paramsList.add(qo.getBusinessType().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FinanceAccountRechargeValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FinanceAccountRechargeRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
