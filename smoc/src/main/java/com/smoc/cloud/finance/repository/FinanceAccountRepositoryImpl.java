package com.smoc.cloud.finance.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.rowmapper.FinanceAccountRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FinanceAccountRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询 身份认证账号
     *
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountValidator> pageIdentification(PageParams<FinanceAccountValidator> pageParams) {

        //查询条件
        FinanceAccountValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  i.IDENTIFICATION_ACCOUNT ACCOUNT_NAME,");
        sqlBuffer.append("  i.IDENTIFICATION_ACCOUNT ACCOUNT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_TOTAL_SUM,");
        sqlBuffer.append("  t.ACCOUNT_USABLE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_FROZEN_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CONSUME_SUM,");
        sqlBuffer.append("  t.ACCOUNT_RECHARGE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CREDIT_SUM,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account t,identification_account_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.IDENTIFICATION_ACCOUNT and i.ENTERPRISE_ID = e.ENTERPRISE_ID and t.ACCOUNT_TYPE ='IDENTIFICATION_ACCOUNT' ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and i.IDENTIFICATION_ACCOUNT =?");
            paramsList.add(qo.getAccountName().trim());
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS =?");
            paramsList.add(qo.getAccountStatus().trim());
        }
        //账号类型
        if (!StringUtils.isEmpty(qo.getAccountType())) {
            sqlBuffer.append(" and t.ACCOUNT_TYPE =?");
            paramsList.add(qo.getAccountType().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FinanceAccountValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FinanceAccountRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 分页查询 业务账号
     *
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountValidator> pageBusinessType(PageParams<FinanceAccountValidator> pageParams) {

        //查询条件
        FinanceAccountValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  i.ACCOUNT_NAME,");
        sqlBuffer.append("  i.ACCOUNT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_TOTAL_SUM,");
        sqlBuffer.append("  t.ACCOUNT_USABLE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_FROZEN_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CONSUME_SUM,");
        sqlBuffer.append("  t.ACCOUNT_RECHARGE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CREDIT_SUM,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account t,account_base_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.ID and i.ENTERPRISE_ID = e.ENTERPRISE_ID and t.ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT' ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and i.ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
        }
        //账号
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and i.ACCOUNT_NAME =?");
            paramsList.add(qo.getAccountName().trim());
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS like ?");
            paramsList.add("%" + qo.getAccountStatus().trim() + "%");
        }
        //账号类型
        if (!StringUtils.isEmpty(qo.getAccountType())) {
            sqlBuffer.append(" and t.ACCOUNT_TYPE =?");
            paramsList.add(qo.getAccountType().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FinanceAccountValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FinanceAccountRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 汇总金额统计
     *
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public Map<String, Object> countSum(String flag) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        StringBuffer sql = new StringBuffer("select sum(ACCOUNT_USABLE_SUM) ACCOUNT_USABLE_SUM,sum(ACCOUNT_FROZEN_SUM) ACCOUNT_FROZEN_SUM,sum(ACCOUNT_CONSUME_SUM) ACCOUNT_CONSUME_SUM,sum(ACCOUNT_RECHARGE_SUM) ACCOUNT_RECHARGE_SUM from finance_account");
        if ("1".equals(flag)) {
           sql.append(" where ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT'");
        }
        if("2".equals(flag)){
            sql.append(" where ACCOUNT_TYPE ='IDENTIFICATION_ACCOUNT'");
        }

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        log.info(new Gson().toJson(map));
       return map;
    }

    /**
     * 检查账户余额，包括了授信金额  true 表示余额 够用
     * @param accountId
     * @param ammount 金额
     * @return
     */
    public boolean checkAccountUsableSum(String accountId,BigDecimal ammount){

        String sql="select ACCOUNT_USABLE_SUM+ACCOUNT_CREDIT_SUM from finance_account where ACCOUNT_ID =?";
        BigDecimal sum = jdbcTemplate.queryForObject(sql,BigDecimal.class,accountId);
        if(null == sum){
            return false;
        }
        return !(sum.compareTo(ammount)==-1);
    }

    /**
     * 冻结金额
     * @param accountId
     * @param ammount
     */
    public void freeze(String accountId,BigDecimal ammount){
        String sql ="update finance_account set ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM-"+ammount+",ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM+"+ammount+" where ACCOUNT_ID='"+accountId+"'";
        jdbcTemplate.update(sql);
    }

    /**
     * 解冻扣费
     * @param accountId
     * @param ammount
     */
    public void unfreeze(String accountId,BigDecimal ammount){
        String sql ="update finance_account set ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM-"+ammount+",ACCOUNT_CONSUME_SUM = ACCOUNT_CONSUME_SUM+"+ammount+" where ACCOUNT_ID='"+accountId+"'";
        jdbcTemplate.update(sql);
    }

    /**
     * 解冻不扣费
     * @param accountId
     * @param ammount
     */
    public void unfreezeFree(String accountId,BigDecimal ammount){
        String sql ="update finance_account set ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM-"+ammount+",ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM+"+ammount+" where ACCOUNT_ID='"+accountId+"'";
        jdbcTemplate.update(sql);
    }

}
