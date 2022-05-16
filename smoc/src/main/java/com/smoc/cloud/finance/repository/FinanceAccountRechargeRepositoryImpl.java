package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.rowmapper.FinanceAccountRechargeRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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
        sqlBuffer.append("  'IDENTIFICATION' BUSINESS_TYPE ,");
        sqlBuffer.append("  'IDENTIFICATION' ACCOUNT_NAME,");
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
        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and e.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
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
     * 分页查询 共用账号充值记录
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountRechargeValidator> pageSystem(PageParams<FinanceAccountRechargeValidator> pageParams){
        //查询条件
        FinanceAccountRechargeValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  t.RECHARGE_SOURCE BUSINESS_TYPE ,");
        sqlBuffer.append("  t.ACCOUNT_ID ACCOUNT_NAME,");
        sqlBuffer.append("  t.RECHARGE_FLOW_NO,");
        sqlBuffer.append("  t.RECHARGE_SOURCE,");
        sqlBuffer.append("  t.RECHARGE_SUM,");
        sqlBuffer.append("  t.RECHARGE_COST,");
        sqlBuffer.append("  t.RECHARGE_ACCOUNT_USABLE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account_recharge t,system_account_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.ACCOUNT and i.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and e.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }
        //账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =?");
            paramsList.add(qo.getAccountId().trim());
        }
        //充值来源
        if (!StringUtils.isEmpty(qo.getRechargeSource())) {
            sqlBuffer.append(" and t.RECHARGE_SOURCE = ?");
            paramsList.add(qo.getRechargeSource().trim());
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
        sqlBuffer.append("  t.ENTERPRISE_NAME,");
        sqlBuffer.append("  t.ENTERPRISE_ID,");
        sqlBuffer.append("  t.BUSINESS_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_NAME,");
        sqlBuffer.append("  t.RECHARGE_FLOW_NO,");
        sqlBuffer.append("  t.RECHARGE_SOURCE,");
        sqlBuffer.append("  t.RECHARGE_SUM,");
        sqlBuffer.append("  t.RECHARGE_COST,");
        sqlBuffer.append("  t.RECHARGE_ACCOUNT_USABLE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_recharge_view t ");
        sqlBuffer.append("  where (1=1) ");
        //log.info("[sql]:{}",sqlBuffer.toString());
        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and t.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =? ");
            paramsList.add(qo.getAccountId().trim());
        }
        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =? ");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
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
     * 统计充值金额
     *
     * @param qo
     * @return
     */
    public Map<String, Object> countRechargeSum(FinanceAccountRechargeValidator qo) {
        StringBuffer sql = new StringBuffer("select");
        sql.append("  sum(t.RECHARGE_SUM) RECHARGE_SUM ");
        sql.append("  from finance_recharge_view t ");
        sql.append("  where  (1=1) ");
        List<Object> paramsList = new ArrayList<Object>();
        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sql.append(" and t.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sql.append(" and t.ACCOUNT_ID =? ");
            paramsList.add(qo.getAccountId().trim());
        }
        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sql.append(" and t.BUSINESS_TYPE =? ");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sql.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sql.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), params);
        //log.info(new Gson().toJson(map));
        return map;
    }
}
