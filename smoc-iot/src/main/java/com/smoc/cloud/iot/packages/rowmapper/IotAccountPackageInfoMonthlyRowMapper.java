package com.smoc.cloud.iot.packages.rowmapper;

import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IotAccountPackageInfoMonthlyRowMapper  implements RowMapper<IotAccountPackageInfoMonthly> {
    @Override
    public IotAccountPackageInfoMonthly mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotAccountPackageInfoMonthly qo = new IotAccountPackageInfoMonthly();
        qo.setPackageId(rs.getString("PACKAGE_ID"));
        qo.setPackageName(rs.getString("PACKAGE_NAME"));
        qo.setChangingType(rs.getString("CHANGING_TYPE"));
        qo.setChangingCycle(rs.getString("CHANGING_CYCLE"));
        qo.setPackageChanging(new BigDecimal(rs.getBigDecimal("PACKAGE_CHANGING").stripTrailingZeros().toPlainString()));
        qo.setAboveQuotaChanging(new BigDecimal(rs.getBigDecimal("ABOVE_QUOTA_CHANGING").stripTrailingZeros().toPlainString()));
        qo.setPackageTempAmount(new BigDecimal(rs.getBigDecimal("PACKAGE_TEMP_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setPackageTempAmountFee(new BigDecimal(rs.getBigDecimal("PACKAGE_TEMP_AMOUNT_FEE").stripTrailingZeros().toPlainString()));
        qo.setCycleFunctionFee(new BigDecimal(rs.getBigDecimal("CYCLE_FUNCTION_FEE").stripTrailingZeros().toPlainString()));
        qo.setPackageCardsNum(rs.getInt("PACKAGE_CARDS_NUM"));
        qo.setPackagePoolSize(new BigDecimal(rs.getBigDecimal("PACKAGE_POOL_SIZE").stripTrailingZeros().toPlainString()));
        qo.setUsedAmount(new BigDecimal(rs.getBigDecimal("USED_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setSurplusAmount(new BigDecimal(rs.getBigDecimal("SURPLUS_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setPackageMonth(rs.getString("PACKAGE_MONTH"));
        qo.setSettlementStatus(rs.getString("SETTLEMENT_STATUS"));
        return qo;
    }
}
