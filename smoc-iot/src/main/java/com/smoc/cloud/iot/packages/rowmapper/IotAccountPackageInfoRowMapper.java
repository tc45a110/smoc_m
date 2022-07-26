package com.smoc.cloud.iot.packages.rowmapper;

import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IotAccountPackageInfoRowMapper implements RowMapper<IotAccountPackageInfo> {
    @Override
    public IotAccountPackageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotAccountPackageInfo qo = new IotAccountPackageInfo();
        qo.setId(rs.getString("ID"));
        qo.setPackageName(rs.getString("PACKAGE_NAME"));
        qo.setPackageType(rs.getString("PACKAGE_TYPE"));
        qo.setPackageChanging(new BigDecimal(rs.getBigDecimal("PACKAGE_CHANGING").stripTrailingZeros().toPlainString()));
        qo.setPackagePoolSize(new BigDecimal(rs.getBigDecimal("PACKAGE_POOL_SIZE").stripTrailingZeros().toPlainString()));
        qo.setChangingCycle(rs.getString("CHANGING_CYCLE"));
        qo.setCycleQuota(new BigDecimal(rs.getBigDecimal("CYCLE_QUOTA").stripTrailingZeros().toPlainString()));
        qo.setAboveQuotaChanging(new BigDecimal(rs.getBigDecimal("ABOVE_QUOTA_CHANGING").stripTrailingZeros().toPlainString()));
        qo.setPackageTempAmount(new BigDecimal(rs.getBigDecimal("PACKAGE_TEMP_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setPackageTempAmountFee(new BigDecimal(rs.getBigDecimal("PACKAGE_TEMP_AMOUNT_FEE").stripTrailingZeros().toPlainString()));
        qo.setCycleFunctionFee(new BigDecimal(rs.getBigDecimal("CYCLE_FUNCTION_FEE").stripTrailingZeros().toPlainString()));
        qo.setWarningLevel(rs.getInt("WARNING_LEVEL"));
        qo.setPackageCardsNum(rs.getInt("PACKAGE_CARDS_NUM"));
        qo.setThisMonthUsedAmount(rs.getBigDecimal("THIS_MONTH_USED_AMOUNT"));
        qo.setThisMonthSurplusAmount(new BigDecimal(qo.getPackagePoolSize().subtract(qo.getThisMonthUsedAmount()).stripTrailingZeros().toPlainString()));
        qo.setLastMonthCarryAmount(rs.getBigDecimal("LAST_MONTH_CARRY_AMOUNT"));
        qo.setSyncDate(rs.getString("SYNC_DATE"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
