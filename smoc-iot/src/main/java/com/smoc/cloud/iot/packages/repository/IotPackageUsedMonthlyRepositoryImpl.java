package com.smoc.cloud.iot.packages.repository;

import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.packages.rowmapper.IotAccountPackageInfoMonthlyRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IotPackageUsedMonthlyRepositoryImpl extends BasePageRepository {

    /**
     * 根据用户账号、套餐id、月份查询套裁历史使用情况
     *
     * @param account
     * @param packageId
     * @param queryMonth
     * @return
     */
    public IotAccountPackageInfoMonthly queryAccountPackageByIdAndMonth(String account, String packageId, String queryMonth) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.PACKAGE_ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.CHARGING_TYPE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.USED_AMOUNT");
        sqlBuffer.append(",t.SURPLUS_AMOUNT");
        sqlBuffer.append(",t.SETTLEMENT_FEE");
        sqlBuffer.append(",t.PACKAGE_MONTH");
        sqlBuffer.append(",t.SETTLEMENT_STATUS");
        sqlBuffer.append(",t.DATA_STATUS");
        sqlBuffer.append("  from iot_package_used_monthly t where t.ACCOUNT=? and t.PACKAGE_ID=? and t.PACKAGE_MONTH=? ");
        sqlBuffer.append(" order by t.CREATED_TIME desc");
        //log.info("[sql]:{}", sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[3];
        params[0] = account;
        params[1] = packageId;
        params[2] = queryMonth;

        IotAccountPackageInfoMonthly iotAccountPackageInfoMonthly = this.jdbcTemplate.queryForObject(sqlBuffer.toString(), new IotAccountPackageInfoMonthlyRowMapper(), params);
        return iotAccountPackageInfoMonthly;
    }

    /**
     * 根据用户账号、历史月份、套裁id，查询套餐历史使用情况
     *
     * @param account
     * @param queryMonth
     * @param packageId
     * @param pageParams
     * @return
     */
    public PageList<IotAccountPackageInfoMonthly> page(String account, String queryMonth, String packageId, PageParams pageParams) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.PACKAGE_ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.CHARGING_TYPE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.USED_AMOUNT");
        sqlBuffer.append(",t.SURPLUS_AMOUNT");
        sqlBuffer.append(",t.SETTLEMENT_FEE");
        sqlBuffer.append(",t.PACKAGE_MONTH");
        sqlBuffer.append(",t.SETTLEMENT_STATUS");
        sqlBuffer.append(",t.DATA_STATUS");
        sqlBuffer.append("  from iot_package_used_monthly t where (1=1)  ");

        //log.info("[sql]:{}", sqlBuffer);
        List<Object> paramsList = new ArrayList<Object>();
        if (!StringUtils.isEmpty(account)) {
            sqlBuffer.append(" and t.ACCOUNT=? ");
            paramsList.add(account);
        }
        if (!StringUtils.isEmpty(queryMonth)) {
            sqlBuffer.append(" and t.PACKAGE_MONTH=? ");
            paramsList.add(queryMonth);
        }
        if (!StringUtils.isEmpty(packageId)) {
            sqlBuffer.append(" and t.PACKAGE_ID=? ");
            paramsList.add(packageId);
        }
        sqlBuffer.append(" order by t.CREATED_TIME desc");
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotAccountPackageInfoMonthly> page = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotAccountPackageInfoMonthlyRowMapper());
        return page;
    }


}
