package com.smoc.cloud.iot.packages.rowmapper;

import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


public class IotPackageInfoRowMapper implements RowMapper<IotPackageInfoValidator> {
    @Override
    public IotPackageInfoValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotPackageInfoValidator qo = new IotPackageInfoValidator();
        qo.setId(rs.getString("ID"));
        qo.setPackageName(rs.getString("PACKAGE_NAME"));
        qo.setPackageType(rs.getString("PACKAGE_TYPE"));
        qo.setPackageCharging(new BigDecimal(rs.getBigDecimal("PACKAGE_CHARGING").stripTrailingZeros().toPlainString()));
        qo.setPackageChargingDiscount(new BigDecimal(rs.getBigDecimal("PACKAGE_CHARGING_DISCOUNT").stripTrailingZeros().toPlainString()));
        qo.setPackagePoolSize(new BigDecimal(rs.getBigDecimal("PACKAGE_POOL_SIZE").stripTrailingZeros().toPlainString()));
        qo.setChargingCycle(rs.getString("CHARGING_CYCLE"));
        qo.setCycleQuota(new BigDecimal(rs.getBigDecimal("CYCLE_QUOTA").stripTrailingZeros().toPlainString()));
        qo.setAboveQuotaCharging(new BigDecimal(rs.getBigDecimal("ABOVE_QUOTA_CHARGING").stripTrailingZeros().toPlainString()));
        qo.setPackageTempAmount(new BigDecimal(rs.getBigDecimal("PACKAGE_TEMP_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setPackageTempAmountFee(new BigDecimal(rs.getBigDecimal("PACKAGE_TEMP_AMOUNT_FEE").stripTrailingZeros().toPlainString()));
        qo.setIsOpenFee(rs.getString("IS_OPEN_FEE"));
        qo.setIsFunctionFee(rs.getString("IS_FUNCTION_FEE"));
        //qo.setCycleFunctionFee(new BigDecimal(rs.getBigDecimal("CYCLE_FUNCTION_FEE").stripTrailingZeros().toPlainString()));
        qo.setWarningLevel(rs.getInt("WARNING_LEVEL"));
        qo.setPackageCardsNum(rs.getInt("PACKAGE_CARDS_NUM"));
        qo.setRemark(rs.getString("REMARK"));
        qo.setUseStatus(rs.getString("USE_STATUS"));
        qo.setThisMonthUsedAmount(new BigDecimal(rs.getBigDecimal("THIS_MONTH_USED_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setThisMonthSurplusAmount(new BigDecimal(qo.getPackagePoolSize().subtract(qo.getThisMonthUsedAmount()).stripTrailingZeros().toPlainString()));
        qo.setLastMonthCarryAmount(new BigDecimal(rs.getBigDecimal("LAST_MONTH_CARRY_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setPackageStatus(rs.getString("PACKAGE_STATUS"));
        qo.setSyncDate(rs.getString("SYNC_DATE"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
