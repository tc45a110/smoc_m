package com.smoc.cloud.iot.packages.repository;

import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.packages.rowmapper.IotAccountPackageInfoRowMapper;
import com.smoc.cloud.iot.packages.rowmapper.IotPackageInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IotPackageInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IotPackageInfoValidator> page(PageParams<IotPackageInfoValidator> pageParams) {

        //查询条件
        IotPackageInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.PACKAGE_TYPE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.PACKAGE_CHARGING_DISCOUNT");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_OPEN_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        //sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.WARNING_LEVEL");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.REMARK");
        sqlBuffer.append(",t.USE_STATUS");
        sqlBuffer.append(",t.THIS_MONTH_USED_AMOUNT");
        sqlBuffer.append(",t.LAST_MONTH_CARRY_AMOUNT");
        sqlBuffer.append(",t.SYNC_DATE");
        sqlBuffer.append(",t.PACKAGE_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_package_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getPackageName())) {
            sqlBuffer.append(" and t.PACKAGE_NAME like ?");
            paramsList.add("%" + qo.getPackageName().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getPackageType())) {
            sqlBuffer.append(" and t.PACKAGE_TYPE = ?");
            paramsList.add(qo.getPackageType().trim());
        }

        if (!StringUtils.isEmpty(qo.getUseStatus())) {
            sqlBuffer.append(" and t.USE_STATUS = ?");
            paramsList.add(qo.getUseStatus().trim());
        }

        if (!StringUtils.isEmpty(qo.getPackageStatus())) {
            sqlBuffer.append(" and t.PACKAGE_STATUS = ?");
            paramsList.add(qo.getPackageStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotPackageInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotPackageInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 根据用户账号分页查询账号套餐
     *
     * @param account
     * @return
     */
    public PageList<IotAccountPackageInfo> page(String account, PageParams<IotAccountPackageInfo> pageParams) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.PACKAGE_TYPE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.PACKAGE_CHARGING_DISCOUNT");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_OPEN_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        //sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.WARNING_LEVEL");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.THIS_MONTH_USED_AMOUNT");
        sqlBuffer.append(",t.LAST_MONTH_CARRY_AMOUNT");
        sqlBuffer.append(",t.SYNC_DATE");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_package_info t,iot_account_package_items a where a.PACKAGE_ID= t.ID and a.ACCOUNT_ID=?");

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        log.info("[sql]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[1];
        params[0] = account;

        PageList<IotAccountPackageInfo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotAccountPackageInfoRowMapper());
        return pageList;
    }

    /**
     * 根据用户账号分页查询账号套餐
     *
     * @param account
     * @param packageId
     * @return
     */
    public IotAccountPackageInfo queryAccountPackageById(String account, String packageId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.PACKAGE_TYPE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.PACKAGE_CHARGING_DISCOUNT");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_OPEN_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        //sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.WARNING_LEVEL");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.THIS_MONTH_USED_AMOUNT");
        sqlBuffer.append(",t.LAST_MONTH_CARRY_AMOUNT");
        sqlBuffer.append(",t.SYNC_DATE");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_package_info t,iot_account_package_items a where a.PACKAGE_ID= t.ID and a.ACCOUNT_ID=? and t.ID=? ");
        sqlBuffer.append(" order by t.CREATED_TIME desc");
        log.info("[sql]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[2];
        params[0] = account;
        params[1] = packageId;

        IotAccountPackageInfo iotAccountPackageInfo = this.jdbcTemplate.queryForObject(sqlBuffer.toString(),new IotAccountPackageInfoRowMapper(),params);
        return iotAccountPackageInfo;
    }


}
