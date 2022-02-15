package com.smoc.cloud.finance.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.finance.rowmapper.FinanceAccountRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class FinanceAccountRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询 共享财务账户
     *
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountValidator> pageShare(PageParams<FinanceAccountValidator> pageParams) {

        //查询条件
        FinanceAccountValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  t.ACCOUNT_NAME,");
        sqlBuffer.append("  t.ACCOUNT_ID ACCOUNT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_TOTAL_SUM,");
        sqlBuffer.append("  t.ACCOUNT_USABLE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_FROZEN_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CONSUME_SUM,");
        sqlBuffer.append("  t.ACCOUNT_RECHARGE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CREDIT_SUM,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.SHARE_ID,");
        sqlBuffer.append("  t.IS_SHARE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account t,enterprise_basic_info e ");
        sqlBuffer.append("  where  t.ENTERPRISE_ID = e.ENTERPRISE_ID and t.ACCOUNT_TYPE ='SHARE_ACCOUNT'");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =?");
            paramsList.add(qo.getAccountId().trim());
        }
        //业务账号名称
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and t.ACCOUNT_NAME like ?");
            paramsList.add("%" + qo.getAccountName().trim() + "%");
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS = ?");
            paramsList.add(qo.getAccountStatus().trim());
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
     * 分页查询 分页查询 身份认证财务账户
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
        sqlBuffer.append("  e.ENTERPRISE_ID,");
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
        sqlBuffer.append("  t.SHARE_ID,");
        sqlBuffer.append("  t.IS_SHARE,");
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
     * 分页查询 业务财务账户
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
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  i.ACCOUNT_NAME,");
        sqlBuffer.append("  i.ACCOUNT_ID ACCOUNT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_TOTAL_SUM,");
        sqlBuffer.append("  t.ACCOUNT_USABLE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_FROZEN_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CONSUME_SUM,");
        sqlBuffer.append("  t.ACCOUNT_RECHARGE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CREDIT_SUM,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.SHARE_ID,");
        sqlBuffer.append("  t.IS_SHARE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account t,account_base_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.ACCOUNT_ID and i.ENTERPRISE_ID = e.ENTERPRISE_ID");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =? ");
            paramsList.add(qo.getAccountId().trim());
        }
        //业务账号名称
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and i.ACCOUNT_NAME like ? ");
            paramsList.add("%" + qo.getAccountName().trim() + "%");
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS = ? ");
            paramsList.add(qo.getAccountStatus().trim());
        }
        //账号类型
        if (!StringUtils.isEmpty(qo.getAccountType())) {
            sqlBuffer.append(" and t.ACCOUNT_TYPE =? ");
            paramsList.add(qo.getAccountType().trim());
        } else {
            sqlBuffer.append(" and t.ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT' and t.ACCOUNT_TYPE !='SHARE_ACCOUNT' ");
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
     * @param flag 1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账户
     * @return
     */
    public Map<String, Object> countSum(String flag, FinanceAccountValidator qo) {

        StringBuffer sql = new StringBuffer("select");
        sql.append("  sum(t.ACCOUNT_USABLE_SUM) ACCOUNT_USABLE_SUM,");
        sql.append("  sum(t.ACCOUNT_FROZEN_SUM) ACCOUNT_FROZEN_SUM,");
        sql.append("  sum(t.ACCOUNT_CONSUME_SUM) ACCOUNT_CONSUME_SUM,");
        sql.append("  sum(t.ACCOUNT_RECHARGE_SUM) ACCOUNT_RECHARGE_SUM");

        if ("1".equals(flag)) {
            sql.append("  from finance_account t,account_base_info i,enterprise_basic_info e ");
            sql.append("  where t.ACCOUNT_ID = i.ACCOUNT_ID and i.ENTERPRISE_ID = e.ENTERPRISE_ID ");
            sql.append("  and t.ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT' and  t.ACCOUNT_TYPE !='SHARE_ACCOUNT' ");
        }
        if ("2".equals(flag)) {
            sql.append("  from finance_account t,identification_account_info i,enterprise_basic_info e ");
            sql.append("  where t.ACCOUNT_ID = i.IDENTIFICATION_ACCOUNT and i.ENTERPRISE_ID = e.ENTERPRISE_ID and t.ACCOUNT_TYPE ='IDENTIFICATION_ACCOUNT' ");
        }

        if ("3".equals(flag)) {
            sql.append("  from finance_account t,enterprise_basic_info e ");
            sql.append("  where  t.ENTERPRISE_ID = e.ENTERPRISE_ID and t.ACCOUNT_TYPE ='SHARE_ACCOUNT'");
        }

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sql.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sql.append(" and t.ACCOUNT_ID = ? ");
            paramsList.add(qo.getAccountId().trim());
        }

        if ("1".equals(flag)) {
            //业务账号名称
            if (!StringUtils.isEmpty(qo.getAccountName())) {
                sql.append(" and i.ACCOUNT_NAME like ? ");
                paramsList.add("%" + qo.getAccountName().trim() + "%");
            }
        }

        if ("3".equals(flag)) {
            //业务账号名称
            if (!StringUtils.isEmpty(qo.getAccountName())) {
                sql.append(" and t.ACCOUNT_NAME like ?");
                paramsList.add("%" + qo.getAccountName().trim() + "%");
            }
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sql.append(" and t.ACCOUNT_STATUS = ? ");
            paramsList.add(qo.getAccountStatus().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        //log.info("[count sql]:{}",sql.toString());

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), params);
        //log.info(new Gson().toJson(map));
        return map;
    }

    /**
     * 检查账户余额，包括了授信金额  true 表示余额 够用
     *
     * @param accountId
     * @param ammount   金额
     * @return
     */
    public boolean checkAccountUsableSum(String accountId, BigDecimal ammount) {

        String sql = "select ACCOUNT_USABLE_SUM+ACCOUNT_CREDIT_SUM from finance_account where ACCOUNT_ID =?";
        BigDecimal sum = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        if (null == sum) {
            return false;
        }
        return !(sum.compareTo(ammount) == -1);
    }

    /**
     * 冻结金额
     *
     * @param accountId
     * @param amount
     */
    public void freeze(String accountId, BigDecimal amount) {
        String sql = "update finance_account set ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM-" + amount + ",ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM+" + amount + " where ACCOUNT_ID='" + accountId + "'";
        jdbcTemplate.update(sql);
    }

    /**
     * 解冻扣费
     *
     * @param accountId
     * @param amount
     */
    public void unfreeze(String accountId, BigDecimal amount) {
        String sql = "update finance_account set ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM-" + amount + ",ACCOUNT_CONSUME_SUM = ACCOUNT_CONSUME_SUM+" + amount + " where ACCOUNT_ID='" + accountId + "'";
        jdbcTemplate.update(sql);
    }

    /**
     * 解冻不扣费
     *
     * @param accountId
     * @param amount
     */
    public void unfreezeFree(String accountId, BigDecimal amount) {
        String sql = "update finance_account set ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM-" + amount + ",ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM+" + amount + " where ACCOUNT_ID='" + accountId + "'";
        jdbcTemplate.update(sql);
    }

    /**
     * 根据企业id，查询企业所有财务账户
     *
     * @param enterpriseId
     * @return
     */
    public List<FinanceAccountValidator> findEnterpriseFinanceAccount(String enterpriseId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  i.ACCOUNT_NAME,");
        sqlBuffer.append("  i.ACCOUNT_ID ACCOUNT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_TOTAL_SUM,");
        sqlBuffer.append("  t.ACCOUNT_USABLE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_FROZEN_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CONSUME_SUM,");
        sqlBuffer.append("  t.ACCOUNT_RECHARGE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CREDIT_SUM,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.SHARE_ID,");
        sqlBuffer.append("  t.IS_SHARE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account t,account_base_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.ACCOUNT_ID and i.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  and t.ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT' ");
        sqlBuffer.append("  and i.ENTERPRISE_ID =?");
        sqlBuffer.append("  order by t.CREATED_TIME desc");
        Object[] params = new Object[1];
        params[0] = enterpriseId;
        List<FinanceAccountValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new FinanceAccountRowMapper());
        return list;
    }

    /**
     * 根据企业enterpriseIds，查询企业所有财务账户(包括子企业财务账户)
     *
     * @param enterpriseIds
     * @return
     */
    public List<FinanceAccountValidator> findEnterpriseAndSubsidiaryFinanceAccount(List<String> enterpriseIds) {


        String con = new Gson().toJson(enterpriseIds).replace("\"", "\'").replace("[", "").replace("]", "");
        log.info(con);
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  e.ENTERPRISE_ID,");
        sqlBuffer.append("  i.ACCOUNT_NAME,");
        sqlBuffer.append("  i.ACCOUNT_ID ACCOUNT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_TOTAL_SUM,");
        sqlBuffer.append("  t.ACCOUNT_USABLE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_FROZEN_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CONSUME_SUM,");
        sqlBuffer.append("  t.ACCOUNT_RECHARGE_SUM,");
        sqlBuffer.append("  t.ACCOUNT_CREDIT_SUM,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.SHARE_ID,");
        sqlBuffer.append("  t.IS_SHARE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_account t,account_base_info i,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ACCOUNT_ID = i.ACCOUNT_ID and i.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  and t.ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT' ");
        sqlBuffer.append("  and i.ENTERPRISE_ID in(" + con + ")");
        sqlBuffer.append("  order by t.CREATED_TIME desc");
        log.info(sqlBuffer.toString());
        List<FinanceAccountValidator> list = this.queryForObjectList(sqlBuffer.toString(), null, new FinanceAccountRowMapper());
        return list;
    }

    /**
     * 根据enterpriseId 汇总企业金额统计
     *
     * @param enterpriseId
     * @return
     */
    public Map<String, Object> countEnterpriseSum(String enterpriseId) {
        StringBuffer sql = new StringBuffer("select");
        sql.append("  sum(t.ACCOUNT_USABLE_SUM) ACCOUNT_USABLE_SUM,");
        sql.append("  sum(t.ACCOUNT_FROZEN_SUM) ACCOUNT_FROZEN_SUM,");
        sql.append("  sum(t.ACCOUNT_CONSUME_SUM) ACCOUNT_CONSUME_SUM,");
        sql.append("  sum(t.ACCOUNT_RECHARGE_SUM) ACCOUNT_RECHARGE_SUM");
        sql.append("  from finance_account t,account_base_info i");
        sql.append("  where t.ACCOUNT_ID = i.ACCOUNT_ID ");
        sql.append("  and ACCOUNT_TYPE !='IDENTIFICATION_ACCOUNT' and ACCOUNT_TYPE !='SHARE_ACCOUNT' ");
        sql.append("  and i.ENTERPRISE_ID =?");
        Object[] params = new Object[1];
        params[0] = enterpriseId;
        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(), params);
        //log.info(new Gson().toJson(map));
        return map;
    }

    /**
     * 创建财务共享账户
     * 包括了创建共享账户流水记录，修改原账户状态，并创建共享账户
     */
    public void createShareFinanceAccount(FinanceAccountValidator qo) {

        if (null == qo.getIsFreezeSumPool()) qo.setIsFreezeSumPool("0");
        if (null == qo.getIsUsableSumPool()) qo.setIsUsableSumPool("0");


        String[] accountIds = qo.getAccountIds().split(",");
        String[] sql = new String[accountIds.length * 2 + 1];

        /**
         * 创建共享账户合并账户明细记录
         */
        //余额归集、冻结金额归集
        if ("1".equals(qo.getIsUsableSumPool()) && "1".equals(qo.getIsFreezeSumPool())) {
            //创建财务共享账号创建明细
            for (int i = 0; i < accountIds.length; i++) {
                StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',ACCOUNT_USABLE_SUM,ACCOUNT_FROZEN_SUM,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + accountIds[i] + "' ");
                log.info(sqlBuffer.toString());
                sql[i] = sqlBuffer.toString();
            }

            //修改原账户状态、金额
            for (int i = accountIds.length, j = 0; i < accountIds.length * 2; i++, j++) {
                sql[i] = " update finance_account set ACCOUNT_USABLE_SUM =0.00,ACCOUNT_FROZEN_SUM =0.00,IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + accountIds[j] + "' ";
                //log.info(sql[i]);
            }
        }

        //余额归集、冻结金额不归集
        if ("1".equals(qo.getIsUsableSumPool()) && "0".equals(qo.getIsFreezeSumPool())) {
            //创建财务共享账号创建明细
            for (int i = 0; i < accountIds.length; i++) {
                StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',ACCOUNT_USABLE_SUM,0.00,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + accountIds[i] + "' ");
                log.info(sqlBuffer.toString());
                sql[i] = sqlBuffer.toString();
            }
            //修改原账户状态、金额
            for (int i = accountIds.length, j = 0; i < accountIds.length * 2; i++, j++) {
                sql[i] = " update finance_account set ACCOUNT_USABLE_SUM =0.00,IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + accountIds[j] + "' ";
                //log.info(sql[i]);
            }
        }

        //余额不归集、冻结金额归集
        if ("0".equals(qo.getIsUsableSumPool()) && "1".equals(qo.getIsFreezeSumPool())) {
            //创建财务共享账号创建明细
            for (int i = 0; i < accountIds.length; i++) {
                StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',0.00,ACCOUNT_FROZEN_SUM,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + accountIds[i] + "' ");
                log.info(sqlBuffer.toString());
                sql[i] = sqlBuffer.toString();
            }

            //修改原账户状态、金额
            for (int i = accountIds.length, j = 0; i < accountIds.length * 2; i++, j++) {
                sql[i] = " update finance_account set ACCOUNT_FROZEN_SUM =0.00,IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + accountIds[j] + "' ";
                //log.info(sql[i]);
            }
        }

        //余额不归集、冻结金额不归集
        if ("0".equals(qo.getIsUsableSumPool()) && "0".equals(qo.getIsFreezeSumPool())) {
            //创建财务共享账号创建明细
            for (int i = 0; i < accountIds.length; i++) {
                StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',0.00,0.00,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + accountIds[i] + "' ");
                log.info(sqlBuffer.toString());
                sql[i] = sqlBuffer.toString();
            }

            //修改原账户状态、金额
            for (int i = accountIds.length, j = 0; i < accountIds.length * 2; i++, j++) {
                sql[i] = " update finance_account set IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + accountIds[j] + "' ";
                //log.info(sql[i]);
            }
        }

        //log.info(new Gson().toJson(qo));


        //创建共享账户语句
        StringBuffer sb = new StringBuffer("insert into finance_account(ACCOUNT_ID,ACCOUNT_TYPE,ACCOUNT_NAME,ENTERPRISE_ID,ACCOUNT_TOTAL_SUM,ACCOUNT_USABLE_SUM,ACCOUNT_FROZEN_SUM,ACCOUNT_CONSUME_SUM,ACCOUNT_RECHARGE_SUM,ACCOUNT_CREDIT_SUM,ACCOUNT_STATUS,IS_SHARE,SHARE_ID,CREATED_BY,CREATED_TIME) ");
        sb.append(" select '" + qo.getAccountId() + "','SHARE_ACCOUNT','" + qo.getAccountName() + "','" + qo.getEnterpriseId() + "',0.00,sum(USABLE_SUM_POOL) USABLE_SUM_POOL,sum(FREEZE_SUM_POOL) FREEZE_SUM_POOL,0.00,0.00," + qo.getAccountCreditSum() + ",'1','0','" + qo.getAccountIds() + "','" + qo.getCreatedBy() + "',now() ");
        sb.append(" from finance_account_share_detail where SHARE_ACCOUNT_ID ='" + qo.getAccountId() + "' ");
        //log.info(sb.toString());
        sql[accountIds.length * 2] = sb.toString();


        this.jdbcTemplate.batchUpdate(sql);
    }

    /**
     * 修改财务共享账户
     * qo 新传递过来的
     * financeAccountValidator 现有
     * 包括了修改共享账户流水记录，修改原账户状态，并修改共享账户
     */
    public void editShareFinanceAccount(FinanceAccountValidator qo, FinanceAccountValidator financeAccountValidator) {

        if (null == qo.getIsFreezeSumPool()) qo.setIsFreezeSumPool("0");
        if (null == qo.getIsUsableSumPool()) qo.setIsUsableSumPool("0");

        /**
         * 对比 新的共享分帐号 及 现有共享分帐号关系
         */

        //统计要执行sql的个数
        int delteLength = 0;
        int addLength = 0;

        //Boolean 为true 表示，新数据中，仍然有该财务账户
        Map<String, Boolean> shareIdsMap = new HashMap<>();
        //要新创建的 分财务账号   结合shareIdsMap 就能分辨出，那些是被删除的分财务账户，那些是新加进来的分财务账户， 新的和原来的都存在的财务账户，不动
        Map<String, Boolean> newShareIdMap = new HashMap<>();

        //现有共享分账户  所有的现有分财务账户，在map 中初始化为false
        String[] shareIds = financeAccountValidator.getShareId().split(",");
        for (int i = 0; i < shareIds.length; i++) {
            shareIdsMap.put(shareIds[i].trim(), false);
            delteLength++;
        }

        //新共享分账户
        String[] accountIds = qo.getAccountIds().split(",");
        for (int i = 0; i < accountIds.length; i++) {
            //表示该共享分账户，存在现有数据中
            if (null != shareIdsMap.get(accountIds[i].trim())) {
                shareIdsMap.put(accountIds[i], true);
                delteLength--;
            } else {
                addLength++;
                newShareIdMap.put(accountIds[i].trim(), true);
            }
        }

        int arrayLength = delteLength + addLength;

        if (0 == arrayLength) {
            StringBuffer updateShareFinanceAccount = new StringBuffer();
            updateShareFinanceAccount.append("update finance_account set ACCOUNT_NAME='" + qo.getAccountName() + "',ACCOUNT_CREDIT_SUM =" + qo.getAccountCreditSum() + " ,UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now() where ACCOUNT_ID ='" + qo.getAccountId() + "'");
            this.jdbcTemplate.batchUpdate(updateShareFinanceAccount.toString());
            return;
        }

        String[] sql = new String[arrayLength * 2];

        /**
         * 修改共享账户合并账户明细记录
         */

        //sql[] index
        int index = 0;

        //现有的分共享账户被删除,所以要恢复被删除的分财务账户
        for (String key : shareIdsMap.keySet()) {
            if (!shareIdsMap.get(key)) {
                //回复原来账号状态
                String update = " update finance_account set IS_SHARE ='0',SHARE_ID='',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + key + "' ";
                sql[index] = update;
                index++;
                //修改原来合并账户明细 的状态
                String updateDetail = " update finance_account_share_detail set SHARE_STATUS ='0',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + key + "' ";
                sql[index] = updateDetail;
                index++;
            }
        }

        //余额归集、冻结金额归集
        if ("1".equals(qo.getIsUsableSumPool()) && "1".equals(qo.getIsFreezeSumPool())) {

            //新加进来的 分财务账户  创建分账号记录明细  并修改原账户
            for (String key : newShareIdMap.keySet()) {
                if (newShareIdMap.get(key)) {
                    //创建合并账户明细
                    StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                    sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',ACCOUNT_USABLE_SUM,ACCOUNT_FROZEN_SUM,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + key + "' ");
                    sql[index] = sqlBuffer.toString();
                    index++;
                    //修改原账户信息 为共享状态
                    String update = " update finance_account set ACCOUNT_USABLE_SUM =0.00,ACCOUNT_FROZEN_SUM =0.00,IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + key + "' ";
                    sql[index] = update;
                    index++;
                }
            }
        }

        //余额归集、冻结金额不归集
        if ("1".equals(qo.getIsUsableSumPool()) && "0".equals(qo.getIsFreezeSumPool())) {

            //新加进来的 分财务账户  创建分账号记录明细  并修改原账户
            for (String key : newShareIdMap.keySet()) {
                if (newShareIdMap.get(key)) {
                    //创建合并账户明细
                    StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                    sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',ACCOUNT_USABLE_SUM,0.00,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + key + "' ");
                    sql[index] = sqlBuffer.toString();
                    index++;
                    //修改原账户信息 为共享状态
                    String update = " update finance_account set ACCOUNT_USABLE_SUM =0.00,IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + key + "' ";
                    sql[index] = update;
                    index++;
                }
            }
            ;
        }


        //余额不归集、冻结金额归集
        if ("0".equals(qo.getIsUsableSumPool()) && "1".equals(qo.getIsFreezeSumPool())) {

            //新加进来的 分财务账户  创建分账号记录明细  并修改原账户
            for (String key : newShareIdMap.keySet()) {
                if (newShareIdMap.get(key)) {
                    //创建合并账户明细
                    StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                    sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',0.00,ACCOUNT_FROZEN_SUM,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + key + "' ");
                    sql[index] = sqlBuffer.toString();
                    index++;
                    //修改原账户信息 为共享状态
                    String update = " update finance_account set ACCOUNT_FROZEN_SUM =0.00,IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + key + "' ";
                    sql[index] = update;
                    index++;
                }
            }
            ;
        }

        //余额不归集、冻结金额不归集
        if ("0".equals(qo.getIsUsableSumPool()) && "0".equals(qo.getIsFreezeSumPool())) {

            //新加进来的 分财务账户  创建分账号记录明细  并修改原账户
            for (String key : newShareIdMap.keySet()) {
                if (newShareIdMap.get(key)) {
                    //创建合并账户明细
                    StringBuffer sqlBuffer = new StringBuffer(" insert into finance_account_share_detail(ID,SHARE_ACCOUNT_ID,ACCOUNT_ID,IS_USABLE_SUM_POOL,IS_FREEZE_SUM_POOL,USABLE_SUM_POOL,FREEZE_SUM_POOL,CREATED_BY,CREATED_TIME,SHARE_STATUS) ");
                    sqlBuffer.append(" select '" + UUID.uuid32() + "','" + qo.getAccountId() + "',ACCOUNT_ID,'" + qo.getIsUsableSumPool() + "','" + qo.getIsFreezeSumPool() + "',0.00,0.00,'" + qo.getCreatedBy() + "' ,now(),'1' from finance_account where ACCOUNT_ID ='" + key + "' ");
                    sql[index] = sqlBuffer.toString();
                    index++;
                    //修改原账户信息 为共享状态
                    String update = " update finance_account set IS_SHARE ='1',SHARE_ID='" + qo.getAccountId() + "',UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now()  where ACCOUNT_ID ='" + key + "' ";
                    sql[index] = update;
                    index++;

                    //log.info(sql.toString());
                }
            }
            ;

        }

        this.jdbcTemplate.batchUpdate(sql);

        //查询 要处理的金额
        StringBuffer selectCount = new StringBuffer();
        selectCount.append(" select sum(USABLE_SUM_POOL) USABLE_SUM_POOL,sum(FREEZE_SUM_POOL) FREEZE_SUM_POOL ");
        selectCount.append(" from finance_account_share_detail where SHARE_ACCOUNT_ID ='" + qo.getAccountId() + "' ");
        Map<String, Object> map = jdbcTemplate.queryForMap(selectCount.toString());
        if (null == map || map.size() < 1) {
            return;
        }

        StringBuffer updateShareFinanceAccount = new StringBuffer();
        updateShareFinanceAccount.append("update finance_account set ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM+" + map.get("USABLE_SUM_POOL") + ",ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM+" + map.get("FREEZE_SUM_POOL") + ",SHARE_ID='" + qo.getAccountIds() + "' ,UPDATED_BY='" + qo.getCreatedBy() + "',UPDATED_TIME=now() where ACCOUNT_ID ='" + qo.getAccountId() + "'");

        this.jdbcTemplate.batchUpdate(updateShareFinanceAccount.toString());
    }

}
